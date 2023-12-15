export interface DeckDetailInformationDTO {
  deckId: number
  deckName: string
  deckSize: number
  cardsToLearn: number
  lastLearned: Date
  deckLearnState: number[]
}
