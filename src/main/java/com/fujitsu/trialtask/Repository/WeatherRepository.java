package com.fujitsu.trialtask.Repository;
import com.fujitsu.trialtask.Entity.Weather;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @NonNull
    Weather save(@NonNull Weather weather);
    List<Weather> findAll();
    Optional<Weather> findById(Long id);

}
