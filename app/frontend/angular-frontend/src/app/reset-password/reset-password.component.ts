import {Component} from '@angular/core';
import {Router, ActivatedRoute, RouterLink} from '@angular/router';
import {NgIf} from "@angular/common";
import {UserService} from "../services/user-service/user.service";
import {ToastService} from "../services/toast-service/toast.service";
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";


@Component({
  standalone: true,
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  imports: [
    NgIf,
    RouterLink,
    CommonModule,
    FormsModule
  ],
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  isLoading: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute, private userService: UserService, private toast: ToastService) {
  }

  comparePassword(firstPw: string, secondPw: string) {
    return firstPw !== secondPw && secondPw !== ''
  }

  submit(newPw: string) {
    if (newPw.length < 8) {
      this.toast.showErrorToast("Password Error", "Password must be at least 8 characters long")
      return
    }

    if (newPw.length > 72) {
      this.toast.showErrorToast("Password Error", "Password can not be longer than 72 characters")
      return
    }

    this.isLoading = true
    this.route.queryParams.subscribe(value => {
      const token = value["token"]
      const email = value["email"]
      this.userService.requestPasswordReset(newPw, token, email).subscribe({
        next: res => {
          this.toast.showSuccessToast("Password Reset", "Your password reset was a success Have fun with your account and happy learning ❤️")
          this.isLoading = false
        },
        error: err => {
          console.log(err)
          this.isLoading = false
          const errStatus = err.error.status as number
          switch (errStatus) {
            case 3: {
              this.toast.showErrorToast("Password Reset", "Error, Please make sure you are using at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/")
              break
            }
            case 4: {
              this.toast.showErrorToast("Password Reset", "Authentication failed: The user is not verified")
              break
            }
            case 5: {
              this.toast.showErrorToast("Password Reset", "The reset-token is valid, expired or already used. Please request another password reset")
              break
            }
            default: {
              this.toast.showErrorToast("Password Reset", "An error occurred while trying to reset your password")
            }
          }
        }
      })
    })
  }
}
