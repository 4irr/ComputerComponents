package com.example.gurbocoursework.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Placement extends Clearance {
    @OneToOne
    private Accessories accessories;
    @Override
    public Accessories getAccessories() { return accessories; }
    @Override
    public void setAccessories(Accessories accessories) { this.accessories = accessories; }
    @Override
    public String getSender() {
        return null;
    }
    @Override
    public void setSender(String sender) {

    }
}
