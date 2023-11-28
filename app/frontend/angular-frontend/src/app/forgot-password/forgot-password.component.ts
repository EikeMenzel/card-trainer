import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {CommonModule} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
  imports: [HttpClientModule, CommonModule]
})

export class ForgotPasswordComponent {
  emailSent: boolean = false;
  userEmail: string = '';
  errorMessage: string = '';
  constructor(private http: HttpClient, private router: Router) { }

  isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  goBackToLogin() {
    this.router.navigate(['/login']);
  }

  onContinue(email: string) {
    if (!this.isValidEmail(email)) {
      return;
    }

    this.userEmail = email;

    this.http.post<any>("/api/v1/password/reset", { email: this.userEmail }, { observe: 'response' })
      .subscribe({
        next: (data) => {
          if (data.status === 202) {
            this.emailSent = true;
            this.errorMessage = '';           // Reset error message
          } else if (data.status === 500) {
            this.emailSent = false;
            this.errorMessage = 'Service not available';
          } else {
            this.emailSent = false;
          }
          this.openConfirmationPopup()
      },
      error: (err) => {
        console.error(err);
        this.emailSent = false;
        this.errorMessage = 'An error occurred';
      }
    });
  }

  openConfirmationPopup() {
    const confirmationText = `If your email matches an existing account we will send you a password reset email to ${this.userEmail}. Please follow the steps provided in the email to reset your password.`;
    const popupWindow = window.open('', '_blank', 'width=600,height=400');
    if (popupWindow) {
      popupWindow.document.write(`<html><body><p>${confirmationText}</p></body></html>`);
    } else {
      alert('Popup blocked. Please allow popups for this site to see the confirmation.');
    }
  }
}
