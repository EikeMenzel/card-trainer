import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {LearnSessionDetailDTO} from "../../models/history/LearnSessionDetailDTO";
import {HistoryDTO} from "../../models/history/HistoryDTO";

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  constructor(private http: HttpClient) {
  }
  public getHistoryDetails(deckId: number,sessionId: number): Observable<HttpResponse<LearnSessionDetailDTO>> {
    return this.http.get<LearnSessionDetailDTO>(`api/v1/decks/${deckId}/histories/` + sessionId, {observe: 'response'})
  }

  public getAllHistories(deckId: number): Observable<HttpResponse<HistoryDTO[]>>{
    return this.http.get<HistoryDTO[]>(`api/v1/decks/${deckId}/histories`,{observe:'response'})
  }
}
