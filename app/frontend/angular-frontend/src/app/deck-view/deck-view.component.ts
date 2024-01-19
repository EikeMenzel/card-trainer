import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BasePageComponent} from "../base-page/base-page.component";
import {DonutChartComponent} from "../donut-chart/donut-chart.component";
import {HttpStatusCode} from "@angular/common/http";
import {CardService} from "../services/card-service/card.service";
import {DeckDetailInformationDTO} from "../models/DeckDetailInformationDTO";
import {ToastService} from "../services/toast-service/toast.service";
import {FormsModule} from "@angular/forms";
import {AuthService} from "../services/auth-service/auth-service";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {faPencil, faSave, faXmark} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {TutorialComponent} from "../tutorial/tutorial.component";


@Component({
  selector: 'app-deck-view',
  standalone: true,
  imports: [
    CommonModule,
    BasePageComponent,
    DonutChartComponent,
    RouterLink,
    FormsModule,
    FaIconComponent,
    TutorialComponent
  ],
  templateUrl: './deck-view.component.html',
  styleUrl: './deck-view.component.css'
})
export class DeckViewComponent implements OnInit {

  @ViewChild('content') private modalReference: ElementRef | undefined;

  public awaitChange: boolean = false;
  public deckTitle: string = "";
  public deckSize: number = 0;
  public deckCardsLeft: number = 0;
  public chartData: number[] = [];
  public chartNames: string[] = [];
  public emailBorder: string = "grey";
  public deckId: string = "";
  public email: string = "";
  private modalRef: NgbModalRef | undefined;
  public emailWarn: boolean = false;
  public buttonIsPressed: boolean = false;
  public editMode: boolean = false;
  public editableTitle: string = "";

  constructor(private modalService: NgbModal,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private cardService: CardService,
              private authService: AuthService,
              private toastService: ToastService) {
  }

  ngOnDestroy(){
    this.modalRef?.close(this.modalReference)
  }

  ngOnInit() {
    this.deckId = this.activatedRoute.snapshot.paramMap.get('deck-id') ?? "";
    if (this.deckId == "") {
      this.toastService.showErrorToast("Error", "Deck not Found");
      this.router.navigate([""]);
    } else {
      this.cardService.detailDecks(Number(this.deckId)).subscribe({
        next: (res) => {
          if (res.status == HttpStatusCode.Ok && res.body) {
            const deckDetails: DeckDetailInformationDTO = res.body;
            this.deckTitle = deckDetails.deckName;
            this.deckSize = deckDetails.deckSize;
            this.deckCardsLeft = (deckDetails.cardsToLearn > 0) ? deckDetails.cardsToLearn : 0;
            this.chartData = this.chartService(deckDetails.deckLearnState);
            this.awaitChange = true;
          }
        },
        error: (err) => {
          const statusCode = err.status;
          switch (statusCode) {
            case HttpStatusCode.InternalServerError:
              this.toastService.showErrorToast("Error", "Server cannot be reached");
              break;
            case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
              this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
              this.authService.logout();
              break;
            case HttpStatusCode.NotFound:
              this.toastService.showErrorToast("Error", "Deck was not Found");
              this.router.navigate([""]);
              break;
            default:
              this.toastService.showErrorToast("Error", "An unpredicted Error occurred");
              break;
          }
        }
      })
    }
  }

