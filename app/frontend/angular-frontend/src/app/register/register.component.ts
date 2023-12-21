import {Component} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {HttpClient, HttpClientModule, HttpStatusCode} from "@angular/common/http";
import {RouterLink, RouterLinkActive} from "@angular/router";
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

  email: string = '';
  username: string = '';
  password: string = '';
  passwordRepeat: string = '';
  loginSuccess = false;

  private http: HttpClient
  public emailBorder: string = "#F0FDEE";
  public usernameBorder: string = "#F0FDEE"
  public passwordBorder: string = "#F0FDEE";
  public passwordRepeatBorder: string = "#F0FDEE"

  public buttonIsPressed:boolean = false;

  constructor(http: HttpClient, private toastService: ToastService) {
    this.http = http;
  }

  onSubmit(registerForm: NgForm) {
    this.buttonIsPressed = true;

    const email: String = registerForm.value["email"]
    const username: String = registerForm.value["username"]
    const password: String = registerForm.value["password"]
    const passwordRepeat: String = registerForm.value["passwordRepeat"]

    this.emailBorder = "#F0FDEE";
    this.usernameBorder = "#F0FDEE"
    this.passwordBorder = "#F0FDEE";
    this.passwordRepeatBorder = "#F0FDEE"

    if (password != "" && password == passwordRepeat && email != "" && username != "") {
      const registerRequest: RegisterRequestDTO = {
        username: this.username,
        email: this.email,
        password: this.password
      };

      this.http.post<RegisterRequestDTO>('/api/v1/register', registerRequest, {observe: 'response'})
        .subscribe({
          next: (response) => {
            if (response.status == HttpStatusCode.Created) {
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
                  this.emailBorder = "#FF6961";
                  break;
                case 2:
                  this.usernameBorder = "#FF6961";
                  break;
                case 3:
                  this.passwordBorder = "#FF6961";
                  this.passwordRepeatBorder = "#FF6961";
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
        this.emailBorder = "#FF6961"
        this.toastService.showWarningToast("Warning", "E-Mail cannot be empty");
      }
      if (username == "") {
        this.usernameBorder = "#FF6961"
        this.toastService.showWarningToast("Warning", "Username cannot be empty");
      }
      if (password == "") {
        this.passwordBorder = "#FF6961"
        this.toastService.showWarningToast("Warning", "Password cannot be empty");
      }
      if (password != passwordRepeat) {
        this.passwordBorder = "#FF6961"
        this.passwordRepeatBorder = "#FF6961"
        this.toastService.showWarningToast("Warning", "Passwords do not match");
      }
      this.buttonIsPressed = false;
    }
  }
}
