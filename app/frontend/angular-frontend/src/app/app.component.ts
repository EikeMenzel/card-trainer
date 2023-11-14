import { Component } from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'angular-frontend';

  constructor(private cookieService: CookieService,
              private route: ActivatedRoute,
              private router: Router) {

  }

  ngOnInit() {
    return;
    if (!this.isLoggedIn) {
      this.router.navigate(["/login"]);
    }
  }

  get isLoggedIn(): boolean {
    return this.cookieService.check("user");
  }
}
