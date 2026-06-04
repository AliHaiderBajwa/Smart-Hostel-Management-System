package hostel.controllers;

import java.util.HashMap;
import java.util.Map;

public class PolicyController {

    // TODO: Replace HashMap with PolicyDAO for DB persistence
    private static final HashMap<String, String> policies = new HashMap<>();

    static {
        policies.put("maxLeaveDays", "10");
        policies.put("maxRoomChanges", "2");
        policies.put("messCutoffTime", "08:30 AM");
        policies.put("baseMessCharges", "Rs. 8000");
        policies.put("penaltyRoomChangeExcess", "Rs. 500");
        policies.put("penaltyPropertyDamage", "Rs. 1000");
    }

    public static String getPolicy(String key) {
        return policies.getOrDefault(key, "");
    }

    public static void updatePolicy(String key, String value) {
        policies.put(key, value);
    }

    public static Map<String, String> getAllPolicies() {
        return new HashMap<>(policies);
    }
}
