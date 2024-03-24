package com.fujitsu.trialtask.Service;

import com.fujitsu.trialtask.Entity.AirTemperatureExtraFee;
import com.fujitsu.trialtask.Entity.WindSpeedExtraFee;
import com.fujitsu.trialtask.Entity.WeatherPhenomenonExtraFee;
import com.fujitsu.trialtask.Repository.AirTemperatureExtraFeeRepository;
import com.fujitsu.trialtask.Repository.WeatherPhenomenonExtraFeeRepository;
import com.fujitsu.trialtask.Repository.WindSpeedExtraFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExtraFeeService {
    private final AirTemperatureExtraFeeRepository atefRepository;
    private final WindSpeedExtraFeeRepository wsefRepository;
    private final WeatherPhenomenonExtraFeeRepository wpefRepository;
    private static final String ATEF = "Atef";
    private static final String WSEF = "Wsef";
    private static final String WPEF = "Wpef";
    @Autowired
    public ExtraFeeService(AirTemperatureExtraFeeRepository atefRepository, WindSpeedExtraFeeRepository wsefRepository, WeatherPhenomenonExtraFeeRepository wpefRepository) {
        this.atefRepository = atefRepository;
        this.wsefRepository = wsefRepository;
        this.wpefRepository = wpefRepository;
    }
    double getExtraFeesForBikeOrScooter(double temperature, double rbf, String weatherPhenomenon) {
        rbf = addTemperatureFees(temperature, rbf);
        rbf = addWeatherPhenomenonFees(weatherPhenomenon, rbf);
        return rbf;
    }

    private double addTemperatureFees(double temperature, double rbf) {
        List<AirTemperatureExtraFee> atefList = atefRepository.findAll();
        for (AirTemperatureExtraFee atef : atefList) {
            rbf = calculateFeeForTemperature(temperature, rbf, atef);
        }
        return rbf;
    }
    /**
     * Calculate the fee for the given temperature
     * @param temperature - temperature
     * @param rbf - rbf value
     * @param atef - Atef object
     * @return rbf value
     */
    private double calculateFeeForTemperature(double temperature, double rbf, AirTemperatureExtraFee atef) {
        if (atef.getBorders().contains("<")) {
            if (temperature < Double.parseDouble(atef.getBorders().split("<")[1])) {
                rbf += atef.getFee();
            }
        } else if (atef.getBorders().contains(">")) {
            if (temperature > Double.parseDouble(atef.getBorders().split(">")[1])) {
                rbf += atef.getFee();
            }
        } else {
            if (atef.getBorders().indexOf("-") == 0) {
                int splitIndex = atef.getBorders().substring(atef.getBorders().indexOf("-") + 1).indexOf("-") + 1;
                double min = Double.parseDouble(atef.getBorders().substring(0, splitIndex));
                double max = Double.parseDouble(atef.getBorders().substring(splitIndex + 1));
                if (temperature >= min && temperature <= max) {
                    rbf += atef.getFee();
                }
            } else {
                String[] interval = atef.getBorders().split("-");
                if (temperature >= Double.parseDouble(interval[0]) && temperature <= Double.parseDouble(interval[1])) {
                    rbf += atef.getFee();
                }
            }
        }
        return rbf;
    }
    // Add extra fee for weather phenomenon
    private double addWeatherPhenomenonFees(String weatherPhenomenon, double rbf) {
        if (!weatherPhenomenon.isEmpty()) {
            List<WeatherPhenomenonExtraFee> wpefList = wpefRepository.findAll();
            for (WeatherPhenomenonExtraFee wpef : wpefList) {
                rbf = calculateFeeForWeatherPhenomenon(weatherPhenomenon, rbf, wpef);
            }
        }
        return rbf;
    }
    // Calculate the fee for the given weather phenomenon
    private double calculateFeeForWeatherPhenomenon(String weatherPhenomenon, double rbf, WeatherPhenomenonExtraFee wpef) {
        if (!weatherPhenomenon.isEmpty()) {
            List<WeatherPhenomenonExtraFee> wpefList = wpefRepository.findAll();
            for (WeatherPhenomenonExtraFee x : wpefList) {

                if (weatherPhenomenon.contains(wpef.getContaining())) {

                    if (wpef.getFee() == -1) {
                        throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
                    }
                    rbf += wpef.getFee();
                }
            }
        }
        return rbf;
    }

    double getExtraFeesForBike(double windSpeed, double rbf) {
        List<WindSpeedExtraFee> wsefList = wsefRepository.findAll();
        for (WindSpeedExtraFee wsef : wsefList) {
            rbf = calculateFeeForWindSpeed(windSpeed, rbf, wsef);
        }
        return rbf;
    }

    // Calculate the fee for the given wind speed

    private double calculateFeeForWindSpeed(double windSpeed, double rbf, WindSpeedExtraFee wsef) {
        List<WindSpeedExtraFee> wsefList = wsefRepository.findAll();

        for (WindSpeedExtraFee y : wsefList) {
            // Check if the wind speed is less than, greater than or in an interval
            if (wsef.getBorders().contains(">")) {
                if (windSpeed > Double.parseDouble(wsef.getBorders().split(">")[1])) {
                    rbf += wsef.getFee();
                }
            } else {

                String[] interval = wsef.getBorders().split("-");
                if (windSpeed >= Double.parseDouble(interval[0]) && windSpeed <= Double.parseDouble(interval[1])) {
                    rbf += wsef.getFee();
                }
            }
        }
        return rbf;
    }

    // Change extra fee rules
    public void changeExtraFeeRules(String table, String oldValue, String newValue, String fee) {
        // Update the value and fee for the given table
        switch (table) {
            case "Atef":
                AirTemperatureExtraFee atef = atefRepository.findByBorders(oldValue);
                if (atef != null) {
                    atef.setBorders(newValue);
                    atef.setFee(Double.parseDouble(fee));
                    atefRepository.save(atef);
                    break;
                }
                else {
                    throw new IllegalArgumentException("Value not found");
                }
            case "Wsef":
                WindSpeedExtraFee wsef = wsefRepository.findByBorders(oldValue);
                if (wsef != null) {
                    wsef.setBorders(newValue);
                    wsef.setFee(Double.parseDouble(fee));
                    wsefRepository.save(wsef);
                    break;
                }
                else {
                    throw new IllegalArgumentException("Value not found");
                }
            case "Wpef":
                WeatherPhenomenonExtraFee wpef = wpefRepository.findByContaining(oldValue);
                if (wpef != null) {
                    wpef.setContaining(newValue);
                    wpef.setFee(Double.parseDouble(fee));
                    wpefRepository.save(wpef);
                    break;
                }
                else {
                    throw new IllegalArgumentException("Value not found");
                }
            default:
                throw new IllegalArgumentException("Invalid table name");
        }
    }

    /**
     * Add new extra fee rule
     *
     * @param table - table name (Atef, Wsef, Wpef)
     * @param value - value
     * @param fee   - fee
     */
    public void addExtraFeeRules(String table, String value, String fee) {
        // Create a new object and save it to the database
        switch (table) {
            case "Atef":
                AirTemperatureExtraFee atef = new AirTemperatureExtraFee(value, Double.parseDouble(fee));
                atefRepository.save(atef);
                break;
            case "Wsef":
                WindSpeedExtraFee wsef = new WindSpeedExtraFee(value, Double.parseDouble(fee));
                wsefRepository.save(wsef);
                break;
            case "Wpef":
                WeatherPhenomenonExtraFee wpef = new WeatherPhenomenonExtraFee(value, Double.parseDouble(fee));
                wpefRepository.save(wpef);
                break;
            default:
                throw new IllegalArgumentException("Invalid table name");
        }
    }

    /**
     * Delete extra fee rule
     *
     * @param table - table name (Atef, Wsef, Wpef)
     * @param value - value
     */

    public void deleteExtraFeeRules(String table, String value) {
        // Find the object based on value and delete it from the database
        switch (table) {
            case "Atef":
                AirTemperatureExtraFee atef = atefRepository.findByBorders(value);
                if (atef != null) {
                    atefRepository.delete(atef);
                    break;
                }
                else {
                    throw new IllegalArgumentException("Value not found");
                }
            case "Wsef":
                WindSpeedExtraFee wsef = wsefRepository.findByBorders(value);
                if (wsef != null) {
                    wsefRepository.delete(wsef);
                    break;
                }
                else {
                    throw new IllegalArgumentException("Value not found");
                }
            case "Wpef":
                WeatherPhenomenonExtraFee wpef = wpefRepository.findByContaining(value);
                if (wpef != null) {
                    wpefRepository.delete(wpef);
                    break;
                }
                else {
                    throw new IllegalArgumentException("Value not found");
                }
            default:
                throw new IllegalArgumentException("Invalid table name");
        }
    }
}