import {SafeUrl} from "@angular/platform-browser";

export class MultipleChoiceCardImage {
  public questionImage: SafeUrl | undefined;
  public answerImages: Array<SafeUrl | undefined>;

  constructor() {
    this.answerImages = [];
  }
}
