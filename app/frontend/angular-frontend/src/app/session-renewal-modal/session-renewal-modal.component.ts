import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {MatButtonModule} from "@angular/material/button";
@Component({
  selector: 'app-session-renewal-modal',
  standalone: true,
  imports: [CommonModule, MatDialogActions, MatDialogContent, MatDialogTitle, MatButtonModule],
  templateUrl: './session-renewal-modal.component.html',
  styleUrl: './session-renewal-modal.component.css'
})
export class SessionRenewalModalComponent {
  constructor(private dialogRef: MatDialogRef<SessionRenewalModalComponent>) {}
  public renewSession(): void {
    this.dialogRef.close('renewed');
  }

  public logout(): void {
    this.dialogRef.close('logged_out');
  }
}
