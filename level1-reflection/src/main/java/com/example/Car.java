package com.example;

// car implements Vehicle
public class Car implements Vehicle {
    private String model;

    // constructor 
    public Car(String model) {this.model = model;}

    // implemnent drive() from Vehicle interface
    @Override
    public void drive(){
        System.out.println(model + "is driving");
    }
}