package org.ebs.generator;

import lombok.Getter;
import lombok.Setter;
import org.ebs.data.Publication;
import org.ebs.data.Subscription;
import org.ebs.file.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<List<Publication>>> futurePublications = new ArrayList<>();
        List<Future<List<Subscription>>> futureSubscriptions = new ArrayList<>();

        // Submit PublicationGenerator tasks
        for (int i = 0; i < numberOfThreads; i++) {
            PublicationGenerator publicationGenerator = new PublicationGenerator(numberOfPublications, numberOfThreads, fileManager);
            Future<List<Publication>> futurePublication = executor.submit(publicationGenerator);
            futurePublications.add(futurePublication);
        }

        // Submit SubscriptionGenerator tasks
        for (int i = 0; i < numberOfThreads; i++) {
            SubscriptionGenerator subscriptionGenerator = new SubscriptionGenerator(numberOfSubscriptions, numberOfThreads, fileManager);
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
                List<Subscription> subscriptions = future.get();
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

    private synchronized void addPublications(List<Publication> publicationsToAdd) {
        this.allPublications.addAll(publicationsToAdd);
    }

    private synchronized void addSubscriptions(List<Subscription> subscriptionsToAdd) {
        this.allSubscriptions.addAll(subscriptionsToAdd);
    }
}
