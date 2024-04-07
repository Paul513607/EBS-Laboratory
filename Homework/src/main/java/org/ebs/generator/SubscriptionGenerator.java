package org.ebs.generator;

import org.ebs.constant.FieldFrequencyMap;
import org.ebs.data.Subscription;
import org.ebs.field.DateField;
import org.ebs.field.DoubleField;
import org.ebs.field.Field;
import org.ebs.field.StringField;
import org.ebs.file.FileManager;
import org.ebs.util.FieldParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class SubscriptionGenerator implements Callable<List<Subscription>> {
    private int numberOfSubscriptions;
    private int numberOfThreads;

    private int threadNum;

    private List<Subscription> subscriptions;

    private FileManager fileManager;

    private final int MAX_ATTEMPTS = 3;

    public SubscriptionGenerator(int numberOfSubscriptions, int numberOfThreads, int threadNum, FileManager fileManager) {
        this.numberOfSubscriptions = numberOfSubscriptions;
        this.numberOfThreads = numberOfThreads;

        this.fileManager = fileManager;

        this.threadNum = threadNum;

        this.subscriptions = new ArrayList<>();
    }

    @Override
    public List<Subscription> call() {
        int subscriptionsToGenerate = numberOfSubscriptions / numberOfThreads;
        if (threadNum == numberOfThreads) {
            subscriptionsToGenerate = subscriptionsToGenerate + (numberOfSubscriptions - numberOfThreads * subscriptionsToGenerate);
        }

        for (int i = 0; i < subscriptionsToGenerate; i++) {
            List<Field<?>> fields = new ArrayList<>();
            int attempts = 0;
            boolean validSubscription = false;
            while (!validSubscription && attempts < MAX_ATTEMPTS) {
                for (String fieldName : FieldFrequencyMap.fieldFrequencyMap.keySet()) {
                    int targetCount = FieldFrequencyMap.fieldTotalCountMap.get(fieldName);
                    double frequency = FieldFrequencyMap.getFieldFrequency(fieldName);

                    if (SubscriptionFieldCounter.getCounterOfField(fieldName) < targetCount &&
                            ThreadLocalRandom.current().nextDouble() < frequency || FieldFrequencyMap.isAtSubscriptionLimit(fieldName, numberOfSubscriptions)) {
                        switch (fieldName) {
                            case "company":
                                SubscriptionFieldCounter.incrementCompanyCounter();
                                fields.add(new StringField(fieldName, FieldParams.companyPossibleValues));
                                break;
                            case "value":
                                SubscriptionFieldCounter.incrementValueCounter();
                                fields.add(new DoubleField(fieldName, FieldParams.valueMin, FieldParams.valueMax));
                                break;
                            case "drop":
                                SubscriptionFieldCounter.incrementDropCounter();
                                fields.add(new DoubleField(fieldName, FieldParams.dropMin, FieldParams.dropMax));
                                break;
                            case "variation":
                                SubscriptionFieldCounter.incrementVariationCounter();
                                fields.add(new DoubleField(fieldName, FieldParams.variationMin, FieldParams.variationMax));
                                break;
                            case "date":
                                SubscriptionFieldCounter.incrementDateCounter();
                                fields.add(new DateField(fieldName, FieldParams.datePossibleValues));
                                break;
                            default:
                                break;
                        }
                    }
                }

                Subscription subscription = new Subscription(fields);
                subscription.generate();
                validSubscription = subscription.getFields().size() > 0;
                if (validSubscription) {
                    subscriptions.add(subscription);
                    SubscriptionFieldCounter.incrementTotalSubscriptionCounter();
                    i++;
                    break;
                } else {
                    attempts++;
                }

                if (attempts == 3) {
                    i++;
                }
            }
        }

        return subscriptions;
    }
}
