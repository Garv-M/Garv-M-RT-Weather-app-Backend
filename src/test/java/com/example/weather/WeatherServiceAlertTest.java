package com.example.weather;

import com.example.weather.model.AlertConfig;
import com.example.weather.model.WeatherData;
import com.example.weather.service.AlertService;
import com.example.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WeatherServiceAlertTest {

    @Mock
    private AlertService alertService;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private WeatherService weatherService;

    private List<AlertConfig> alertConfigs;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        alertConfigs = new ArrayList<>();
        alertConfigs.add(new AlertConfig("user1@example.com", "temperature", ">", 35.0,null));
        alertConfigs.add(new AlertConfig("user2@example.com", "condition", "equals", 0.0, "Rain"));

        injectPrivateField(weatherService, "alertService", alertService);
        injectPrivateField(weatherService, "mailSender", mailSender);

        when(alertService.getAlertConfigs()).thenReturn(alertConfigs);
    }

    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void testTemperatureThresholdBreached() {
        WeatherData weatherData = new WeatherData("Delhi", "Clear", 50, 3.0, 36.0, 35.0, System.currentTimeMillis());

        weatherService.checkForThresholdBreaches(weatherData);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testConditionThresholdBreached() {
        WeatherData weatherData = new WeatherData("Delhi", "Rain", 85, 2.5, 28.0, 27.0, System.currentTimeMillis());

        weatherService.checkForThresholdBreaches(weatherData);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testNoAlertWhenThresholdNotBreached() {
        WeatherData weatherData = new WeatherData("Delhi", "Clear", 50, 3.0, 30.0, 28.0, System.currentTimeMillis());

        weatherService.checkForThresholdBreaches(weatherData);

        verify(mailSender, times(0)).send(any(SimpleMailMessage.class));
    }
}
