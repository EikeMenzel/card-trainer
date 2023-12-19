import {Injectable} from '@angular/core';
import {DeckDTO} from "../../models/DeckDTO";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {DeckDetailInformationDTO} from "../../models/DeckDetailInformationDTO";
import {CardDTO} from "../../models/CardDTO";
import {RatingDTO} from "../../models/learn-session/RatingDTO";

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


  createLearnSession(deckId: number) {
    return this.http.post<number>(`api/v1/decks/${deckId}/learn-sessions`, {observe: 'response'});
  }

  getDetailCardInformation(deckId: number) {
    return this.http.get(`api/v1/decks/${deckId}/next-card`, {observe: 'response'});
  }

  saveLearnSessionRating(learnSessionId: number, ratingDTO: RatingDTO, cardId: number) {
    return this.http.put(`api/v1/learn-sessions/${learnSessionId}/rating`, {
      "cardId": cardId,
      "ratingLevelDTO": ratingDTO
    }, {observe: 'response'})
  }

  updateLearnSessionStatus(learnSessionId: number) {

    const headers = new HttpHeaders()
      .append('Content-Type', 'application/json')

    return this.http.put(`api/v1/learn-sessions/${learnSessionId}/status`, JSON.stringify("FINISHED"), {
      observe: 'response',
      headers
    })
  }
  getImageOfCard(imageId: number) {
    return this.http.get(`/api/v1/images/${imageId}`, {responseType: 'blob', observe: 'response'})
  }

  getCardsToLearn(deckId: number) {
    return this.http.get<number>(`/api/v1/decks/${deckId}/cards-to-learn`, {observe: 'response'});
  }
}
