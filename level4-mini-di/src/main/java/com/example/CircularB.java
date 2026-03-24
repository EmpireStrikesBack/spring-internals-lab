package com.example;

/*
Step 8 : Circular dependency demo : CircularB injects CircularA

CircularB --> CircularA --> CircularB (cycle)
*/

@Component 
public class CircularB {
    @Inject
    private CircularA a; // need CircularA - closes the cycle 

    public String pong() {return "B->" + a.ping();}
}
