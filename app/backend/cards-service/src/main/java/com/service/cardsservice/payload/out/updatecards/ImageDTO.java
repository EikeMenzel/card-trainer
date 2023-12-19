package com.service.cardsservice.payload.out.updatecards;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ImageDTO {
    private Long imageId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String imagePath;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private byte[] imageData;
    private OperationDTO operationDTO;

    public ImageDTO(Long imageId, String imagePath, OperationDTO operationDTO) {
        this.imageId = imageId;
        this.imagePath = imagePath;
        this.operationDTO = operationDTO;
    }

    public ImageDTO() {
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
