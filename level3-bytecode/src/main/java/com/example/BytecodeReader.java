package com.example;

import org.objectweb.asm.*;
import java.io.InputStream;

/*
Step 3 : Read class sructure programmatically using ASM
- Load HelloWorld.class
- Parse it using ASM
- Intercept class, methods & fields 
*/

public class BytecodeReader {
    public static void main(String[] args) throws Exception {

        //Load the .class file as a stream
        InputStream is = BytecodeReader.class
            .getClassLoader()
            .getResourceAsStream("com/example/HelloWorld.class");

        // ASM class reader parses raw bytecode 
        ClassReader classReader = new ClassReader(is);

        /*
        ClassVisitor = ASM hook system
        - Allow the interception of the class while it is being parsed
        */
       classReader.accept(new ClassVisitor(Opcodes.ASM9) {
            // called when class metadata is visited 
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces ) {
                System.out.println("== CLASS ==");
                System.out.println("Name: " + name);
                System.out.println("Super: " + superName);
            }

            // called for each method in the class 
            @Override
            public  MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                System.out.println("\n== METHOD ==");
                System.out.println("Name: " + name);
                System.out.println("Descriptor: " + descriptor);

                // MethodVisitor lets us inspect bytecode instructions 
                return new MethodVisitor(Opcodes.ASM9) {
                    @Override
                    public void visitInsn(int opcode) {
                        System.out.println("Instruction opcode: " + opcode);
                    }

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        System.out.println("Method call: ");
                        System.out.println(" Owner: " + owner);
                        System.out.println(" Name: " + name);
                    }
                };
            }
       }, 0);
    }
}

