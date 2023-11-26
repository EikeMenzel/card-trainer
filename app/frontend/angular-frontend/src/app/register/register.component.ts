import {Component} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {HttpClient, HttpClientModule, HttpStatusCode} from "@angular/common/http";
import {RouterLink} from "@angular/router";
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
  public emailBorder: string = "white";
  public usernameBorder: string = "white"
  public passwordBorder: string = "white";
  public passwordRepeatBorder: string = "white"

  constructor(http: HttpClient, private toastService: ToastService) {
    this.http = http;
  }

  onSubmit(registerForm: NgForm) {
    const email: String = registerForm.value["email"]
    const username: String = registerForm.value["username"]
    const password: String = registerForm.value["password"]
    const passwordRepeat: String = registerForm.value["passwordRepeat"]

    this.emailBorder = "white";
    this.usernameBorder = "white"
    this.passwordBorder = "white";
    this.passwordRepeatBorder = "white"

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
                  this.emailBorder = "red";
                  break;
                case 2:
                  this.username = "red";
                  break;
                case 3:
                  this.passwordBorder = "red";
                  this.passwordRepeatBorder = "red";
                  break;
              }
            }
            if (statusCode == HttpStatusCode.InternalServerError)
              this.toastService.showErrorToast("Error", "Server cannot be reached");
          }
        })
    } else {
      if (email == "") {
        this.emailBorder = "red"
        this.toastService.showWarningToast("Warning", "E-Mail cannot be empty");
      }
      if (username == "") {
        this.usernameBorder = "red"
        this.toastService.showWarningToast("Warning", "Username cannot be empty");
      }
      if (password == "") {
        this.passwordBorder = "red"
        this.toastService.showWarningToast("Warning", "Password cannot be empty");
      }
      if (password != passwordRepeat) {
        this.passwordBorder = "red"
        this.passwordRepeatBorder = "red"
        this.toastService.showWarningToast("Warning", "Passwords do not match");
      }
    }
  }
}
