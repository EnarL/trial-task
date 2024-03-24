package com.fujitsu.trialtask.Repository;
import com.fujitsu.trialtask.Entity.WeatherPhenomenonExtraFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherPhenomenonExtraFeeRepository extends JpaRepository<WeatherPhenomenonExtraFee, Long> {
    WeatherPhenomenonExtraFee findByContaining(String string);
}

