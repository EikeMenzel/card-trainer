import {Component} from '@angular/core';
import {Router, ActivatedRoute, RouterLink} from '@angular/router';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {UserService} from "../services/user-service/user.service";
import {ToastService} from "../services/toast-service/toast.service";
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faEye, faEyeSlash} from "@fortawesome/free-solid-svg-icons";
import {HttpStatusCode} from "@angular/common/http";
import {TranslateModule, TranslateService} from '@ngx-translate/core';


@Component({
  standalone: true,
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  imports: [
    NgIf,
    RouterLink,
    CommonModule,
    FormsModule,
    FaIconComponent,
    NgOptimizedImage,
    TranslateModule
  ],
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  isLoading: boolean = false;
  protected readonly faEye = faEye;
  protected readonly faEyeSlash = faEyeSlash;
  showPassword: boolean = false;
  showPasswordRepeat: boolean = false;
  isButtonPressed: boolean = false;
  passwordError: string = "";
  passwordRepeatError: string = "";
  newPassword: string = '';
  reenterNewPassword: string = '';
  private statusCode: HttpStatusCode | undefined;


  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
    private toast: ToastService,
    private toastService: ToastService,
    private translate: TranslateService) {
  }

  submit(newPw: string) {

    this.passwordError = "";
    this.passwordRepeatError = "";

    // Check if newPassword and reenterNewPassword are empty
    if (!this.newPassword || !this.reenterNewPassword) {
      this.passwordError = this.translate.instant("field_cannot_be_empty");
      this.passwordRepeatError = this.translate.instant("field_cannot_be_empty");
      return;
    }

    // Check if newPassword and reenterNewPassword match
    if (this.newPassword !== this.reenterNewPassword) {
      this.passwordError = this.translate.instant("passwords_do_not_match");
      this.passwordRepeatError = this.translate.instant("passwords_do_not_match");
      return;
    }

    // Check if newPassword length is between 8 and 72 characters
    if (this.newPassword.length < 8) {
      this.passwordError = this.translate.instant("password_min_length");
      return;
    } else if (this.newPassword.length > 72) {
      this.passwordError = this.translate.instant("password_max_length");
      return;
    }

    this.isButtonPressed = true;
    this.isLoading = true;
    this.route.queryParams.subscribe((value) => {
      const token = value['token'];
      const email = value['email'];
      this.userService.requestPasswordReset(newPw, token, email).subscribe({
        next: res => {
          this.toast.showSuccessToast(this.translate.instant("password_reset"), this.translate.instant("password_reset_success"))
          this.router.navigate(["/login"]);
          this.isButtonPressed = false;
          this.isLoading = false
        },
        error: err => {
          this.isLoading = false
          const errStatus = err.error.status as number
          switch (errStatus) {
            case 3: {
              this.passwordError = this.translate.instant("password_constraint");
              break
            }
            case 4: {
              this.passwordError = this.translate.instant("verification_constraint");
              break
            }
            case 5: {
              this.passwordError = this.translate.instant("reset_token_response");
              break
            }
            default: {
              this.passwordError = this.translate.instant("password_reset_default");
            }
          }
          if (this.statusCode == HttpStatusCode.InternalServerError) {
            this.toast.showErrorToast(this.translate.instant("password_reset"), this.translate.instant("server_unreachable"))
          }
          this.isButtonPressed = false;
          return;
        },
      });
    });
  }
}
