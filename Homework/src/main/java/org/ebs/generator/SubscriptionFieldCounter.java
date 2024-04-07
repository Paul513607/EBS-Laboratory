package org.ebs.generator;

import lombok.Getter;
import lombok.Synchronized;

public class SubscriptionFieldCounter {
    private static int totalSubscriptionCounter = 0;
    private static int companyCounter = 0;
    private static int valueCounter = 0;
    private static int dropCounter = 0;
    private static int variationCounter = 0;
    private static int dateCounter = 0;

    public static synchronized int getCounterOfField(String fieldName) {
        switch (fieldName) {
            case "company":
                return companyCounter;
            case "value":
                return valueCounter;
            case "drop":
                return dropCounter;
            case "variation":
                return variationCounter;
            case "date":
                return dateCounter;
            default:
                throw new RuntimeException("Unknown field name");
        }
    }

    public static synchronized int getTotalSubscriptionCounter() {
        return totalSubscriptionCounter;
    }

    public static synchronized void incrementCompanyCounter() {
        companyCounter++;
    }

    public static synchronized void incrementValueCounter() {
        valueCounter++;
    }

    public static synchronized void incrementDropCounter() {
        dropCounter++;
    }

    public static synchronized void incrementVariationCounter() {
        variationCounter++;
    }

    public static synchronized void incrementDateCounter() {
        dateCounter++;
    }

    public static synchronized void incrementTotalSubscriptionCounter() {
        totalSubscriptionCounter++;
    }
}
