import {Injectable} from '@angular/core';
import {HttpClient, HttpStatusCode} from "@angular/common/http";
import {MessageResponseDTO} from "../../models/MessageResponseDTO";
import {UserInfoDTO} from "../../models/UserInfoDTO";
import {AuthService} from "../auth-service/auth-service";
import {Router} from "@angular/router";
import {ToastService} from "../toast-service/toast.service";
import {RegisterRequestDTO} from "../../models/RegisterRequestDTO";
import {TranslateService} from "@ngx-translate/core";
import {CookieService} from "ngx-cookie-service";

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
    private toast: ToastService,
    private translate: TranslateService
  ) {}

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
      this.translate.use(this.userData.langCode.toLowerCase())
      return
    }

    if (request.status == HttpStatusCode.PreconditionFailed || request.status == HttpStatusCode.Unauthorized) {
      this.auth.logout()
    }

    if (request.status != HttpStatusCode.Unauthorized)
      this.toast.showErrorToast(this.translate.instant("user_loading_error"), this.translate.instant("user_loading_error_data"))
  }

  registerUser(registerRequest: RegisterRequestDTO) {
    return this.http.post<RegisterRequestDTO>('/api/v1/register', registerRequest, {observe: 'response'})
  }

}
