package com.fujitsu.trialtask.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Wsef")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WindSpeedExtraFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String borders;
    public double fee;

    public WindSpeedExtraFee(String borders, double fee) {
        this.borders = borders;
        this.fee = fee;
    }
}