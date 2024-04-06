package org.ebs.data;

import lombok.Getter;
import lombok.Setter;
import org.ebs.field.DateField;
import org.ebs.field.DoubleField;
import org.ebs.field.StringField;


@Getter
@Setter
public class Publication {
    private StringField company;
    private DoubleField value;
    private DoubleField drop;
    private DoubleField variation;
    private DateField date;

    public Publication(StringField company, DoubleField value, DoubleField drop, DoubleField variation, DateField date) {
        this.company = company;
        this.value = value;
        this.drop = drop;
        this.variation = variation;
        this.date = date;
    }

    public void generate() {
        this.company.setRandomValue();
        this.value.setRandomValue();
        this.drop.setRandomValue();
        this.variation.setRandomValue();
        this.date.setRandomValue();
    }

    @Override
    public String toString() {
        return "{" + this.company.toString() + ";" +
                this.value.toString() + ";" +
                this.drop.toString() + ";" +
                this.variation.toString() + ";" +
                this.date.toString() +
                "}";
    }
}
