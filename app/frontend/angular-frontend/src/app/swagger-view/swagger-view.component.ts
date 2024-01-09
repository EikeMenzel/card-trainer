import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SwaggerUIBundle, SwaggerUIStandalonePreset} from 'swagger-ui-dist';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
@Component({
  selector: 'app-swagger-view',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './swagger-view.component.html',
  styleUrl: './swagger-view.component.css'
})
export class SwaggerViewComponent implements OnInit {
  serviceDisplayNames = [
    ['auth', 'Authentication Service'],
    ['mail', 'Mail Service'],
    ['user', 'User Service'],
    ['cards', 'Cards Service']
  ];
  selectedService = 'auth';

  ngOnInit() {
    SwaggerUIBundle({
      url: this.getSwaggerUrl(),
      domNode: document.getElementById('swagger-ui'),
      presets: [SwaggerUIBundle['presets']['apis'], SwaggerUIStandalonePreset],
      layout: 'StandaloneLayout'
    });
  }

  getSwaggerUrl(): string {
    return `/api/v1/${this.selectedService}/v3/api-docs`;
  }

  selectService(service: string): void {
    this.selectedService = service;
    SwaggerUIBundle({
      url: this.getSwaggerUrl(),
      domNode: document.getElementById('swagger-ui'),
      presets: [SwaggerUIBundle['presets']['apis'], SwaggerUIStandalonePreset],
      layout: 'StandaloneLayout'
    });
  }
}
