package com.fujitsu.trialtask.Entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "RBF")
@Builder
public class Rbf {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String city;
    public String vehicle;
    public double fee;

    public Rbf(String city, String vehicle, double fee) {
        this.city = city;
        this.vehicle = vehicle;
        this.fee = fee;
    }



}
