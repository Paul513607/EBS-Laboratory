package org.ebs.constant;

import java.util.HashMap;
import java.util.Map;

public class EqOperatorFrequencyMap {
    public static Map<String, Double> eqOperatorFrequencyMap = new HashMap<>();
    // TODO: change so it can vary
    static {
        eqOperatorFrequencyMap.put("company", 0.5);
        eqOperatorFrequencyMap.put("value", 0.5);
        eqOperatorFrequencyMap.put("drop", 0.5);
        eqOperatorFrequencyMap.put("variation", 0.5);
        eqOperatorFrequencyMap.put("date", 0.5);
    }

    public static double getEqFrequency(String fieldName) {
        return eqOperatorFrequencyMap.get(fieldName);
    }

    public static boolean eqMapContainsKey(String fieldName) {
        return eqOperatorFrequencyMap.containsKey(fieldName);
    }
}
