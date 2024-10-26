package com.example.weather.controller;

import com.example.weather.model.AlertConfig;
import com.example.weather.model.DailyWeatherSummary;
import com.example.weather.model.WeatherData;
import com.example.weather.service.AlertService;
import com.example.weather.service.WeatherService;
import com.example.weather.service.WeatherSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private WeatherSummaryService weatherSummaryService;

    // Get weather data for all cities
    @GetMapping("/all")
    public List<WeatherData> getAllWeatherData() {
        return weatherService.getAllWeatherData();
    }

    // Get weather data for a specific city
    @GetMapping("/{city}")
    public Optional<WeatherData> getWeatherDataForCity(@PathVariable String city) {
        return weatherService.getWeatherDataForCity(city);
    }

    // Save the alert configs from the user
    @PostMapping("/alert-config")
    public String setAlertConfig(@RequestBody AlertConfig alertConfig) {
        alertService.saveAlertConfig(alertConfig);
        return "Alert configuration saved successfully!";
    }

    // Total Alert configs
    @GetMapping("/triggered-alerts")
    public List<AlertConfig> getTriggeredAlerts() {
        return alertService.getTriggeredAlerts();
    }

    // Get daily summaries for a specific date
    @GetMapping("/daily-summaries")
    public List<DailyWeatherSummary> getSummariesByDate() {
        return weatherSummaryService.getSummaryForPreviousDay();
    }
}
