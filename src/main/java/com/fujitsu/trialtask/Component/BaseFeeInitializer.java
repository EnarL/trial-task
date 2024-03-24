package com.fujitsu.trialtask.Component;
import com.fujitsu.trialtask.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.fujitsu.trialtask.Repository.*;
import java.util.Arrays;
import java.util.List;
@Component
public class BaseFeeInitializer implements ApplicationRunner {
    private final RegionalBaseFeeRepository rbfRepository;
    @Autowired
    public BaseFeeInitializer(RegionalBaseFeeRepository rbfRepository) {
        this.rbfRepository = rbfRepository;
    }

    /**
     * Adding initial base fees to the database
     *
     * @param args incoming application arguments
     * @throws Exception if an error occurred
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<RegionalBaseFee> rbfList = Arrays.asList(
                new RegionalBaseFee("Tartu", "Car", 3.5),
                new RegionalBaseFee("Tartu", "Scooter", 3.0),
                new RegionalBaseFee("Tartu", "Bike", 2.5),
                new RegionalBaseFee("Tallinn", "Car", 4.0),
                new RegionalBaseFee("Tallinn", "Scooter", 3.5),
                new RegionalBaseFee("Tallinn", "Bike", 3.0),
                new RegionalBaseFee("Pärnu", "Car", 3.0),
                new RegionalBaseFee("Pärnu", "Scooter", 2.5),
                new RegionalBaseFee("Pärnu", "Bike", 2.0)
        );
        rbfRepository.saveAll(rbfList);
    }
}