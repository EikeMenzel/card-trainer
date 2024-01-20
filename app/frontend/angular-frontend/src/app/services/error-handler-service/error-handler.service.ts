import { Injectable } from '@angular/core';
import {MessageResponseDTO} from "../../models/MessageResponseDTO";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(private translate: TranslateService) { }

  public getErrorMessageFromResponse(messageResponseDTO: MessageResponseDTO): string {
    switch (messageResponseDTO.status) {
      case 1: {
        if (messageResponseDTO.response.includes("already exists")) {
          return this.translate.instant("email_in_use");
        }
        return this.translate.instant("your_email_is_invalid");
      }
      case 2: {
        return this.translate.instant("username_constraint");
      }
      case 3: {
        return this.translate.instant("password_constraint");
      }
      case 4: {
        return this.translate.instant("verification_constraint")
      }
      case 5: {
        return this.translate.instant("reset_token_response")
      }
      default:
        return messageResponseDTO.response;
    }
  }
}
