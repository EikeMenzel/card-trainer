export class UpdateCardDTO {
  constructor(
    private cardId: number,
    public question: string,
    public imageId: number | null
  ) {

  }
}
