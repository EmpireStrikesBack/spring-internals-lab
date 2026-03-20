package com.example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*
Step 4 : DependencyInjector 
 - Wires @Inject fields & invokeds @PostConstruct callbacks 
  - Reflection operations used : 
    - getDeclaredFields() : field inspection
    - fields.setAccessible(true) : bypass encapsulation
    - fields.set(instance, value) : private field mutation
    - getDeclaredMethods() : method inspection
    - method.invoke(instance) : private method invocation
 - Spring equivalent : AutowiredAnnotationBeanPostProcessor
    - Implements BeanPostProcessor & runs fater every bean is created
Our injector is called explicitly (same logic, no post-processor chain)
*/

public class DependencyInjector {
  private final BeanRegistry registry;
  
  public DependencyInjector(BeanRegistry registry) {
    this.registry = registry;
  }

  /*
  Main entry point 
  Walks the full class hierarchy so, @Inject fields declared in a supreclass are also resolved
  */
 public void inject(Object instance) throws Exception {
    Class<?> cursor = instance.getClass();
    while (cursor != null && cursor != Object.class) {
        injectFields(instance, cursor);
        cursor = cursor.getSuperclass();
    }
    // lifecycle: run @PostConstruct after all fields are wired 
    invokePostConstruct(instance);
 }

 private void injectFields(Object instance, Class<?> clazz) throws Exception {
    // getDeclaredFielsd : return ALl fields declared in clazz itself (including private ones)
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
        if (!field.isAnnotationPresent(Inject.class)) {
            continue;
        }

        // setAccessible(true) : bypass Java acess controle 
        field.setAccessible(true);

        Object dependency = registry.getBean(field.getType());

        if (dependency == null) {
            throw new RuntimeException(
                "[Injector] Unsatisfied dependency: "
                + clazz.getSimpleName() + "." + field.getName()
                + "requires bean of type " + field.getType().getSimpleName()
                + "but none is registered."
            );
        }

        // field .Set() : mutates the private fields without a setter 
        field.set(instance, dependency);

        System.out.println(
            "[Injector] Injected"
            + dependency.getClass().getSimpleName()
            + " -> " + clazz.getSimpleName() + "." + field.getName()
        );
    }
 }

 // lyfecycle 
 private void invokePostConstruct(Object instance) throws Exception {
    Class <?> clazz = instance.getClass();

    // getDeclaredMethod() : returns all methods including private ones 
    for (Method method : clazz.getDeclaredMethods()) {
        if (!method.isAnnotationPresent(PostConstruct.class)) {
            continue;
        }
        if (method.getParameterCount() > 0) {
            throw new RuntimeException(
                "[Injector] @PostConstruct method must have no param"
                + clazz.getSimpleName() + "." + method.getName() + "()"
            );
        }
        method.setAccessible(true);
        System.out.println(
            "[Lifecycle] @PostConstruct -> "
            + clazz.getSimpleName() + "." + method.getName() + "()"
        );
        method.invoke(instance);
    }
 }
}
