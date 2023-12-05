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

  toggleOptions() {
    this.showOptions = !this.showOptions;
    document.getElementById("")?.classList.toggle("show")
  }

  constructor(private cardService: CardService, private router: Router, private toast: ToastService, private userService: AuthService) {
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
        console.log(err)
        this.toast.showErrorToast("Loading Error", "The Decks could not be loaded")
      }
    })
  }

  addItem() {

    const deck1: DeckDTO = {
      deckName: "NewItem",
      deckId: -1,
      cardsToLearn: 0
    }
    this.deckList.push(deck1)
  }

  addedNewDeck() {
    const newDeckName = (document.getElementById("add-new-item-field") as HTMLInputElement).value;
    if (newDeckName == "") {
      return
    }
    this.cardService.newDecks(newDeckName).subscribe({
      next: res => {
        if (res.status == 201) {
          this.updateDecks()
        }
      }
    })
  }

  deleteItem($event: Event, id: number) {
    $event.stopPropagation()
    this.cardService.deleteDeck(id).subscribe({
      complete: () => {
        this.deckList = this.deckList.filter(value => value.deckId != id);
      }
    })
  }

  toggleDeleteItem() {
    this.showDelete = !this.showDelete
  }
}
