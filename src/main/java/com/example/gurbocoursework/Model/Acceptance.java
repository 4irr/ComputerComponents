package com.example.gurbocoursework.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Acceptance extends Clearance {
    @OneToOne
    private Accessories accessories;
    private String sender;
    @Override
    public Accessories getAccessories() { return this.accessories; }
    @Override
    public void setAccessories(Accessories accessories) { this.accessories = accessories; }
    @Override
    public String getSender() { return this.sender; }
    @Override
    public void setSender(String sender) { this.sender = sender; }
}
