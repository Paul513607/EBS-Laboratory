package org.ebs.generator;

import lombok.Getter;
import lombok.Setter;
import org.ebs.constant.FieldFrequencyMap;
import org.ebs.data.Publication;
import org.ebs.data.Subscription;
import org.ebs.field.Field;
import org.ebs.file.FileManager;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class Generator {
    private int numberOfThreads;

    private int numberOfPublications;
    private int numberOfSubscriptions;

    private List<Publication> allPublications = new ArrayList<>();
    private List<Subscription> allSubscriptions = new ArrayList<>();

    private FileManager fileManager;

    public Generator(int numberOfThreads, int numberOfPublications, int numberOfSubscriptions, FileManager fileManager) {
        this.numberOfThreads = numberOfThreads;
        this.numberOfPublications = numberOfPublications;
        this.numberOfSubscriptions = numberOfSubscriptions;
        this.fileManager = fileManager;
    }

    public void generate() {
        FieldFrequencyMap.fillFieldTotalCountMap(numberOfSubscriptions);

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<List<Publication>>> futurePublications = new ArrayList<>();
        List<Future<List<Subscription>>> futureSubscriptions = new ArrayList<>();

        // Submit PublicationGenerator tasks
        for (int i = 0; i < numberOfThreads; i++) {
            PublicationGenerator publicationGenerator = new PublicationGenerator(numberOfPublications, numberOfThreads, i + 1, fileManager);
            Future<List<Publication>> futurePublication = executor.submit(publicationGenerator);
            futurePublications.add(futurePublication);
        }

        // Submit SubscriptionGenerator tasks
        for (int i = 0; i < numberOfThreads; i++) {
            SubscriptionGenerator subscriptionGenerator = new SubscriptionGenerator(numberOfSubscriptions, numberOfThreads, i + 1, fileManager);
            Future<List<Subscription>> futureSubscription = executor.submit(subscriptionGenerator);
            futureSubscriptions.add(futureSubscription);
        }

        // Retrieve publications from futures
        for (Future<List<Publication>> future : futurePublications) {
            try {
                List<Publication> publications = future.get();
                addPublications(publications);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Retrieve subscriptions from futures
        for (Future<List<Subscription>> future : futureSubscriptions) {
            try {
                List<Subscription> subscriptions = splitSubscriptions(future.get());
                addSubscriptions(subscriptions);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }

    public void writePublicationsToFile() {
        try {
            fileManager.writePublicationToFile(allPublications);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeSubscriptionsToFile() {
        try {
            fileManager.writeSubscriptionToFile(allSubscriptions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        int randomIndex = ThreadLocalRandom.current().nextInt(1, fields.size() - 1);
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

    private synchronized void addPublications(List<Publication> publicationsToAdd) {
        this.allPublications.addAll(publicationsToAdd);
    }

    private synchronized void addSubscriptions(List<Subscription> subscriptionsToAdd) {
        this.allSubscriptions.addAll(subscriptionsToAdd);
    }
}
