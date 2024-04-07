package org.ebs.generator;

import org.ebs.constant.FieldFrequencyMap;
import org.ebs.data.Subscription;
import org.ebs.field.DateField;
import org.ebs.field.DoubleField;
import org.ebs.field.Field;
import org.ebs.field.StringField;
import org.ebs.file.FileManager;
import org.ebs.util.FieldCounter;
import org.ebs.util.FieldParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SubscriptionGenerator implements Callable<List<Subscription>> {
    private int numberOfSubscriptions;
    private int numberOfThreads;

    private int threadNum;

    private List<Subscription> subscriptions;

    private FileManager fileManager;
    private FieldCounter fieldCounter;

    public SubscriptionGenerator(int numberOfSubscriptions, int numberOfThreads, int threadNum, FileManager fileManager) {
        this.numberOfSubscriptions = numberOfSubscriptions;
        this.numberOfThreads = numberOfThreads;
        this.threadNum = threadNum;

        this.fileManager = fileManager;

        this.subscriptions = new ArrayList<>();
        this.fieldCounter = new FieldCounter(threadNum);
    }

    @Override
    public List<Subscription> call() {
        int subscriptionsToGenerate = numberOfSubscriptions / numberOfThreads;
        if (threadNum == numberOfThreads) {
            subscriptionsToGenerate += numberOfSubscriptions - numberOfThreads * subscriptionsToGenerate;
        }

        for (int i = 0; i < subscriptionsToGenerate; i++) {
            List<Field<?>> fields = new ArrayList<>();
            for (String fieldName : FieldFrequencyMap.fieldFrequencyMap.keySet()) {
                int targetCount = fieldCounter.getFieldTotalCountMapThread().get(fieldName);
                double frequency = FieldFrequencyMap.getFieldFrequency(fieldName);

                if (fieldCounter.isAtSubscriptionLimit(fieldName, subscriptionsToGenerate, threadNum) || fieldCounter.getCounterOfField(fieldName) < targetCount &&
                        ThreadLocalRandom.current().nextDouble() < frequency) {
                    switch (fieldName) {
                        case "company":
                            fieldCounter.incrementCompanyCounter();
                            fields.add(new StringField(fieldName, FieldParams.companyPossibleValues));
                            break;
                        case "value":
                            fieldCounter.incrementValueCounter();
                            fields.add(new DoubleField(fieldName, FieldParams.valueMin, FieldParams.valueMax));
                            break;
                        case "drop":
                            fieldCounter.incrementDropCounter();
                            fields.add(new DoubleField(fieldName, FieldParams.dropMin, FieldParams.dropMax));
                            break;
                        case "variation":
                            fieldCounter.incrementVariationCounter();
                            fields.add(new DoubleField(fieldName, FieldParams.variationMin, FieldParams.variationMax));
                            break;
                        case "date":
                            fieldCounter.incrementDateCounter();
                            fields.add(new DateField(fieldName, FieldParams.datePossibleValues));
                            break;
                        default:
                            break;
                    }
                }
            }

            Subscription subscription = new Subscription(fields);
            subscription.generate();
            subscriptions.add(subscription);
            fieldCounter.incrementTotalSubscriptionCounter();
        }

        return subscriptions;
    }
}
