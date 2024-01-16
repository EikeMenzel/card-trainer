import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({ providedIn: 'root' })

export class PeekSessionService {

  private inPeekSession = new BehaviorSubject<boolean>(false);

  setLearningSession(status: boolean) {
    this.inPeekSession.next(status);
  }

  getLearningSessionStatus(): Observable<boolean> {
    return this.inPeekSession.asObservable();
  }
}
