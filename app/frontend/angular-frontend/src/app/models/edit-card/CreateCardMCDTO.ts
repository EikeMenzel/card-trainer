import {EditCardDTO} from "./EditCardDTO";
import {EditCardAnswer} from "./EditCardAnswer";

export class CreateCardMCDTO {

  constructor(
    public cardDTO: EditCardDTO,
    public choiceAnswers: EditCardAnswer[]
  ) {

  }
}
