package com.example;

import org.objectweb.asm.*;
import java.lang.reflect.Method;

public class BytecodeGenerator {
    public static void main(String[] args) throws Exception {
        // create a ClassWritter (builds bytecode)
        ClassWriter cw = new ClassWriter(0);

        // define the class
        cw.visit(
            Opcodes.V21, // java version
            Opcodes.ACC_PUBLIC,
            "com/example/GeneratedHello", // internal name 
            null,
            "java/lang/Object",
            null
        );

        // Create constructor 
        MethodVisitor constructor = cw.visitMethod(
            Opcodes.ACC_PUBLIC,
            "<init>",
            "()V",
            null,
            null
        );
        constructor.visitCode();

        // super()
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/lang/Object",
            "<init>",
            "()V",
            false
        );

        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();

        // create sayHello() method 
        MethodVisitor mv = cw.visitMethod(
            Opcodes.ACC_PUBLIC,
            "sayHello",
            "()V",
            null,
            null
        );
        mv.visitCode();

        // System.out.println("Hello from ASM!");
        mv.visitFieldInsn(
            Opcodes.GETSTATIC,
            "java/lang/System",
            "out",
            "Ljava/io/PrintStream;"
        );
        mv.visitLdcInsn("Hello from ASM!");

        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/String;)V",
            false
        );
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(2,1);
        mv.visitEnd();

        // finish class 
        cw.visitEnd();

        // convert to byte array 
        byte[] classBytes = cw.toByteArray();

        // load class dynamically
        Class<?> generatedClass = new CustomClassLoader().defineClass("com.example.GeneratedHello", classBytes);

        // Instantiate and onvoke method 
        Object instance = generatedClass.getDeclaredConstructor().newInstance();

        Method method = generatedClass.getMethod("sayHello");
        method.invoke(instance);
    }
}
