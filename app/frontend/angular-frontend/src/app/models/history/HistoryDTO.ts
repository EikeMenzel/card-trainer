import {Timestamp} from "rxjs";

export interface HistoryDTO {
  historyId: number;
  createdAt: Timestamp<any>;
  status: string;
  cardsLearned: number;
}
