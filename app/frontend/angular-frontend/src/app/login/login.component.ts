import {Component, NgModule} from '@angular/core';
import {FormBuilder, FormGroup, Validators, ReactiveFormsModule, NgForm, FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, FormsModule]
})
export class LoginComponent {
  loginForm: FormGroup;
  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      password: ['', Validators.required],
      remember: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  submitLogin(loginform: NgForm) {

    console.log('Your form data : ', loginform.value);
    if (this.loginForm.valid) {
      console.log(this.loginForm.value);
    }
  }
}
