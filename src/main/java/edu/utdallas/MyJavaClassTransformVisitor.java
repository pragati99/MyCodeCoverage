package edu.utdallas;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author: Miti Desai
 * Create Time: 03/15/17
 */
class MyJavaClassTransformVisitor extends ClassVisitor implements Opcodes {
    String classname;
    public MyJavaClassTransformVisitor(final ClassVisitor codevisitor) {
        super(ASM5, codevisitor);
    }

    @Override
    public void visit(int i, int i1, String nameOfClass, String str1, String str2, String[] strings) {
        super.visit(i, i1, nameOfClass, str1, str2, strings);
        this.classname=nameOfClass;

    }

    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

        return mv == null ? null : new MethodTransformVisitor(mv,classname);
    }
}
