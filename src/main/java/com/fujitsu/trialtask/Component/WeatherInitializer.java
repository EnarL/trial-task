package com.fujitsu.trialtask.Component;

import com.fujitsu.trialtask.Service.WeatherService;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import com.fujitsu.trialtask.Entity.*;
import com.fujitsu.trialtask.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
@Component
public class WeatherInitializer implements ApplicationRunner {
    private  final WeatherService weatherService;
    private final WeatherRepository weatherRepository;
    @Autowired
    public WeatherInitializer(WeatherService weatherService, WeatherRepository weatherRepository) {
        this.weatherService = weatherService;
        this.weatherRepository = weatherRepository;
    }

    // Adding initial weather data to the database
    // @param args incoming application arguments
    // @throws Exception if an error occurred
    @Override
    public void run(ApplicationArguments args) throws Exception {

        weatherService.fetchWeatherData();
        List<Weather> weatherList = Arrays.asList(
                new Weather("Tartu-Tõravere", "26242", -3.0, 4.0, "", Timestamp.valueOf("2024-02-19 06:15:00")),
                new Weather("Tartu-Tõravere", "26242", 4.0, 0.4, "light snow", Timestamp.valueOf("2024-02-18 11:15:00")),
                new Weather("Tartu-Tõravere", "26242", -5.0, 3.0, "", Timestamp.valueOf("2024-03-02 10:15:00")),
                new Weather("Tallinn-Harku", "26038", -8.5, 3.0, "hail", Timestamp.valueOf("2024-02-13 09:15:00")),
                new Weather("Tallinn-Harku", "26038", 5.0, 0.6, "", Timestamp.valueOf("2024-02-22 18:15:00")),
                new Weather("Tallinn-Harku", "26038", -4.0, 6.0, "light snow", Timestamp.valueOf("2024-03-01 12:15:00")),
                new Weather("Pärnu", "41803", 0.4, 1.2, "light snow", Timestamp.valueOf("2024-02-12 08:15:00")),
                new Weather("Pärnu", "41803", 4.0, 0.5, "", Timestamp.valueOf("2024-02-22 12:15:00")),
                new Weather("Pärnu", "41803", -6.0, 4.0, "rain", Timestamp.valueOf("2024-03-01 10:15:00"))
        );
        weatherRepository.saveAll(weatherList);
    }
}