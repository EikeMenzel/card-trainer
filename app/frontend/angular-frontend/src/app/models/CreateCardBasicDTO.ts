import {EditCardDTO} from "./EditCardDTO";

export class CreateCardBasicDTO {
  constructor(
    public cardDTO: EditCardDTO,
    public imagePath: string,
    public textAnswer: string
  ) {}

}
