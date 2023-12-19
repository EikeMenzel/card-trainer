import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import * as bootstrap from 'bootstrap';
import {FormsModule} from "@angular/forms";
import {CommonModule} from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {ToastService} from "../services/toast-service/toast.service";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faArrowLeft, faFileImage, faPlus, faSave} from "@fortawesome/free-solid-svg-icons";
import {faUpload} from "@fortawesome/free-solid-svg-icons/faUpload";
import {faTrash} from "@fortawesome/free-solid-svg-icons/faTrash";
import {CookieService} from "ngx-cookie-service";
import {EditCardBasicDTO} from "../models/edit-card/EditCardBasicDTO";
import {EditCardDTO} from "../models/edit-card/EditCardDTO";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {CardService} from "../services/card-service/card.service";
import {HttpStatusCode} from "@angular/common/http";
import {EditCardMCDTO} from "../models/edit-card/EditCardMCDTO";
import {UpdateBasicCardDTO} from "../models/edit-card/update-card/UpdateBasicCardDTO";
import {UpdateCardDTO} from "../models/edit-card/update-card/UpdateCardDTO";
import {ImageDTO} from "../models/edit-card/update-card/ImageDTO";
import {OperationDTO} from "../models/edit-card/update-card/OperationDTO";
import {UpdateMCCardDTO} from "../models/edit-card/update-card/UpdateMCCardDTO";
import {EditCardMCHolder} from "../models/edit-card/EditCardMCHolder";
import {UpdateMCAnswerDTO} from "../models/edit-card/update-card/UpdateMCAnswerDTO";

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
  ],
  styleUrls: ['./edit-card-view.component.css']
})

export class EditCardViewComponent implements OnInit {

  protected readonly faUpload = faUpload;
  protected readonly faTrash = faTrash;
  protected readonly faPlus = faPlus;
  protected readonly faSave = faSave;
  protected readonly faFileImage = faFileImage;

  isBasicCard: boolean = true
  //TODO need a better way to represent both card types
  cardDTO: EditCardDTO = new EditCardDTO("", "");
  questionImage: File | null = null;
  basicAnswer = ""
  choiceAnswers: EditCardMCHolder[] = [];
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  imageUploadErrorMessage: string | null = null;
  // this represents for which question or answer the uploaded image was meant to be. (-1 is the question)
  private currentImageModal: number = 0;
  public selectedFile: File | null = null;
  private basicAnswerImage: File | null = null;
  public cardId: string = this.route.snapshot.paramMap.get("card-id") ?? "";
  public deckId: string = this.route.snapshot.paramMap.get("deck-id") ?? "";
  questionImgFromBackend: string = "";
  answerImgFromBackend: string = "";
  private originalCard: any;

  constructor(
    private toast: ToastService,
    private cookieService: CookieService,
    private route: ActivatedRoute,
    private cardService: CardService,
    private router: Router
  ) {
    route.params.subscribe({
      next: value => {
        this.cardId = value["card-id"]
        console.log(this.cardId + "sdfgsdgfdf")
        this.ngOnInit()
      }
    });
  }

  ngOnInit() {
    if (!this.cookieService.check("simon-seen-edit-card"))
      this.showWelcomeModal();
    if (this.cardId == "new") {
      return
    }

    this.loadCardData(this.deckId, this.cardId)
  }

  addOption() {
    if (this.isBasicCard) {
      return
    }

    this.choiceAnswers.push(new EditCardMCHolder("", false, null, null, null, null, null));
  }

  updateOptionText(index: number, text: string) {
    this.choiceAnswers[index].answer = text;
  }

