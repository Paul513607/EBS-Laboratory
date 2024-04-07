package org.ebs.util;

import lombok.Getter;
import org.ebs.Main;
import org.ebs.constant.FieldFrequencyMap;
import org.ebs.generator.SubscriptionFieldCounter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class FieldCounter {
    private int totalSubscriptionCounter = 0;
    private int companyCounter = 0;
    private int valueCounter = 0;
    private int dropCounter = 0;
    private int variationCounter = 0;
    private int dateCounter = 0;

    private Map<String, Integer> fieldTotalCountMapThread = new HashMap<>();

    public FieldCounter(int threadNum) {
        List<String> fieldNames = List.of("company", "value", "drop", "variation", "date");
        for (String fieldName : fieldNames) {
            int totalFieldCount = FieldFrequencyMap.fieldTotalCountMap.get(fieldName);
            int toGenerate = totalFieldCount / Main.numberOfThreads;
            if (threadNum == Main.numberOfThreads) {
                toGenerate += totalFieldCount - Main.numberOfThreads * toGenerate;
            }

            fieldTotalCountMapThread.put(fieldName, toGenerate);
        }
    }

    public boolean isAtSubscriptionLimit(String fieldName, int subscriptionsToGenerate, int threadNum) {
        int targetCount = fieldTotalCountMapThread.get(fieldName);
        int generatedCount = this.getCounterOfField(fieldName);

        return subscriptionsToGenerate - totalSubscriptionCounter == targetCount - generatedCount;
    }

    public int getCounterOfField(String fieldName) {
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

    public void incrementCompanyCounter() {
        companyCounter++;
    }

    public void incrementValueCounter() {
        valueCounter++;
    }

    public void incrementDropCounter() {
        dropCounter++;
    }

    public void incrementVariationCounter() {
        variationCounter++;
    }

    public void incrementDateCounter() {
        dateCounter++;
    }

    public void incrementTotalSubscriptionCounter() {
        totalSubscriptionCounter++;
    }
}
