import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { AchievementService } from "../services/achievement-service/achievement-service"
import {AchievementDetailsDTO} from "../models/AchievementDetailsDTO";
import {NgIf} from "@angular/common";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-achievement-modal',
  templateUrl: './achievement-modal.component.html',
  standalone: true,
  imports: [
    NgIf,
    TranslateModule
  ],
  styleUrls: ['./achievement-modal.component.css']
})
export class AchievementModalComponent implements OnInit {
  @Input() achievementDetails: AchievementDetailsDTO | null = null;
  achievementImageUrl: SafeUrl | null = null;
  public achievementName = "";
  public achievementDescription = "";
  constructor(
    private achievementService: AchievementService,
    private sanitizer: DomSanitizer,
    public activeModal: NgbActiveModal,

  ) {}

  ngOnInit() {
    // Ensure that achievementDetails is available
    if (this.achievementDetails) {
      this.achievementName = this.achievementDetails.achievementName
      this.achievementDescription = this.achievementDetails.description
      this.loadAchievementImage(this.achievementDetails.achievementId);
    }
  }

  loadAchievementImage(achievementId: number) {
    this.achievementService.getAchievementImage(achievementId).subscribe(blob => {
      const objectURL = URL.createObjectURL(blob);
      this.achievementImageUrl = this.sanitizer.bypassSecurityTrustUrl(objectURL);
    });
  }

  closeModal() {
    this.activeModal.close();
  }
}
