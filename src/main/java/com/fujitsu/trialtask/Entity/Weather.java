package com.fujitsu.trialtask.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "Weather")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String station;
    public Double air;
    public Double wind;
    public String weather;
    public String WMO;
    public Timestamp observation;

    public Weather(String station, String WMO, Double air, Double wind, String weather, Timestamp observation) {
        this.station = station;
        this.WMO = WMO;
        this.air = air;
        this.wind = wind;
        this.weather = weather;
        this.observation = observation;
    }
}