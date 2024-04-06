package org.ebs.constant;

import java.util.HashMap;
import java.util.Map;

public class FieldFrequencyMap {
    public static Map<String, Double> fieldFrequencyMap = new HashMap<>();
    // TODO: change so it can vary
    static {
        fieldFrequencyMap.put("company", 0.5);
        fieldFrequencyMap.put("value", 0.5);
        fieldFrequencyMap.put("drop", 0.5);
        fieldFrequencyMap.put("variation", 0.5);
        fieldFrequencyMap.put("date", 0.5);
    }

    public static double getFieldFrequency(String fieldName) {
        return fieldFrequencyMap.get(fieldName);
    }

    public static boolean fieldFrequencyMapContainsKey(String fieldName) {
        return fieldFrequencyMap.containsKey(fieldName);
    }

    public static void normalizeFieldFrequencies() {
        double sum = fieldFrequencyMap.values().stream().mapToDouble(Double::doubleValue).sum();
        if (sum > 1.0) {
            double factor = 1.0 / sum;
            fieldFrequencyMap.replaceAll((k, v) -> v * factor);
        }
    }
}
