package com.example.weather.repository;

import com.example.weather.model.DailyWeatherSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyWeatherSummaryRepository extends MongoRepository<DailyWeatherSummary, String> {

    List<DailyWeatherSummary> findAll();
}