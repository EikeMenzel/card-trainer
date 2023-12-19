// "choiceAnswerId": 1,
//   "answer": "BULLSHIT",
//   "correct": false,
//   "imageDTO": null
import {ImageDTO} from "./ImageDTO";

export class UpdateMCAnswerDTO {
  constructor(
    public choiceAnswerId: number,
    public answer: string,
    public correct: boolean,
    public imageDTO: ImageDTO | null
  ) {
  }
}
