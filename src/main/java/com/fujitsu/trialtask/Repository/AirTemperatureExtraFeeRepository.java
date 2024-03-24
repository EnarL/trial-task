package com.fujitsu.trialtask.Repository;
import com.fujitsu.trialtask.Entity.AirTemperatureExtraFee;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface AirTemperatureExtraFeeRepository extends JpaRepository<AirTemperatureExtraFee, Long> {
    AirTemperatureExtraFee findByBorders(String borders);
}
