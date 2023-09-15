package com.example.gurbocoursework.Builder;

import com.example.gurbocoursework.Model.Accessories;
import com.example.gurbocoursework.Model.Clearance;
import com.example.gurbocoursework.Model.User;

import java.time.LocalDate;

public class OperationsBuilder implements Builder {
    private Clearance operation;
    @Override
    public void Reset(Clearance operation) {
        this.operation = operation;
    }

    @Override
    public void setUser(User user) {
        this.operation.setUser(user);
    }

    @Override
    public void setDate(LocalDate date) {
        this.operation.setDate(date);
    }

    @Override
    public void setType(String type) {
        this.operation.setType(type);
    }

    @Override
    public void setAccessories(Accessories accessories) {
        this.operation.setAccessories(accessories);
    }

    @Override
    public void setSender(String sender) {
        this.operation.setSender(sender);
    }

    @Override
    public Clearance getResult() {
        return this.operation;
    }
}
