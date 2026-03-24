package com.example;

import java.util.ArrayList;
import java.util.List;

/*
Demo bean ; UserRepository 
 - A leaf in the dependency graph : no @Inject fields 
 - Has a @PostConstruct that loads seed data once injectionis complete 
 - Dependency graph position 
    - UserRepository <-- UserService <-- AppController 
Spring ewuivalent ; @Repository bean seeded with @PostConstruct 
*/

@Component 
public class UserRepository {
    private final List<String> store = new ArrayList<>();

    @PostConstruct 
    public void init() {
        store.add("Alice");
        store.add("Bob");
        store.add("Charly");
        System.out.println(
            "[UserRepository.init] Seeded " + store.size() + "users"
        );
    }

    public List<String> findAll() {
        return store;
    }

    public String findById(int index) {
        if (index < 0 || index >= store.size()) {
            throw new IllegalArgumentException("No user at index" + index);
        }
        return store.get(index);
    }

    public int count() {
        return store.size();
    }
}
