package com.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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


            // step 3 : inspect all declared methods 

            System.out.println("\n== METHODS ==");

            // getDeclaredMethods() returns all declared methods in a class (including private ones)
            Method[] methods = personClass.getDeclaredMethods();

            for (Method method : methods) {
                System.out.print(
                    "Name:" + method.getName() +
                    ", Return Type:" + method.getReturnType().getSimpleName() +
                    ", Modifiers: " + Modifier.toString(method.getModifiers())
                );

                // print params if any
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length > 0) {
                    System.out.print(", Params : ");
                    for (Class<?> param : parameterTypes) {
                        System.out.print(param.getSimpleName() + " ");
                    }
                }
                System.out.println();
            }

            // step 4 : dynamic instanciation 

            Constructor<?> defaultConstructor = personClass.getConstructor(); //public no-arg constructor 
            Object personInstance = defaultConstructor.newInstance(); // dynamicaaly create instance 
            
            // use getters to print default values 
            System.out.println("\n== DYNAMIC INSTANCE==");
            System.out.println("Default name: " + ((Person) personInstance).getName());
            System.out.println("Default age: " + ((Person) personInstance).getAge());
        } catch (Exception e) {
            // Reflection API throw checked exceptions (ClassNotFound etc...)
            e.printStackTrace();
        }
    }
}
