import { Injectable } from '@angular/core';
import {MessageResponseDTO} from "../../models/MessageResponseDTO";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor() { }

  public getErrorMessageFromResponse(messageResponseDTO: MessageResponseDTO): string {
    switch (messageResponseDTO.status) {
      case 1: {
        return "Your Email is invalid";
      }
      case 2: {
        return "Username is to long or to short";
      }
      case 3: {
        return "Please make sure you are using at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/"
      }
      case 4: {
        return "Authentication failed: The user is not verified"
      }
      case 5: {
        return "The reset-token is valid, expired or already used. Please request another password reset"
      }
      default:
        return messageResponseDTO.response;
    }
  }
}
