package com.example;

import java.util.List;
import java.util.ArrayList;

// Classroom : collection of Person objects (students)
public class Classroom {
    // private field for Classroom name
    private String name;
    // Students' list
    private List<Person> students;

    // constructor : initialize name & empty student list
    public Classroom(String name) {
        this.name = name; 
        this.students = new ArrayList<>();
    }

    // add a student to the Classroom
    public void addStudent(Person p) {students.add(p);}
    // return the students' list 
    public List<Person> getStudents() {return students;}

    // name getter for Classroom
    public String getName() {return name;}
}
