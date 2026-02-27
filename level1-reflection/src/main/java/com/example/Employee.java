package com.example;

// Employee extends Person : inherits name & age fields & methods
public class Employee extends Person {
    // Private field only for Employee
    private String department;

    // Default constructor : calls super() to initialize Person fields
    public Employee() {
        super(); // calls Person() constructor
        this.department = "None"; // default department 
    }

    // constructor with parms : initialize inherited fields via super()
    public Employee(String name, int age, String department) {
        super(name, age); // call Person(name, age)
        this.department = department;
    }

    // department's getter & setters
    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}
}
