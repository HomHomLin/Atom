package com.meiyou.atom;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Linhh on 2017/10/10.
 */

public class AtomMetaWriter implements Opcodes {
    public static byte[] makeMetas(int index, AtomNode node){
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "com/meiyou/atom/metas/AtomMeta" + index, null, "java/lang/Object", new String[] { "java/lang/Runnable" });

        {
            fv = cw.visitField(ACC_PRIVATE, "varObj", "L" + node.mClazz + ";", null, null);
            fv.visitEnd();
        }
        {
            List<Type> types = node.mTypes;
            if(types != null) {
                for (int i = 0; i < types.size(); i ++){
                    fv = cw.visitField(ACC_PRIVATE, "var" + i, types.get(i).toString(), null, null);
                    fv.visitEnd();
                }
            }
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(L" + node.mClazz +";" + node.mTypeDescrible + ")V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, "com/meiyou/atom/metas/AtomMeta" + index, "varObj", "L" + node.mClazz + ";");

            int start = 2;
            List<Type> types = node.mTypes;
            if(types != null) {
                for (int i = 0; i < types.size(); i ++){
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitVarInsn(ALOAD, start);
                    mv.visitFieldInsn(PUTFIELD, "com/meiyou/atom/metas/AtomMeta" + index, "var" + i, types.get(i).toString());
                    start ++;
//                    fv = cw.visitField(ACC_PRIVATE, "var" + i, types.get(i).toString(), null, null);
//                    fv.visitEnd();
                }
            }

            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2 + (types != null ? types.size() : 0 ));
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
            mv.visitCode();
            //do somethings...
            //读取源对象
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "com/meiyou/atom/metas/AtomMeta" + index, "varObj", "L" + node.mClazz + ";");

            List<Type> types = node.mTypes;
            if(types != null) {
                for (int i = 0; i < types.size(); i ++){
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitFieldInsn(GETFIELD, "com/meiyou/atom/metas/AtomMeta" + index, "var" + i, types.get(i).toString());
                }
            }
            mv.visitMethodInsn(INVOKEVIRTUAL, node.mClazz, "atomOrgin_" + node.mMethodName, node.mdesc, false);

            mv.visitInsn(RETURN);
            mv.visitMaxs(1 + (types == null ? 0 : types.size()), 1);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
