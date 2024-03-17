package com.fujitsu.trialtask.repository;
import com.fujitsu.trialtask.Entities.Rbf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;
@Repository
public interface RbfRepository extends JpaRepository<Rbf, Long> {
    List<Rbf> findByCity(String city);
    Rbf findByCityAndVehicle(String city, String vehicle);

}
