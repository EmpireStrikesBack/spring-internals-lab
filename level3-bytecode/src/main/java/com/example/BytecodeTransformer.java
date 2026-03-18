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

                    // instrument sayHello() --> void method 
                    if (name.equals("sayHello") && descriptor.equals("()V")) {
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
                                mv.visitLdcInsn(">> Entering sayHello");
                                mv.visitMethodInsn(
                                    Opcodes.INVOKEVIRTUAL,
                                    "java/io/PrintStream",
                                    "println",
                                    "(Ljava/lang/String;)V",
                                    false
                                );
                            }

                            @Override
                            public void visitInsn(int opcode) {
                                // check if it's a RETURN instruciotn
                                if (opcode == Opcodes.RETURN) {
                                    // inject before returning (effectively after method logic)
                                    mv.visitFieldInsn(
                                        Opcodes.GETSTATIC,
                                        "java/lang/System",
                                        "out",
                                        "Ljava/io/PrintStream;"
                                    );
                                    mv.visitLdcInsn(">> Exiting method");
                                    mv.visitMethodInsn(
                                        Opcodes.INVOKEVIRTUAL,
                                        "java/io/PrintStream",
                                        "println",
                                        "(Ljava/lang/String;)V",
                                        false
                                    );
                                }
                                super.visitInsn(opcode);
                            }
                        };
                    }

                    // Instrument greet(String) --> String return 
                    if (name.equals("greet") && descriptor.equals("(Ljava/lang/String;)Ljava/lang/String;")) {
                        return new MethodVisitor(Opcodes.ASM9, mv) {
                            @Override
                            public void visitInsn(int opcode) {
                                // intercept return value 
                                if (opcode == Opcodes.ARETURN) {
                                    // remove original return value 
                                    mv.visitInsn(Opcodes.POP);
                                    // push new value 
                                    mv.visitLdcInsn("intercepted! :)");
                                    // return modified value 
                                    mv.visitInsn(Opcodes.ARETURN);
                                    return; // skip original value 
                                }
                                super.visitInsn(opcode);
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
            
            // create instance 
            Object instance = modifiedClass.getDeclaredConstructor().newInstance();

            // call sayHello()
            Method sayHello = modifiedClass.getMethod("sayHello");
            sayHello.invoke(instance);

            System.out.println();

            // call greet(String)
            Method greet = modifiedClass.getMethod("greet", String.class);
            Object result = greet.invoke(instance, "Alice");

            System.out.println("Returned value: " + result );
    }
}
