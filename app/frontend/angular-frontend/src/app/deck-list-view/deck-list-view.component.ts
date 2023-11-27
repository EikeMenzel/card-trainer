import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {CardService} from "../services/card-service/card.service";
import {DeckDTO} from "../models/DeckDTO";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-deck-list-view',
  standalone: true,
  imports: [CommonModule, BasePageComponent, RouterLink],
  templateUrl: './deck-list-view.component.html',
  styleUrl: './deck-list-view.component.css'
})
export class DeckListViewComponent {

  deckList: DeckDTO[] = [];

  constructor(private cardService: CardService, private router: Router) {
  }

  ngOnInit() {
    this.deckList = this.cardService.getListOfDecks();
  }
}
