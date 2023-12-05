import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MessageResponseDTO} from "../../models/MessageResponseDTO";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  sendResetPasswordRequest(email: string) {
    return this.http.post<MessageResponseDTO>("api/v1/password/reset", {"email": email}, {observe: "response"})
  }

  requestPasswordReset(password: string, token: string, email: string) {
    return this.http.put("/api/v1/password/reset", {password, token, email}, {observe: "response"})
  }
}
