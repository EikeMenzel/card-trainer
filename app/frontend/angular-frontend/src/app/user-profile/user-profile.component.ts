import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {HttpStatusCode} from "@angular/common/http";
import {CommonModule, NgClass} from "@angular/common";
import {UserInfoDTO} from "../models/UserInfoDTO";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faEye, faEyeSlash} from "@fortawesome/free-solid-svg-icons";
import {UserService} from "../services/user-service/user.service";
import {ToastService} from "../services/toast-service/toast.service";
import {AuthService} from "../services/auth-service/auth-service";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {BasePageComponent} from "../base-page/base-page.component";
import {AchievementDetailsDTO} from "../models/AchievementDetailsDTO";
import {AchievementService} from "../services/achievement-service/achievement-service";
import {TutorialComponent} from "../tutorial/tutorial.component";

@Component({
  standalone: true,
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  imports: [
    FormsModule,
    CommonModule,
    NgClass,
    FaIconComponent,
    RouterLink,
    BasePageComponent,
    TutorialComponent
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
  achievements: AchievementDetailsDTO[] = [];
  achievementImages: Map<number, string> = new Map();
  EMAIL_REGEX = "^[a-zA-Z0-9.%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*\.[a-zA-Z]{2,}(\.[a-zA-Z]{2,})?$";


  showPassword: boolean = false;
  showPasswordRepeat: boolean = false;
  protected readonly faEye = faEye;
  protected readonly faEyeSlash = faEyeSlash;

  emailError: string = "";
  nameError: string = "";
  passwordError: string = "";
  passwordRepeatError: string = "";

  constructor(
    private router: Router,
    private userService: UserService,
    private toast: ToastService,
    public authService: AuthService,
    private modalService: NgbModal,
    private achievementService: AchievementService
  ) {
  }

  ngOnInit(): void {
    this.userService.getUpdatedUserInfo();
    this.getUserInfo();
    this.getAchievements();
    this.checkScreenWidth();

    window.addEventListener('resize', () => {
      this.checkScreenWidth();
    });
  }

  changePassword(): void {
    this.buttonModalIsPressed = true;
    this.validatePasswordFields();
    if (this.passwordError || this.passwordRepeatError) {
      this.buttonModalIsPressed = false;
      return;
    }
    this.userService.changePassword(this.newPassword).subscribe({
      next: () => {
        this.toast.showSuccessToast("Password update", 'Password updated successfully');
        if (this.modalRef) {
          this.modalRef.close();
        }
      },
      error: (err) => {
        switch (err.status) {
          case HttpStatusCode.Unauthorized:
            this.toast.showErrorToast("Error", "You are not login. You will be send to the login screen")
            this.router.navigate(['/login']);
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

  validatePasswordFields(): void {
    this.passwordError = '';
    this.passwordRepeatError = '';

    if (!this.newPassword || this.newPassword.trim().length === 0) {
      this.passwordError = 'Field cannot be empty';
    } else if (this.newPassword.length < 8 || this.newPassword.length > 72) {
      this.passwordError = 'Your password must be between 8 and 72 characters';
    }

    if (!this.reenterNewPassword || this.reenterNewPassword.trim().length === 0) {
      this.passwordRepeatError = 'Field cannot be empty';
    } else if (this.newPassword !== this.reenterNewPassword) {
      this.passwordRepeatError = 'Passwords do not match';
    }
  }

  saveProfile(): void {
    this.passwordError = "";
    this.passwordRepeatError = "";

    if (!this.validateNameAndEmail()) {
      return;
    }

    const numberOfCards = this.userProfile.cardsToLearn;

    if (numberOfCards < 1) {
      this.toast.showInfoToast("Info", "Please select at least one card to learn.");
      return;
    }

    this.userProfile.receiveLearnNotification = (document.getElementById("receiveNotifications") as HTMLInputElement).checked;

    this.userService.updateUserInfo(this.userProfile).subscribe({
      next: (data) => {
        this.userProfile = data;
        this.toast.showSuccessToast("Profile Update", "Your profile has been updated successfully");
      },

      error: (err) => {
        switch (err.status) {
          case HttpStatusCode.Unauthorized:
            this.toast.showErrorToast("Error", "You are not logged in. You will be sent to the login screen");
            this.router.navigate(['/login']);
            break;
          default:
            console.error(err);
            this.toast.showErrorToast("Profile Update", "An error occurred");
            break;
        }
      }
    });
  }


  validateNameAndEmail(): boolean {
    this.nameError = '';
    this.emailError = '';
    let isValid = true;
    if (!this.userProfile.username || this.userProfile.username.trim().length === 0) {
      this.nameError = 'Field cannot be empty';
      isValid = false;
    } else if (this.userProfile.username.length < 4) {
      this.nameError = 'Username too short';
      isValid = false;
    } else if (this.userProfile.username.length > 30) {
      this.nameError = 'Username too long';
      isValid = false;
    }
    if (!this.userProfile.email || this.userProfile.email.trim().length === 0) {
      this.emailError = 'Field cannot be empty';
      isValid = false;
    } else if (!this.validateEmail(this.userProfile.email)) {
      this.emailError = 'Invalid email address';
      isValid = false;
    }
    return isValid;
  }
  validateEmail(email: string): boolean {
    return new RegExp(this.EMAIL_REGEX).test(email);
  }
  getUserInfo(): void {
    this.userProfile = this.userService.getUserInfoDTO()
  }

  openModal(content: any) {
    this.modalRef = this.modalService.open(content);
  }

  emptyPasswordModalFields() {
    this.reenterNewPassword = "";
    this.newPassword = "";
  }

  logoutWarningPopup(): void {
    if (confirm("Are you sure you want to logout?")) {
      this.authService.logout();
    }
  }

  getAchievements(): void {

    // First check whether the user has Achievement IDs
    if (this.userProfile.achievementIds && this.userProfile.achievementIds.length > 0) {
      this.userProfile.achievementIds.forEach(achievementId => {

        // Retrieve achievements based on the user's IDs
        this.achievementService.getAchievementById(achievementId).subscribe({
          next: (achievement) => {

            // Check if the achievement is not a daily one before pushing
            if (!achievement.daily) {
              this.achievements.push(achievement);

              // It then retrieves the corresponding image
              this.achievementService.getAchievementImage(achievement.imageId)
                .subscribe({
                  next: (imageBlob) => {
                    const imageUrl = URL.createObjectURL(imageBlob);
                    this.achievementImages.set(achievement.achievementId, imageUrl);
                  }
                });
            }
          },
          error: (err) => {
            console.error('Error fetching achievement:', err);
            switch (err.status) {
              case HttpStatusCode.BadRequest:
                this.toast.showErrorToast("Error", "Invalid request for achievement data");
                break;
              case HttpStatusCode.Unauthorized:
                this.toast.showErrorToast("Error", "You are not logged in. Redirecting to login screen");
                this.router.navigate(['/login']);
                break;
              case HttpStatusCode.NotFound:
                this.toast.showErrorToast("Error", "Achievement not found");
                break;
              default:
                this.toast.showErrorToast("Error", "An error occurred while fetching achievements");
                break;
            }
          }
        });
      });
    }
  }

  @ViewChild('achievementsModal') achievementsModal!: ElementRef;
  isSmallScreen: boolean = false;
  checkScreenWidth(): void {
    this.isSmallScreen = window.innerWidth <= 767;
  }
  openAchievementsModal(): void {
    this.modalService.open(this.achievementsModal, { centered: true });
  }
}
