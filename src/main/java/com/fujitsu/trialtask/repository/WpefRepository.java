package com.fujitsu.trialtask.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fujitsu.trialtask.Entities.Wpef;
import java.util.List;


@Repository
public interface WpefRepository extends JpaRepository<Wpef, Long> {
    List<Wpef> findByContainer(String container);
    }

