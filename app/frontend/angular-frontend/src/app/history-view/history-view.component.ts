import {Component, ElementRef, ViewChild} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {HistoryDTO} from "../models/history/HistoryDTO";
import {HistoryService} from "../services/history-service/history.service";
import {HttpStatusCode} from "@angular/common/http";
import {defaults} from "chart.js";
import {ToastService} from "../services/toast-service/toast.service";
import {AuthService} from "../services/auth-service/auth-service";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {DonutChartComponent} from "../donut-chart/donut-chart.component";
import {LearnSessionDetailDTO} from "../models/history/LearnSessionDetailDTO";
import {Timestamp} from "rxjs";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {UserService} from "../services/user-service/user.service";

@Component({
  selector: 'app-history-view',
  standalone: true,
  imports: [CommonModule, BasePageComponent, FormsModule, ReactiveFormsModule, RouterLink, DonutChartComponent, TranslateModule],
  templateUrl: './history-view.component.html',
  providers: [DatePipe],
  styleUrl: './history-view.component.css'
})
export class HistoryViewComponent {
  private modalRef: NgbModalRef | undefined;

  @ViewChild('content') private modalReference: ElementRef | undefined;

  deckDetails: LearnSessionDetailDTO | undefined;
  histories: HistoryDTO[] = [];
  deckId: string = "";
  learnedCards: number = 0;

  chartNames: string[] = [
    this.translate.instant("easy"),
    this.translate.instant("ok"),
    this.translate.instant("kinda_difficult"),
    this.translate.instant("difficult"),
    this.translate.instant("guessed"),
    this.translate.instant("no_clue")
  ];
  chartColor: string[] =  ["#cce5ff","#ccffcc","#fff2cc","#FAC898","#FFB8A9","#E96954"];
  chartData: number[] = []
  awaitChange: boolean = false;


  constructor(private historyService: HistoryService,
              private toast: ToastService,
              private authService: AuthService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private modalService: NgbModal,
              private datePipe: DatePipe,
              private translate: TranslateService,
              private userService: UserService
  ) {
  }


  ngOnDestroy() {
    this.modalRef?.close(this.modalReference)
  }

  ngOnInit() {
    this.deckId = this.activatedRoute.snapshot.paramMap.get('deck-id') ?? "";
    this.historyService.getAllHistories(Number(this.deckId)).subscribe({
      next: value => {
        this.histories = value.body ?? [];
      },
      error: err => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("server_unreachable"));
            break;
          case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("authentication_failed_login_again"));
            this.authService.logout();
            this.router.navigate(["/login"])
            break;
          case HttpStatusCode.NoContent:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("no_histories_found"))
            break;
          case defaults:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("unpredicted_error"));
            break;
        }
      }
    })
  }

  getPercentValue(chartData: number[], cardsAmount: number) {
    let preSave: number[] = []
    for (let i = 0; i < chartData.length; i++) {
      preSave[i] = 100 / cardsAmount * chartData[i];
    }
    return preSave;
  }

  openModal(content: any, historyId: number) {
    this.modalRef = this.modalService.open(content, {
      ariaLabelledBy: 'modal-basic-title',
      beforeDismiss: () => {
        this.closeModal(content);
        return true;
      }
    });

    this.historyService.getHistoryDetails(Number(this.deckId), historyId).subscribe({
      next: res => {
        if (res.body) {
          this.deckDetails = res.body;
          const chart: number[] = [this.deckDetails.difficulty_6, this.deckDetails.difficulty_5, this.deckDetails.difficulty_4, this.deckDetails.difficulty_3, this.deckDetails.difficulty_2, this.deckDetails.difficulty_1]
          this.chartData = this.getPercentValue(chart, this.deckDetails.cardsLearned)

          this.learnedCards = this.deckDetails.cardsLearned
          this.awaitChange = true;
        }
      },
      error: err => {
        const statusCode = err.status;
        switch (statusCode) {
          case HttpStatusCode.InternalServerError:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("server_unreachable"));
            break;
          case HttpStatusCode.PreconditionFailed || HttpStatusCode.Unauthorized:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("authentication_failed_login_again"));
            this.authService.logout();
            this.router.navigate(["/login"])
            break;
          case HttpStatusCode.NoContent:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("no_histories_found"))
            break;
          case defaults:
            this.toast.showErrorToast(this.translate.instant("error"), this.translate.instant("unpredicted_error"));
            break;
        }
      }
    })
  }

  closeModal(content: any) {
    this.modalRef?.close(content);
    this.awaitChange = false;
    this.chartData = [];
    this.deckDetails = undefined;
  }


  changeDateFormat(createdAt: Timestamp<any>): string | null | undefined {
    let date = new Date(createdAt.toString())
    return this.datePipe?.transform(date, this.translate.instant("date_format"))
  }

}
