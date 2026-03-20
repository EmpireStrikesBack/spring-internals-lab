package com.example;

import java.util.ArrayList;
import java.util.List;

/*
Step 3 : ComponentScanner 
 - Scans a list of provided Class objects for the @Component annotation
  - What Spring does : uses ClassPahtScanningCandidateComponentProvider
    - Iterates classpath URLs via ClassLoader.getResources()
    - Opens each .class file as a byte stream
    - Passes it to ASM"s classReader to read constant pool
    - Checks for @Component in the annotaiton table 
    - Never loads the class into the JVM until confirmed as a candidate 
- Springs reads annotations via ASM (not via Class.forName())
    - Avoids accidentally initialising classes with side-effectful static blocks 
 - Our approach : accept explicit Class<?> varargs 
    - Same detection logic (isAnnotationPresent) but simplified discovery 
*/

public class ComponentScanner {
    public List<Class<?>> scan(Class<?>... candidates) {
        List<Class<?>> components = new ArrayList<>();

        for (Class<?> clazz : candidates) {
            if (clazz.isAnnotationPresent(Component.class)) {
                Component annotation = clazz.getAnnotation(Component.class);
                String beanName = annotation.value().isEmpty()
                    ? clazz.getSimpleName()
                    : annotation.value();
                System.out.println(
                    "[Scanner] @Component found: "
                    + clazz.getSimpleName() + " -> bean name :  \"" 
                    + beanName + "\""
                );
                components.add(clazz);
            } else {
                System.out.println(
                    "[Scanner] Skipped (no @Component) : "
                    + clazz.getSimpleName()
                );
            }
        }
        System.out.println(
            "[Scanner] Total components found: " 
            + components.size()
        );
        return components; 
    }
}
