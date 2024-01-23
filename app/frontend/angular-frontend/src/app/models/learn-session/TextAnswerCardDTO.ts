import {CardDTO} from "./CardDTO";

export interface TextAnswerCardDTO {
  cardDTO: CardDTO;
  textAnswerCardId: number;
  textAnswer: string;
  imageId: number | null;
}
