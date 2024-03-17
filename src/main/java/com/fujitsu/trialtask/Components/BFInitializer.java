package com.fujitsu.trialtask.Components;
import com.fujitsu.trialtask.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import com.fujitsu.trialtask.repository.RbfRepository;
import com.fujitsu.trialtask.repository.WpefRepository;
import java.util.Arrays;
import java.util.List;
@Component
public class BFInitializer {
    private final RbfRepository rbfRepository;
    @Autowired
    public BFInitializer(RbfRepository rbfRepository, WpefRepository wpefRepository) {
        this.rbfRepository = rbfRepository;
    }

    //adding base values to db

     //override later
    public void run(ApplicationArguments args) throws Exception {
        List<Rbf> rbfList = Arrays.asList(
                new Rbf("Tallinn", "Car", 4.0),
                new Rbf("Tallinn", "Scooter", 3.5),
                new Rbf("Tallinn", "Bike", 3.0),
                new Rbf("Tartu", "Car", 3.5),
                new Rbf("Tartu", "Scooter", 3.0),
                new Rbf("Tartu", "Bike", 2.5),
                new Rbf("Pärnu", "Car", 3.0),
                new Rbf("Pärnu", "Scooter", 2.5),
                new Rbf("Pärnu", "Bike", 2.0)
        );
        rbfRepository.saveAll(rbfList);
    }

}
