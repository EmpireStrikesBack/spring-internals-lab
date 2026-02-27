package com.example;

// Utility class for math operations
public class MathUtils {
    public static final double PI = 3.14159;

    public static int square(int x) {return x * x;}
    public static int cube(int x) {return x * x * x;}

    // private static method, nuver used locally
    private static void hidden(){
        System.out.println("Hidden static method");
    }
}
