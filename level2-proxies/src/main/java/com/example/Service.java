package com.example;

/*
Step 1 : define a service interface 
- Dynamic proxies operate on interfaces 
- The proxy object will implement this interface & intercept method calls

Step 5 : add a method that takes params 
- Allows the proxy to intercepts args passed by the caller 
*/

public interface Service {
    // Method that'll be intercepted by the proxy 
    void execute();

    // new method with params 
    String process(String name);
}
