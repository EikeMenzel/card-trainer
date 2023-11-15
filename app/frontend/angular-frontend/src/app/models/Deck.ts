export class Deck {

  constructor(deckName: string, deckId: number) {
    this.deckId = deckId;
    this.deckName = deckName;
  }

  public deckName: string;
  public deckId: number;
}
