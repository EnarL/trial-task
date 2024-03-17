package com.fujitsu.trialtask.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fujitsu.trialtask.Entities.Atef;
@Repository
public interface AtefRepository extends JpaRepository<Atef, Long> {
    Atef findByBorders(String borders);
    }

