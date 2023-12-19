import {CardType} from "./CardType";

export interface CardDTO {
  id: number;
  question: string;
  imageId: number | null;
  cardType: CardType;
}
