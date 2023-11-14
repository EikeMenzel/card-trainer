import { Component } from '@angular/core';
import {NavbarComponent} from "../navbar/navbar.component";

@Component({
  selector: 'app-base-page',
  standalone: true,
  templateUrl: './base-page.component.html',
  imports: [
    NavbarComponent
  ],
  styleUrls: ['./base-page.component.css']
})
export class BasePageComponent {

}
