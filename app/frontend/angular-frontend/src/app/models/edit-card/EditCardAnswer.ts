export class EditCardAnswer {
  constructor(
    public choiceAnswerId: number | null,
    public answer: string,
    public rightAnswer: boolean,
    public imageId: number | null,
  ) {
  }
}
