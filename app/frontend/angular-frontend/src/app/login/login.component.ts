import {Component} from '@angular/core';
import {
  ReactiveFormsModule,
  FormsModule,
  FormControl, FormGroup, Validators, NgForm
} from '@angular/forms';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, FormsModule, RouterLink]
})
export class LoginComponent {

  onSubmit(loginForm: NgForm) {
    if(loginForm.valid) {
      if (loginForm.value["remember"] == "") {
        loginForm.value["remember"] = false;
      }
      console.log(loginForm.value)
    }
  }
}
