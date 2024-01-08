import {EditCardDTO} from "./EditCardDTO";

export class CreateCardBasicDTO {

  constructor(
    public cardDTO: EditCardDTO,
    public textAnswer: string,
    public imagePath: number | null
  ) {
  }
}
