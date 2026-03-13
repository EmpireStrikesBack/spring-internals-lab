package com.example;

import java.lang.reflect.Proxy;

/*
Step 4 : create & test the dynamic proxy
- Proxy.newProxyInstance() : dynamically generates a class that implements the Service interface
*/

public class ProxyDemo {
    public static void main(String[] args) {
        // create the real service object 
        Service realService = new ServiceImpl();

        /*
        create proxy instance 

        params:
        - ClassLoader
        - Interfaces implemented by proxy
        - Invocationhandler that intercepts calls 
        */
       Service proxyService = (Service) Proxy.newProxyInstance
       (
        Service.class.getClassLoader(),
        new Class[]{Service.class},
        new LoggingHandler(realService)
       );

       //call method through proxy
       proxyService.execute();

       // new method with params 
       System.out.println("\n== ARG MODIFICATION ==");

       String result = proxyService.process("Alice");

       System.out.println("Returned value: " + result);
    }
}
