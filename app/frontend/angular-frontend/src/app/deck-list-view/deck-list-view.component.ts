import {Component, OnInit} from '@angular/core';
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

@Component({
  selector: 'app-deck-list-view',
  standalone: true,
  imports: [CommonModule, BasePageComponent, RouterLink, FormsModule, ToasterComponent],
  templateUrl: './deck-list-view.component.html',
  styleUrl: './deck-list-view.component.css'
})
export class DeckListViewComponent implements OnInit {

  deckList: DeckDTO[] = [];
  showOptions: boolean = false;
  showDelete: boolean = false;
  private modalRef: NgbModalRef | undefined;
  buttonIsPressed: boolean = false;
  importFile: string = "";
  private selectedFile: File | null = null;
  private wipCreateDeck: boolean = false;

  toggleOptions() {
    this.showOptions = !this.showOptions;
    document.getElementById("")?.classList.toggle("show")
  }

  constructor(
    private cardService: CardService,
    private router: Router,
    private toast: ToastService,
    private userService: AuthService,
    private modalService: NgbModal
  ) {
  }

  ngOnInit() {
    if (!this.userService.isLoggedIn) {
      this.router.navigate(["/login"])
      return;
    }
    this.updateDecks()
  }


  updateDecks() {
    let observable = this.cardService.updateDecks();
    observable.subscribe({
      next: value => {
        this.deckList = value.body ?? [];
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
    // Added this to avoid having multible 'Enter new Deck name here'-Field
    if (this.deckList.at(-1)?.deckId == -1) {
      this.deckList.pop()
      return
    }

    const deck1: DeckDTO = {
      deckName: "NewItem",
      deckId: -1,
      cardsToLearn: 0
    }

    this.deckList.push(deck1)
  }

  addedNewDeck() {
    if (this.wipCreateDeck) {
      return;
    }
    this.wipCreateDeck = true
    const newDeckName = (document.getElementById("add-new-item-field") as HTMLInputElement).value;
    if (newDeckName == "") {
      this.toast.showErrorToast("Error","Deck Name can not be Empty")
      return
    }

    if(newDeckName.length > 128){
      this.toast.showErrorToast("Error","No more then 128 Character allowed")
      return;
    }

    this.addItem()
    this.cardService.newDecks(newDeckName).subscribe({
      next: res => {
        if (res.status == 201) {
          this.updateDecks()
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
      },
      complete: () => {
        this.wipCreateDeck = false
      }
    })
  }

  deleteItem($event: Event, id: number) {
    $event.stopPropagation()
    if (!confirm("Are you sure you want to delete the deck?"))
      return
    this.cardService.deleteDeck(id).subscribe({
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

}
