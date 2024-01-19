import { Component, Input, OnInit } from '@angular/core';
import { TutorialService } from "../services/tutorial-service/tutorial-service";
import {CommonModule, NgOptimizedImage} from "@angular/common";
import * as bootstrap from 'bootstrap';
import {NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-tutorial',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage],
  templateUrl: './tutorial.component.html',
  styleUrls: ['./tutorial.component.css']
})

export class TutorialComponent implements OnInit {
  @Input() pageName: string = "";

  constructor(
      private tutorialService: TutorialService
  ) {}

  modalRef: bootstrap.Modal | undefined;

  ngOnInit() {
    this.showTutorial();
  }

  ngOnDestroy(){
    this.modalRef?.dispose()
  }

  private showTutorial() {
    if (!this.tutorialService.hasSeenTutorial(this.pageName)) {
      const modalElement = document.getElementById('tutorialModal');
      if (modalElement) {
        this.modalRef = new bootstrap.Modal(modalElement);
        this.modalRef.show();
        // Event listener to set the cookie when the tutorial is closed
        modalElement.addEventListener('hidden.bs.modal', () => {
          this.tutorialService.setSeenTutorial(this.pageName);
        });
      }
    }
  }


  getTutorialContent(page: string): string {
    switch (page) {
      case 'user-profile':
        return "Wow, lookin´ good on that profile picture!\n" +
          "On your user profile, you're the boss!\n" +
          "Change your name, switch up your email address, and level up that password game. " +
          "Want to flex your learning muscles? " +
          "You get to choose how many cards you conquer per session. " +
          "Feeling multilingual? " +
          "Pick your preferred language, and decide if you're in the mood for some learning notifications.\n" +
          "And oh, the magic word is 'Save' – don't forget to give it a tap!\n" +
          "Sending loads of love and learning vibes your way,\n" +
          "🦊Simon";
      case 'edit-card':
        return "Welcome to the card creation wonderland!\n" +
          "Unleash your creativity, edit existing cards, and even upload images to spice up your learning journey. " +
          "It's your canvas, so paint it with knowledge!\n" +
          "Sending oodles of love and heaps of fun your way,\n" +
          "🦊Simon";
      case 'deck-view':
        return "Welcome to your deck command center!\n" +
          "Dive into the details: see how grand your deck has become and how many cards await your mastery. " +
          "Fancy a deck makeover? " +
          "You can change its name, split it, export the wisdom within, and edit individual cards.\n" +
          "But here's the real magic – your learning history awaits! " +
          "Above all, gear up for a learning session that'll leave you feeling unstoppable.\n" +
          "Or, feeling curious for a quick peek today?\n" +
          "Sending love and knowledge vibes,\n" +
          "🦊Simon";
      case 'learn-card-view':
        return "Welcome to your flashcard fiesta!\n" +
          "Ready for the flip? " +
          "Click the rotate button to reveal the back, and click again to return to the front-row seat of knowledge.\n" +
          "The feedback buttons are your trusty companions. " +
          "Clicking them not only helps you but also guides you to the next card in your learning adventure.\n" +
          "Embrace the joy of learning! 🚀 Let the fun begin!\n" +
          "🦊Simon";
      case 'peek-card-view':
        return "Feeling a bit spontaneous?" +
          "Perfect! In the Peek Learning Session, it's all about your pace. " +
          "Click the Rotate button for a sneak peek at the flip side, and with another click, you're back to the front.\n" +
          "Ready for more? Hit the Next button to dive into the next card waiting to share its secrets.\n" +
          "Enjoy the adventure of learning!\n" +
          "🦊Simon";
      default:
        return "";
    }
  }

  public closeTutorial() {
    this.tutorialService.setSeenTutorial(this.pageName);
  }
}
