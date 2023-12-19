import {UpdateCardDTO} from "./UpdateCardDTO";
import {ImageDTO} from "./ImageDTO";
import {UpdateMCAnswerDTO} from "./UpdateMCAnswerDTO";

export class UpdateMCCardDTO {
  constructor(
    public textAnswerCardId: number,
    public cardDTO: UpdateCardDTO,
    public choiceAnswers: UpdateMCAnswerDTO[]
  ) {
  }
}
