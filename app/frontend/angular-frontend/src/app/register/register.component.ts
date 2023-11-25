import {Component} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {HttpClient, HttpClientModule, HttpStatusCode} from "@angular/common/http";
import {RouterLink} from "@angular/router";
import {RegisterRequestDTO} from "../models/RegisterRequestDTO";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {NgIf} from "@angular/common";
import {RegisterSuccessfulComponent} from "../register-successful/register-successful.component";

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  imports: [
    FormsModule,
    HttpClientModule,
    RouterLink,
    NgIf,
    RegisterSuccessfulComponent
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

  constructor(http: HttpClient) {
    this.http = http;
  }

  onSubmit(registerForm: NgForm) {
    this.loginSuccess = true;
    const email: String = registerForm.value["_email"]
    const username: String = registerForm.value["username"]
    const password: String = registerForm.value["password"]
    const passwordRepeat: String = registerForm.value["passwordRepeat"]
    if (password == passwordRepeat && email != "" && username != "") {
      const registerRequest: RegisterRequestDTO = {
        username: this.username,
        email: this.email,
        password: this.password
      };
      console.log(registerRequest);

      this.http.post<RegisterRequestDTO>('/api/v1/register', registerRequest, {observe: 'response'})
        .subscribe({
          next: (response) => {
            const statusCode = response.status;
            console.log("Successfully created Account diese")
          },
          error: (error) => {
            const statusCode = error.status;
            if (statusCode == HttpStatusCode.Conflict)
              console.log("Error: " + error.toString());
            if (statusCode == HttpStatusCode.InternalServerError)
              console.log("Error: No contact to Server");
          }
        })
    }
  }

  protected readonly RegisterSuccessfulComponent = RegisterSuccessfulComponent;
}


/*.subscribe(
         (response) => {
          const statusCode = response.status;
        },
         (error) => {
          const statusCode = error.status;
          if(statusCode == HttpStatusCode.Conflict)
            console.log("Email is already taken"); //TODO replace with toast.
        })
    } else {
      //TODO Toast for Password not correct
    } */
