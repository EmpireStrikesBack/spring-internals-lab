package com.example;

/*
Step 1 : simple class whose bycote we'll inspect 
*/

public class HelloWorld implements Greeter{
    @Override
    public void sayHello() {
        System.out.println("Hello, there to inspect your bytecode");
    }
    
    @Override
    public String greet(String name){
        return "Hello" + name;
    }
}
