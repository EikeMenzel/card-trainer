import {Component} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {HttpClient, HttpClientModule, HttpStatusCode} from "@angular/common/http";
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {RegisterRequestDTO} from "../models/RegisterRequestDTO";
import {NgIf} from "@angular/common";
import {RegisterSuccessfulComponent} from "../register-successful/register-successful.component";
import {MessageResponseDTO} from "../models/MessageResponseDTO";
import {ToastService} from "../services/toast-service/toast.service";
import {ToasterComponent} from "../toaster/toaster.component";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  imports: [
    FormsModule,
    HttpClientModule,
    RouterLink,
    NgIf,
    RegisterSuccessfulComponent,
    ToasterComponent,
    RouterLinkActive,
    FontAwesomeModule
  ],
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  email: string = "";
  loginSuccess: boolean = false;
  submitted = false;

  emailBorder: string = "var(--bg-main-color)";
  usernameBorder: string = "var(--bg-main-color)";
  passwordBorder: string = "var(--bg-main-color)";
  passwordRepeatBorder: string = "var(--bg-main-color)";

  getEmailError: boolean = false;
  getUsernameError: boolean = false;
  getPasswordError: boolean = false;
  getPasswordRepeatError: boolean = false;

  errorCode: string = "";

  buttonIsPressed:boolean = false;
  showPassword: boolean = false;
  showPasswordRepeat: boolean = false;
  faEye = faEye;
  faEyeSlash = faEyeSlash;

  constructor(
    private http: HttpClient,
    private toastService: ToastService) {
  }

  onSubmit(registerForm: NgForm) {

    this.getEmailError = false;
    this.getUsernameError = false;
    this.getPasswordError = false;
    this.getPasswordRepeatError = false;
    this.errorCode = ""

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

      this.email = email;

      this.http.post<RegisterRequestDTO>('/api/v1/register', registerRequest, {observe: 'response'})
        .subscribe({
          next: (response) => {
            if (response.status == HttpStatusCode.Created) {
              this.buttonIsPressed = false;
              this.loginSuccess = true;
            }
          },
          error: (error) => {
            const message: MessageResponseDTO = {
              status: error.error.status,
              response: error.error.message
            }
            const statusCode = error.status;
            this.errorCode = message.response;
            if (statusCode == HttpStatusCode.Conflict || statusCode == HttpStatusCode.BadRequest) {
              switch (message.status) {
                case 1:
                  this.emailBorder = "var(--primary-error-color)";
                  this.getEmailError = true;
                  break;
                case 2:
                  this.usernameBorder = "var(--primary-error-color)";
                  this.getUsernameError = true;
                  break;
                case 3:
                  this.passwordBorder = "var(--primary-error-color)";
                  this.passwordRepeatBorder = "var(--primary-error-color)";
                  this.getPasswordError = true;
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
    } else {
      if (email == "") {
        this.emailBorder = "var(--primary-error-color)";
      }
      if (username == "") {
        this.usernameBorder = "var(--primary-error-color)";
      }
      if (password == "") {
        this.passwordBorder = "var(--primary-error-color)";
      }
      if (passwordRepeat == "") {
        this.passwordRepeatBorder = "var(--primary-error-color)";
      }
      if (password != passwordRepeat) {
        this.errorCode = "Please make sure the Passwords are the same!"
        this.getPasswordRepeatError = true;
        this.passwordBorder = "var(--primary-error-color)";
        this.passwordRepeatBorder = "var(--primary-error-color)";
      }
      this.buttonIsPressed = false;
    }
  }
}
