import {ImageDTO} from "./ImageDTO";

export class UpdateCardDTO {
  constructor(
    public cardId: number,
    public question: string,
    public imageDTO: ImageDTO | null
  ) {
  }
}
