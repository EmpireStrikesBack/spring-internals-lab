package com.example;

import org.objectweb.asm.*;
import java.io.InputStream;

public class BytecodeTransformer {

    public static byte[] getTransformedBytes() throws Exception {

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
                            if (opcode == Opcodes.RETURN) {

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
                            if (opcode == Opcodes.ARETURN) {

                                mv.visitInsn(Opcodes.POP); // remove original
                                mv.visitLdcInsn("Intercepted! get ready to be hacked mtfcker :)"); // new value
                                mv.visitInsn(Opcodes.ARETURN);

                                return; // critical
                            }
                            super.visitInsn(opcode);
                        }
                    };
                }

                return mv;
            }
        };

        cr.accept(cv, 0);

        // return modified bytecode ONLY
        return cw.toByteArray();
    }
}