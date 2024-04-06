package org.ebs.field;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class StringField extends Field<String> {
    private List<String> predefinedValues;

    public StringField(String name, List<String> predefinedValues) {
        super(name);
        this.predefinedValues = predefinedValues;
    }

    @Override
    public void setRandomValue() {
        if (predefinedValues != null && !predefinedValues.isEmpty()) {
            this.value = predefinedValues.get(ThreadLocalRandom.current().nextInt(predefinedValues.size()));
        } else {
            this.value = "Unsupported type";
        }
    }

    @Override
    public String toString() {
        return "(" + this.name + ",\"" + this.value + "\")";
    }

    @Override
    public String toStringWithOperator(String operator) {
        return "(" + this.name + "," + operator + ",\"" + this.value + "\")";
    }
}