package com.example.weather.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "daily_weather_summaries")
public class DailyWeatherSummary {
    @Id
    private String id;
    private String city;
    private double averageTemperature;
    private double maxTemperature;
    private double minTemperature;
    private String dominantCondition;
    private double averageHumidity;
    private double averageWindspeed;
    private LocalDate date;

    public DailyWeatherSummary(String city, double averageTemperature, double maxTemperature, double minTemperature, String dominantCondition, double averageHumidity, double averageWindspeed) {
        this.city = city;
        this.averageTemperature = averageTemperature;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.dominantCondition = dominantCondition;
        this.averageHumidity = averageHumidity;
        this.averageWindspeed = averageWindspeed;
        this.date = LocalDate.now();
    }

    public double getAverageHumidity() {
        return averageHumidity;
    }

    public void setAverageHumidity(double averageHumidity) {
        this.averageHumidity = averageHumidity;
    }

    public double getAverageWindspeed() {
        return averageWindspeed;
    }

    public void setAverageWindspeed(double averageWindspeed) {
        this.averageWindspeed = averageWindspeed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getDominantCondition() {
        return dominantCondition;
    }

    public void setDominantCondition(String dominantCondition) {
        this.dominantCondition = dominantCondition;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

