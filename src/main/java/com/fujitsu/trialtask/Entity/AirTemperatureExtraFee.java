package com.fujitsu.trialtask.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Atef")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirTemperatureExtraFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String borders;
    public double fee;

    public AirTemperatureExtraFee(String borders, double fee) {
        this.borders = borders;
        this.fee = fee;
    }
}