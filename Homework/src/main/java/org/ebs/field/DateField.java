package org.ebs.field;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class DateField extends Field<LocalDate> {
    private List<LocalDate> predefinedValues;

    public DateField(String name, List<LocalDate> predefinedValues) {
        super(name);
        this.predefinedValues = predefinedValues;
    }

    @Override
    public void setRandomValue() {
        if (predefinedValues != null && !predefinedValues.isEmpty()) {
            this.value = predefinedValues.get(ThreadLocalRandom.current().nextInt(predefinedValues.size()));
        } else {
            this.value = LocalDate.now();
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return "(" + this.name + "," + this.value.format(pattern) + ")";
    }

    @Override
    public String toStringWithOperator(String operator) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return "(" + this.name + "," + operator + "," + this.value.format(pattern) + ")";
    }
}