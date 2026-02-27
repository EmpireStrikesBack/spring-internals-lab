package com.example;

public class Person {

    // private fiedls only accessible within this class
    private String name;
    private int age;

    // default constructor : set default values 
    public Person(){
        this.name = "Unknown";
        this.age = 0;
    }

    // overloaded constructor : allows setting name & age
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // getter & setter for name
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    // getter & setter for age
    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}

    // private method only accessible within this class 
    private void secretMethod() {
        System.out.println("this is a secret");
    }
}