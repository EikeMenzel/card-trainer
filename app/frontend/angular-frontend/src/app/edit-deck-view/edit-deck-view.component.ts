import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {CardService} from "../services/card-service/card.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {ToastService} from "../services/toast-service/toast.service";
import {CardDTO} from "../models/CardDTO";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {FormsModule} from "@angular/forms";
import {faReply, faSearch, faArrowUpZA} from "@fortawesome/free-solid-svg-icons";
import {$localize} from "@angular/localize/init";

@Component({
  selector: 'app-edit-deck-view',
  standalone: true,
  imports: [CommonModule, BasePageComponent, RouterLink, FaIconComponent, FormsModule],
  templateUrl: './edit-deck-view.component.html',
  styleUrl: './edit-deck-view.component.css'
})
export class EditDeckViewComponent implements OnInit {

  originalCards: CardDTO[] = [];
  displayedCards: CardDTO[] = [];
  searchTerm: string = '';
  sortOrder: string = '';
  deckId: string = this.getDeckID();
  showOptions: boolean = false;
  showDelete: boolean = false;
  public pageTitle: string = $localize`Edit Deck`;
  public basicCardName: string = $localize`Basic Card`;
  public MCCardName: string = $localize`Basic Card`;

  constructor(private cardService: CardService,
              private route: ActivatedRoute,
              private toast: ToastService) {
  }

  ngOnInit() {
    this.loadCards()
  }

  private loadCards() {
    this.cardService.getAllCardsByDeck(this.deckId).subscribe({
      next: value => {
        this.originalCards = value.body ?? [];
        this.applyFilters()
      },
      error: err => {
        console.log(err)
        this.toast.showErrorToast("Error", "Could not load Questions for this deck")
      }
    })
  }

  onSearchChange() {
    this.applyFilters();
  }

  onSortOrderChange() {
    this.applyFilters();
  }

  private applyFilters() {
    let filteredCards = [...this.originalCards];

    // Apply search filter
    if (this.searchTerm) {
      filteredCards = filteredCards.filter(card =>
        card.question.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }

    // Apply sort order
    switch (this.sortOrder) {
      case 'oldest':
        // No specific sorting, use the default order (by ID)
        filteredCards.sort((a, b) => a.id - b.id);
        break;
      case 'newest':
        // No specific sorting, use the default order (by ID)
        filteredCards.sort((a, b) => b.id - a.id);
        break;
      case 'nameAsc':
        filteredCards.sort((a, b) => a.question.localeCompare(b.question));
        break;
      case 'nameDesc':
        filteredCards.sort((a, b) => b.question.localeCompare(a.question));
        break;
    }
    this.displayedCards = filteredCards;
  }

  toggleOptions() {
    this.showOptions = !this.showOptions;
    document.getElementById("")?.classList.toggle("show")
  }

  toggleDeleteItem() {
    this.showDelete = !this.showDelete
  }

  deleteItem($event: MouseEvent, cardId: number) {
    $event.stopPropagation()
    this.cardService.deleteCard(this.deckId, cardId).subscribe({
      error: err => {
        console.log(err)
        this.toast.showErrorToast("Error", "The Question could not be deleted")
      },
      complete: () => {
        this.loadCards()
      }
    })
  }

  private getDeckID() {
    return this.route.snapshot.paramMap.get("deck-id") ?? ""
  }

  onClickReturn() {
    return this.getDeckID()
  }

  protected readonly faSearch = faSearch;
  protected readonly faReply = faReply;
  protected readonly faArrowUpZA = faArrowUpZA;
  protected readonly $localize = $localize;
}
