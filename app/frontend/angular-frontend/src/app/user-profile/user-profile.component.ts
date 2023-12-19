import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {HttpStatusCode} from "@angular/common/http";
import {CommonModule, NgClass} from "@angular/common";
import * as bootstrap from 'bootstrap';
import {UserInfoDTO} from "../models/UserInfoDTO";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faHouse} from "@fortawesome/free-solid-svg-icons";
import {UserService} from "../services/user-service/user.service";
import {ToastService} from "../services/toast-service/toast.service";
import {AuthService} from "../services/auth-service/auth-service";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
  standalone: true,
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  imports: [
    FormsModule, CommonModule, NgClass, FaIconComponent, RouterLink
  ]
})

export class UserProfileComponent implements OnInit {

  userProfile: UserInfoDTO = {
    username: '',
    email: '',
    cardsToLearn: 0,
    receiveLearnNotification: true,
    langCode: 'EN',
    achievementIds: []
  };

  newPassword: string = '';
  reenterNewPassword: string = '';
  private modalRef: NgbModalRef | undefined;
  buttonModalIsPressed: boolean = false;

  protected readonly faHouse = faHouse;

  constructor(
    private router: Router,
    private userService: UserService,
    private toast: ToastService,
    public authService: AuthService,
    private modalService: NgbModal
  ) {
  }

  ngOnInit(): void {
    this.getUserInfo();
    this.getAchievements();
  }

  validatePassword(): boolean {
    return this.newPassword.trim().length >= 8 && this.newPassword.trim().length < 72
  }

  openModal(content: any) {
    this.modalRef = this.modalService.open(content);
  }

  changePassword(): void {
    this.buttonModalIsPressed = true;
    if (!
      this.newPassword.trim() || !this.reenterNewPassword.trim()
    ) {
      this.toast.showErrorToast("Update Password", "Field cannot be empty")
      this.buttonModalIsPressed = false;
      return;
    }
    if (this.newPassword !== this.reenterNewPassword) {
      this.toast.showErrorToast("Update Password", "Passwords do not match")
      this.buttonModalIsPressed = false;
      this.emptyPasswordModalFields()
      return;
    }
    if (!this.validatePassword()) {
      this.toast.showErrorToast("Update Password", "You Password must be between 8 and 72 characters")
      this.buttonModalIsPressed = false;
      this.emptyPasswordModalFields()
      return;
    }

    this.userService.changePassword(this.newPassword).subscribe({
      next: () => {
        this.toast.showSuccessToast("Password update", 'Password updated successfully');
        if (this.modalRef) {
          this.buttonModalIsPressed = false;
          this.modalRef.close();
        }
      },
      error: (err) => {
        switch (err.status) {
          case HttpStatusCode.Unauthorized:
            this.toast.showErrorToast("Error", "You are not login. You will be send to the login screen")
            this.router.navigate(['/login']);
            break;
          case HttpStatusCode.BadRequest:
            this.toast.showErrorToast("Password update", 'Your password is too easy to guess. Please make sure you are using at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/');
            break;
          default:
            console.error(err)
            this.toast.showErrorToast("Password update", "An error occurred")
            break;
        }
        this.buttonModalIsPressed = false;
      }
    });
    this.emptyPasswordModalFields()
  }

  getUserInfo(): void {
    this.userProfile = this.userService.getUserInfoDTO()
  }

  saveProfile(): void {
    if (!this.userProfile.username.trim() || !this.userProfile.email.trim()) {
      this.toast.showErrorToast("Update Userinfo", "Please make sure that your Username and Email is not empty")
    }
    this.userProfile.receiveLearnNotification = (document.getElementById("receiveNotifications") as HTMLInputElement).checked
    this.userService.updateUserInfo(this.userProfile).subscribe({
      next: (data) => {
        this.userProfile = data;
        this.toast.showSuccessToast("Profile Update", "Your profile has been updated successful")
      },
      error: (err) => {
        switch (err.status) {
          case HttpStatusCode.BadRequest:
            this.toast.showErrorToast("Error", "Invalid data format or information.")
            break;
          case HttpStatusCode.Unauthorized:
            this.toast.showErrorToast("Error", "You are not login. You will be send to the login screen")
            this.router.navigate(['/login']);
            break;
          default:
            this.toast.showErrorToast("Error", "An error occurred while trying to save profile data")
            break;
        }
      }
    });
  }

  logoutWarningPopup(): void {
    if (confirm("Are you sure you want to logout?")) {
      this.authService.logout();
    }
  }

  emptyPasswordModalFields() {
    this.reenterNewPassword = "";
    this.newPassword = "";
  }


//TODO: Achievement handling
  getAchievements(): void {
  }
}
