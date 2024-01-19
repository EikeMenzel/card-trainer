import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {CardService} from "../services/card-service/card.service";
import {DeckDTO} from "../models/DeckDTO";
import {Router, RouterLink} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {ToastService} from "../services/toast-service/toast.service";
import {ToasterComponent} from "../toaster/toaster.component";
import {AuthService} from "../services/auth-service/auth-service";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {HttpStatusCode} from "@angular/common/http";
import {defaults} from "chart.js";
import {faArrowUpZA, faFileImport, faListUl, faPlus, faSearch, faTrashCan} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faSort} from "@fortawesome/free-solid-svg-icons/faSort";

@Component({
  selector: 'app-deck-list-view',
  standalone: true,
  imports: [
    CommonModule,
    BasePageComponent,
    RouterLink,
    FormsModule,
    ToasterComponent,
    FaIconComponent],
  templateUrl: './deck-list-view.component.html',
  styleUrl: './deck-list-view.component.css'
})
export class DeckListViewComponent implements OnInit {

  searchTerm: string = '';
  sortOrder: string = '';
  filteredList: DeckDTO[] = [];
  deckList: DeckDTO[] = [];
  showOptions: boolean = false;
  showDelete: boolean = false;
  private modalRef: NgbModalRef | undefined;
  buttonIsPressed: boolean = false;
  importFile: string = "";
  private selectedFile: File | null = null;
  private wipCreateDeck: boolean = false;
  newItemAdding: boolean = false;
  isSearchActive: boolean = false;


  constructor(
    private cardService: CardService,
    private router: Router,
    private toast: ToastService,
    private userService: AuthService,
    private modalService: NgbModal,
    private toastService: ToastService
  ) { }

  @ViewChild('content') private modalReference: ElementRef | undefined;

  ngOnInit() {
    if (!this.userService.isLoggedIn) {
      this.router.navigate(["/login"])
      return;
    }
    this.updateDecks()
  }

  ngOnDestroy() {
    this.modalRef?.close(this.modalReference)
  }

