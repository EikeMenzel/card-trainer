import {EditCardDTO} from "./EditCardDTO";
import {MCCardAnswerDTO} from "./MCCardAnswerDTO";
import {EditCardMCHolder} from "./EditCardMCHolder";

export class EditCardMCDTO {

  constructor(
    public cardDTO: EditCardDTO,
    public choiceAnswers: EditCardMCHolder[]
  ) {}
}
