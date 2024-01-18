import {Injectable, OnDestroy, OnInit} from "@angular/core";
import {RxStompService} from "./rx-stomp.service";
import {Message} from '@stomp/stompjs';
import {Subscription} from "rxjs";
import {AuthService} from "../auth-service/auth-service";
import {AchievementService} from "../achievement-service/achievement-service";
import {AchievementDetailsDTO} from "../../models/AchievementDetailsDTO";
import {AchievementModalComponent} from "../../achievement-modal/achievement-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService implements OnDestroy {
  private topicSubscription: Subscription | undefined;
  private achievementQueue: AchievementDetailsDTO[] = [];
  private isModalOpen = false;

  constructor(
    public rxStompService: RxStompService,
    private authService: AuthService,
    private achievementService: AchievementService,
    private modalService: NgbModal
  ) {
    this.subscribeTopics();
    if(this.authService.isLoggedIn) {
      this.rxStompService.activate();
    } else {
      this.rxStompService.deactivate();
    }
  }

  subscribeTopics() {
    this.topicSubscription = this.rxStompService
      .watch("/user/topic/achievement-notification")
      .subscribe((message: Message) => {
        const achievementId = parseInt(message.body, 10);
        this.checkForAchievement(achievementId);
      });
  }

  checkForAchievement(achievementId: number) {
    this.achievementService.getAchievementById(achievementId).subscribe({
      next: (achievement: AchievementDetailsDTO) => {
        if (achievement) {
          this.achievementQueue.push(achievement);
          this.showNextAchievement();
        }
      }
    });
  }

  showNextAchievement() {
    if (this.achievementQueue.length > 0 && !this.isModalOpen) {
      const nextAchievement = this.achievementQueue.shift();
      if (nextAchievement) {
        this.showAchievementModal(nextAchievement);
      }
    }
  }

  showAchievementModal(achievement: AchievementDetailsDTO) {
    this.isModalOpen = true;

    const modalRef = this.modalService.open(AchievementModalComponent, { size: 'md' });
    modalRef.componentInstance.achievementDetails = achievement;

    modalRef.result.then(
      () => {
        this.isModalOpen = false;
        this.showNextAchievement();
      },
      () => {
        this.isModalOpen = false;
        this.showNextAchievement();
      }
    );
  }


  ngOnDestroy() {
    // @ts-ignore
    this.topicSubscription.unsubscribe();
  }
}
