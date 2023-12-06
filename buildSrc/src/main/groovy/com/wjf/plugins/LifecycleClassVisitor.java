package com.wjf.plugins;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class LifecycleClassVisitor extends ClassVisitor {
    private String className;

    public LifecycleClassVisitor(ClassVisitor cv) {
        /**
         * 参数1：ASM API版本，源码规定只能为4，5，6
         * 参数2：ClassVisitor不能为 null
         */
        super(Opcodes.ASM6, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("ClassVisitor visitMethod name-------" + name);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

        if (name.startsWith("on")) {
            //处理onXX()方法
            return new LifecycleMethodVisitor(mv, className, name);
        } else {
            return new MethodTimeVisitor(mv, access, className, name, desc);
        }
//        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}