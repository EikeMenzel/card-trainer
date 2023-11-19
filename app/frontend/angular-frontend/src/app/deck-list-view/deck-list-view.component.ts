import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {CardService} from "../services/card.service";
import {Deck} from "../models/Deck";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-deck-list-view',
  standalone: true,
  imports: [CommonModule, BasePageComponent, RouterLink],
  templateUrl: './deck-list-view.component.html',
  styleUrl: './deck-list-view.component.css'
})
export class DeckListViewComponent {

  private cardService: CardService;
  deckList: Deck[] | undefined;
  private router: Router;

  constructor(cardService: CardService, router: Router) {
    this.router = router;
    this.cardService = cardService;
  }

  ngOnInit() {
    this.deckList = this.cardService.getListOfDecks();
  }
}
