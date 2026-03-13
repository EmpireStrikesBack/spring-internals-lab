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


            // step 5 : Invoke private method

            Method secretMethod = personClass.getDeclaredMethod("secretMethod");
            secretMethod.setAccessible(true); // allow access to private method 
            System.out.println("\n== INVOKING PRIVATE METHOD ==");
            secretMethod.invoke(personInstance); // prints its a secret 


            // step 6 : modify private fields 

            System.out.println("\n== MODIFYING PRIVATE FIELDS==");

            // get the 'name' field
            Field nameField = personClass.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(personInstance, "Alice"); // change value 

            // get the age field 
            Field ageField = personClass.getDeclaredField("age");
            ageField.setAccessible(true);
            ageField.set(personInstance, 30); // change value 

            // print modified values 
            System.out.println("Modified Name: " + ((Person) personInstance).getName());
            System.out.println("Modified Age: " + ((Person) personInstance).getAge());

            
            // step 7: Instantiate Using Parameterized Constructor

            System.out.println("\n== PARAMETERIZED CONSTRUCTOR ==");

            // Retrieve constructor with parameters (String, int)
            Constructor<?> paramConstructor =
                    personClass.getConstructor(String.class, int.class);

            // Create a new Person instance using those parameters
            Object personWithParams = paramConstructor.newInstance("Bob", 45);

            // Cast so we can call getters
            Person p2 = (Person) personWithParams;

            // Verify values
            System.out.println("Name: " + p2.getName());
            System.out.println("Age: " + p2.getAge());

        } catch (Exception e) {
            // Reflection API throw checked exceptions (ClassNotFound etc...)
            e.printStackTrace();
        }
    }
}
