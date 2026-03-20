package com.example;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
Step 5 : MiniApplicationContext 
 - Orchestrates the full container lifecycle in 4 phases : 
    - Scan ; ComponentScnanner finds @component classes
    - Create : Instantiate every bean (no injection yet)
    - Inject : Wire @Inject fields into every bean
    - Report : print container summary 
2 phases crate-then-inject is critical : 
 - If we injected during creation the order classes appear in the list would determine whether dependencies exist yet
 - By creating all instances first then injecting : 
    - Every bean is guaranteed to be in the registry before any wiring starts 

Spring uses the same separation: 
    - AbstractBeanFactory.createBean() : creates all instances 
    - AutowiredAnnoationBeanPostProcessor.postProcessProperties() : injection

Circular dependency detection is built at instance creation using "currently-creating" set.
If the same class appears twice in the creation stack
    - We know there is a cycle and fail fast.
*/

public class MiniApplicationContext {
    private final BeanRegistry registry = new BeanRegistry();
    private final ComponentScanner scanner = new ComponentScanner();
    private final DependencyInjector injector = new DependencyInjector(registry);

    // tracks beans mid-instantiation to detect circular constructor deps
    private final Set<Class<?>> currentlyCreating = new HashSet<>();

    // public API
    public void register(Class<?>... classes) throws Exception {
        System.out.println("=== Phase 1 : SCAN ===");
        List<Class<?>> components = scanner.scan(classes);

        System.out.println("\n=== Phase 2 : CREATE ===");
        for (Class<?> clazz : components) {
            instantiateAndRegister(clazz);
        }

        System.out.println("\n=== phase 3 : INJECT ===");
        for (Object bean : registry.getAllBeans().values()) {
            injector.inject(bean);
        }

        System.out.println(
            "\n=== Container ready : "
            + registry.getAllBeans().size() + "beans ===\n"
        );
    }

    public <T> T getBean(Class<T> type) {
        T bean = registry.getBean(type);
        if (bean == null) {
            throw new RuntimeException(
                "[Context] No bean registered for type: "
                + type.getSimpleName()
            );
        }
        return bean;
    }

    public void printSummary() {
        System.out.println("\n== CONTAINER SUMMARY==");
        for (Map.Entry<Class<?>, Object> entry : registry.getAllBeans().entrySet()) {
            System.out.println(
                " " + entry.getKey().getSimpleName()
                + " -> " + entry.getValue().getClass().getName()
                + "@" + Integer.toHexString(System.identityHashCode(entry.getValue()))
            );
        }
    }

    // private helpers 
    private void instantiateAndRegister(Class<?> clazz) throws Exception {
        if (registry.contains(clazz)) {
            return; // already created
        }

        // circular dependency guard 
        if (currentlyCreating.contains(clazz)) {
            throw new RuntimeException(
                "[Context] Circular dependency detected while creating: "
                + clazz.getSimpleName()
            );
        }
        currentlyCreating.add(clazz);

       // getDeclaredConstructor() : no-arg constructor for simplicity
        Constructor<?> ctor = clazz.getDeclaredConstructor(); 
        ctor.setAccessible(true);
        Object instance = ctor.newInstance();

        registry.register(clazz, instance);
        currentlyCreating.remove(clazz);

        System.out.println(
            "[Context] created bean: "
            + clazz.getSimpleName()
            + " @ " + Integer.toHexString(System.identityHashCode(instance))
        );
    }

    

}
