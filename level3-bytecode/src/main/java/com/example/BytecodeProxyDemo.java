package com.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BytecodeProxyDemo {

    public static void main(String[] args) throws Exception {

        // Get transformed bytecode from ASM
        byte[] modifiedBytes = BytecodeTransformer.getTransformedBytes();

        // Load class using custom classloader
        Class<?> clazz = new CustomClassLoader()
                .defineClass("com.example.HelloWorld", modifiedBytes);

        // Create real instance
        Object target = clazz.getDeclaredConstructor().newInstance();

        // Create proxy (JDK dynamic proxy)
        Greeter proxy = (Greeter) Proxy.newProxyInstance(
                Greeter.class.getClassLoader(),
                new Class[]{Greeter.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        System.out.println(">>> Proxy intercepted: " + method.getName());

                        // Call real method (on transformed class)
                        Object result = method.invoke(target, args);

                        // Post-processing
                        if (method.getName().equals("greet")) {
                            System.out.println("greet returned: " + result);
                        }

                        return result;
                    }
                }
        );

        // Execute methods through proxy
        proxy.sayHello();

        String res = proxy.greet("John");

        System.out.println("Final result: " + res);
    }
}