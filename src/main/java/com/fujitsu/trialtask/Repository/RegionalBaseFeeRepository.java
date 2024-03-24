package com.fujitsu.trialtask.Repository;
import com.fujitsu.trialtask.Entity.RegionalBaseFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Long> {
    List<RegionalBaseFee> findByCity(String city);
    RegionalBaseFee findByCityAndVehicle(String city, String vehicle);
}
