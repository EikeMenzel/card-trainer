import {UpdateCardDTO} from "./UpdateCardDTO";
import {ImageDTO} from "./ImageDTO";

export class UpdateBasicCardDTO {
  constructor(
    public textAnswerCardId: number,
    public cardDTO: UpdateCardDTO,
    public textAnswer: string,
    public imageDTO: ImageDTO | null
  ) {
  }
}
