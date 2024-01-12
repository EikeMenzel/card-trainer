import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({ providedIn: 'root' })

export class LearningSessionService {

  private inLearningSession = new BehaviorSubject<boolean>(false);

  setLearningSession(status: boolean) {
    this.inLearningSession.next(status);
  }

  getLearningSessionStatus(): Observable<boolean> {
    return this.inLearningSession.asObservable();
  }
}
