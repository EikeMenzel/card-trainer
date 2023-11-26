import {Injectable} from '@angular/core';
import {DeckDTO} from "../../models/DeckDTO";

@Injectable({
  providedIn: 'root'
})
export class CardService {

  constructor() {

  }

  public getListOfDecks(): DeckDTO[] {
    const deck1: DeckDTO = {
      deckName: "Mathe",
      deckId: 1,
      cardsLeft: 0
    }
    const deck2: DeckDTO = {
      deckName: "Deutsch",
      deckId: 2,
      cardsLeft: 0
    }
    const deck3: DeckDTO = {
      deckName: "LA",
      deckId: 3,
      cardsLeft: 0
    }
    return [deck1, deck2, deck3];
  }
}
