package com.fujitsu.trialtask.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "WPEF")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wpef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String container;
    public double fee;
    public Wpef(String container, double fee) {
        this.container = container;
        this.fee = fee;
    }
}
