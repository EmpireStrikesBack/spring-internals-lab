package com.example;

public class ReflectionDemo {
    public static void main(String[] args) {
        try {
            // step 1 : inspect Person class
            Class<?> personClass = Person.class;

            // class name
            System.out.println("Class name:" + personClass.getName());

            // superclass
            System.out.println("SuperClass:" + personClass.getSuperclass().getName());

            // interfaces 
            Class<?>[] interfaces = personClass.getInterfaces();
            System.out.println("Interfaces:" + (interfaces.length == 0 ? "None" : interfaces[0].getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
