import {Component} from '@angular/core';
import {
  ReactiveFormsModule,
  FormsModule,
  FormControl, FormGroup, Validators, NgForm
} from '@angular/forms';
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {HttpClient, HttpClientModule, HttpStatusCode} from "@angular/common/http";
import {ToastService} from "../services/toast-service/toast.service";
import {ToasterComponent} from "../toaster/toaster.component";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "../services/auth-service/auth-service";
import {UserService} from "../services/user-service/user.service";

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, FormsModule, RouterLink, HttpClientModule, ToasterComponent, RouterLinkActive]
})
export class LoginComponent {

  public emailBorder: string = "#F0FDEE";
  public passwordBorder: string = "#F0FDEE";

  constructor(
    private http: HttpClient,
    private toastService: ToastService,
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) {
  }

  private errorPassword() {
    this.passwordBorder = "#FF6961"
    setTimeout(() => {
      this.passwordBorder = "#F0FDEE"
    }, 3000)
  }

  private errorEmail() {
    this.emailBorder = "#FF6961"
    setTimeout(() => {
      this.emailBorder = "#F0FDEE"
    }, 3000)
  }

  onSubmit(loginForm: NgForm) {
    if (!loginForm.valid) {
      this.toastService.showErrorToast("Error","Please Enter a valid E-Mail and Password")
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
          this.authService.resetCookieToSessionCookie();
          this.authService.startSessionTimer();
        }

        this.toastService.showSuccessToast("Login", "Login succeeded")
        this.userService.getUpdatedUserInfo()
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
