package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/*
Step 1a : @Component annotation 
 - @Retention(RUNTIME) : critical --> annotation is stripped without it
    - isAnnotationPresent() would retrun false at runtime
    - Spring uses RUNTIME retention on all its meta-annotations for this reason
 - @Target(TYPE) : resticts this annotaiton to class declarations only 
    - Prevents accidental use on fields or methods 
*/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    // optionnal logical bean name : mirrors Spring's @Component("myBean")
    String value() default "";
}
