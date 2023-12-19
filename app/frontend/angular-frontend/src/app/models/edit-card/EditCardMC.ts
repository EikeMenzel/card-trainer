import {MCCardAnswerDTO} from "./MCCardAnswerDTO";
import {EditCardDTO} from "./EditCardDTO";

export class EditCardMC {
  constructor(public cardDTO: EditCardDTO, public choiceAnswers: MCCardAnswerDTO[]) {
  }
}
