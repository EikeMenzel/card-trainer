import {OperationDTO} from "./OperationDTO";

export class ImageDTO {
  constructor(
    public imageId: number | null,
    public imagePath: string | null,
    public operationDTO: OperationDTO
  ) {
  }
}
