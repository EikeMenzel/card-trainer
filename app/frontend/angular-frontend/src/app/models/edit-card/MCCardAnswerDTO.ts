export class MCCardAnswerDTO {
  constructor(
    public answerId: number,
    public answer: string,
    public isRightAnswer: boolean,
    public imagePath: string | null
  ) {
  }
}
