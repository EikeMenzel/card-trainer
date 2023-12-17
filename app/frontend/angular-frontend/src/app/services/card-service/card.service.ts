import {Injectable, Input} from '@angular/core';
import {DeckDTO} from "../../models/DeckDTO";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {DeckDetailInformationDTO} from "../../models/DeckDetailInformationDTO";
import {CardDTO} from "../../models/CardDTO";

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

  getAllCardsByDeck(deckId: string) {
    return this.http.get<CardDTO[]>("api/v1/decks/" + deckId + "/cards", {observe: "response"})
  }

  deleteCard(deckId: string, cardId: number) {
    return this.http.delete("api/v1/decks/" + deckId + "/cards/" + cardId, {observe: "response"})
  }

  detailDecks(id: number): Observable<HttpResponse<DeckDetailInformationDTO>> {
    return this.http.get<DeckDetailInformationDTO>(`api/v1/decks/${id}`, {observe: 'response'})
  }

  getExportFile(deckId: number): Observable<HttpResponse<ArrayBuffer>> {
    return this.http.get(`api/v1/decks/${deckId}/export`, {observe: 'response', responseType: 'arraybuffer'});
  }

  shareDeck(deckId: number, email: string) {
    return this.http.post(`api/v1/decks/${deckId}/share`, {"email": email}, {observe: 'response'});
  }

  importDeckUpload(file: FormData) {
    return this.http.post("api/v1/decks/import", file, {
      observe: "response"
    })
  }
}
