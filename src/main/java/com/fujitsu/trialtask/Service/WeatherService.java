package com.fujitsu.trialtask.Service;

import com.fujitsu.trialtask.Entity.*;
import com.fujitsu.trialtask.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;

    private final RestTemplate restTemplate;

    private final BaseFeeService baseFeeService;
    private final ExtraFeeService extraFeeService;
    private static final String URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    private static final String BIKE = "Bike";
    private static final String SCOOTER = "Scooter";
    private static final String TALLINN_HARKU = "Tallinn-Harku";
    private static final String PÄRNU = "Pärnu";
    private static final String TARTU_TÕRAVERE = "Tartu-Tõravere";
    @Autowired
    public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate, BaseFeeService baseFeeService, ExtraFeeService extraFeeService) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
        this.baseFeeService = baseFeeService;
        this.extraFeeService = extraFeeService;
    }
    // Calculate the RBF value for a specific city, vehicle type and datetime
    // The RBF value is calculated based on the base fee and extra fees
    public double calculateRbf(@RequestParam(required = false) String city, String vehicleType, String datetime) {
        validateInputs(city, vehicleType);

        List<Weather> weatherList = getWeatherData();
        String station = String.valueOf(getStation(city));
        Weather latestWeather = getLatestWeather(weatherList, station, datetime);

        double rbf = baseFeeService.getBaseRegionalFee(city, vehicleType);
        validateRbf(rbf);

        if (isBikeOrScooter(vehicleType)) {
            rbf = extraFeeService.getExtraFeesForBikeOrScooter(latestWeather.getAir(), rbf, latestWeather.getWeather());
        }

        if (isBike(vehicleType)) {
            rbf = extraFeeService.getExtraFeesForBike(latestWeather.getWind(), rbf);
        }
        return rbf;
    }

    private void validateInputs(String city, String vehicleType) {
        if (city == null || vehicleType == null) {
            throw new IllegalArgumentException("City and vehicle type cannot be null");
        }
    }

    private List<Weather> getWeatherData() {
        List<Weather> weatherList = weatherRepository.findAll();
        if (weatherList.isEmpty()) {
            fetchWeatherData();
            weatherList = weatherRepository.findAll();
        }
        return weatherList;
    }

    private void validateRbf(double rbf) {
        if (rbf == -1) {
            throw new IllegalArgumentException("Invalid vehicle type");
        }
    }

    private boolean isBikeOrScooter(String vehicleType) {
        return BIKE.equals(vehicleType) || SCOOTER.equals(vehicleType);
    }

    private boolean isBike(String vehicleType) {
        return BIKE.equals(vehicleType);
    }
    // Get the latest weather data for the given station and time
    private Weather getLatestWeather(List<Weather> weatherList, String station, String datetime) {
        Optional<Weather> latestWeather;
        if (datetime == null) {
            latestWeather = getLatestWeather(weatherList, station);
        } else {
            Timestamp queryTimestamp = Timestamp.valueOf(datetime);
            latestWeather = getWeatherAtTime(weatherList, station, queryTimestamp);
        }
        return latestWeather.orElseThrow(() -> new IllegalArgumentException("Weather data not found"));
    }
    private Optional<Weather> getWeatherAtTime(List<Weather> weatherList, String station, Timestamp queryTimestamp) {
        return weatherList.stream()
                .filter(weather -> weather.getStation().equals(station) &&
                        weather.getObservation().compareTo(queryTimestamp) <= 0)
                .max(Comparator.comparing(Weather::getObservation));
    }

    // Get the latest weather data for the given station
    // If the station is not found, return an empty Optional
    private Optional<Weather> getLatestWeather(List<Weather> weatherList, String station) {
        return weatherList.stream()
                .filter(weather -> station.equals(weather.getStation()))
                .max(Comparator.comparing(Weather::getObservation));
    }

    /**
     * Get the station name for the given city
     *
     * @param city - city name
     * @return station name
     */
    private static Optional<String> getStation(String city) {

        switch (city){
            case "Tallinn" -> {
                return Optional.of(TALLINN_HARKU);
            }
            case "Tartu" -> {
                return Optional.of(TARTU_TÕRAVERE);
            }
            case "Pärnu" -> {
                return Optional.of(PÄRNU);
            }
            default -> {
                return Optional.empty();
            }
        }
    }



    /**
     * Fetch weather data from the Estonian Weather Service and save it to db
     * It is scheduled to run every hour, 15 minutes past the hour
     * The cron expression is defined in the application.properties file
     */
    @Scheduled(cron = "${scheduling.weather.cron}")
    public void fetchWeatherData() {
        String weatherDataXml = restTemplate.getForObject(URL, String.class);
        try {
            //Parsing the XML data

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document weatherDataDoc = docBuilder.parse(new InputSource(new StringReader(weatherDataXml)));
            Element rootElement = weatherDataDoc.getDocumentElement();

            // Get the weather stations
            NodeList weatherStationNodes = rootElement.getElementsByTagName("station");
            for (int i = 0; i < weatherStationNodes.getLength(); i++) {
                Element weatherStationElement = (Element) weatherStationNodes.item(i);
                //Checking if the station is one of the required stations
                String stationName = weatherStationElement.getElementsByTagName("name").item(0).getTextContent();
                if (stationName.equals("Tallinn-Harku") || stationName.equals("Pärnu") || stationName.equals("Tartu-Tõravere")) {
                    //Extracting the required data
                    String wmoCode = weatherStationElement.getElementsByTagName("wmocode").item(0).getTextContent();
                    String airTemperature = weatherStationElement.getElementsByTagName("airtemperature").item(0).getTextContent();
                    String weatherPhenomenon = weatherStationElement.getElementsByTagName("phenomenon").item(0).getTextContent();
                    String windSpeed = weatherStationElement.getElementsByTagName("windspeed").item(0).getTextContent();
                    Timestamp currentTimestamp = Timestamp.from(Instant.now());
                    Weather weather = new Weather(stationName, wmoCode, Double.parseDouble(airTemperature), Double.parseDouble(windSpeed), weatherPhenomenon, currentTimestamp);
                    weatherRepository.save(weather);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}