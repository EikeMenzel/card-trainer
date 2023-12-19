import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {CardService} from "../services/card-service/card.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {ToastService} from "../services/toast-service/toast.service";
import {CardDTO} from "../models/CardDTO";

@Component({
  selector: 'app-edit-deck-view',
  standalone: true,
  imports: [CommonModule, BasePageComponent, RouterLink],
  templateUrl: './edit-deck-view.component.html',
  styleUrl: './edit-deck-view.component.css'
})
export class EditDeckViewComponent implements OnInit {

  cards: CardDTO[] = [];
  deckId: string = this.getDeckID();
  showOptions: boolean = false;
  showDelete: boolean = false;

  private getDeckID() {
    return this.route.snapshot.paramMap.get("deck-id") ?? ""
  }

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
        if (value.body == null || value.body?.length == 0) {
          this.toast.showInfoToast("Info", "You have no questions for this decks")
        }
        this.cards = value.body ?? [];
      },
      error: err => {
        console.log(err)
        this.toast.showErrorToast("Error", "Could not load Questions for this deck")
      }
    })
  }

  toggleDeleteItem() {
    this.showDelete = !this.showDelete
  }

  toggleOptions() {
    this.showOptions = !this.showOptions;
    document.getElementById("")?.classList.toggle("show")
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
}
