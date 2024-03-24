package com.fujitsu.trialtask.Repository;

import com.fujitsu.trialtask.Entity.WindSpeedExtraFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WindSpeedExtraFeeRepository extends JpaRepository<WindSpeedExtraFee, Long> {
    WindSpeedExtraFee findByBorders(String borders);
}