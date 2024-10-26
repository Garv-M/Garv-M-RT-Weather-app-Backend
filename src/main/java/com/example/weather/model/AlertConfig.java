package com.example.weather.model;

public class AlertConfig {
    private String email;
    private String attributeType;  // "temperature" or "condition"
    private String condition;      // ">", "<", "=" for temperature or "equals" for condition
    private double threshold;      // Temperature threshold
    private String conditionValue; // Condition threshold, e.g., "Rain"

    public AlertConfig(String email, String attributeType, String condition, double threshold, String conditionValue) {
        this.email = email;
        this.attributeType = attributeType;
        this.condition = condition;
        this.threshold = threshold;
        this.conditionValue = conditionValue;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }
}
