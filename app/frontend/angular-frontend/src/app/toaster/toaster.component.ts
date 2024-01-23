import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ToastEvent } from '../services/toast-service/models/toast-event';
import { ToastService } from '../services/toast-service/toast.service';
import {ToastComponent} from "../toast/toast.component";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-toaster',
  standalone: true,
  templateUrl: './toaster.component.html',
  styleUrls: ['./toaster.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ToastComponent,
    NgForOf
  ]
})
export class ToasterComponent implements OnInit {
  currentToasts: ToastEvent[] = [];

  constructor(
    private toastService: ToastService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.subscribeToToasts();
  }

  subscribeToToasts() {
    this.toastService.toastEvents.subscribe((toasts) => {
      const currentToast: ToastEvent = {
        type: toasts.type,
        title: toasts.title,
        message: toasts.message,
      };
      this.currentToasts.push(currentToast);
      this.cdr.detectChanges();
    });
  }

  dispose(index: number) {
    this.currentToasts.splice(index, 1);
    this.cdr.detectChanges();
  }
}
