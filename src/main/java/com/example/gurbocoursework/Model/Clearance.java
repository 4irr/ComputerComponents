package com.example.gurbocoursework.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(
        strategy = InheritanceType.JOINED
)
public abstract class Clearance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long operationId;
    @ManyToOne
    private User user;
    private LocalDate date;
    private String type;
    public abstract Accessories getAccessories();
    public abstract void setAccessories(Accessories accessories);
    public abstract String getSender();
    public abstract void setSender(String sender);
}
