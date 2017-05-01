package edu.utdallas;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author: Miti Desai
 * Create Time: 03/15/17
 */
class MethodTransformVisitor extends MethodVisitor implements Opcodes {
    
    protected int lastVisitedLine;
	protected String className;
	
    public MethodTransformVisitor(final MethodVisitor mv, String nameOfclass) {

        super(ASM5, mv);
        this.className=nameOfclass;

    }

	Integer totalLinesVisited = lastVisitedLine+ lastVisitedLine;
    @Override
    public void visitLineNumber(int line, Label start) {
		if (0 != line) {
	    	lastVisitedLine = line;
	     
	     
			mv.visitLdcInsn(className);
			mv.visitLdcInsn(new Integer(line));
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKESTATIC, "edu/utdallas/CodeCoverageCollect", "addMethodLine", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);

			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
	    	mv.visitLdcInsn(className + " : " + line);
	    	mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
	}
	//	System.out.println(totalLinesVisited+"is the total lines visited for"+ className);
    	super.visitLineNumber(line, start);
	}

    @Override
    public void visitEnd() {
      
        super.visitEnd();
    }
}
