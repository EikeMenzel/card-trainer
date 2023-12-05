import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {HttpClientModule, HttpStatusCode} from '@angular/common/http';
import {CommonModule} from "@angular/common";
import {UserService} from "../services/user-service/user.service";
import {FormsModule} from "@angular/forms";
import {ToastService} from "../services/toast-service/toast.service";
import {ToasterComponent} from "../toaster/toaster.component";

@Component({
  standalone: true,
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
  imports: [HttpClientModule, CommonModule, RouterLink, FormsModule, ToasterComponent]
})

export class ForgotPasswordComponent {
  isLoading = false;

  constructor(private router: Router, private userService: UserService, private toast: ToastService) { }

  isValidEmail(email: string): boolean {
    const emailRegex = new RegExp(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*\.[a-zA-Z]{2,}(\.[a-zA-Z]+)?$/);
    return emailRegex.test(email);
  }

  onSubmit(email: string) {
    if (!this.isValidEmail(email)) {
      return;
    }

    this.isLoading = true

    this.userService.sendResetPasswordRequest(email).subscribe({
      next: (data) => {
        if (data.status == HttpStatusCode.Accepted) {
          this.toast.showSuccessToast("Success",  "We sent you an email to " + email + ". Please follow the steps provided in the email to reset your password. Sending the E-Mail could take a while.")
          this.router.navigate(["/"])
        } else {
          this.toast.showErrorToast("Error", "We could not send you an password reset email")
        }
        this.isLoading = false;

      },
      error: (err) => {
        this.toast.showErrorToast("Error", "The password reset request could not be send")
        this.isLoading = false;
      }
    });
  }

  checkEmail() {
    return;
  }
}
