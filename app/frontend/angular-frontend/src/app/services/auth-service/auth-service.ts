import {Injectable} from "@angular/core";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MatDialog} from "@angular/material/dialog";
import {SessionRenewalModalComponent} from "../../session-renewal-modal/session-renewal-modal.component";
import {HttpClient} from "@angular/common/http";
import {ToastService} from "../toast-service/toast.service";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private sessionTimeoutMinutes = 60;
  private sessionTimer: any;
  private cookieName: string = "card-trainer-user";

  constructor(
    private cookieService: CookieService,
    private router: Router,
    public dialog: MatDialog
  ) {
  }

  getJwtValueFromCookie() {
    return this.cookieService.get(this.cookieName);
  }

  resetCookieToSessionCookie() {
    this.cookieService.set(this.cookieName, this.getJwtValueFromCookie());
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
}
