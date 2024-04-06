package org.ebs.constant;

import lombok.Getter;

@Getter
public enum FieldNames {
    COMPANY("company"),
    VALUE("value"),
    DROP("drop"),
    VARIATION("variation"),
    DATE("date");

    private final String fieldName;

    FieldNames(String fieldName) {
        this.fieldName = fieldName;
    }
}
