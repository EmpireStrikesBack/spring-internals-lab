package com.example;

import org.objectweb.asm.*;
import java.io.InputStream;
import java.lang.reflect.Method;

public class BytecodeTransformer {
    public static void main(String[] args) throws Exception {
        // load original class
        InputStream is = BytecodeTransformer.class
            .getClassLoader()
            .getResourceAsStream("com/example/HelloWorld.class");
        
            ClassReader cr = new ClassReader(is);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            // transform class
            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
                @Override
                public MethodVisitor visitMethod(
                    int access,
                    String name,
                    String descriptor,
                    String signature,
                    String[] exceptions
                ) {
                    MethodVisitor mv = super.visitMethod(
                        access, name, descriptor, signature, exceptions
                    );

                    // only target sayHello()
                    if (name.equals("sayHello")) {
                        return new MethodVisitor(Opcodes.ASM9, mv) {
                            @Override
                            public void visitCode() {
                                super.visitCode();

                                // inject before original method body
                                mv.visitFieldInsn(
                                    Opcodes.GETSTATIC,
                                    "java/lang/System",
                                    "out",
                                    "Ljava/io/PrintStream;"
                                );
                                mv.visitLdcInsn(">> Entering method");

                                mv.visitMethodInsn(
                                    Opcodes.INVOKEVIRTUAL,
                                    "java/io/PrintStream",
                                    "println",
                                    "(Ljava/lang/String;)V",
                                    false
                                );
                            }
                        };
                    }
                    return mv;
                }
            };
            cr.accept(cv, 0);

            // get modified bytecode
            byte[] modifiedBytes = cw.toByteArray();

            // load modified class 
            Class<?> modifiedClass = new CustomClassLoader()
                .defineClass("com.example.HelloWorld", modifiedBytes);
            
            // execute
            Object instance = modifiedClass.getDeclaredConstructor().newInstance();

            Method method = modifiedClass.getMethod("sayHello");
            method.invoke(instance);
    }
}
