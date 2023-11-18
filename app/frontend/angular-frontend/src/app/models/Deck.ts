export class Deck {

  constructor(deckName: string, deckId: number, cardsLeft: number) {
    this.deckId = deckId;
    this.deckName = deckName;
    this.cardsLeft =cardsLeft;
  }

  public deckName: string;
  public deckId: number;
  public cardsLeft: number;
}
