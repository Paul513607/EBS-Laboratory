package org.ebs.constant;

import org.ebs.Main;
import org.ebs.generator.SubscriptionFieldCounter;

import java.util.HashMap;
import java.util.Map;

public class FieldFrequencyMap {
    public static double MAX_FREQUENCY = 1.0;

    public static Map<String, Double> fieldFrequencyMap = new HashMap<>();
    static {
        fieldFrequencyMap.put("company", 0.5);
        fieldFrequencyMap.put("value", 0.5);
        fieldFrequencyMap.put("drop", 0.5);
        fieldFrequencyMap.put("variation", 0.5);
        fieldFrequencyMap.put("date", 0.5);
    }

    public static Map<String, Integer> fieldTotalCountMap = new HashMap<>();

    public static void fillFieldTotalCountMap(int totalCount) {
        fieldTotalCountMap.put("company", (int) (totalCount * fieldFrequencyMap.get("company")));
        fieldTotalCountMap.put("value", (int) (totalCount * fieldFrequencyMap.get("value")));
        fieldTotalCountMap.put("drop", (int) (totalCount * fieldFrequencyMap.get("drop")));
        fieldTotalCountMap.put("variation", (int) (totalCount * fieldFrequencyMap.get("variation")));
        fieldTotalCountMap.put("date", (int) (totalCount * fieldFrequencyMap.get("date")));
    }

    public static double getFieldFrequency(String fieldName) {
        return fieldFrequencyMap.get(fieldName);
    }

    public synchronized static double getAdjustedFieldFrequency(String fieldName, int numberOfSubscriptions) {
        int targetCount = fieldTotalCountMap.get(fieldName);
        int generatedCount = SubscriptionFieldCounter.getCounterOfField(fieldName);

        int remainingCount = targetCount - generatedCount;

        if (remainingCount <= 0) {
            return 0.0;  // No more subscriptions needed for this field
        }
        double baseFrequency = fieldFrequencyMap.get(fieldName);
        double adjustedFrequency = baseFrequency * ((double) remainingCount / numberOfSubscriptions);

        // Ensure adjusted frequency is within bounds
        adjustedFrequency = Math.min(1.0, adjustedFrequency);
        adjustedFrequency = Math.max(0.0, adjustedFrequency);

        return adjustedFrequency;
    }

    public static boolean fieldFrequencyMapContainsKey(String fieldName) {
        return fieldFrequencyMap.containsKey(fieldName);
    }

    public static synchronized boolean isAtSubscriptionLimit(String fieldName, int numberOfSubscriptions) {
        int targetCount = fieldTotalCountMap.get(fieldName);
        int generatedCount = SubscriptionFieldCounter.getCounterOfField(fieldName);

        return numberOfSubscriptions - SubscriptionFieldCounter.getTotalSubscriptionCounter() == targetCount - generatedCount;
    }

    public static void normalizeFieldFrequencies() {
        double sum = fieldFrequencyMap.values().stream().mapToDouble(Double::doubleValue).sum();
        if (sum > 1.0) {
            double factor = 1.0 / sum;
            fieldFrequencyMap.replaceAll((k, v) -> v * factor);
        }
    }
}
