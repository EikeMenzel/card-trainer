package com.service.databaseservice.payload.inc.learnsession;

public enum StatusTypeDTO {
    FINISHED(2L),
    CANCELED(3L);
    private final Long fieldName;

    StatusTypeDTO(Long fieldName) {
        this.fieldName = fieldName;
    }

    public Long getFieldId() {
        return fieldName;
    }
}
