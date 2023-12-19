import {EditCardDTO} from "./EditCardDTO";

export class EditCardBasicDTO {

  constructor(
    public cardDTO: EditCardDTO,
    public textAnswer: string,
    public imagePath: string | undefined
  ) {}
}
