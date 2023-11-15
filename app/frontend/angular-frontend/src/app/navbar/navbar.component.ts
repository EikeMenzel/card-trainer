import { Component } from '@angular/core';
import { faHouse, faUser } from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {NgbInputDatepicker, NgbModal} from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [FontAwesomeModule, NgbInputDatepicker],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  faHouse = faHouse;
  faUser = faUser;

  constructor(private modalService: NgbModal) {
  }

  public open(modal: any): void {
    this.modalService.open(modal);
  }
}
