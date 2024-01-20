import {Component, Input} from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterLink} from "@angular/router";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-register-successful',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslateModule],
  templateUrl: './register-successful.component.html',
  styleUrl: './register-successful.component.css'
})


export class RegisterSuccessfulComponent {
  @Input() userEmail: string = "Unknown"
}
