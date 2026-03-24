package com.example;

import java.util.List;

/*
Demo bean : AppController 
 - Top of the dependency graph 
 - Injects UserService which trnasitively requires UserRepository 
 - Has @PostConstruct to confirm it's fully wired before use 

 Depdendency graph position : 
  - UserRepository <-- UserService <-- AppController 
  - 2 phase create-inject strategy in MiniApplication handles it correctly regarless of registration order 

*/

@Component 
public class AppController {
    @Inject 
    private UserService userService;

    @PostConstruct 
    public void onReady() {
        System.out.println(
            " [AppController.onReady] Wired."
            + "UserService sees " + userService.userCount() + "users."
        );
    }

    public void handleRequest() {
        System.out.println("\n== HANLDING REQUEST ==");
        List<String> users = userService.getAllUsers();
        System.out.println("All users: " + users);
        System.out.println("User at 0: " + userService.getUser(0));
        System.out.println("Processed: " + userService.processUser(users.get(1)));
    }
}
