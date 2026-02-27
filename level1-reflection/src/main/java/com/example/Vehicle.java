package com.example;

public interface Vehicle {
    void drive();
}

public class Car implements Vehicle {
    private String model;

    public Car(String model) {this.model = model;}

    @Override
    public void drive(){
        System.out.println(model + "is driving");
    }
}
