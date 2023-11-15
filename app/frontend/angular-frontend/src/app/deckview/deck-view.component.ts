import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {CardService} from "../services/card.service";
import {Deck} from "../models/Deck";

@Component({
  selector: 'app-deckview',
  standalone: true,
  imports: [CommonModule, BasePageComponent],
  templateUrl: './deck-view.component.html',
  styleUrl: './deck-view.component.css'
})
export class DeckViewComponent {

  private cardService: CardService;
  deckList: Deck[] | undefined;

  constructor(cardService: CardService) {
    this.cardService = cardService;
  }

  ngOnInit() {
    this.deckList = this.cardService.getListOfDecks();
  }
}
