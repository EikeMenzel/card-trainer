import {Component} from '@angular/core';
import {
  ReactiveFormsModule,
  FormsModule,
  FormControl, FormGroup, Validators, NgForm
} from '@angular/forms';
import {Router, RouterLink} from "@angular/router";
import {HttpClient, HttpClientModule, HttpStatusCode} from "@angular/common/http";
import {ToastService} from "../services/toast-service/toast.service";
import {ToasterComponent} from "../toaster/toaster.component";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "../services/auth-service/auth-service";

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, FormsModule, RouterLink, HttpClientModule, ToasterComponent]
})
export class LoginComponent {

  public emailBorder: string = "white";
  public passwordBorder: string = "white";

  constructor(
    private http: HttpClient,
    private toastService: ToastService,
    private router: Router,
    private authService: AuthService) {
  }

  private errorPassword() {
    this.passwordBorder = "red"
    setTimeout(() => {
      this.passwordBorder = "white"
    }, 3000)
  }

  private errorEmail() {
    this.emailBorder = "red"
    setTimeout(() => {
      this.emailBorder = "white"
    }, 3000)
  }

  onSubmit(loginForm: NgForm) {
    if (!loginForm.valid) {
      this.errorEmail()
      this.errorPassword()
      return;
    }
    if (loginForm.value["remember"] == "") {
      loginForm.value["remember"] = false;
    }
    const email: String = loginForm.value["email"]
    const password: String = loginForm.value["password"]
    this.http.post<any>("/api/v1/login", {email, password}, {observe: 'response'}).subscribe({
      next: value => {

        if(loginForm.value["remember"] === false) {
          this.authService.startSessionTimer();
        }

        this.toastService.showSuccessToast("Login", "Login successes")
        this.router.navigate(["/"])
      },
      error: err => {
        this.errorEmail()
        this.errorPassword()
        if (err.status == HttpStatusCode.Unauthorized) {
          this.toastService.showErrorToast("Login Failed", "Wrong Credentials or the user isn't verified")
        }
        if (err.status == HttpStatusCode.InternalServerError) {
          this.toastService.showErrorToast("Login Failed", "The Login Service is currently unavailable")
        }
      }
    });
  }
}
