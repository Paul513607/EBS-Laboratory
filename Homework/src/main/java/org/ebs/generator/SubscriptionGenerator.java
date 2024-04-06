package org.ebs.generator;

import org.ebs.constant.FieldFrequencyMap;
import org.ebs.data.Subscription;
import org.ebs.field.DateField;
import org.ebs.field.DoubleField;
import org.ebs.field.Field;
import org.ebs.field.StringField;
import org.ebs.util.FieldParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class SubscriptionGenerator implements Callable<List<Subscription>> {
    private int numberOfSubscriptions;
    private int numberOfThreads;

    private List<Subscription> subscriptions;

    public SubscriptionGenerator(int numberOfSubscriptions, int numberOfThreads) {
        this.numberOfSubscriptions = numberOfSubscriptions;
        this.numberOfThreads = numberOfThreads;

        this.subscriptions = new ArrayList<>();
    }

    @Override
    public List<Subscription> call() {
        for (int i = 0; i < numberOfSubscriptions / numberOfThreads; i++) {
            List<Field<?>> fields = new ArrayList<>();
            for (String fieldName : FieldFrequencyMap.fieldFrequencyMap.keySet()) {
                switch (fieldName) {
                    case "company":
                        if (ThreadLocalRandom.current().nextDouble() < FieldFrequencyMap.getFieldFrequency(fieldName)) {
                            fields.add(new StringField(fieldName, FieldParams.companyPossibleValues));
                        }
                        break;
                    case "value":
                        if (ThreadLocalRandom.current().nextDouble() < FieldFrequencyMap.getFieldFrequency(fieldName)) {
                            fields.add(new DoubleField(fieldName, FieldParams.valueMin, FieldParams.valueMax));
                        }
                        break;
                    case "drop":
                        if (ThreadLocalRandom.current().nextDouble() < FieldFrequencyMap.getFieldFrequency(fieldName)) {
                            fields.add(new DoubleField(fieldName, FieldParams.dropMin, FieldParams.dropMax));
                        }
                        break;
                    case "variation":
                        if (ThreadLocalRandom.current().nextDouble() < FieldFrequencyMap.getFieldFrequency(fieldName)) {
                            fields.add(new DoubleField(fieldName, FieldParams.variationMin, FieldParams.variationMax));
                        }
                        break;
                    case "date":
                        if (ThreadLocalRandom.current().nextDouble() < FieldFrequencyMap.getFieldFrequency(fieldName)) {
                            fields.add(new DateField(fieldName, FieldParams.datePossibleValues));
                        }
                        break;
                    default:
                        break;
                }
            }

            Subscription subscription = new Subscription(fields);
            subscription.generate();
            subscriptions.add(subscription);
        }

        return subscriptions;
    }
}
