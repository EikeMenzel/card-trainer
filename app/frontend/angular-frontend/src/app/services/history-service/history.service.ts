import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {DeckDTO} from "../../models/DeckDTO";
import {LearnSessionDetailDTO} from "../../models/history/LearnSessionDetailDTO";

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  constructor(private http: HttpClient) {
  }
  public getHistoryDetails(sessionId: number): Observable<HttpResponse<LearnSessionDetailDTO>> {
    return this.http.get<LearnSessionDetailDTO>("api/v1/histories/" + sessionId, {observe: 'response'})
  }
}
