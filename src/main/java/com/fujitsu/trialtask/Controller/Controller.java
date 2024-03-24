package com.fujitsu.trialtask.Controller;
import com.fujitsu.trialtask.Service.BaseFeeService;
import com.fujitsu.trialtask.Service.WeatherService;
import com.fujitsu.trialtask.Service.ExtraFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {
    private final BaseFeeService baseFeeService;
    private final ExtraFeeService extraFeeService;
    private final WeatherService weatherService;

    @Autowired
    public Controller(WeatherService weatherService, BaseFeeService baseFeeService, ExtraFeeService extraFeeService) {
        this.weatherService = weatherService;
        this.baseFeeService = baseFeeService;
        this.extraFeeService = extraFeeService;
    }

    /**
     * Calculate RBF value for a specific city, vehicle type and datetime
     *
     * @param city        - city name
     * @param vehicleType - vehicle type
     * @param datetime    - datetime in the format "yyyy-MM-dd HH:mm:ss" (optional)
     * @return RBF value if successful, or an error message if an error occurred
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/calculateRbf")
    public ResponseEntity<String> calculateRbfByTime(
            @RequestParam("city") String city,
            @RequestParam("vehicleType") String vehicleType,
            @RequestParam(value = "datetime", required = false) String datetime
    ) {
        try {
            double price = weatherService.calculateRbf(city, vehicleType, datetime);
            return ResponseEntity.ok(String.valueOf(price));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There was an error:" + e.getMessage());
        }
    }

    /**
     * Change base fee for a specific city and vehicle type
     *
     * @param CityName    - city name
     * @param VehicleName - vehicle type
     * @param fee             - new fee
     * @return "Business rules updated" if successful, or an error message if an error occurred
     */
    @PutMapping("/changeBaseFeeRules/{CityName}/{VehicleName}/{fee}")
    public ResponseEntity<String> changeBaseFeeRules(
            @PathVariable("CityName") String CityName,
            @PathVariable("VehicleName") String VehicleName,
            @PathVariable("fee") String fee

    ) {
        try {
            baseFeeService.changeBaseFeeRules(CityName, VehicleName, fee);
            return ResponseEntity.ok("Business rules updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error: " + e.getMessage());
        }
    }

    /**
     * Change extra fee tables' values or fees.
     * Insert old value as newValue if you only want to change fee.
     * Insert old fee as newFee if you only want to change value.
     *
     * @param table    - table name (Atef, Wsef, Wpef)
     * @param oldValue - old value
     * @param newValue - new value
     * @param fee      - new fee
     * @return "Business rules updated" if successful, or an error message if an error occurred
     */
    @PutMapping("/changeExtraFeeRules/{table}/{oldValue}/{newValue}/{fee}")
    public ResponseEntity<String> changeExtraFeeRules(
            @PathVariable("table") String table,
            @PathVariable("oldValue") String oldValue,
            @PathVariable("newValue") String newValue,
            @PathVariable("fee") String fee
    ) {
        try {
            extraFeeService.changeExtraFeeRules(table, oldValue, newValue, fee);
            return ResponseEntity.ok("Business rules updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error: " + e.getMessage());
        }
    }

    /**
     * Add new extra fee rule
     *
     * @param table - table name (Atef, Wsef, Wpef)
     * @param value - value
     * @param fee   - fee
     * @return "Business rules updated" if successful, or an error message if an error occurred
     */
    @PostMapping("/addExtraFeeRules/{table}/{value}/{fee}")
    public ResponseEntity<String> addExtraFeeRules(
            @PathVariable("table") String table,
            @PathVariable("value") String value,
            @PathVariable("fee") String fee
    ) {
        try {
            extraFeeService.addExtraFeeRules(table, value, fee);
            return ResponseEntity.ok("Business rules updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error: " + e.getMessage());
        }
    }

    /**
     * Delete extra fee rule
     *
     * @param table - table name (Atef, Wsef, Wpef)
     * @param value - value
     * @return "Business rules updated" if successful, or an error message if an error occurred
     */
    @DeleteMapping("/deleteExtraFeeRules/{table}/{value}")
    public ResponseEntity<String> deleteExtraFeeRules(
            @PathVariable("table") String table,
            @PathVariable("value") String value
    ) {
        try {
            extraFeeService.deleteExtraFeeRules(table, value);
            return ResponseEntity.ok("Business rules updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error: " + e.getMessage());
        }
    }
}