import {Component} from '@angular/core';
import {
  ReactiveFormsModule,
  FormsModule,
  FormControl, FormGroup, Validators
} from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, FormsModule]
})
export class LoginComponent {
  email = new FormControl('');
  password = new FormControl('');
  remember = new FormControl('');
  loginGroup = new FormGroup({
    email: new FormControl(this.email.value, [Validators.email, Validators.required]),
  });
  constructor() {

  }

  submitLogin() {
    if (this.loginGroup.valid) {
      console.log(this.email.value)
    } else {
      console.log("Not valid")
    }
  }
}
