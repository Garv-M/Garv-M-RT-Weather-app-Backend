package com.example.weather.service;

import com.example.weather.model.CityWeatherDataSummary;
import com.example.weather.model.DailyWeatherSummary;
import com.example.weather.repository.DailyWeatherSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeatherSummaryService {

    private Map<String, List<CityWeatherDataSummary>> cityWeatherDataMap = new HashMap<>();
    private Map<String, Map<String, Integer>> cityWeatherConditions = new HashMap<>(); // Clear -> 3 times, Rain -> 5 times
    private DailyWeatherSummaryRepository dailyWeatherSummaryRepository;

    @Autowired
    public WeatherSummaryService(DailyWeatherSummaryRepository dailyWeatherSummaryRepository) {
        this.dailyWeatherSummaryRepository = dailyWeatherSummaryRepository;
    }

    // This method will be called by WeatherService every 5 minutes
    public void updateWeatherData(String city, double temperature, String condition, int humidity, double windSpeed) {
        CityWeatherDataSummary data = new CityWeatherDataSummary(temperature, humidity, windSpeed);
        cityWeatherDataMap.putIfAbsent(city, new ArrayList<>());
        cityWeatherDataMap.get(city).add(data);

        cityWeatherConditions.putIfAbsent(city, new HashMap<>());
        Map<String, Integer> conditionMap = cityWeatherConditions.get(city);
        conditionMap.put(condition, conditionMap.getOrDefault(condition, 0) + 1);
    }

    @Scheduled(cron = "0 0 0 * * *")  // At midnight, every day
    public void generateDailySummaries() {
        System.out.println("Generating daily summaries...");
        for (String city : cityWeatherDataMap.keySet()) {
            DailyWeatherSummary summary = calculateDailySummary(city);
            if (summary != null) {
                storeSummaryInDB(summary);
            }
        }

        // Clear the maps for the next day
        cityWeatherDataMap.clear();
        cityWeatherConditions.clear();
    }

    public DailyWeatherSummary calculateDailySummary(String city) {
        Map<String, Integer> conditionCounts = cityWeatherConditions.get(city);
        List<CityWeatherDataSummary> weatherDataList = cityWeatherDataMap.get(city);

        if (weatherDataList == null || weatherDataList.isEmpty()) {
            System.out.println("No data available for " + city);
            return null;
        }

        double totalTemperature = 0;
        double totalHumidity = 0;
        double totalWindSpeed = 0;

        double maxTemperature = Double.MIN_VALUE;
        double minTemperature = Double.MAX_VALUE;

        for (CityWeatherDataSummary data : weatherDataList) {
            double temp = data.getTemperature();
            double humidity = data.getHumidity();
            double windSpeed = data.getWindSpeed();

            totalTemperature += temp;
            totalHumidity += humidity;
            totalWindSpeed += windSpeed;

            maxTemperature = Math.max(maxTemperature, temp);
            minTemperature = Math.min(minTemperature, temp);
        }

        double averageTemperature = totalTemperature / weatherDataList.size();
        double averageHumidity = !weatherDataList.isEmpty() ? totalHumidity / weatherDataList.size() : 0.0;
        double averageWindSpeed = !weatherDataList.isEmpty() ? totalWindSpeed / weatherDataList.size() : 0.0;

        String dominantCondition = conditionCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");

        return new DailyWeatherSummary(city, averageTemperature, maxTemperature, minTemperature, dominantCondition, averageHumidity, averageWindSpeed);
    }


    public void storeSummaryInDB(DailyWeatherSummary summary) {
        dailyWeatherSummaryRepository.save(summary);
    }

    public List<DailyWeatherSummary> getSummaryForPreviousDay() {
        return dailyWeatherSummaryRepository.findAll();
    }

    public void resetDataForTest() {
        cityWeatherDataMap.clear();
        cityWeatherConditions.clear();
    }
}
