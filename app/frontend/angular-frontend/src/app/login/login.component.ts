import {Component} from '@angular/core';
import {
  ReactiveFormsModule,
  FormsModule,
  FormControl, FormGroup, Validators, NgForm
} from '@angular/forms';
import {RouterLink} from "@angular/router";
import {HttpClient, HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, FormsModule, RouterLink, HttpClientModule]
})
export class LoginComponent {

  private http: HttpClient

  constructor(http: HttpClient) {
    this.http = http;
  }
  onSubmit(loginForm: NgForm) {
    if(loginForm.valid) {
      if (loginForm.value["remember"] == "") {
        loginForm.value["remember"] = false;
      }

      const email: String = loginForm.value["email"]
      const password: String = loginForm.value["password"]

      this.http.post<any>("/api/v1/login", {email, password}, {observe: 'response'}).subscribe((data) => {
        if (data.status != 200) {

        }
      });
    }
  }
}
