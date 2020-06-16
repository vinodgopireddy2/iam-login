package com.intuit.iam.validation.Rules;

public class Field<T> {
    String fieldName;

    String pattern;

    T field;
    public Field(String fieldName, T field, String pattern) {
        this.field = field;
        this.fieldName = fieldName;
        this.pattern = pattern;
    }
    public String getFieldName() {
        return fieldName;
    }
    public T getField() {
        return field;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
