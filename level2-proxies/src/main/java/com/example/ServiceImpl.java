package com.example;

/*
Step 2 : real implementation of the service interface
- Represents the actual business logic 
- The proxy will delegate calls to this object
*/

public class ServiceImpl implements Service {
    @Override
    public void execute() {
        System.out.println("Service is executing...");
    }
}
