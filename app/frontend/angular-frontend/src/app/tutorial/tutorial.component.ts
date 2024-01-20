import { Component, Input, OnInit } from '@angular/core';
import { TutorialService } from "../services/tutorial-service/tutorial-service";
import {CommonModule, NgOptimizedImage} from "@angular/common";
import * as bootstrap from 'bootstrap';
import {NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-tutorial',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage, TranslateModule],
  templateUrl: './tutorial.component.html',
  styleUrls: ['./tutorial.component.css']
})

export class TutorialComponent implements OnInit {
  @Input() pageName: string = "";

  constructor(
      private tutorialService: TutorialService,
      private translate: TranslateService
  ) {}

  modalRef: bootstrap.Modal | undefined;

  ngOnInit() {
    this.showTutorial();
  }

  ngOnDestroy(){
    this.modalRef?.dispose()
  }

  private showTutorial() {
    this.tutorialService.hasSeenTutorial(this.pageName).subscribe(hasSeen => {
      // console.log(`Hello ${this.pageName}: ${hasSeen}`);

      if (!hasSeen) {
        const modalElement = document.getElementById('tutorialModal');
        if (modalElement) {
          this.modalRef = new bootstrap.Modal(modalElement);
          this.modalRef.show();

          // Event listener to set the tutorial as seen when the modal is closed
          modalElement.addEventListener('hidden.bs.modal', () => {
            // console.log(`Set seen ${this.pageName}`);
            this.tutorialService.setSeenTutorial(this.pageName).subscribe(() => {
             // console.log(`Mark seen ${this.pageName}`);
            });
          });
        }
      }
    });
  }




  getTutorialContent(page: string): string {
    switch (page) {
      case 'USER_PROFILE':
        return this.translate.instant("tutorial_user_profile");
      case 'EDIT_DECK':
        return this.translate.instant("tutorial_edit_card");
      case 'DECK_VIEW':
        return this.translate.instant("tutorial_deck_view");
      case 'LEARN_CARD_VIEW':
        return this.translate.instant("tutorial_learn_card_view");
      case 'PEEK_CARD_VIEW':
        return this.translate.instant("tutorial_peek_card_view");
      default:
        return "";
    }
  }

  public closeTutorial() {
    this.tutorialService.setSeenTutorial(this.pageName);
  }
}
