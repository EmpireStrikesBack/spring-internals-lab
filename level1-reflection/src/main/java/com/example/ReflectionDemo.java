package com.example;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionDemo {
    public static void main(String[] args) {
        try {
            // step 1 : inspect Class metadata inspection

            // obtain the Class object representing Person
            Class<?> personClass = Person.class;

            // print Class name
            System.out.println("Class name:" + personClass.getName());

            // print Superclass (Person extends Object)
            System.out.println("SuperClass:" + personClass.getSuperclass().getName());

            // print implemented interfaces  (none here)
            Class<?>[] interfaces = personClass.getInterfaces();
            System.out.println("Interfaces:" + (interfaces.length == 0 ? "None" : interfaces[0].getName()));


            // step 2 : inspect all declared fields (including private)

            System.out.println("\n== FIELDS ==");

            // getDeclaredFields() returns all declared fields  in the Class itself
            Field[] fields = personClass.getDeclaredFields();

            for (Field field : fields) {
                // fields.getMofidiers() : bitmask of modifiers (private, static, ect ...)
                System.out.println(
                    "Name:" + field.getName() +
                    ", Type:" + field.getType().getSimpleName() +
                    ", Modifiers:" + Modifier.toString(field.getModifiers())
                );
            } 
        } catch (Exception e) {
            // Reflection API throw checked exceptions (ClassNotFound etc...)
            e.printStackTrace();
        }
    }
}
