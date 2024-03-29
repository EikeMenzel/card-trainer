import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import {ActivatedRoute, Router} from "@angular/router";
import {ToastComponent} from "./toast/toast.component";
import {ToasterComponent} from "./toaster/toaster.component";
import {AuthService} from "./services/auth-service/auth-service";
import {HttpClientModule} from "@angular/common/http";
import {WebsocketService} from "./services/websocket/websocket-service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, ToastComponent, ToasterComponent, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Card Trainer';

  constructor(private router: Router) {
  }
}
