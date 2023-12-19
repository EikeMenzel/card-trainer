export class EditCardMCHolder {
  constructor(
    public answer: string,
    public isRightAnswer: boolean,
    public imagePath: string | null,
    public imageId: string | null,
    public ogImageid: string | null,
    public answerId: number | null,
    public imageFile: File | null,
  ) {
  }
}
