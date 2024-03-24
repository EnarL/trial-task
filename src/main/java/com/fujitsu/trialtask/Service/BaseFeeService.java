package com.fujitsu.trialtask.Service;

import com.fujitsu.trialtask.Entity.RegionalBaseFee;
import com.fujitsu.trialtask.Repository.RegionalBaseFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaseFeeService {
    private final RegionalBaseFeeRepository rbfRepository;

    @Autowired
    public BaseFeeService(RegionalBaseFeeRepository rbfRepository) {
        this.rbfRepository = rbfRepository;
    }

    /**
     * Get the base fee for the given city and vehicle type
     *
     * @param city        - city name
     * @param vehicleType - vehicle type
     * @return base regional fee value
     */
    double getBaseRegionalFee(String city, String vehicleType) {
        // Create a list for fees in one city and search for the vehicle type
        List<RegionalBaseFee> rbfList = rbfRepository.findByCity(city);
        Optional<RegionalBaseFee> rbfOptional = Optional.empty();
        for (RegionalBaseFee rbf : rbfList) {
            if (rbf.getVehicle().equals(vehicleType)) {
                rbfOptional = Optional.of(rbf);
                break;
            }
        }
        return rbfOptional.map(RegionalBaseFee::getFee).orElse(-1.0);
    }

    /**
     * Change the base fee for a specific city and vehicle
     *
     * @param CityName    - city name
     * @param VehicleName - vehicle type
     * @param fee             - new fee
     */
    public void changeBaseFeeRules(String CityName, String VehicleName, String fee) {
        // Find the city and vehicle in the database
        Optional<RegionalBaseFee> rbfOptional = Optional.ofNullable(rbfRepository.findByCityAndVehicle(CityName, VehicleName));
        if (rbfOptional.isPresent()) {
            RegionalBaseFee rbf = rbfOptional.get();
            rbf.setFee(Double.parseDouble(fee));
            rbfRepository.save(rbf);
        }
        else {
            throw new IllegalArgumentException("City or vehicle not found");
        }
    }
}
