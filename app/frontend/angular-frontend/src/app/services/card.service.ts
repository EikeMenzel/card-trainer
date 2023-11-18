import {Injectable} from '@angular/core';
import {Deck} from "../models/Deck";

@Injectable({
  providedIn: 'root'
})
export class CardService {

  constructor() {

  }

  public getListOfDecks(): Deck[] {
    return [new Deck("Mathe", 1,0), new Deck("Deutsch", 2, 0), new Deck("LA", 3, 0)];
  }
}
