package org.ebs.field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Field<T> {
    protected String name;

    protected T value;

    public Field(String name) {
        this.name = name;
    }

    public abstract void setRandomValue();
    public abstract String toStringWithOperator(String operator);
}