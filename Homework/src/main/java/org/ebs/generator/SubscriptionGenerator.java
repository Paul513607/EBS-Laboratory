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
import java.util.*;
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

        subscriptions = splitSubscriptions(subscriptions);
        Collections.shuffle(subscriptions);
        return subscriptions;
    }

    private List<Subscription> splitSubscriptions(List<Subscription> subscriptions) {
        if (!hasEmptyPublications(subscriptions)) {
            return subscriptions;
        }

        subscriptions.sort(Comparator.comparingInt(s -> s.getFields().size()));
        Collections.reverse(subscriptions);
        int countOfEmptySubscriptions = (int) subscriptions.stream().filter(this::isEmpty).count();
        List<Subscription> splitSubscriptions = new ArrayList<>(subscriptions
                .subList(countOfEmptySubscriptions, subscriptions.size() - countOfEmptySubscriptions));
        for (int i = 0; i < countOfEmptySubscriptions; i++) {
            Subscription subscription = subscriptions.get(i);
            splitSubscriptions.addAll(splitSubscription(subscription));
        }

        return splitSubscriptions;
    }

    private List<Subscription> splitSubscription(Subscription subscription) {
        Subscription firstSubscription, secondSubscription;
        List<Field<?>> fields = subscription.getFields();
        List<String> fieldOperators = subscription.getFieldOperators();
        int randomIndex = ThreadLocalRandom.current().nextInt(0, fields.size() - 1);
        firstSubscription = new Subscription(fields.subList(0, randomIndex + 1));
        firstSubscription.setFieldOperators(fieldOperators.subList(0, randomIndex + 1));
        secondSubscription = new Subscription(fields.subList(randomIndex + 1, fields.size()));
        secondSubscription.setFieldOperators(fieldOperators.subList(randomIndex + 1, fields.size()));

        return List.of(firstSubscription, secondSubscription);
    }

    private boolean hasEmptyPublications(List<Subscription> subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (isEmpty(subscription)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmpty(Subscription subscription) {
        return subscription.getFields().isEmpty();
    }
}
