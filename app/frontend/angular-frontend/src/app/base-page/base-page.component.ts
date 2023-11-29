import {Component, Input} from '@angular/core';
import {NavbarComponent} from "../navbar/navbar.component";
import {AuthService} from "../services/auth-service/auth-service";
import {ToasterComponent} from "../toaster/toaster.component";


@Component({
  selector: 'app-base-page',
  standalone: true,
  templateUrl: './base-page.component.html',
    imports: [
        NavbarComponent,
        ToasterComponent
    ],
  styleUrls: ['./base-page.component.css']
})
export class BasePageComponent {
  @Input() PageTitle: string = "Card Trainer";
  @Input() Username: string = this.getUsername()

  constructor(private authService: AuthService) {
  }


  getUsername() {
    return this.authService.getUserInfo().username
  }
}
