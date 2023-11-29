import {Injectable, Input} from '@angular/core';
import {DeckDTO} from "../../models/DeckDTO";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CardService {

  constructor(private http: HttpClient) {

  }

  public updateDecks(): Observable<HttpResponse<DeckDTO[]>> {
    return this.http.get<DeckDTO[]>("api/v1/decks", {observe: 'response'})
  }

  public newDecks(name: string): Observable<HttpResponse<any>> {
    return this.http.post<any>("api/v1/decks", {"deckName": name}, {observe: 'response'})
  }

  deleteDeck(id: number) {
    return this.http.delete<any>("api/v1/decks/" + id.toString())
  }
}
