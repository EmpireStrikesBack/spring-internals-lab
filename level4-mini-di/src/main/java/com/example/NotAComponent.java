package com.example;

/*
Plain class : intentonally not annoted with @Component 
    - Used in the scanner demo to confirm that ComponentScanner correctly ignores classes without the annotation
    - Attempting to inject this type from any @Inject field would throw an "Unsatisfied dependency" RuntimeException from DependencyInjector
*/


public class NotAComponent {
    public String label() {
        return "I'm not managed by the container";
    }
}
