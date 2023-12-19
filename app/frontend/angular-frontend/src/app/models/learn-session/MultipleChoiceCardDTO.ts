import {CardDTO} from "./CardDTO";
import {ChoiceAnswerDTO} from "./ChoiceAnswerDTO";

export interface MultipleChoiceCardDTO {
  cardDTO: CardDTO;
  multipleChoiceCardId: number;
  choiceAnswers: ChoiceAnswerDTO[];
}
