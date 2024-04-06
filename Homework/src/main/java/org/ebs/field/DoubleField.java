package org.ebs.field;

import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class DoubleField extends Field<Double> {
    private double minValue;
    private double maxValue;

    public DoubleField(String name, double minValue, double maxValue) {
        super(name);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void setRandomValue() {
        this.value = ThreadLocalRandom.current().nextDouble(minValue, maxValue);
    }

    @Override
    public String toString() {
        return "(" + this.name + "," + this.value + ")";
    }

    @Override
    public String toStringWithOperator(String operator) {
        return "(" + this.name + "," + operator + "," + this.value + ")";
    }
}