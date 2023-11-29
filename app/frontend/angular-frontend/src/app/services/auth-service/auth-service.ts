import {Component, Injectable} from "@angular/core";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MatDialog} from "@angular/material/dialog";
import {SessionRenewalModalComponent} from "../../session-renewal-modal/session-renewal-modal.component";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {UserInfoDTO} from "../../models/UserInfoDTO";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private sessionTimeoutMinutes = 60;
  private sessionTimer: any;
  private cookieName: string = "card-trainer-user";
  private userData: UserInfoDTO = {
    username: "",
    email: "",
    achievementIds: [0],
    langCode: "",
    cardsToLearn: 0,
    receiveLearnNotification: false
};

  constructor(
    private cookieService: CookieService,
    private modalService: NgbModal,
    private router: Router,
    public dialog: MatDialog,
    private http: HttpClient
  ) {
  }

  logout() {
    this.cookieService.delete(this.cookieName);
    this.router.navigate(["/login"])
  }

  get isLoggedIn(): boolean {
    return this.cookieService.check(this.cookieName);
  }

  startSessionTimer() {
    this.clearSessionTimer();
    this.sessionTimer = setTimeout(() => {
      this.openSessionRenewalModal();
    }, this.sessionTimeoutMinutes * 60 * 1000); // Convert minutes to milliseconds
  }

  clearSessionTimer() {
    if (this.sessionTimer) {
      clearTimeout(this.sessionTimer);
      this.sessionTimer = null;
    }
  }

  openSessionRenewalModal() {
    const dialogRef = this.dialog.open(SessionRenewalModalComponent, {
      width: 'auto',
      maxWidth: '600px',
      panelClass: ['renewal-modal'],
      position: {
        top: '10px',
        left: 'calc(50% - 300px)'
      },
      disableClose: true
    });
    dialogRef.afterClosed().subscribe(result => {
      switch (result) {
        case "renewed": {
          console.log(result)
          this.startSessionTimer();
          return;
        }
        case "logged_out" || undefined: {
          console.log(result)
          this.logout();
          return;
        }
      }
    });
  }

  getUserInfo(): UserInfoDTO {
    console.log("Get Userdata")
    if (this.userData.username == "") {
      this.updateUserInfo()
    }

    return this.userData
  }

  updateUserInfo() {
    const request: XMLHttpRequest = new XMLHttpRequest();
    request.open('GET', "/api/v1/account", false);
    request.send(null);
    this.userData = JSON.parse(request.responseText);
  }
}
