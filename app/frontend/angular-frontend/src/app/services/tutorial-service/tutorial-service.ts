import { CookieService } from 'ngx-cookie-service';
import {Injectable} from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class TutorialService {
    constructor(private cookieService: CookieService) {}

  public hasSeenTutorial(page: string): boolean {
    return this.cookieService.check(`simon-seen-${page}`);
  }

    public setSeenTutorial(page: string): void {
        this.cookieService.set(`simon-seen-${page}`, 'true');
    }
}