  deleteOption(index: number) {
    if (this.choiceAnswers.length > 2) {
      this.choiceAnswers.splice(index, 1);
    } else {
      this.toast.showWarningToast('Warning', 'At least two answer options required.');
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
    this.previewImage();
  }

  onFileSelected(event: Event, input: HTMLInputElement) {
    event.preventDefault();
    if (!input.files) {
      return
    }

    this.selectedFile = input.files[0]
    if (input.files && input.files.length > 0) {
      this.setToBeUploadedImage(input.files[0])
      this.previewImage();
    }
  }

  previewImage() {
    // if (this.selectedFile) {
    //   const reader = new FileReader();
    //   const fileName = this.selectedFile.name;
    //
    //   reader.onload = (event: any) => {
    //     const base64String = event.target.result;
    //     this.uploadedFiles.push({name: fileName, url: base64String});
    //   };
    //   reader.readAsDataURL(this.selectedFile);
    // }
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
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  private saveNewCard(formData: FormData) {
    this.cardService.createCard(formData, this.deckId).subscribe({
      next: value => {
        if (value.status == HttpStatusCode.Created) {
          this.toast.showSuccessToast("Saved", "Your card has been created")
          this.cardService.getAllCardsByDeck(this.deckId).subscribe({
            next: cards => {
              let sort = cards.body?.sort((a, b) => {
                return b.id - a.id
              });

              if (sort == undefined) {
                return
              }

              let latest = sort[0].id;
              this.router.navigate([`deck/${this.deckId}/card/${latest}/edit`])
            }
          })
        }
      },
      error: err => {
        console.log(err)
        if (err.status == HttpStatusCode.InternalServerError) {
          this.toast.showErrorToast("Save Card", "The service is currently unavailable")
          return
        }
        this.toast.showErrorToast("Save Card", "An error occurred")
      }
    })
  }

  confirmImageSave() {
    if (this.currentImageModal == -1) {
      this.questionImage = this.selectedFile;
    } else {
      if (this.isBasicCard) {
        this.basicAnswerImage = this.selectedFile;
      } else {
        if (this.selectedFile) {
          this.choiceAnswers[this.currentImageModal].imagePath = this.selectedFile.name
          this.choiceAnswers[this.currentImageModal].imageFile = this.selectedFile;
        }
      }
    }

    this.selectedFile = null;
  }

  // Just experimental
  updateCardType(selectInput: HTMLSelectElement) {
    const newValue = selectInput.value == "basic"
    const oldValue = this.isBasicCard;
    this.isBasicCard = newValue;
    if (newValue == oldValue)
      return


    this.choiceAnswers = [
      new EditCardMCHolder("", true, null, null, null, null, null),
      new EditCardMCHolder("", true, null, null, null, null, null),
    ]

    this.basicAnswer = ""
    this.selectedFile = null;
  };

  private loadCardData(deckId: string, cardId: string) {
    this.cardService.getCard(deckId, cardId).subscribe({
      next: res => {
        this.originalCard = res.body
        this.cardDTO.question = res.body.cardDTO.question;
        if (res.body.cardDTO.cardType == "BASIC") {
          this.isBasicCard = true
          const basicCard: EditCardBasicDTO = res.body
          if (res.body.cardDTO.imageId)
            //TODO make DTO
            this.questionImgFromBackend = "api/v1/images/" + res.body.cardDTO.imageId
          if (res.body.imageId)
            this.answerImgFromBackend = "api/v1/images/" + res.body.imageId
          console.log(basicCard.imagePath)
          console.log(this.questionImgFromBackend)
          this.basicAnswer = basicCard.textAnswer
        } else {
          this.isBasicCard = false
          const mcCard: EditCardMCDTO = res.body as EditCardMCDTO;
          (document.getElementById("cardTypeSelect") as HTMLSelectElement).value = "multipleChoice"
          this.choiceAnswers = []
          for (let answer of mcCard.choiceAnswers) {
            this.choiceAnswers.push(new EditCardMCHolder(answer.answer, answer.isRightAnswer, answer.imagePath, answer.imageId, answer.imageId, answer.answerId, null))
          }
        }
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
    if (this.isBasicCard) {
      if (this.currentImageModal == -1) {
        this.questionImage = file;
        return
      }

      this.basicAnswerImage = file;
    } else {
      if (this.currentImageModal == -1) {
        this.questionImage = file;
        return
      }

      this.choiceAnswers[this.currentImageModal].imageFile = file;
    }
  }

  protected readonly faArrowLeft = faArrowLeft;

  deleteImgBasicAnswer() {
    if (!confirm("Do you want to delete the image?")) {
      return
    }

    this.answerImgFromBackend = ""

  }

  saveCardToBackend() {
    if (this.isBasicCard) {
      if (this.cardDTO.question == "" && this.cardDTO.question == "") {
        this.toast.showWarningToast('Warning', 'Field cannot be empty.'); // Displays the toast if one of the text areas is empty
        return
      }

      if (this.cardId == "new") {
        let formData = new FormData();
        let tmp = new EditCardBasicDTO(
          this.cardDTO,
          this.basicAnswer,
          this.basicAnswerImage?.name
        )
        //formData.append('testField', 'testValue');
        if (this.questionImage != null) {
          formData.append("images", this.questionImage, this.questionImage.name)
          tmp.cardDTO.imagePath = this.questionImage.name;
        }
        if (this.basicAnswerImage != null) {
          formData.append("images", this.basicAnswerImage, this.basicAnswerImage.name)
          tmp.imagePath = this.basicAnswerImage.name
        }
        formData.append("cardNode", JSON.stringify(tmp));

        this.saveNewCard(formData);
        return;
      }

      let basicQuestionImageDTO: ImageDTO | null = null
      let basicAnswerImageDTO: ImageDTO | null = null
      if (!this.answerImgFromBackend) {
        if (this.basicAnswerImage) {
          if (this.originalCard.imageId) {
            basicAnswerImageDTO = new ImageDTO(
              Number(this.originalCard.imageId),
              this.basicAnswerImage.name,
              OperationDTO.UPDATE
            )
          } else {
            basicAnswerImageDTO = new ImageDTO(
              null,
              this.basicAnswerImage.name,
              OperationDTO.CREATE
            )
          }
        } else {
          basicAnswerImageDTO = new ImageDTO(
            Number(this.originalCard.imageId),
            null,
            OperationDTO.DELETE
          )
        }
      }

      if (!this.questionImgFromBackend) {
        if (this.questionImage) {
          if (this.originalCard.cardDTO.imageId) {
            basicQuestionImageDTO = new ImageDTO(
              Number(this.originalCard.cardDTO.imageId),
              this.questionImage.name,
              OperationDTO.UPDATE
            )
          } else {
            basicQuestionImageDTO = new ImageDTO(
              null,
              this.questionImage.name,
              OperationDTO.CREATE
            )
          }
        } else {
          basicQuestionImageDTO = new ImageDTO(
            Number(this.originalCard.cardDTO.imageId),
            null,
            OperationDTO.DELETE
          )
        }
      }
      let formData = new FormData();
      if (this.questionImage != null) {
        formData.append("images", this.questionImage, this.questionImage.name)
      }
      if (this.basicAnswerImage != null) {
        formData.append("images", this.basicAnswerImage, this.basicAnswerImage.name)
      }


      let updateBasicCardDTO = new UpdateBasicCardDTO(
        Number(this.cardId),
        new UpdateCardDTO(
          Number(this.cardId),
          this.cardDTO.question,
          basicQuestionImageDTO
        ),
        this.basicAnswer,
        basicAnswerImageDTO
      );
      formData.append("cardNode", JSON.stringify(updateBasicCardDTO));

      this.cardService.updateBasicCard(this.deckId, this.cardId, formData).subscribe({
        next: res => {
          this.loadCardData(this.deckId, this.cardId)
        }, error: err => {
          console.log(err)
          this.toast.showErrorToast("Save", "Card could not be saved")
        }
      })
    } else {
      if (this.cardDTO.question == "" && this.choiceAnswers.every(value => value.answer == "")) {
        this.toast.showWarningToast('Warning', 'Fields cannot be empty.'); // Displays the toast if one of the text areas is empty
        return
      }

      if (this.cardId == "new") {
        let formData = new FormData();
        formData.append("cardNode", JSON.stringify(new EditCardMCDTO(this.cardDTO, this.choiceAnswers)));
        //formData.append('testField', 'testValue');
        if (this.questionImage != null)
          formData.append("images", this.questionImage, this.questionImage.name)
        this.choiceAnswers.filter(value => value.imageFile != null).forEach(value => {
          formData.append("images", value.imageFile as File, value.imageFile?.name)
        })
        this.saveNewCard(formData);
        return;
      }

      let mcQuestionImageDTO: ImageDTO | null = null

      if (!this.questionImgFromBackend) {
        if (this.questionImage) {
          if (this.originalCard.cardDTO.imageId) {
            mcQuestionImageDTO = new ImageDTO(
              Number(this.originalCard.cardDTO.imageId),
              this.questionImage.name,
              OperationDTO.UPDATE
            )
          } else {
            mcQuestionImageDTO = new ImageDTO(
              null,
              this.questionImage.name,
              OperationDTO.CREATE
            )
          }
        } else {
          mcQuestionImageDTO = new ImageDTO(
            Number(this.originalCard.cardDTO.imageId),
            null,
            OperationDTO.DELETE
          )
        }
      }

      let formData = new FormData();

      let answers: UpdateMCAnswerDTO[] = []
      this.choiceAnswers.forEach(value => {
        let imageDTO: ImageDTO | null = null;
        if (value.imageFile && value.ogImageid) {
          imageDTO = new ImageDTO(
            Number(value.ogImageid),
            value.imageFile.name,
            OperationDTO.UPDATE
          )
        }

        if (!value.imageFile && value.ogImageid) {
          imageDTO = new ImageDTO(
            Number(value.ogImageid),
            value.ogImageid,
            OperationDTO.DELETE
          )
        }

        if (value.imageFile && !value.ogImageid) {
          imageDTO = new ImageDTO(
            null,
            value.imageFile.name,
            OperationDTO.CREATE
          )
        }

        const ans = new UpdateMCAnswerDTO(
          Number(value.answerId),
          value.answer,
          value.isRightAnswer,
          imageDTO
        )
        answers.push(ans)

        if(value.imageFile)
          formData.append("images", value.imageFile, value.imageFile.name)
      })

      let updateMCCardDTO = new UpdateMCCardDTO(
        Number(this.cardId),
        new UpdateCardDTO(
          Number(this.cardId),
          this.cardDTO.question,
          mcQuestionImageDTO
        ),
        answers
      );


      let basicAnswerImageDTO: ImageDTO | null = null

      if (!this.questionImgFromBackend) {
        if (this.questionImage) {
          if (this.originalCard.cardDTO.imageId) {
            basicAnswerImageDTO = new ImageDTO(
              Number(this.originalCard.cardDTO.imageId),
              this.questionImage.name,
              OperationDTO.UPDATE
            )
          } else {
            basicAnswerImageDTO = new ImageDTO(
              null,
              this.questionImage.name,
              OperationDTO.CREATE
            )
          }
        } else {
          basicAnswerImageDTO = new ImageDTO(
            Number(this.originalCard.cardDTO.imageId),
            null,
            OperationDTO.DELETE
          )
        }
      }

      if (this.questionImage != null) {
        formData.append("images", this.questionImage, this.questionImage.name)
        updateMCCardDTO.cardDTO.imageDTO = basicAnswerImageDTO;
      }

      this.cardService.updateMCCard(this.deckId, this.cardId, formData).subscribe({
        next: res => {
          this.loadCardData(this.deckId, this.cardId)
        }, error: err => {
          console.log(err)
          this.toast.showErrorToast("Save", "Card could not be saved")
        }
      })
    }
  }

  deleteImgBasicQuestion() {
    if (!confirm("Are you sure you want to delete the image?"))
      return
    this.questionImgFromBackend = ""
  }
}
