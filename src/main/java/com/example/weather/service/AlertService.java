package com.example.weather.service;

import com.example.weather.model.AlertConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {

    private List<AlertConfig> alertConfigs = new ArrayList<>();

    public void saveAlertConfig(AlertConfig alertConfig) {
        alertConfigs.add(alertConfig);
    }

    public List<AlertConfig> getAlertConfigs() {
        return alertConfigs;
    }

    public void updateAlertConfigs(List<AlertConfig> updatedConfigs) {
        this.alertConfigs = updatedConfigs;
    }

    public List<AlertConfig> getTriggeredAlerts() {
        return alertConfigs;
    }
}

