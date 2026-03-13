package com.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/*
Step 3 : InvocationHandler intercepts all method calls
- When a proxy method is called, Java routes the cal through this invoke() method
*/

public class LoggingHandler implements InvocationHandler {
    // real object behind the proxy
    private Object target;

    public LoggingHandler(Object target) {
        this.target = target;
    }

    /*
    invoke() is executed whenever a proxied method is called 
    - proxy : proxy instance
    - method : method invoked
    - args : arguments passed to the method 
    */
   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    //logic executed BEFORE the real method
    System.out.println("Before method: " + method.getName());

    // call the actual method in the real object
    Object result = method.invoke(target, args);

    // logic exexcuted AFTER the real method 
    System.out.println("After method: " + method.getName());

    return result;
   }
}
