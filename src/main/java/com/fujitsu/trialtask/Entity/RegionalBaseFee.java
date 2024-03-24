package com.fujitsu.trialtask.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "Rbf")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionalBaseFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String city;
    public String vehicle;
    public double fee;

    public RegionalBaseFee(String city, String vehicle, double fee) {
        this.city = city;
        this.vehicle = vehicle;
        this.fee = fee;
    }


}