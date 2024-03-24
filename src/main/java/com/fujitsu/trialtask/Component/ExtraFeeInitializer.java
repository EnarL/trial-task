package com.fujitsu.trialtask.Component;
import com.fujitsu.trialtask.Entity.*;
import com.fujitsu.trialtask.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ExtraFeeInitializer implements ApplicationRunner {
    private final AirTemperatureExtraFeeRepository atefRepository;
    private final WindSpeedExtraFeeRepository wsefRepository;
    private final WeatherPhenomenonExtraFeeRepository wpefRepository;

    @Autowired
    public ExtraFeeInitializer(AirTemperatureExtraFeeRepository atefRepository, WindSpeedExtraFeeRepository wsefRepository, WeatherPhenomenonExtraFeeRepository wpefRepository) {
        this.atefRepository = atefRepository;
        this.wsefRepository = wsefRepository;
        this.wpefRepository = wpefRepository;
    }


    // Adding initial extra fees to the database
    // @param args incoming application arguments
    // @throws Exception if an error occurred
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<AirTemperatureExtraFee> atefList = Arrays.asList(
                new AirTemperatureExtraFee("-10-0", 0.5),
                new AirTemperatureExtraFee("<-10", 1)
        );
        atefRepository.saveAll(atefList);

        List<WeatherPhenomenonExtraFee> wpefList = Arrays.asList(
                new WeatherPhenomenonExtraFee("sleet", 1),
                new WeatherPhenomenonExtraFee("snow", 1),
                new WeatherPhenomenonExtraFee("rain", 0.5),
                new WeatherPhenomenonExtraFee("hail", -1),
                new WeatherPhenomenonExtraFee("glaze", -1),
                new WeatherPhenomenonExtraFee("thunder", -1));
        wpefRepository.saveAll(wpefList);

        List<WindSpeedExtraFee> wsefList = Arrays.asList(
                new WindSpeedExtraFee(">20", -1),
                new WindSpeedExtraFee("10-20", 0.5)

        );
        wsefRepository.saveAll(wsefList);


    }
    }