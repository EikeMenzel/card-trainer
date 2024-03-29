import {Component, Input} from '@angular/core';
import {faHouse, faUser} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {
  NgbDropdown,
  NgbDropdownMenu,
  NgbDropdownToggle,
  NgbInputDatepicker,
  NgbModal
} from '@ng-bootstrap/ng-bootstrap';
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../services/auth-service/auth-service";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    FontAwesomeModule,
    NgbInputDatepicker,
    RouterLink,
    NgbDropdown,
    NgbDropdownToggle,
    NgbDropdownMenu,
    TranslateModule
  ],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  faHouse = faHouse;
  faUser = faUser;
  @Input() title: string | undefined;
  @Input() username: string | undefined;

  constructor(
    private modalService: NgbModal,
    private authService: AuthService,
    private router: Router,
    private translate: TranslateService
  ) {
  }

  public open(modal: any): void {
    this.modalService.open(modal);
  }

  onClickReturn(targetUrl: string) {
    this.router.navigateByUrl(targetUrl);
  }

  logoutWarningPopup(): void {
    if (confirm(this.translate.instant("logout_confirmation"))) {
      this.authService.logout();
    }
  }
}
