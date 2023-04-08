package com.jira.health_information_system.config;

import java.util.HashMap;
import java.util.Map;

public class Helper {
    public static Map<String, String>makeMessage(String message){
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("message", message);
        return stringMap;
    }
}
