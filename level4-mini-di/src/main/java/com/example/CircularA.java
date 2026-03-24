package com.example;

/*
Step 8 :  Circular dependency demo : Circular 1 injects circularB

CircularA --> CircularB --> CircularA (cycle)

Always fails with constructor injection 
 - JVM can't instantiate either class without the other existing first

With field injection : Spring detects it via the currently Creating set
- Throws a RuntimeException with a clear message 

*/

@Component 
public class CircularA {
    @Inject 
    private CircularB b; // requires CircularB

    public String ping() {return "A->" + b.pong();}
}
