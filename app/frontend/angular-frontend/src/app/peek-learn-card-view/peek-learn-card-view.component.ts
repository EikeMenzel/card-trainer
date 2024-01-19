import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {Component, OnInit} from "@angular/core";
import * as bootstrap from "bootstrap";
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {BasePageComponent} from "../base-page/base-page.component";
import {HttpStatusCode} from "@angular/common/http";
import {CardService} from "../services/card-service/card.service";
import {ToastService} from "../services/toast-service/toast.service";
import {AuthService} from "../services/auth-service/auth-service";
import {TextAnswerCardDTO} from "../models/learn-session/TextAnswerCardDTO";
import {MultipleChoiceCardDTO} from "../models/learn-session/MultipleChoiceCardDTO";
import {ActivatedRoute, Router} from "@angular/router";
import {faArrowsRotate} from "@fortawesome/free-solid-svg-icons";
import {catchError, map, Observable, of} from "rxjs";
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {TextAnswerCardImage} from "../models/learn-session/TextAnswerCardImage";
import {MultipleChoiceCardImage} from "../models/learn-session/MultipleChoiceCardImage";
import {TutorialComponent} from "../tutorial/tutorial.component";

@Component({
  standalone: true,
  selector: 'app-peek-learn-card-view',
  templateUrl: './peek-learn-card-view.component.html',
    imports: [
        NgIf,
        NgForOf,
        FontAwesomeModule,
        BasePageComponent,
        NgOptimizedImage,
        TutorialComponent
    ],
  styleUrls: ['./peek-learn-card-view.component.css']
})


export class PeekCardViewComponent implements OnInit {
  flipped: boolean = false;
  selectedChoices: string[] = [];

  card: TextAnswerCardDTO | MultipleChoiceCardDTO | undefined;
  textAnswerCard: TextAnswerCardDTO | undefined;
  multipleChoiceCard: MultipleChoiceCardDTO | undefined;

  peekSessionId: number = 0;
  deckId: string = "";

  cardLength: number = 1; //at least one cards need to be learned
  cardsLearned: number = 0;

  imageInformationTextAnswerCard: TextAnswerCardImage | undefined;
  imageInformationMultipleChoiceCard: MultipleChoiceCardImage | undefined;

  buttonIsPressed: boolean = false;
  isSessionFinished: boolean = false;
  unauthorizedFound: boolean = false;

  constructor(
    private cardService: CardService,
    private toastService: ToastService,
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private sanitizer: DomSanitizer,
  ) {
  }


  ngOnInit() {
    this.deckId = this.activatedRoute.snapshot.paramMap.get('deck-id') ?? "";
    this.startPeekSession();
  }


  startPeekSession() {
    this.cardService.startPeekSession(Number(this.deckId)).subscribe({
      next: (res) => {
        this.peekSessionId = res;
        this.fetchCardsToLearn()
      },
      error: (err) => {
        switch (err.status) {
          case HttpStatusCode.InternalServerError:
            this.toastService.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed:
          case HttpStatusCode.Unauthorized:
            this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.unauthorizedFound = true;
            this.authService.logout();
            break;
          case HttpStatusCode.NotFound:
            this.toastService.showErrorToast("Error", "User or Deck not found");
            break;
          default:
            this.toastService.showErrorToast("Error", "An unpredicted Error occurred");
            break;
        }
      }
    })
  }

  fetchCardsToLearn() {
    this.cardService.getNumberofCardsInDeck(Number(this.deckId)).subscribe({
      next: (res) => {
        if (res.body)
          this.cardLength = res.body;
        this.nextCard();
      },
      error: (err) => {
        switch (err.status) {
          case HttpStatusCode.InternalServerError:
            this.toastService.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed:
          case HttpStatusCode.Unauthorized:
            this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.unauthorizedFound = true;
            this.authService.logout();
            break;
          case HttpStatusCode.NotFound:
            this.toastService.showErrorToast("Error", "No Cards found. Please create Cards");
            break;
          default:
            this.toastService.showErrorToast("Error", "An unpredicted Error occurred");
            break;
        }
      }
    })
  }

  nextCard() {
    this.fetchNextCard()
    this.buttonIsPressed = false;
    if (this.cardsLearned < this.cardLength - 1) {
      this.flipped = false;
      this.checkAndShowModal();
    }
  }

