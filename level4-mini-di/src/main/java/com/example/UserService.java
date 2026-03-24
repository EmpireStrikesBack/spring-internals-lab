package com.example;

import java.util.List;

/*
Demo bean : UserService
 - Middle layer in the dependency graph
 - Injects UserRepository via @Inject 
    - The container'll call field.setAccessible(true) & field.set(this, repo) cause field is private

Dependency graph position 
 - UserRepository <-- UserService <-- AppController 
*/

@Component 
public class UserService {
    @Inject 
    private UserRepository userRepository;

    public List<String> getAllUsers() {
        return userRepository.findAll();
    }

    public String getUser(int index) {
        return userRepository.findById(index);
    }

    public String processUser(String name) {
        return "processed: " + name.toUpperCase();
    }

    public int userCount() {
        return userRepository.count();
    }
}
