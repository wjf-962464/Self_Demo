package com.wjf.plugins;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;


public class MethodTimeVisitor extends AdviceAdapter {
    private String className;
    private String methodName;
    private int startTimeVarIndex;

    protected MethodTimeVisitor(MethodVisitor methodVisitor, int access, String className, String methodName, String desc) {
        super(Opcodes.ASM6, methodVisitor, access, methodName, desc);
        this.className = className.replace('/', '.'); // 转换类名格式以匹配Java的规范
        this.methodName = methodName;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();

        // 插入当前时间毫秒值
        startTimeVarIndex = newLocal(Type.LONG_TYPE);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitVarInsn(LSTORE, startTimeVarIndex);
    }

    @Override
    protected void onMethodExit(int opcode) {
        Label startElseBlock = new Label();
        Label endIfBlock = new Label();

        // 检查当前线程是否为主线程
        mv.visitMethodInsn(INVOKESTATIC, "android/os/Looper", "getMainLooper", "()Landroid/os/Looper;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "android/os/Looper", "getThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitJumpInsn(IF_ACMPNE, startElseBlock);

        // 如果是主线程，计算方法耗时，并记录
        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LLOAD, startTimeVarIndex);
            mv.visitInsn(LSUB);
            // Store the elapsed time on the stack
            int elapsedTimeVarIndex = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, elapsedTimeVarIndex);

            // Use the Log class to print the elapsed time
            mv.visitLdcInsn("Timing"); // Log tag
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn(className + "." + methodName + " took ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(LLOAD, elapsedTimeVarIndex);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn(" ms on main thread");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            // Invoke Log.d(String tag, String msg)
            mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(POP);
        }
        mv.visitJumpInsn(GOTO, endIfBlock);

        // Else block (不是主线程时不执行任何操作)
        mv.visitLabel(startElseBlock);

        // End if block
        mv.visitLabel(endIfBlock);

        super.onMethodExit(opcode);
    }
}