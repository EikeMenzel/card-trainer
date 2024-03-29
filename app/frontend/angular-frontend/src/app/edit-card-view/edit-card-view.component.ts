import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import * as bootstrap from 'bootstrap';
import {FormsModule} from "@angular/forms";
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {ToastService} from "../services/toast-service/toast.service";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faArrowLeft, faFileImage, faPlus, faReply, faSave, faSearch} from "@fortawesome/free-solid-svg-icons";
import {faUpload} from "@fortawesome/free-solid-svg-icons/faUpload";
import {faTrash} from "@fortawesome/free-solid-svg-icons/faTrash";
import {CookieService} from "ngx-cookie-service";
import {CreateCardBasicDTO} from "../models/edit-card/CreateCardBasicDTO";
import {EditCardDTO} from "../models/edit-card/EditCardDTO";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {CardService} from "../services/card-service/card.service";
import {HttpStatusCode} from "@angular/common/http";
import {CreateCardMCDTO} from "../models/edit-card/CreateCardMCDTO";
import {EditCardAnswer} from "../models/edit-card/EditCardAnswer";
import {UpdateCardBasicDTO} from "../models/edit-card/UpdateCardBasicDTO";
import {UpdateCardDTO} from "../models/edit-card/UpdateCardDTO";
import {UpdateCardMCDTO} from "../models/edit-card/UpdateCardMCDTO";
import {TutorialComponent} from "../tutorial/tutorial.component";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  standalone: true,
  selector: 'app-edit-card-view',
  templateUrl: './edit-card-view.component.html',
  imports: [
    FormsModule,
    CommonModule,
    BasePageComponent,
    FaIconComponent,
    RouterLink,
    NgOptimizedImage,
    TutorialComponent,
    TranslateModule,
  ],
  styleUrls: ['./edit-card-view.component.css']
})

export class EditCardViewComponent implements OnInit {

  protected readonly faUpload = faUpload;
  protected readonly faTrash = faTrash;
  protected readonly faPlus = faPlus;
  protected readonly faSave = faSave;
  protected readonly faFileImage = faFileImage;
  protected readonly faArrowLeft = faArrowLeft;

  hasLoaded = true;
  saveInProgress = false;
  isBasicCard: boolean = true
  questionCardDTO: EditCardDTO = new EditCardDTO("", null);
  choiceAnswers: EditCardAnswer[] = [];
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  // this represents for which question or answer the uploaded image was meant to be. (-1 is the question)
  private currentImageModal: number = 0;
  public selectedFile: File | null = null;
  public cardId: string = this.route.snapshot.paramMap.get("card-id") ?? "";
  public deckId: string = this.route.snapshot.paramMap.get("deck-id") ?? "";
  public cardType: string = "basic";
  private modalReference: bootstrap.Modal | undefined
  private imgModalRef:  bootstrap.Modal | undefined;

  constructor(
    private toast: ToastService,
    private cookieService: CookieService,
    private route: ActivatedRoute,
    private cardService: CardService,
    private router: Router,
    private sanitizer: DomSanitizer,
    private translate: TranslateService
  ) {
  }

  ngOnDestroy(){
    this.modalReference?.dispose()
  }

  ngOnInit() {
    this.cardId = this.route.snapshot.paramMap.get("card-id") ?? "new";
    if (!this.cookieService.check("simon-seen-edit-card"))
      this.showWelcomeModal();

    if (this.cardId == "new") {
      this.choiceAnswers[0] = new EditCardAnswer(null, "", false, null);
      this.choiceAnswers[1] = new EditCardAnswer(null, "", false, null);
      return
    }
    this.hasLoaded = false

    if (isNaN(Number(this.cardId))) {
      this.toast.showWarningToast(this.translate.instant("edit_card"), this.translate.instant("invalid_card_is_part1") + this.cardId +  this.translate.instant("invalid_card_is_part2"));
      this.router.navigate([`deck/${this.deckId}/card/new/edit`])
      return;
    }

    this.loadCardData(this.deckId, this.cardId)

  }

  addOption() {
    if (this.isBasicCard) {
      return
    }

    this.choiceAnswers.push(new EditCardAnswer(null, "", false, null));
  }

  updateOptionText(index: number, text: string) {
    this.choiceAnswers[index].answer = text;
  }

  deleteOption(index: number) {
    if (this.choiceAnswers.length > 2) {
      this.choiceAnswers.splice(index, 1);
    } else {
      this.toast.showWarningToast(this.translate.instant("warning"), this.translate.instant("answer_constraint"));
    }
  }

  openFilePicker(event: Event) {
    event.preventDefault();
    this.fileInput.nativeElement.click();
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation(); // If necessary, in order not to publicise the event further
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    if (event.dataTransfer)
      this.setToBeUploadedImage(event.dataTransfer.files[0])
  }

