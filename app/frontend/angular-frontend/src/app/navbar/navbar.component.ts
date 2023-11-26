import {Component, Input} from '@angular/core';
import { faHouse, faUser } from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {NgbInputDatepicker, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {RouterLink} from "@angular/router";
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [FontAwesomeModule, NgbInputDatepicker, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  faHouse = faHouse;
  faUser = faUser;
  @Input() title: string | undefined;
  @Input() username: string | undefined;

  constructor(private modalService: NgbModal) {
  }

  public open(modal: any): void {
    this.modalService.open(modal);
  }
}
