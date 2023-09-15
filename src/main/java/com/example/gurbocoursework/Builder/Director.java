package com.example.gurbocoursework.Builder;

import com.example.gurbocoursework.Model.*;

public class Director {
    public void makeAcceptance(Builder builder, Clearance operation, User user){
        builder.Reset(new Acceptance());
        builder.setUser(user);
        builder.setDate(operation.getDate());
        builder.setType("Прибытие");
        builder.setAccessories(operation.getAccessories());
        builder.setSender(operation.getSender());
    }
    public void makePlacement(Builder builder, Clearance operation, User user){
        builder.Reset(new Placement());
        builder.setUser(user);
        builder.setDate(operation.getDate());
        builder.setType("Распределение");
        builder.setAccessories(operation.getAccessories());
    }
    public void makeRelease(Builder builder, Clearance operation, User user){
        builder.Reset(new Sale());
        builder.setUser(user);
        builder.setDate(operation.getDate());
        builder.setType("Продажа");
        builder.setAccessories(operation.getAccessories());
    }
}
