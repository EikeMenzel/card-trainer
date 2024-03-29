import {Injectable} from '@angular/core';
import {DeckDTO} from "../../models/DeckDTO";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {DeckDetailInformationDTO} from "../../models/DeckDetailInformationDTO";
import {CardDTO} from "../../models/CardDTO";
import {RatingDTO} from "../../models/learn-session/RatingDTO";
import {CreateCardMCDTO} from "../../models/edit-card/CreateCardMCDTO";
import {CreateCardBasicDTO} from "../../models/edit-card/CreateCardBasicDTO";
import {UpdateCardBasicDTO} from "../../models/edit-card/UpdateCardBasicDTO";
import {UpdateCardMCDTO} from "../../models/edit-card/UpdateCardMCDTO";

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

  updateDeckDetails(deckId: number, newDeckname: string): Observable<HttpResponse<any>> {
    return this.http.put<HttpResponse<any>>(`api/v1/decks/${deckId}`, {"deckName": newDeckname}, { observe: 'response' });
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

  getDetailCardInformation(deckId: number,learnSessionId: number) {
    return this.http.get(`api/v1/decks/${deckId}/learn-sessions/${learnSessionId}/next-card`, {observe: 'response'});
  }

  saveLearnSessionRating(learnSessionId: number, ratingDTO: RatingDTO, cardId: number) {
    return this.http.put(`api/v1/learn-sessions/${learnSessionId}/rating`, {
      "cardId": cardId,
      "ratingLevelDTO": ratingDTO
    }, {observe: 'response'})
  }

  finishedLearnSessionStatus(learnSessionId: number) {

    const headers = new HttpHeaders()
      .append('Content-Type', 'application/json')

    return this.http.put(`api/v1/learn-sessions/${learnSessionId}/status`, JSON.stringify("FINISHED"), {
      observe: 'response',
      headers
    })
  }

  cancelLearnSessionStatus(learnSessionId: number) {

    const headers = new HttpHeaders()
      .append('Content-Type', 'application/json')

    return this.http.put(`api/v1/learn-sessions/${learnSessionId}/status`, JSON.stringify("CANCELED"), {
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

    createCard(cardDTO: CreateCardBasicDTO | CreateCardMCDTO, deckId: string) {
    return this.http.post(`/api/v1/decks/${deckId}/cards`, cardDTO, {
      observe: "response"
    });
  }

  getCard(deckId: string, cardId: string): Observable<HttpResponse<any>> {
    return this.http.get(`/api/v1/decks/${deckId}/cards/${cardId}`, {observe: "response"})
  }

  updateMCCard(deckId: string, cardId: string, formData: FormData) {
    return this.http.put(`/api/v1/decks/${deckId}/cards/${cardId}`, formData, {observe: "response"})
  }

  updateBasicCard(deckId: string, cardId: string, formData: FormData) {
    return this.http.put(`/api/v1/decks/${deckId}/cards/${cardId}`, formData, {observe: "response"})

  }

  saveImage(selectedFile: File) {
    const formData = new FormData();
    formData.append("image", selectedFile, selectedFile.name);
    return this.http.post(`/api/v1/images`, formData, {observe: "response"})
  }

  updateCard(cardDTO: UpdateCardBasicDTO | UpdateCardMCDTO, deckId: string, cardId: string) {
    return this.http.put(`/api/v1/decks/${deckId}/cards/${cardId}`, cardDTO, {observe: "response"})
  }

  startPeekSession(deckId:number) {
   return this.http.post<number>(`api/v1/decks/${deckId}/peek-sessions`,{obeserve:"response"})
  }

  getNextPeekCard(peekSessionId: number){
    return this.http.get(`api/v1/peek-sessions/${peekSessionId}/next-card`,{observe:"response"})
  }

  finishStatusOfCardPeek(peekSessionId: number) {
    const headers = new HttpHeaders()
      .append('Content-Type', 'application/json')

    return this.http.put(`api/v1/peek-sessions/${peekSessionId}/status`,JSON.stringify("FINISHED"),{observe:"response", headers})
  }

  savePeekSessionCard(peekSessionId: number,cardId:number){
    return this.http.post(`api/v1/peek-sessions/${peekSessionId}/cards/${cardId}`,{observe:'response'})
  }

  getNumberofCardsInDeck(deckId: number) {
    return this.http.get<number>(`api/v1/decks/${deckId}/size`,{observe:'response'});
  }

  setCancelledPeekStatus(peekSessionId: number) {
    const headers = new HttpHeaders()
      .append('Content-Type', 'application/json')

    return this.http.put(`api/v1/peek-sessions/${peekSessionId}/status`,JSON.stringify("CANCELED"),{observe:"response", headers})
  }
}
