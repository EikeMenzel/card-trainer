import {CardDTO} from "../CardDTO";
import {UpdateCardDTO} from "./UpdateCardDTO";
import {EditCardAnswer} from "./EditCardAnswer";

export class UpdateCardMCDTO {

  constructor(
    public multipleChoiceCardId: number,
    public cardDTO: UpdateCardDTO,
    public choiceAnswers: EditCardAnswer[]
  ) {
  }
}
