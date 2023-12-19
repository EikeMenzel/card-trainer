import {Injectable} from '@angular/core';
import {HttpClient, HttpStatusCode} from "@angular/common/http";
import {MessageResponseDTO} from "../../models/MessageResponseDTO";
import {UserInfoDTO} from "../../models/UserInfoDTO";
import {AuthService} from "../auth-service/auth-service";
import {Router} from "@angular/router";
import {ToastService} from "../toast-service/toast.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userData: UserInfoDTO = {
    username: "",
    email: "",
    achievementIds: [],
    langCode: "EN",
    cardsToLearn: 0,
    receiveLearnNotification: false
  };

  constructor(
    private http: HttpClient,
    private auth: AuthService,
    private router: Router,
    private toast: ToastService
  ) {
  }

  sendResetPasswordRequest(email: string) {
    return this.http.post<MessageResponseDTO>("api/v1/password/reset", {"email": email}, {observe: "response"})
  }

  requestPasswordReset(password: string, token: string, email: string) {
    return this.http.put("/api/v1/password/reset", {password, token, email}, {observe: "response"})
  }

  changePassword(newPassword: string) {
    return this.http.put('api/v1/password', {"password": newPassword}, {observe: "response"})
  }

  updateUserInfo(userProfile: UserInfoDTO) {
    return this.http.put<UserInfoDTO>('api/v1/account', userProfile)
  }

  getUserInfoDTO(): UserInfoDTO {
    if (this.userData.username == "") {
      this.getUpdatedUserInfo()
    }

    return this.userData
  }

  getUpdatedUserInfo() {
    const request: XMLHttpRequest = new XMLHttpRequest();
    request.open('GET', "/api/v1/account", false);
    request.send(null);
    if (request.status == HttpStatusCode.Ok) {
      this.userData = JSON.parse(request.responseText);
      return
    }

    if (request.status == HttpStatusCode.PreconditionFailed || request.status == HttpStatusCode.Unauthorized) {
      this.toast.showErrorToast("Error","Authorization failed. Please Login again.")
      this.auth.logout()
      this.router.navigate(["/login"])
    }

    if(request.status != HttpStatusCode.Unauthorized)
      this.toast.showErrorToast("User Loading error", "Could not load user Data")
  }
}
