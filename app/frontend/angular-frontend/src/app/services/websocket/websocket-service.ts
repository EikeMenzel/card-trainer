import {Injectable, OnDestroy, OnInit} from "@angular/core";
import {RxStompService} from "./rx-stomp.service";
import {Message} from '@stomp/stompjs';
import {Subscription} from "rxjs";
import {AuthService} from "../auth-service/auth-service";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService implements OnDestroy {
  // @ts-ignore, to suppress warning related to being undefined
  private topicSubscription: Subscription;

  constructor(public rxStompService: RxStompService, private authService: AuthService) {
    this.subscribeTopics();
    if(this.authService.isLoggedIn) {
      this.rxStompService.activate();
      //this.rxStompService.deactivate();

    } else {
      this.rxStompService.deactivate();
    }
  }

  subscribeTopics() {
    this.topicSubscription = this.rxStompService
      .watch("/user/topic/achievement-notification")
      .subscribe((message: Message) => {});
  }

  ngOnDestroy() {
    this.topicSubscription.unsubscribe();
  }
}
