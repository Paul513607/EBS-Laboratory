package org.ebs.constant;

import java.util.HashMap;
import java.util.Map;

public class NameToTypeMap {
    public static final Map<String, String> nameToTypeMap = new HashMap<>();

    static {
        nameToTypeMap.put("company", "string");
        nameToTypeMap.put("value", "double");
        nameToTypeMap.put("drop", "double");
        nameToTypeMap.put("variation", "double");
        nameToTypeMap.put("date", "date");
    }
}
