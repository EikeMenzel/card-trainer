import { Component } from '@angular/core';
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
  userNotFound: boolean = false;
  userEmail: string = '';

  constructor(private http: HttpClient) { }

  isValidEmail(email: string): boolean {
    // Hier könntest du eine einfache Validierung für die Email-Adresse implementieren
    // Zum Beispiel:
    // return /\S+@\S+\.\S+/.test(email);
    return true; // Dummy-Validierung für das Beispiel
  }

  onContinue(email: string) {
    if (!this.isValidEmail(email)) {
      return;
    }

    this.userEmail = email;

    this.http.post<any>("/api/v1/forgot-password", { email: this.userEmail }, { observe: 'response' }).subscribe({
      next: (data) => {
        if (data.status === 200) {
          this.emailSent = true;
          this.userNotFound = false;
        } else if (data.status === 404) {
          this.userNotFound = true;
          this.emailSent = false;
        }
      },
      error: (err) => {
        console.error(err);
        this.userNotFound = true; // Assuming any error means user not found
        this.emailSent = false;
      }
    });
  }
}
