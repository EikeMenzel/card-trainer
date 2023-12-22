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
  ],
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  email: string = "";
  loginSuccess: boolean = false;

  public emailBorder: string = "var(--bg-main-color)";
  public usernameBorder: string = "var(--bg-main-color)";
  public passwordBorder: string = "var(--bg-main-color)";
  public passwordRepeatBorder: string = "var(--bg-main-color)";

  public buttonIsPressed:boolean = false;

  constructor(private http: HttpClient, private toastService: ToastService, private router: Router) {
  }

  onSubmit(registerForm: NgForm) {
    this.buttonIsPressed = true;

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
            if (statusCode == HttpStatusCode.Conflict || statusCode == HttpStatusCode.BadRequest) {
              this.toastService.showErrorToast("Error", message.response)
              switch (message.status) {
                case 1:
                  this.emailBorder = "var(--primary-error-color)";
                  break;
                case 2:
                  this.usernameBorder = "var(--primary-error-color)";
                  break;
                case 3:
                  this.passwordBorder = "var(--primary-error-color)";
                  this.passwordRepeatBorder = "var(--primary-error-color)";
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
        this.toastService.showWarningToast("Warning", "E-Mail cannot be empty");
      }
      if (username == "") {
        this.usernameBorder = "var(--primary-error-color)";
        this.toastService.showWarningToast("Warning", "Username cannot be empty");
      }
      if (password == "") {
        this.passwordBorder = "var(--primary-error-color)";
        this.toastService.showWarningToast("Warning", "Password cannot be empty");
      }
      if (password != passwordRepeat) {
        this.passwordBorder = "var(--primary-error-color)";
        this.passwordRepeatBorder = "var(--primary-error-color)";
        this.toastService.showWarningToast("Warning", "Passwords do not match");
      }
      this.buttonIsPressed = false;
    }
  }
}