  updateDecks() {
    let observable = this.cardService.updateDecks();
    observable.subscribe({
      next: value => {
        this.deckList = value.body ?? [];
        this.filteredList = this.deckList;
        this.applySort();
      },
      error: err => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toast.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
            this.toast.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.userService.logout();
            this.router.navigate(["/login"])
            break;
          case HttpStatusCode.NoContent:
            this.toast.showErrorToast("Error", "No Deck was found")
            break;
          case defaults:
            this.toast.showErrorToast("Error", "Deck could not be Updated")
            break;
        }
      }
    })
  }

  addItem() {

    if (this.isSearchActive) {
      this.toastService.showInfoToast('Info', 'Adding not possible while a search is active');
      return;
    }

    if (this.deckList.at(-1)?.deckId == -1) {
      this.deckList.pop();
      return;
    }

    this.newItemAdding = true;

    const newDeck: DeckDTO = {
      deckId: -1,
      deckName: "NewItem",
      deckSize: 0,
      lastLearned: new Date(),
      cardsToLearn: 0
    };

    this.deckList.push(newDeck);
    this.updateFilteredList();
  }

  updateFilteredList() {
    if (this.newItemAdding) {
      const newItemIndex = this.deckList.findIndex(deck => deck.deckId === -1);
      if (newItemIndex > -1) {
        const newItem = this.deckList.splice(newItemIndex, 1)[0];
        this.filteredList.push(newItem);
      }
      this.newItemAdding = false;
    }
  }
  addedNewDeck() {
    this.newItemAdding = false;
    if (this.wipCreateDeck) {
      return;
    }
    this.wipCreateDeck = true
    const newDeckName = (document.getElementById("add-new-item-field") as HTMLInputElement).value;
    if (newDeckName == "") {
      this.toast.showErrorToast("Error","Deck Name can not be Empty")
      this.wipCreateDeck = false;
      return;
    }

    if(newDeckName.length > 128){
      this.toast.showErrorToast("Error","No more then 128 Character allowed")
      this.wipCreateDeck = false;
      return;
    }

    this.addItem()
    this.cardService.newDecks(newDeckName).subscribe({
      next: res => {
        if (res.status == HttpStatusCode.Created) {
          this.updateDecks();
        }
      },
      error: err => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toast.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
            this.toast.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.userService.logout();
            this.router.navigate(["/login"])
            break;
          case defaults:
            this.toast.showErrorToast("Error", "Could not create a new Deck")
            break;
        }
        this.wipCreateDeck = false;
      },
      complete: () => {
        this.wipCreateDeck = false;
      }
    })
  }

  // Call this method every time the search term changes
  onSearchChange() {
    if (this.searchTerm.trim() === '') {
      this.searchTerm = '';
    }
    this.isSearchActive = this.searchTerm.trim() !== '';
    this.filteredList = this.searchTerm ? this.applySearchFilter() : [...this.deckList];
    this.applySort();
  }

  onSortOrderChange() {
    this.applySort();
  }

  applySearchFilter() {
    return this.deckList.filter(deck => deck.deckName.toLowerCase().includes(this.searchTerm.toLowerCase()));
  }

  applySort() {
    switch (this.sortOrder) {
      case 'oldest':
        this.filteredList.sort((a, b) => a.deckId - b.deckId);
        break;
      case 'newest':
        this.filteredList.sort((a, b) => b.deckId - a.deckId);
        break;
      case 'nameAsc':
        this.filteredList.sort((a, b) => a.deckName.localeCompare(b.deckName));
        break;
      case 'nameDesc':
        this.filteredList.sort((a, b) => b.deckName.localeCompare(a.deckName));
        break;
      case 'cardsToLearnAsc':
        this.filteredList.sort((a, b) => a.cardsToLearn - b.cardsToLearn);
        break;
      case 'cardsToLearnDesc':
        this.filteredList.sort((a, b) => b.cardsToLearn - a.cardsToLearn);
        break;
      case 'lastLearnedAsc':
        this.filteredList.sort((a, b) => {
          let dateA = a.lastLearned ? new Date(a.lastLearned).getTime() : -Infinity;
          let dateB = b.lastLearned ? new Date(b.lastLearned).getTime() : -Infinity;
          return dateB - dateA;
        });
        break;
      case 'lastLearnedDesc':
        this.filteredList.sort((a, b) => {
          let dateA = a.lastLearned ? new Date(a.lastLearned).getTime() : -Infinity;
          let dateB = b.lastLearned ? new Date(b.lastLearned).getTime() : -Infinity;
          return dateA - dateB;
        });
        break;
    }
  }

  cancelAddItem() {
    if (this.filteredList.length > 0 && this.filteredList.at(-1)?.deckId === -1) {
      this.filteredList.pop();
    }
    this.newItemAdding = false;
    this.updateFilteredList();
  }

  deleteItem($event: Event, id: number) {
    $event.stopPropagation()
    if (!confirm("Are you sure you want to delete the deck?"))
      return
    this.cardService.deleteDeck(id).subscribe({
      next: () => {
        this.deckList = this.deckList.filter(deck => deck.deckId != id);
        this.filteredList = this.filteredList.filter(deck => deck.deckId != id);
      },
      complete: () => {
        this.deckList = this.deckList.filter(value => value.deckId != id);
      },
      error: err => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toast.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
            this.toast.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.userService.logout();
            this.router.navigate(["/login"])
            break;
          case defaults:
            this.toast.showErrorToast("Loading Error", "Deck could not be deleted")
            break;
        }
      }
    })
  }

  toggleOptions() {
    this.showOptions = !this.showOptions;
    document.getElementById("")?.classList.toggle("show")
  }
  toggleDeleteItem() {
    this.showDelete = !this.showDelete
  }

  onSubmitImportDeck(event: any) {
    this.buttonIsPressed = true;
    if (!this.selectedFile) {
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile, this.selectedFile.name);
    this.cardService.importDeckUpload(formData).subscribe({
      next: value => {
        this.toast.showSuccessToast("Import Deck", "Your deck has been imported successfully");
        this.buttonIsPressed = false;
        this.updateDecks();
        this.modalRef?.close()
      },
      error: err => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toast.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
            this.toast.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.userService.logout();
            this.router.navigate(["/login"])
            break;
          case HttpStatusCode.UnprocessableEntity:
            this.toast.showErrorToast("Error", "Wrong format")
            break;
          case defaults:
            this.toast.showErrorToast("Error", "Deck could not be imported")
            break;
        }
        this.buttonIsPressed = false;
      }
    });
  }

  showImportModal(content: any) {
    this.modalRef = this.modalService.open(content);
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  showSearchActiveToast(): void {
    this.toastService.showInfoToast('Action not possible', 'Action not possible while searching');
  }

  protected readonly faSearch = faSearch;
  protected readonly faTrashCan = faTrashCan;
  protected readonly faArrowUpZA = faArrowUpZA;
  protected readonly faPlus = faPlus;
  protected readonly faFileImport = faFileImport;
  protected readonly faListUl = faListUl;
}
