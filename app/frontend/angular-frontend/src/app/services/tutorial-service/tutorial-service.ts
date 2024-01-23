import { CookieService } from 'ngx-cookie-service';
import {Injectable} from "@angular/core";
import {catchError, map, Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class TutorialService {
  constructor(
    private httpClient: HttpClient
  ) {}

  public hasSeenTutorial(page: string): Observable<boolean> {
    return this.httpClient.get<boolean>(`/api/v1/tutorials/${page}`)
      .pipe(
        map(() => true),
        catchError(error => {
          if (error.status === 404) {
            return of(false); // If 404, this means that the user has not yet seen the page
          } else {
            throw error;
          }
        })
      );
  }

  public setSeenTutorial(page: string): Observable<void> {
    return this.httpClient.post<void>(`/api/v1/tutorials/${page}`, {});
  }
}


