package com.example.weather;

import com.example.weather.model.DailyWeatherSummary;
import com.example.weather.repository.DailyWeatherSummaryRepository;
import com.example.weather.service.WeatherSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherSummaryServiceTest {

    @Mock
    private DailyWeatherSummaryRepository dailyWeatherSummaryRepository;

    @InjectMocks
    private WeatherSummaryService weatherSummaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateDailySummary() {
        String city = "Delhi";

        weatherSummaryService.updateWeatherData(city, 30.0, "Clear", 60, 5.0);
        weatherSummaryService.updateWeatherData(city, 35.0, "Clear", 55, 4.5);
        weatherSummaryService.updateWeatherData(city, 25.0, "Clouds", 65, 3.5);
        weatherSummaryService.updateWeatherData(city, 28.0, "Clear", 63, 4.0);

        DailyWeatherSummary summary = weatherSummaryService.calculateDailySummary(city);

        assertEquals(city, summary.getCity());
        assertEquals(29.5, summary.getAverageTemperature(), 0.1, "Average temperature should be 29.5°C");
        assertEquals(35.0, summary.getMaxTemperature(), 0.1, "Max temperature should be 35.0°C");
        assertEquals(25.0, summary.getMinTemperature(), 0.1, "Min temperature should be 25.0°C");
        assertEquals("Clear", summary.getDominantCondition(), "Dominant condition should be 'Clear'");
    }

    @Test
    public void testMultipleDaySummaries() {
        // Simulate data for two different days
        String city = "Mumbai";

        // Day 1
        weatherSummaryService.updateWeatherData(city, 29.0, "Rain", 80, 3.0);
        weatherSummaryService.updateWeatherData(city, 33.0, "Rain", 78, 2.5);
        weatherSummaryService.updateWeatherData(city, 27.0, "Clouds", 85, 2.8);

        DailyWeatherSummary day1Summary = weatherSummaryService.calculateDailySummary(city);
        assertEquals(29.67, day1Summary.getAverageTemperature(), 0.1, "Average temperature for Day 1 should be around 29.67°C");
        assertEquals("Rain", day1Summary.getDominantCondition(), "Dominant condition for Day 1 should be 'Rain'");

        weatherSummaryService.resetDataForTest();

        //Day 2
        weatherSummaryService.updateWeatherData(city, 32.0, "Clear", 70, 3.2);
        weatherSummaryService.updateWeatherData(city, 28.0, "Clear", 68, 3.5);
        weatherSummaryService.updateWeatherData(city, 26.0, "Clouds", 75, 4.0);

        DailyWeatherSummary day2Summary = weatherSummaryService.calculateDailySummary(city);
        assertEquals(28.67, day2Summary.getAverageTemperature(), 0.1, "Average temperature for Day 2 should be around 28.67°C");
        assertEquals("Clear", day2Summary.getDominantCondition(), "Dominant condition for Day 2 should be 'Clear'");
    }
}
