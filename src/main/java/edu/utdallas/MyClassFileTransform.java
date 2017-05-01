package edu.utdallas;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;


class MyClassFileTransform implements ClassFileTransformer {


    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.startsWith("org/apache/commons/dbutils") || className.startsWith("org/joda/time")){
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            MyJavaClassTransformVisitor ca = new MyJavaClassTransformVisitor(cw);
            
            cr.accept(ca, 0);
            return cw.toByteArray();
        }
        return classfileBuffer;
        
    }
}
