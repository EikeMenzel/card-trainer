import {Component} from '@angular/core';
import {
  ReactiveFormsModule,
  FormsModule,
  NgForm
} from '@angular/forms';
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {HttpClient, HttpClientModule, HttpStatusCode} from "@angular/common/http";
import {ToastService} from "../services/toast-service/toast.service";
import {ToasterComponent} from "../toaster/toaster.component";
import {AuthService} from "../services/auth-service/auth-service";
import {UserService} from "../services/user-service/user.service";
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {faEye, faEyeSlash} from '@fortawesome/free-solid-svg-icons';
import {WebsocketService} from "../services/websocket/websocket-service";

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [
    ReactiveFormsModule,
    FormsModule,
    RouterLink,
    HttpClientModule,
    ToasterComponent,
    RouterLinkActive,
    FontAwesomeModule
  ]
})
export class LoginComponent {

  public emailBorder: string = "var(--bg-main-color)";
  public passwordBorder: string = "var(--bg-main-color)";

  showPassword: boolean = false;
  faEye = faEye;
  faEyeSlash = faEyeSlash;


  constructor(
    private http: HttpClient,
    private toastService: ToastService,
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private websocketService: WebsocketService
  ) {
  }

  private errorPassword() {
    this.passwordBorder = "var(--primary-error-color)";
    setTimeout(() => {
      this.passwordBorder = "var(--bg-main-color)";
    }, 3000)
  }

  private errorEmail() {
    this.emailBorder = "var(--primary-error-color)";
    setTimeout(() => {
      this.emailBorder = "var(--bg-main-color)";
    }, 3000)
  }

  onSubmit(loginForm: NgForm) {
    if (!loginForm.valid) {
      this.toastService.showErrorToast("Error", "Please Enter a valid E-Mail and Password")
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

        if (loginForm.value["remember"] === false) {
          this.authService.resetCookieToSessionCookie();
          this.authService.startSessionTimer();
        }

        this.userService.getUpdatedUserInfo()
        this.websocketService.rxStompService.activate();
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
