package com.example.weather.service;

import com.example.weather.model.AlertConfig;
import com.example.weather.model.WeatherData;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String[] cities = {"Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad"};

    // to store WeatherData for every 5 minutes.
    private List<WeatherData> weatherDataList = new ArrayList<>();
    private final WeatherSummaryService weatherSummaryService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public WeatherService(WeatherSummaryService weatherSummaryService) {
        this.weatherSummaryService = weatherSummaryService;
        for (String city : cities) {
            weatherDataList.add(new WeatherData(city, "", 0, 0.0, 0.0,0.0,0));
        }
    }

    @Scheduled(fixedRateString = "${weather.update.interval}") // 5 minutes interval
    public void updateWeatherData() {
        System.out.println("Update weather data Called!");
        for (String city : cities) {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
            String response = restTemplate.getForObject(apiUrl, String.class);
            JSONObject weatherDataJson = new JSONObject(response);

            String mainCondition = weatherDataJson.getJSONArray("weather").getJSONObject(0).getString("main");
            double tempKelvin = weatherDataJson.getJSONObject("main").getDouble("temp");
            double tempCelsius = convertToCelsius(tempKelvin);

            double feelsLikeKelvin = weatherDataJson.getJSONObject("main").getDouble("feels_like");
            double feelsLikeCelsius = convertToCelsius(feelsLikeKelvin);
            int humidity = weatherDataJson.getJSONObject("main").getInt("humidity");
            double windSpeed = weatherDataJson.getJSONObject("wind").getDouble("speed");

            long timestamp = weatherDataJson.getLong("dt");

            WeatherData weatherData = new WeatherData(city, mainCondition, humidity, windSpeed, tempCelsius, feelsLikeCelsius, timestamp);
            updateCityWeather(city, weatherData);
            weatherSummaryService.updateWeatherData(city, tempCelsius, mainCondition, humidity, windSpeed);
            for (WeatherData dataWeather : getAllWeatherData()) {
                checkForThresholdBreaches(dataWeather);
            }
        }
    }

    private double convertToCelsius(double tempKelvin){
        return tempKelvin - 273.15;
    }

    private void updateCityWeather(String city, WeatherData newWeatherData) {
        for (int i = 0; i < weatherDataList.size(); i++) {
            if (weatherDataList.get(i).getCity().equalsIgnoreCase(city)) {
                weatherDataList.set(i, newWeatherData);
                break;
            }
        }
    }

    public List<WeatherData> getAllWeatherData() {
        return weatherDataList;
    }

    public Optional<WeatherData> getWeatherDataForCity(String city) {
        return weatherDataList.stream().filter(data -> data.getCity().equalsIgnoreCase(city)).findFirst();
    }

    public void checkForThresholdBreaches(WeatherData weatherData) {
        List<AlertConfig> alertConfigs = alertService.getAlertConfigs();
        List<AlertConfig> alertsToRemove = new ArrayList<>();

        for (AlertConfig config : alertConfigs) {
            boolean thresholdBreached = false;

            // Check temperature threshold
            if (Objects.equals(weatherData.getCity(), "Delhi") && config.getAttributeType().equalsIgnoreCase("temperature")) {
                switch (config.getCondition()) {
                    case ">":
                        if (weatherData.getTemperature() >= config.getThreshold()) {
                            thresholdBreached = true;
                        }
                        break;
                    case "<":
                        if (weatherData.getTemperature() <= config.getThreshold()) {
                            thresholdBreached = true;
                        }
                        break;
                }
            }
            else if (config.getAttributeType().equalsIgnoreCase("condition")) {
                if (config.getCondition().equalsIgnoreCase("equals") &&
                        weatherData.getMainCondition().equalsIgnoreCase(config.getConditionValue())) {
                    thresholdBreached = true;
                }
            }

            if (thresholdBreached) {
                System.out.println("Threshold breached for " + config.getEmail());
                sendEmailAlert(config.getEmail(), config);
                alertsToRemove.add(config);
            }
        }

        if (!alertsToRemove.isEmpty()) {
            alertConfigs.removeAll(alertsToRemove);
            alertService.updateAlertConfigs(alertConfigs);
        }
    }


    public void sendEmailAlert(String email, AlertConfig alertConfig) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Weather Alert");
        if (!Objects.equals(alertConfig.getCondition(), "equals")) {
            message.setText("Weather Alert: The temperature has exceeded the threshold!\n" +
                    alertConfig.getCondition() + " " + alertConfig.getThreshold() + " degree C");
        } else {
            message.setText("Weather Alert: The Main Condition has matched! : " + alertConfig.getConditionValue());
        }
        mailSender.send(message);

        System.out.println("Email sent to " + email);
    }

    public double convertTemperature(double kelvin, String preference) {
        return switch (preference.toLowerCase()) {
            case "celsius" -> kelvin - 273.15;
            case "fahrenheit" -> (kelvin - 273.15) * 9 / 5 + 32;
            default -> throw new IllegalArgumentException("Unsupported temperature preference: " + preference);
        };
    }
}