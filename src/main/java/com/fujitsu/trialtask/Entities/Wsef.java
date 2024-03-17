package com.fujitsu.trialtask.Entities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "WSEF")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wsef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String borders;
    public double fee;
    public Wsef(String borders, double fee) {
        this.borders = borders;
        this.fee = fee;
    }

}
