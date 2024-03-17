package com.fujitsu.trialtask.repository;

import org.springframework.stereotype.Repository;
import com.fujitsu.trialtask.Entities.Wsef;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface WsefRepository extends JpaRepository<Wsef, Long>{
    Wsef findByBorders(String borders);
}
