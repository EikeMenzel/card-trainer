import {Component} from '@angular/core';
import {Router, ActivatedRoute, RouterLink} from '@angular/router';
import {NgIf} from "@angular/common";
import {UserService} from "../services/user-service/user.service";
import {ToastService} from "../services/toast-service/toast.service";
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";
import {TranslateModule, TranslateService} from "@ngx-translate/core";


@Component({
  standalone: true,
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  imports: [
    NgIf,
    RouterLink,
    CommonModule,
    FormsModule,
    TranslateModule
  ],
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
    private toast: ToastService,
    private translate: TranslateService
  ) {}

  comparePassword(firstPw: string, secondPw: string) {
    return firstPw !== secondPw && secondPw !== ''
  }

  submit(newPw: string) {
    if (newPw.length < 8) {
      this.toast.showErrorToast(this.translate.instant("password_error"), this.translate.instant("password_min_length"))
      return
    }

    if (newPw.length > 72) {
      this.toast.showErrorToast(this.translate.instant("password_error"), this.translate.instant("password_max_length"))
      return
    }

    this.isLoading = true
    this.route.queryParams.subscribe(value => {
      const token = value["token"]
      const email = value["email"]
      this.userService.requestPasswordReset(newPw, token, email).subscribe({
        next: res => {
          this.toast.showSuccessToast(this.translate.instant("password_reset"), this.translate.instant("password_reset_success"))
          this.router.navigate(["/login"]);
          this.isLoading = false
        },
        error: err => {
          console.log(err)
          this.isLoading = false
          const errStatus = err.error.status as number
          switch (errStatus) {
            case 3: {
              this.toast.showErrorToast(this.translate.instant("password_reset"), this.translate.instant("password_constraint"))
              break
            }
            case 4: {
              this.toast.showErrorToast(this.translate.instant("password_reset"), this.translate.instant("verification_constraint"))
              break
            }
            case 5: {
              this.toast.showErrorToast(this.translate.instant("password_reset"), this.translate.instant("reset_token_response"))
              break
            }
            default: {
              this.toast.showErrorToast(this.translate.instant("password_reset"), this.translate.instant("password_reset_default"))
            }
          }
        }
      })
    })
  }
}
