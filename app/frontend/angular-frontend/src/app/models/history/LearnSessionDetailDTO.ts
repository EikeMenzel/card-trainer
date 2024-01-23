import {Timestamp} from "rxjs";

export interface LearnSessionDetailDTO {
  historyId: number;
  deckName: string;
  createdAt: Timestamp<any>;
  finishedAt: Timestamp<any>;
  difficulty_1: number;
  difficulty_2: number;
  difficulty_3: number;
  difficulty_4: number;
  difficulty_5: number;
  difficulty_6: number;
  status: string;
  cardsLearned: number;
}
