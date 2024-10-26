package com.example.weather;

import com.example.weather.model.WeatherData;
import com.example.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WeatherApplicationTests {

	@Value("${openweathermap.api.key}")
	private String apiKey;
	String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=delhi&appid=" + apiKey;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private WeatherService weatherService;
	private ScheduledExecutorService executorService;
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		String mockResponse = "{ \"weather\": [{ \"main\": \"Clear\" }], \"main\": { \"temp\": 295.15, \"feels_like\": 294.15, \"humidity\": 50 }, \"wind\": { \"speed\": 1.5 }, \"dt\": 1625164800 }";
		when(restTemplate.getForObject(apiUrl,String.class)).thenReturn(mockResponse);

		executorService = Executors.newSingleThreadScheduledExecutor();
	}

	@Test
	public void testSystemStartsAndConnectsToAPI() {
		List<WeatherData> weatherDataList = weatherService.getAllWeatherData();

		assertNotNull(weatherDataList, "Weather data list should not be null");
		assertFalse(weatherDataList.isEmpty(), "Weather data list should not be empty");

		assertNotNull(apiKey, "API key should not be null");
		System.out.println("API Key is loaded successfully.");
	}

	@Test
	public void testConvertTemperatureToCelsius() {
		assertEquals(0.0, weatherService.convertTemperature(273.15, "celsius"), 0.01, "273.15 K should be 0°C");
		assertEquals(25.0, weatherService.convertTemperature(298.15, "celsius"), 0.01, "298.15 K should be 25°C");
		assertEquals(-273.15, weatherService.convertTemperature(0, "celsius"), 0.01, "0 K should be -273.15°C");
	}

	@Test
	public void testConvertTemperatureToFahrenheit() {
		assertEquals(32.0, weatherService.convertTemperature(273.15, "fahrenheit"), 0.01, "273.15 K should be 32°F");
		assertEquals(77.0, weatherService.convertTemperature(298.15, "fahrenheit"), 0.01, "298.15 K should be 77°F");
		assertEquals(-459.67, weatherService.convertTemperature(0, "fahrenheit"), 0.01, "0 K should be -459.67°F");
	}

	@Test
	public void testInvalidTemperaturePreference() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			weatherService.convertTemperature(273.15, "kelvin");
		});
		assertEquals("Unsupported temperature preference: kelvin", exception.getMessage());
	}



}
