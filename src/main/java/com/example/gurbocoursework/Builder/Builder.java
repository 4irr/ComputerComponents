package com.example.gurbocoursework.Builder;

import com.example.gurbocoursework.Model.Accessories;
import com.example.gurbocoursework.Model.Clearance;
import com.example.gurbocoursework.Model.User;

import java.time.LocalDate;

public interface Builder {
    public void Reset(Clearance operation);
    public void setUser(User user);
    public void setDate(LocalDate date);
    public void setType(String type);
    public void setAccessories(Accessories accessories);
    public void setSender(String sender);
    public Clearance getResult();
}
