import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import * as bootstrap from 'bootstrap';
import {FormsModule} from "@angular/forms";
import {CommonModule} from '@angular/common';
import {BasePageComponent} from "../base-page/base-page.component";
import {ToastService} from "../services/toast-service/toast.service";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faFileImage, faHouse, faPlus, faSave} from "@fortawesome/free-solid-svg-icons";
import {faUpload} from "@fortawesome/free-solid-svg-icons/faUpload";
import {faTrash} from "@fortawesome/free-solid-svg-icons/faTrash";
import {CookieService} from "ngx-cookie-service";

@Component({
  standalone: true,
  selector: 'app-edit-card-view',
  templateUrl: './edit-card-view.component.html',
  imports: [
    FormsModule,
    CommonModule,
    BasePageComponent,
    FaIconComponent,
  ],
  styleUrls: ['./edit-card-view.component.css']
})

export class EditCardViewComponent implements OnInit {

  card: any = {
    type: 'basic',  // initial card type
    front: '',
    back: '',
    question: '',   // for multiple choice
    options: ['', ''],  // initial two option for multiple choice
  };

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  selectedFile: File | null = null;
  uploadedFiles: Array<{ name: string, url: string }> = [];
  imageUploadErrorMessage: string | null = null;
  toastMessage: string | null = null;

  constructor(
    private toast: ToastService,
    private cookieService: CookieService
  ) {
  }

  addOption() {
    this.card.options.push({text: '', correct: false});
  }

  updateOptionText(index: number, text: string) {
    this.card.options[index].text = text;
  }

  deleteOption(index: number) {
    if (this.card.options.length > 2) {
      this.card.options.splice(index, 1);
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
    if (this.uploadedFiles.length >= 1) {
      this.imageUploadErrorMessage = 'Only one image upload allowed.';
      return;
    }
    this.imageUploadErrorMessage = null;

    if (event.dataTransfer && event.dataTransfer.files.length) {
      this.selectedFile = event.dataTransfer.files[0];
      this.previewImage();
    }
  }

  onFileSelected(event: Event) {

    event.preventDefault();
    if (this.uploadedFiles.length >= 1) {
      this.imageUploadErrorMessage = 'Only one image upload allowed.';
      return;
    }
    this.imageUploadErrorMessage = null;

    event.preventDefault();
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.previewImage();
    }
  }

  previewImage() {
    if (this.selectedFile) {
      const reader = new FileReader();
      const fileName = this.selectedFile.name;

      reader.onload = (event: any) => {
        const base64String = event.target.result;
        this.uploadedFiles.push({ name: fileName, url: base64String });
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  deleteFile(index: number) {
    this.uploadedFiles.splice(index, 1);
    this.selectedFile = null;
  }

  clearUploadedFiles() {
    this.uploadedFiles = [];
  }

  openModal() {
    const modalElement = document.getElementById('uploadModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  // TODO
  saveCardToBackend() {
    if (!this.card.front.trim() || !this.card.back.trim()) {
      this.toast.showWarningToast('Warning', 'Field cannot be empty.'); // Displays the toast if one of the text areas is empty
    } else {
      console.log('Card saved:', this.card);
      // Backend connectivity
    }
  }

  // TODO
  uploadImageToBackend() {
    console.log('Upload successful: ', this.card)
  }

  // Just experimental
  ngOnInit() {
    if (!this.cookieService.check("simon-seen-edit-card"))
      this.showWelcomeModal();
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

  protected readonly faUpload = faUpload;
  protected readonly faTrash = faTrash;
  protected readonly faPlus = faPlus;
  protected readonly faSave = faSave;
  protected readonly faFileImage = faFileImage;
}
