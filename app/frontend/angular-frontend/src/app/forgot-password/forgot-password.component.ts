import {Component} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {HttpClientModule, HttpStatusCode} from '@angular/common/http';
import {CommonModule} from "@angular/common";
import {UserService} from "../services/user-service/user.service";
import {FormsModule} from "@angular/forms";
import {ToastService} from "../services/toast-service/toast.service";
import {ToasterComponent} from "../toaster/toaster.component";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  standalone: true,
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
  imports: [HttpClientModule, CommonModule, RouterLink, FormsModule, ToasterComponent, TranslateModule]
})

export class ForgotPasswordComponent {
  isLoading = false;

  constructor(
    private router: Router,
    private userService: UserService,
    private toast: ToastService,
    private translate: TranslateService
  ) {
  }

  emailError: string = ""

  isValidEmail(email: string) {
    const emailRegex = new RegExp(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*\.[a-zA-Z]{2,}(\.[a-zA-Z]+)?$/);
    if (!emailRegex.test(email) && email.length <= 64) {
      this.emailError = this.translate.instant("email_invalid")
      return false;
    }
    return true;
  }

  onSubmit(email: string) {
    this.emailError = ""
    if (email === ""){
      this.emailError = this.translate.instant("field_cannot_be_empty")
      return;
    }

    if (!this.isValidEmail(email)) {
      return;
    }

    this.isLoading = true

    this.userService.sendResetPasswordRequest(email).subscribe({
      next: (data) => {
        if (data.status == HttpStatusCode.Accepted) {
          this.router.navigate(["/"])
          this.toast.showSuccessToast(this.translate.instant("success"), this.translate.instant("password_reset_request_part_1") + "" + email + "" + this.translate.instant("password_reset_request_part_2"))
        } else {
          this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("we_could_not_send_password_reset_mail"))
        }
        this.isLoading = false;

      },
      error: (err) => {
        this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("we_could_not_send_password_reset_mail"))
        this.isLoading = false;
      }
    });
  }

}
