package org.ebs.data;

import lombok.Getter;
import lombok.Setter;
import org.ebs.field.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static org.ebs.constant.EqOperatorFrequencyMap.*;

@Getter
@Setter
public class Subscription {
    private List<Field<?>> fields;
    private List<String> fieldOperators;

    private final List<String> operators = List.of("=", ">=", ">", "<", "<=");

    public Subscription(List<Field<?>> fields) {
        this.fields = fields;
        this.fieldOperators = new ArrayList<>();
    }

    public void generate() {
        for (Field<?> field : fields) {
            field.setRandomValue();
            String operator;
            if (eqMapContainsKey(field.getName())) {
                operator = getRandomOperator(field.getName());
            } else {
                operator = getRandomOperator(null);
            }
            fieldOperators.add(operator);
        }
    }

    private String getRandomOperator(String fieldName) {
        if (fieldName == null) {
            int index = ThreadLocalRandom.current().nextInt(operators.size());
            return operators.get(index);
        }

        double randomNumber = ThreadLocalRandom.current().nextDouble(0, 1);
        if (randomNumber < getEqFrequency(fieldName)) {
            return "=";
        } else {
            List<String> operatorsButEqual = operators.subList(1, operators.size());
            int index = ThreadLocalRandom.current().nextInt(operatorsButEqual.size());
            return operatorsButEqual.get(index);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < fields.size() - 1; i++) {
            sb.append(fields.get(i).toStringWithOperator(fieldOperators.get(i))).append(";");
        }
        sb.append(fields.get(fields.size() - 1).toStringWithOperator(fieldOperators.get(fields.size() - 1)));
        sb.append("}");
        return sb.toString();
    }
}
