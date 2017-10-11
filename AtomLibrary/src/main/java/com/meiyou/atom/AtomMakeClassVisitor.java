package com.meiyou.atom;

import com.meiyou.atom.inject.WorkThread;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linhh on 2017/10/10.
 */

public class AtomMakeClassVisitor extends ClassVisitor implements Opcodes{
    private String mClazzName;
    private String mOutClazzName;
    public List<AtomNode> mAtomNodes;

    public AtomMakeClassVisitor(int api, ClassVisitor cv,String outClazz, List<AtomNode> atomNodes) {
        super(api, cv);
        mOutClazzName = outClazz;
        mAtomNodes = atomNodes;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mClazzName = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions) {
        if(name.startsWith("atomOrgin_") || mAtomNodes == null){
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }
        if(!mOutClazzName.equals(mClazzName)){
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }
        for(AtomNode node : mAtomNodes){
            if(node.mdesc.equals(desc) && node.mMethodName.equals(name)){
                return cv.visitMethod(access, "atomOrgin_" + name, desc, signature, exceptions);
            }
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);

    }

    @Override
    public void visitEnd() {
        if(mOutClazzName.equals(mClazzName) && mAtomNodes != null) {
            for(AtomNode node : mAtomNodes){
                MethodVisitor mv = cv.visitMethod(node.mAccess, node.mMethodName, node.mdesc, node.mSignature, node.mExceptions);
                mv.visitCode();
                //创建tasknode
                String meta = "com/meiyou/atom/metas/AtomMeta" + node.mIndex;
                mv.visitTypeInsn(NEW, meta);
                mv.visitInsn(DUP);
                mv.visitVarInsn(ALOAD, 0);

                String initDesc  = "";
                List<Type> types = node.mTypes;
                if(types != null) {
                    for (int i = 0; i < types.size(); i ++){
                        initDesc = initDesc + types.get(i).toString();
                        mv.visitVarInsn(ALOAD, i + 1);
//                    fv = cw.visitField(ACC_PRIVATE, "var" + i, types.get(i).toString(), null, null);
//                    fv.visitEnd();
                    }
                }
                mv.visitMethodInsn(INVOKESPECIAL, meta, "<init>", "("+"L" + node.mClazz + ";" + initDesc + ")V", false);
                mv.visitVarInsn(ASTORE, 2);
                if(node.mNodeType == AtomVar.TYPE_WORKTHREAD) {
                    mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/atom/managers/TaskManager", "getIntance", "()Lcom/meiyou/atom/managers/TaskManager;", false);
                }else if(node.mNodeType == AtomVar.TYPE_UITHREAD) {
                    mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/atom/managers/UIThreadManager", "getIntance", "()Lcom/meiyou/atom/managers/UIThreadManager;", false);
                }
                AnnotationNode annotationNode = node.mAnnotation;

                String annotationNodeDesc = "";
                if(annotationNode != null && annotationNode.values != null){
                    for(int node_index = 0 ; node_index < annotationNode.values.size(); node_index ++){
                        annotationNodeDesc = annotationNodeDesc + String.valueOf(annotationNode.values.get(node_index)) + ";";
                    }
                }
                mv.visitLdcInsn(annotationNodeDesc);
                mv.visitVarInsn(ALOAD, 2);
                if(node.mNodeType == AtomVar.TYPE_WORKTHREAD) {
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/TaskManager", "submitTask", "(Ljava/lang/String;Ljava/lang/Runnable;)V", false);
                }else if(node.mNodeType == AtomVar.TYPE_UITHREAD){
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/UIThreadManager", "submitTask", "(Ljava/lang/String;Ljava/lang/Runnable;)V", false);
                }

                mv.visitInsn(RETURN);
                mv.visitMaxs(1 + (node.mTypes == null ? 0 : node.mTypes.size()), 1 + (node.mTypes == null ? 0 : node.mTypes.size()));
                mv.visitEnd();
            }
        }
        super.visitEnd();

    }
}
