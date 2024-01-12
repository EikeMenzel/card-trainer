import {Component, Input} from '@angular/core';
import { faHouse, faUser } from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {
  NgbDropdown,
  NgbDropdownMenu,
  NgbDropdownToggle,
  NgbInputDatepicker,
  NgbModal
} from '@ng-bootstrap/ng-bootstrap';
import {Router, RouterLink} from "@angular/router";
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import {AuthService} from "../services/auth-service/auth-service";
import {LearningSessionService} from "../services/learn-session-service/learn-session.service";
import {take} from "rxjs";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    FontAwesomeModule,
    NgbInputDatepicker,
    RouterLink,
    NgbDropdown,
    NgbDropdownToggle,
    NgbDropdownMenu],
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
    private learningSessionService: LearningSessionService,
    private router: Router
  ) { }

  public open(modal: any): void {
    this.modalService.open(modal);
  }

  onClickReturn(targetUrl: string) {
    this.learningSessionService.getLearningSessionStatus().pipe(
      take(1)
    ).subscribe(inSession => {
      if (inSession) {
        if (confirm("Are you sure you want to quit your learn session?")) {
          this.learningSessionService.setLearningSession(false);
          this.router.navigateByUrl(targetUrl);
        }
      } else {
        this.router.navigateByUrl(targetUrl);
      }
    });
  }


}
