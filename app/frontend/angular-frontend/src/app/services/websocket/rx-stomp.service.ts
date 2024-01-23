import { Injectable } from '@angular/core';
import { RxStomp } from '@stomp/rx-stomp';
import {rxStompServiceFactory} from "./rx-stomp-service-factory";

@Injectable({
  providedIn: 'root',
  useFactory: rxStompServiceFactory
})
export class RxStompService extends RxStomp {
  constructor() {
    super();
  }
}
