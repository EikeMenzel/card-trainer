import {Component, OnInit} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {HttpClientModule, HttpStatusCode} from "@angular/common/http";
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {RegisterRequestDTO} from "../models/RegisterRequestDTO";
import {NgIf} from "@angular/common";
import {RegisterSuccessfulComponent} from "../register-successful/register-successful.component";
import {MessageResponseDTO} from "../models/MessageResponseDTO";
import {ToastService} from "../services/toast-service/toast.service";
import {ToasterComponent} from "../toaster/toaster.component";
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {faEye, faEyeSlash} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../services/user-service/user.service";
import {ErrorHandlerService} from "../services/error-handler-service/error-handler.service";
import {AuthService} from "../services/auth-service/auth-service";

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  imports: [
    FormsModule,
    RouterLink,
    NgIf,
    RegisterSuccessfulComponent,
    ToasterComponent,
    RouterLinkActive,
    FontAwesomeModule
  ],
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  email: string = "";
  loginSuccess: boolean = false;
  submitted = false;

  emailBorder: string = "var(--bg-main-color)";
  usernameBorder: string = "var(--bg-main-color)";
  passwordBorder: string = "var(--bg-main-color)";
  passwordRepeatBorder: string = "var(--bg-main-color)";

  buttonIsPressed: boolean = false;
  showPassword: boolean = false;
  showPasswordRepeat: boolean = false;
  faEye = faEye;
  faEyeSlash = faEyeSlash;

  emailError: string = "";
  usernameError: string = "";
  passwordError: string = "";
  passwordRepeatError: string = "";


  constructor(
    private toastService: ToastService,
    private userService: UserService,
    private errHandlerService: ErrorHandlerService,
    private authService: AuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn) {
      this.toastService.showWarningToast("Register", "You are already logged in. If you want to register again, please logout first.");
      this.router.navigate(['/']);
    }
  }

  validateInput(field: string, value: string) { //Checks if the Input fields are empty or not
    switch (field) {
      case 'email':
        if (value != "") {
          this.emailError = "";
          this.emailBorder = "var(--bg-main-color)";
        } else {
          this.emailError = "Please enter a valid email.";
          this.emailBorder = "var(--primary-error-color)";
        }
        break;
      case 'username':
        if (value != "") {
          this.usernameError = "";
          this.usernameBorder = "var(--bg-main-color)";
        } else {
          this.usernameError = "Username is required.";
          this.usernameBorder = "var(--primary-error-color)";
        }
        break;
      case 'password':
        if (value != "") {
          this.passwordError = "";
          this.passwordBorder = "var(--bg-main-color)";
        } else {
          this.passwordError = "Password is required.";
          this.passwordBorder = "var(--primary-error-color)";
        }
        break;
      case 'passwordRepeat':
        if (value != "") {
          this.passwordRepeatError = "";
          this.passwordRepeatBorder = "var(--bg-main-color)";
        } else {
          this.passwordRepeatError = "Password is required.";
          this.passwordRepeatBorder = "var(--primary-error-color)";
        }
        break;
    }
  }


  onSubmit(registerForm: NgForm) {
    this.emailError = "";
    this.usernameError = "";
    this.passwordError = "";
    this.passwordRepeatError = "";

    this.buttonIsPressed = true;
    this.submitted = true;

    const email: string = registerForm.value["email"]
    const username: string = registerForm.value["username"]
    const password: string = registerForm.value["password"]
    const passwordRepeat: string = registerForm.value["password-repeat"]

    this.emailBorder = "var(--bg-main-color)";
    this.usernameBorder = "var(--bg-main-color)";
    this.passwordBorder = "var(--bg-main-color)";
    this.passwordRepeatBorder = "var(--bg-main-color)";

    if (password != "" && password == passwordRepeat && email != "" && username != "") {
      const registerRequest: RegisterRequestDTO = {
        username: username,
        email: email,
        password: password
      };

      if (this.authService.isLoggedIn) {
        this.authService.removeCookie();
      }

      this.userService.registerUser(registerRequest).subscribe({
        next: (response) => {
          if (response.status == HttpStatusCode.Created) {
            this.buttonIsPressed = false;
            this.loginSuccess = true;
          }
        },
        error: (err) => {
          const message: MessageResponseDTO = {
            status: err.error.status,
            response: err.error.message
          }
          const statusCode = err.status;
          if (statusCode == HttpStatusCode.Conflict || statusCode == HttpStatusCode.BadRequest) {
            switch (message.status) {
              case 1:
                this.emailBorder = "var(--primary-error-color)";
                this.emailError = this.errHandlerService.getErrorMessageFromResponse(message);
                break;
              case 2:
                this.usernameBorder = "var(--primary-error-color)";
                this.usernameError = this.errHandlerService.getErrorMessageFromResponse(message);
                break;
              case 3:
                this.passwordBorder = "var(--primary-error-color)";
                this.passwordRepeatBorder = "var(--primary-error-color)";
                this.passwordError = this.errHandlerService.getErrorMessageFromResponse(message);
                break;
            }
          }
          if (statusCode == HttpStatusCode.InternalServerError) {
            this.toastService.showErrorToast("Error", "Server cannot be reached");
          }
          this.buttonIsPressed = false;
          return;
        }
      })
    } else { //Checks if the Input Fields are empty by the time a Submit was sent
      if (email == "") {
        this.emailBorder = "var(--primary-error-color)";
        this.emailError = "Please enter a valid email."
      }
      if (username == "") {
        this.usernameBorder = "var(--primary-error-color)";
        this.usernameError = "Username is required."
      }
      if (password == "") {
        this.passwordBorder = "var(--primary-error-color)";
        this.passwordError = "Password is required."
      }
      if (passwordRepeat == "") {
        this.passwordRepeatBorder = "var(--primary-error-color)";
        this.passwordRepeatError = "Password is required."
      }
      if (password != passwordRepeat && passwordRepeat != "") {
        this.passwordBorder = "var(--primary-error-color)";
        this.passwordRepeatBorder = "var(--primary-error-color)";
        this.passwordRepeatError = "Passwords are not the same!"
      }
      this.buttonIsPressed = false;
    }
  }
}
