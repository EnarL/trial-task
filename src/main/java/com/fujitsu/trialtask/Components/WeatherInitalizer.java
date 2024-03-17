package com.fujitsu.trialtask.Components;

import org.springframework.stereotype.Component;

import com.fujitsu.trialtask.Entities.*;
import com.fujitsu.trialtask.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class WeatherInitalizer implements ApplicationRunner{
    //need to use weather service in here
    private final WeatherRepository weatherRepository;
    public WeatherInitalizer(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;

    }
@Override
    public void run(ApplicationArguments args) throws Exception {

        //implement fetch weather data from service
       //and save it to repository
    }

}