  onFileSelected(event: Event, input: HTMLInputElement) {
    event.preventDefault();

    if (!input.files) {
      return
    }

    if (input.files && input.files.length > 0) {
      this.setToBeUploadedImage(input.files[0])
    }
  }

  deleteFile() {
    this.selectedFile = null;
  }

  closeModal() {
    this.currentImageModal = 0;
  }

  openModal(frontendImageIndex: number) {
    this.currentImageModal = frontendImageIndex;
    const modalElement = document.getElementById('uploadModal');
    if (modalElement) {
      this.modalReference = new bootstrap.Modal(modalElement)
      this.modalReference.show();
    }
  }

  private saveNewCard(cardDTO: CreateCardBasicDTO | CreateCardMCDTO) {
    this.cardService.createCard(cardDTO, this.deckId).subscribe({
      next: value => {
        if (value.status == HttpStatusCode.Created) {
          this.toast.showSuccessToast(this.translate.instant("success"), this.translate.instant("card_created"))
          this.saveInProgress = false;
          this.router.navigate([`/deck/${this.deckId}/edit`])
        }
      },
      error: err => {
        console.log(err)
        if (err.status == HttpStatusCode.InternalServerError) {
          this.toast.showErrorToast(this.translate.instant("save_card"), this.translate.instant("service_unavailable"))
          return
        }
        this.toast.showErrorToast(this.translate.instant("save_card"), this.translate.instant("unpredicted_error"))
      }
    })
  }

  confirmImageSave() {
    const index = this.currentImageModal;
    if (this.selectedFile == null) {
      return
    }
    this.cardService.saveImage(this.selectedFile).subscribe({
      next: res => {
        if (res.status == HttpStatusCode.Created) {
          this.toast.showSuccessToast(this.translate.instant("save_image"), this.translate.instant("save_image_success"))
          if (index == -1) {
            this.questionCardDTO.imageId = Number(res.body);
            return
          }
          if (this.isBasicCard) {
            this.choiceAnswers[0].imageId = Number(res.body);
          } else {
            this.choiceAnswers[index].imageId = Number(res.body);
          }
        }
      }
    })
    this.selectedFile = null;
  }

  // Just experimental
  updateCardType(selectInput: HTMLSelectElement) {
    const newValue = selectInput.value == "basic"
    const oldValue = this.isBasicCard;
    this.isBasicCard = newValue;
    if (newValue == oldValue)
      return

    this.selectedFile = null;
  };

  private loadCardData(deckId: string, cardId: string) {
    this.cardService.getCard(deckId, cardId).subscribe({
      next: res => {
        this.questionCardDTO.question = res.body.cardDTO.question;
        if (res.body.cardDTO.cardType == "BASIC") {
          this.choiceAnswers[0] = new EditCardAnswer(null, "", false, null);
          this.isBasicCard = true
          const basicCard: CreateCardBasicDTO = res.body
          if (res.body.cardDTO.imageId)
            this.questionCardDTO.imageId = Number(res.body.cardDTO.imageId)
          if (res.body.imageId)
            this.choiceAnswers[0].imageId = Number(res.body.imageId)
          this.choiceAnswers[0].answer = basicCard.textAnswer
        } else {
          this.isBasicCard = false
          const mcCard: CreateCardMCDTO = res.body as CreateCardMCDTO;

          if(mcCard.cardDTO.imageId)
            this.questionCardDTO.imageId = Number(mcCard.cardDTO.imageId);


          this.cardType = "multipleChoice"
          this.choiceAnswers = []
          for (let answer of mcCard.choiceAnswers) {
            this.choiceAnswers.push(new EditCardAnswer(answer.choiceAnswerId, answer.answer, answer.rightAnswer, answer.imageId))
          }
        }
      },
      error: err => {
        console.log(err)
        if (err.status == HttpStatusCode.NotFound) {
          this.toast.showErrorToast(this.translate.instant("edit_card"), this.translate.instant("edit_card_not_found"))
          this.router.navigate([`deck/${this.deckId}/edit`])
          this.hasLoaded = true;
          return
        }

        this.toast.showErrorToast(this.translate.instant("edit_card"), this.translate.instant("unpredicted_error"))
        this.router.navigate([`deck/${this.deckId}/edit`])
        this.hasLoaded = true;
      },
      complete: () => {
        this.hasLoaded = true
      }
    })
  }

  dontShowSimonAgainOnPageLoad() {
    this.cookieService.set("simon-seen-edit-card", "true")
  }

  showWelcomeModal() {
    const modalElement = document.getElementById('welcomeModal');
    if (modalElement) {
      const welcomeModal = new bootstrap.Modal(modalElement);
      welcomeModal.show();
    }
  }