  fetchNextCard() {
    this.cardService.getNextPeekCard(this.peekSessionId).subscribe({
      next: (res) => {
        switch (res.status) {
          case HttpStatusCode.Ok:
            this.card = JSON.parse(<string>res.body);
            if (this.card?.cardDTO.cardType === "BASIC") {
              this.textAnswerCard = JSON.parse(<string>res.body);
              this.setImageInformation();
            } else if (this.card?.cardDTO.cardType === "MULTIPLE_CHOICE") {
              this.multipleChoiceCard = JSON.parse(<string>res.body)
              this.setImageInformation();
            }
            break;
          case HttpStatusCode.NoContent: //No cards left -> set Status to finished
            this.finishPeekSession(Number(this.deckId));
            this.isSessionFinished = true;
            this.router.navigate([`/deck/${this.deckId}`]);
            break;
          default:
            this.toastService.showInfoToast("Notice", "Received unhandled status code");
            break;
        }
      },
      error: (err) => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toastService.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed:
          case HttpStatusCode.Unauthorized:
            this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.unauthorizedFound = true;
            this.authService.logout();
            break;
          case HttpStatusCode.NotFound:
            this.toastService.showErrorToast("Error", "User or Deck not found");
            break;
          default:
            this.toastService.showErrorToast("Error", "An unpredicted Error occurred");
            break;
        }
        this.buttonIsPressed = false;
      }
    });
  }

  finishPeekSession(deckId: number) {
    this.cardService.finishStatusOfCardPeek(this.peekSessionId).subscribe({
      next: (res) => {},
      error: (err) => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toastService.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed:
          case HttpStatusCode.Unauthorized:
            this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.unauthorizedFound = true;
            this.authService.logout();
            break;
          case HttpStatusCode.NotFound:
            this.toastService.showErrorToast("Error", "User or PeekSession not found");
            break;
          default:
            this.toastService.showErrorToast("Error", "An unpredicted Error occurred");
            break;
        }
      }
    });
  }

  getAnswerImageUrl(index: number): SafeUrl | undefined {
    if (this.imageInformationMultipleChoiceCard && index < this.imageInformationMultipleChoiceCard.answerImages.length) {
      return this.imageInformationMultipleChoiceCard.answerImages[index];
    }
    return undefined;
  }

  setImageInformation() {
    if (this.card?.cardDTO?.cardType === 'BASIC') {
      this.imageInformationTextAnswerCard = new TextAnswerCardImage();
      this.getImageUrl(this.card.cardDTO.imageId).subscribe(url => {
        if (url && this.imageInformationTextAnswerCard instanceof TextAnswerCardImage) {
          this.imageInformationTextAnswerCard.questionImage = url;
        }
      });

      if ("imageId" in this.card) {
        this.getImageUrl(this.card.imageId).subscribe(url => {
          if (url && this.imageInformationTextAnswerCard instanceof TextAnswerCardImage) {
            this.imageInformationTextAnswerCard.answerImage = url;
          }
        });
      }
    } else if (this.card) {
      this.imageInformationMultipleChoiceCard = new MultipleChoiceCardImage();
      this.getImageUrl(this.card.cardDTO.imageId).subscribe(url => {
        if (url && this.imageInformationMultipleChoiceCard instanceof MultipleChoiceCardImage) {
          this.imageInformationMultipleChoiceCard.questionImage = url;
        }
      });

      if ("choiceAnswers" in this.card && this.card.choiceAnswers) {
        this.card.choiceAnswers.forEach((choiceAnswer, index) => {
          this.getImageUrl(choiceAnswer.imageId).subscribe(url => {
            if (url && this.imageInformationMultipleChoiceCard instanceof MultipleChoiceCardImage) {
              // Ensure the answerImages array is initialized and has a sufficient length
              if (!this.imageInformationMultipleChoiceCard.answerImages) {
                this.imageInformationMultipleChoiceCard.answerImages = [];
              }
              while (index >= this.imageInformationMultipleChoiceCard.answerImages.length) {
                this.imageInformationMultipleChoiceCard.answerImages.push(undefined);
              }

              this.imageInformationMultipleChoiceCard.answerImages[index] = url;
            }
          });
        });
      }
    }
  }

  canDestroy():boolean {
      return this.unauthorizedFound || this.isSessionFinished || (confirm("Are you sure you want to leave this peek session") && !this.isSessionFinished && !this.unauthorizedFound);
  }

  ngOnDestroy(): void {
    if (!this.isSessionFinished && !this.unauthorizedFound) {
        this.cardService.setCancelledPeekStatus(this.peekSessionId).subscribe()
    }
  }

  get currentCard() {
    return this.card;
  }

  flipCard() {
    this.flipped = !this.flipped;
  }

  checkAndShowModal() {
    const halfWayThrough = Math.ceil(this.cardLength / 2);
    if (this.cardsLearned === halfWayThrough) {
      this.openMotivationalModal();
    }
  }

  openMotivationalModal() {
    const halfWayThrough = Math.ceil(this.cardLength / 2);
    if (this.cardsLearned === halfWayThrough) {
      const modalElement = document.getElementById('motivationModal');
      if (modalElement) {
        const motivationModal = new bootstrap.Modal(modalElement);
        motivationModal.show();
      }
    }
  }

  calculateProgress() {
    return Math.round((this.cardsLearned / this.cardLength) * 100);
  }

  isSelected(choice: string) {
    return this.selectedChoices.includes(choice);
  }

  updateSelectedChoices(event: Event, choice: string) {
    const checkbox = event.target as HTMLInputElement;
    if (checkbox.checked) {
      if (!this.isSelected(choice)) {
        this.selectedChoices.push(choice);
      }
    } else {
      this.selectedChoices = this.selectedChoices.filter(c => c !== choice); // Removes the selection
    }
  }

  isCorrectChoice(choice: string) {
    if (this.currentCard) {
      if (this.currentCard.cardDTO.cardType === "BASIC") {
        return this.textAnswerCard?.textAnswer === choice;
      }
      if (this.currentCard.cardDTO.cardType === "MULTIPLE_CHOICE") {
        if (this.multipleChoiceCard) {
          for (let i = 0; i < this.multipleChoiceCard?.choiceAnswers.length; i++)
            if (this.multipleChoiceCard?.choiceAnswers[i].answer === choice)
              return this.multipleChoiceCard?.choiceAnswers[i].rightAnswer === true;
        }
      }
    }
    return false;
  }

  getImageUrl(imageId: number | undefined | null): Observable<SafeUrl | undefined> {
    if (imageId == null) {
      return of(undefined); // Return an observable with 'undefined'
    }

    return this.cardService.getImageOfCard(imageId).pipe(
      map(res => {
        if (res.body) {
          return this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(res.body));
        }
        return undefined;
      }),
      catchError(err => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toastService.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed:
          case HttpStatusCode.Unauthorized:
            this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.unauthorizedFound = true;
            this.authService.logout();
            break;
          case HttpStatusCode.NotFound:
            this.toastService.showErrorToast("Error", "Image not found");
            break;
          default:
            this.toastService.showErrorToast("Error", "An unpredicted Error occurred");
            break;
        }
        return of(undefined);
      })
    );
  }

  savePeekSessionCardLearned() {
    this.buttonIsPressed = true;
    this.cardService.savePeekSessionCard(this.peekSessionId, <number>this.card?.cardDTO.id).subscribe({
      next: (res) => {
        this.nextCard();
        this.cardsLearned++;
      },
      error: (err) => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toastService.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed:
          case HttpStatusCode.Unauthorized:
            this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.unauthorizedFound = true;
            this.authService.logout();
            break;
          case HttpStatusCode.NotFound:
            this.toastService.showErrorToast("Error", "User or Deck not found");
            break;
          default:
            this.toastService.showErrorToast("Error", "An unpredicted Error occurred");
            break;
        }
        this.buttonIsPressed = false;
      }
    });
  }

  protected readonly faArrowsRotate = faArrowsRotate;

  openImageModal(imageUrl: SafeUrl | undefined) {
    if (imageUrl) {
      const imageElement: HTMLImageElement = document.getElementById('modalImage') as HTMLImageElement;
      // Verwenden des Sanitizers, um die URL sicher zu machen
      imageElement.src = this.sanitizer.sanitize(4, imageUrl) || ''; // 4 steht für Sanitization von URLs

      // Öffne das Bootstrap-Modal
      const modalElement = document.getElementById('imageModal');
      if (modalElement) {
        const imageModal = new bootstrap.Modal(modalElement);
        imageModal.show();
      }
    }
  }
}
