import {CardDTO} from "../CardDTO";
import {UpdateCardDTO} from "./UpdateCardDTO";

export class UpdateCardBasicDTO {

  constructor(
    public textAnswerCardId: number,
    public cardDTO: UpdateCardDTO,
    public textAnswer: string,
    public imageId: number | null
  ) {
  }
}
