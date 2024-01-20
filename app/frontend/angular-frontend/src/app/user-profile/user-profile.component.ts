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
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {CookieService} from "ngx-cookie-service";

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
    TutorialComponent,
    TranslateModule
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

  @ViewChild('content') private modalReference: ElementRef | undefined;
  @ViewChild('achievementsModal') achievementsModal: ElementRef | undefined;

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
    private achievementService: AchievementService,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.userService.getUpdatedUserInfo();
    this.getUserInfo();
    this.getAchievements();
    this.checkScreenWidth();

    window.addEventListener('resize', () => {
      this.checkScreenWidth();
    });
  }

  ngOnDestroy() {
    this.modalRef?.close()
    this.achievementModalRef?.close()
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
        this.toast.showErrorToast(this.translate.instant("password_update"), this.translate.instant("password_update_success"),)
        if (this.modalRef) {
          this.modalRef.close();
        }
      },
      error: (err) => {
        switch (err.status) {
          case HttpStatusCode.Unauthorized:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("your_not_signed_in"))
            this.router.navigate(['/login']);
            break;
          default:
            console.error(err)
            this.toast.showErrorToast(this.translate.instant("password_update"), this.translate.instant("an_error_occurred"))
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
      this.passwordError = this.translate.instant("field_cannot_be_empty");
    } else if (this.newPassword.length < 8 || this.newPassword.length > 72) {
      this.passwordError = this.translate.instant("password_length_min_max");
    } else if (!/\d/.test(this.newPassword)) {
      // Check if the password contains at least one number
      this.passwordError = this.translate.instant("password_requirements");
    } else if (!/[~`!@#\$%\^&*\(\)_\-\+=\{\[\}\]\|:;<,>\.?\/]/.test(this.newPassword)) {
      // Check if the password contains at least one special character
      this.passwordError = this.translate.instant("password_requirements");
    }

    if (!this.reenterNewPassword || this.reenterNewPassword.trim().length === 0) {
      this.passwordRepeatError = this.translate.instant("field_cannot_be_empty");
    } else if (this.newPassword !== this.reenterNewPassword) {
      this.passwordRepeatError = this.translate.instant("passwords_not_match");
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
      this.toast.showInfoToast(this.translate.instant("info"), this.translate.instant("select_one_card_to_learn"));
      return;
    }

    this.userProfile.receiveLearnNotification = (document.getElementById("receiveNotifications") as HTMLInputElement).checked;

    this.userService.updateUserInfo(this.userProfile).subscribe({
      next: (data) => {
        this.userProfile = data
        this.translate.use(this.userProfile.langCode.toLowerCase())

        this.toast.showSuccessToast(this.translate.instant("profile_update"), this.translate.instant("profile_updated_successfully"));
      },

      error: (err) => {
        switch (err.status) {
          case HttpStatusCode.Unauthorized:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("your_are_not_logged_in_send_to_login"));
            this.router.navigate(['/login']);
            break;
          default:
            console.error(err);
            this.toast.showErrorToast(this.translate.instant("profile_update"), this.translate.instant("an_error_occurred"));
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
      this.nameError = this.translate.instant("field_cannot_be_empty");
      isValid = false;
    } else if (this.userProfile.username.length < 4) {
      this.nameError = this.translate.instant("username_too_short");
      isValid = false;
    } else if (this.userProfile.username.length > 30) {
      this.nameError = this.translate.instant("username_too_long");
      isValid = false;
    }
    if (!this.userProfile.email || this.userProfile.email.trim().length === 0) {
      this.emailError = this.translate.instant("field_cannot_be_empty");
      isValid = false;
    } else if (!this.validateEmail(this.userProfile.email)) {
      this.emailError = this.translate.instant("email_invalid");
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
    this.modalRef = this.modalService.open(content, {
      ariaLabelledBy: 'modal-basic-title',
      beforeDismiss: () => {
        this.newPassword = "";
        this.reenterNewPassword = "";
        this.passwordError = ""
        this.passwordRepeatError = ""
        return true;
      }
    });
  }

  emptyPasswordModalFields() {
    this.reenterNewPassword = "";
    this.newPassword = "";
  }

  logoutWarningPopup(): void {
    if (confirm(this.translate.instant("are_you_sure_logout"))) {
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
                this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("achievement_request_invalid"));
                break;
              case HttpStatusCode.Unauthorized:
                this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("not_logged_in_redirect"));
                this.router.navigate(['/login']);
                break;
              case HttpStatusCode.NotFound:
                this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("achievement_not_found"));
                break;
              default:
                this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("achievement_default_error"));
                break;
            }
          }
        });
      });
    }
  }

  isSmallScreen: boolean = false;
  private achievementModalRef: NgbModalRef | undefined
  checkScreenWidth(): void {
    this.isSmallScreen = window.innerWidth <= 767;
  }
  openAchievementsModal(): void {
    this.achievementModalRef = this.modalService.open(this.achievementsModal, { centered: true });
  }
}
