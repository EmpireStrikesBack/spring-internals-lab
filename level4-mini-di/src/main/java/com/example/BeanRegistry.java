package com.example;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/*
Step 2 : BeanRegistry
 - It's the heart of any DI container : Maps Class<?> keys to singleton Object instances
  - Spring equivalent : DefaultSingletonBeanRgistry 
    - Maintains a singletonObjects ConcurrentHasMap internally 
 - Key design decisions : 
    - LinkedsHashMap : preserves insertion order (deterministic printSummary)
    - getBean() walks the registry for assignability 
        - registry.getBean(Runnable.class) will find a Thread instance 
        - Thread implements Runnable 
 - This is how Spring resolves beans by interface type
*/
public class BeanRegistry {
    // Class<?> singleton instance 
    private final Map<Class<?>, Object> beans = new LinkedHashMap<>();

    // Write
    public void register(Class<?> type, Object instance) {
        beans.put(type, instance);
    }

    /*
    Read 
    Exact match first then assignability scan 
    Assignability lets callers request an interface type& receive the concrete implementation
    It's the same contract that Spring's ApplicationContext.getBean(Class<T>) provides
    */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        // exact match 
        Object exact = beans.get(type);
        if (exact != null) {
            return (T) exact;
        }

        // assignability scan (interface --> implementation lookup)
        for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
            if (type.isAssignableFrom(entry.getKey())) {
                return (T) entry.getValue();
            }
        }
        return null;
    }

    public boolean contains(Class<?> type) {
        return beans.containsKey(type);
    }

    public Map<Class<?>, Object> getAllbeans() {
        return Collections.unmodifiableMap(beans);
    }

}
