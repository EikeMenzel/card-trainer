package com.service.databaseservice.payload.inc.learnsession;

public enum RatingLevelDTO {
    RATING_0("rating0"),
    RATING_1("rating1"),
    RATING_2("rating2"),
    RATING_3("rating3"),
    RATING_4("rating4"),
    RATING_5("rating5");

    private final String fieldName;

    RatingLevelDTO(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
