package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
Step 1c : @PostConstruct annotation 
 - Marks a method to be invoked after the beean is fully constructed & all @Inject fields have been resolved
 - Lifecycle order 
    - Constructor called (instantiation)
    - @Inject fields set (injection)
    - @PostConstruct method invoked (initialization)
 - Mirrors javax/jakarta @PostConstruct that Spring honours
    - Spring invokes it via reflection : method.invoke(instance)
What we implement is : DependencyInjector.invokePostConstruct()
*/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostConstruct {
    
}