  private setToBeUploadedImage(file: File) {
    if (file.type != "image/jpeg" && file.type != "image/png") {
      this.toast.showErrorToast(this.translate.instant("image_error"), this.translate.instant("valid_file_format"))
      return
    }
    this.selectedFile = file
  }


  deleteImgBasicAnswer(number: number) {
    if (!confirm(this.translate.instant("delete_image_confirmation"))) {
      return
    }

    if (this.isBasicCard)
      this.choiceAnswers[0].imageId = null
    else {
      this.choiceAnswers[this.currentImageModal].imageId = null
    }
  }

  saveCardToBackend() {
    if(this.isBasicCard) {
      if(this.choiceAnswers[0].answer.trim().length === 0) {
        this.toast.showErrorToast(this.translate.instant("save_card"), this.translate.instant("answer_constraint_mp"));
        return;
      }
    } else {
      for (const choiceAnswer of this.choiceAnswers) {
        if (choiceAnswer.answer.trim().length === 0) {
          this.toast.showErrorToast(this.translate.instant("save_card"), this.translate.instant("answer_constraint_mp"));
          return;
        }
      }
    }

    if (this.questionCardDTO.question.trim().length === 0) {
      this.toast.showErrorToast(this.translate.instant("save_card"), this.translate.instant("question_constraint"))
      return;
    }

    if (this.saveInProgress) {
      return;
    }

    this.saveInProgress = true;
    if (this.isBasicCard)
      this.saveBasicCard();
    else
      this.saveMCCard();
  }

  private saveBasicCard() {
    if (this.cardId == "new") {
      const cardDTO: CreateCardBasicDTO = new CreateCardBasicDTO(this.questionCardDTO, this.choiceAnswers[0].answer, this.choiceAnswers[0].imageId);
      this.saveNewCard(cardDTO);
      return
    }

    const updateCardDTO: UpdateCardDTO = new UpdateCardDTO(Number(this.cardId), this.questionCardDTO.question, this.questionCardDTO.imageId)
    const cardDTO: UpdateCardBasicDTO = new UpdateCardBasicDTO(Number(this.cardId), updateCardDTO, this.choiceAnswers[0].answer, this.choiceAnswers[0].imageId);
    this.cardService.updateCard(cardDTO, this.deckId, this.cardId).subscribe({
      next: res => {
        if (res.status == HttpStatusCode.NoContent) {
          this.toast.showSuccessToast(this.translate.instant("success"), this.translate.instant("update_card_success"))
          this.loadCardData(this.deckId, this.cardId);
        }
      },
      error: err => {
        console.log(err)
        this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("update_card_error"))
      },
      complete: () => {
        this.saveInProgress = false
      }
    })
  }

  private saveMCCard() {
    if (this.cardId == "new") {
      const cardDTO: CreateCardMCDTO = new CreateCardMCDTO(this.questionCardDTO, this.choiceAnswers);
      this.saveNewCard(cardDTO);
      return
    }

    const updateCardDTO: UpdateCardDTO = new UpdateCardDTO(Number(this.cardId), this.questionCardDTO.question, this.questionCardDTO.imageId)
    const updateCardMCDTO: UpdateCardMCDTO = new UpdateCardMCDTO(Number(this.cardId), updateCardDTO, this.choiceAnswers)
    this.cardService.updateCard(updateCardMCDTO, this.deckId, this.cardId).subscribe({
      next: res => {
        if (res.status == HttpStatusCode.NoContent) {
          this.toast.showSuccessToast(this.translate.instant("success"), this.translate.instant("update_card_success"))
          this.loadCardData(this.deckId, this.cardId);
        }
      },
      error: err => {

        console.log(err)
        this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("update_card_error"))
      },
      complete: () => {
        this.saveInProgress = false
      }
    })
  }

  deleteImgBasicQuestion() {
    if (!confirm(this.translate.instant("delete_image_confirmation_2")))
      return
    this.questionCardDTO.imageId = null
  }

  isCorrectCheckChanged(i: number, $event: Event) {
    this.choiceAnswers[i].rightAnswer = ($event.target as HTMLInputElement).checked;
  }
  openImageModal(imageUrl: SafeUrl | undefined) {
    console.log(imageUrl)
    if (imageUrl) {
      const imageElement: HTMLImageElement = document.getElementById('modalLargeImage') as HTMLImageElement;
      // Use the sanitizer to make the URL secure
      imageElement.src = this.sanitizer.sanitize(4, imageUrl) || ''; // 4 stands for sanitization of URLs

      // Open the bootstrap modal
      const modalElement = document.getElementById('imageLargeModal');
      if (modalElement) {
        this.imgModalRef = new bootstrap.Modal(modalElement);
        this.imgModalRef.show();
      }
    }
  }

  protected readonly faSearch = faSearch;
  protected readonly faReply = faReply;
}
