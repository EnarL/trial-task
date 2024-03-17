package com.fujitsu.trialtask.repository;
import com.fujitsu.trialtask.Entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;


@Repository
    public interface WeatherRepository extends JpaRepository<Weather, Long> {
        List<Weather> findAll();
        Optional<Weather> findById(String city);
        Weather save(Weather weather);
    }

