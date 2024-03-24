package com.fujitsu.trialtask.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Wpef")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherPhenomenonExtraFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public double fee;
    public String containing;


    public WeatherPhenomenonExtraFee(String containing, double fee) {
        this.containing = containing;
        this.fee = fee;
    }
}