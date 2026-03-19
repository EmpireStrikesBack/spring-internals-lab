package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
Step 1b : @Inject annotation
 - @Target(FIELD) : applies to instance fields only.
    - The injecetor scans getDeclaredFields() & looks for this annotation 
    - Equivalent to Spring's @Autowire in fields 
 - Security : field injection requires setAccessible(true)
    - Same mechanism used in Lvl1 step 6 to bypass encapsulation
    - This is why injection frameworks need ReflectPermission in security managers
*/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
    
}
