package com.example;

/*
Step 1 : define a service interface 
- Dynamic proxies operate on interfaces 
- The proxy object will implement this interface & intercept method calls
*/

public interface Service {
    // Method that'll be intercepted by the proxy 
    void execute();    
}
