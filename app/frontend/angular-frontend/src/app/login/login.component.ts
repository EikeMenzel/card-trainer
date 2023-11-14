import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
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
  submitLogin() {
    if (this.loginForm.valid) {
      console.log(this.loginForm.value);
    }
  }
}
