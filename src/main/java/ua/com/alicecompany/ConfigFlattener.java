package ua.com.alicecompany;

import java.util.HashMap;
import java.util.Map;

public class ConfigFlattener {
    public static Map<String, String> flattenConfig(Map<String, Object> nestedConfig, String prefix) {
        Map<String, String> flatConfig = new HashMap<>();
        nestedConfig.forEach((key, value) -> {
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;
            if (value instanceof Map) {
                flatConfig.putAll(flattenConfig((Map<String, Object>) value, fullKey));
            } else {
                flatConfig.put(fullKey, value.toString());
            }
        });
        return flatConfig;
    }
}