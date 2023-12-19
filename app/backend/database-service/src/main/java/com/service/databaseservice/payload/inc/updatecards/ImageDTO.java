package com.service.databaseservice.payload.inc.updatecards;

public final class ImageDTO {
    private Long imageId;
    private byte[] imageData;
    private OperationDTO operationDTO;

    public ImageDTO(Long imageId, byte[] imageData, OperationDTO operationDTO) {
        this.imageId = imageId;
        this.imageData = imageData;
        this.operationDTO = operationDTO;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
    public OperationDTO getOperationDTO() {
        return operationDTO;
    }

    public void setOperationDTO(OperationDTO operationDTO) {
        this.operationDTO = operationDTO;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

}