  exportButtonPressed() {
    this.cardService.getExportFile(Number(this.deckId)).subscribe({
      next: (res) => {
        if (res.body != null && res.status == HttpStatusCode.Ok) {
          const blob = new Blob([res.body], {type: 'application/zip'})
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `Exported-Deck-${this.deckTitle}.zip`;
          a.click();
          window.URL.revokeObjectURL(url);
        }
      },
      error: (err) => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toastService.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
            this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.authService.logout();
            break;
          default:
            this.toastService.showErrorToast("Error", "Could not Export Deck");
            break;
        }
      }
    })
  }

  chartService(chartData: number[]) {
    let tempData: number[];
    tempData = this.swapOrderOfChart(chartData);
    tempData = this.checkValues(tempData);
    return tempData;
  }

  checkValues(chartData: number[]) {
    let cp: number = 0;
    for (let i = 0; i < chartData.length; i++) {
      if (chartData[i] == 0) {
        cp++;
      }
    }
    if (cp == chartData.length) {
      this.chartNames = ["You have no cards"];
      chartData = [100];
      return chartData;
    } else {
      this.chartNames = ['Cards not learned', 'Easy', 'Ok', 'Kinda difficult', 'Difficult', 'I guessed', 'No clue']
      return this.getPercentValue(chartData);
    }
  }

  getPercentValue(chartData: number[]) {
    let preSave: number[] = []
    for (let i = 0; i < chartData.length; i++) {
      preSave[i] = 100 / this.deckSize * chartData[i];
    }
    return preSave;
  }

  swapOrderOfChart(chartData: number[]) {
    chartData = [chartData[0], chartData[6], chartData[5], chartData[4], chartData[3], chartData[2], chartData[1]];
    return chartData;
  }

  onSubmit() {
    this.buttonIsPressed = true;
    if (this.email != "") {
      this.emailBorder = "grey";
      this.emailWarn = false;
      this.cardService.shareDeck(Number(this.deckId), this.email).subscribe({
          next: () => {
            if (this.modalRef)
              this.modalRef.close();
            this.buttonIsPressed = false;
            this.toastService.showSuccessToast("Successful", "Deck was sent");
          },
          error: (err) => {
            const statusCode = err.status;
            switch (statusCode) {
              case HttpStatusCode.InternalServerError:
                this.toastService.showErrorToast("Error", "Server cannot be reached");
                break;
              case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
                this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
                if (this.modalRef)
                  this.modalRef.close();
                this.authService.logout();
                break;
              case HttpStatusCode.NotFound:
                this.toastService.showErrorToast("Error", "User or Deck not found");
                break;
              default:
                this.toastService.showErrorToast("Error", "An unpredicted Error occurred");
                if (this.modalRef)
                  this.modalRef.close();
                break;
            }
            this.buttonIsPressed = false;
          }
        }
      )
    } else {
      this.emailBorder = "red";
      this.emailWarn = true;
      this.buttonIsPressed = false;
    }
    this.email = "";
  }

  openModal(content: any) {
    this.emailBorder = "grey";
    this.emailWarn = false;
    this.modalRef = this.modalService.open(content, {
      beforeDismiss: () => {
        this.email = "";
        return true;
      }
    });
  }

  enableEditMode() {
    this.editableTitle = this.deckTitle;
    this.editMode = true;
  }

  cancelEdit() {
    this.editMode = false;
  }

  saveEdit() {
    if (this.editableTitle.trim().length === 0 || this.editableTitle.trim().length > 128) {
      this.toastService.showWarningToast("Warning", "Deck can't be empty");
      return;
    }

    if (this.editableTitle.trim().length > 128) {
      this.toastService.showWarningToast("Warning", "Deck can't be more then 128 chars long");
      return;
    }

    this.cardService.updateDeckDetails(Number(this.deckId), this.editableTitle).subscribe({
      next: (res) => {
        if (res.status == HttpStatusCode.NoContent) {
          this.deckTitle = this.editableTitle;
          this.toastService.showSuccessToast("Success", "Deck title updated successfully");
        }
      },
      error: (err) => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toastService.showErrorToast("Error", "Server cannot be reached");
            break;
          case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
            this.toastService.showErrorToast("Error", "Authentication Failed. Please Login again.");
            this.authService.logout();
            break;
          case HttpStatusCode.NotFound:
            this.toastService.showErrorToast("Error", "Deck was not Found");
            this.router.navigate([""]);
            break;
          default:
            this.toastService.showErrorToast("Error", "An unpredicted Error occurred");
            break;
        }
      }
    });
    this.editMode = false;
  }


  protected readonly faXmark = faXmark;
  protected readonly faSave = faSave;
  protected readonly faPencil = faPencil;
}
