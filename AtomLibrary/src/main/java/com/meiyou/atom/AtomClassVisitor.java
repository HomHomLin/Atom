package com.meiyou.atom;


import com.meiyou.atom.inject.MActivity;
import com.meiyou.atom.inject.MFragment;
import com.meiyou.atom.inject.MViewGroup;
import com.meiyou.atom.inject.SupressCode;
import com.meiyou.atom.inject.UiThread;
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
 * Created by Linhh on 17/6/8.
 */

public class AtomClassVisitor extends ClassVisitor {
    public String mClazzName;
    public List<AtomNode> mAtomNodes;
    public int mIndex;
    public AnnotationNode mClazzNode;
    public int mClazzType = -1;

    public AtomClassVisitor(int api, ClassVisitor cv, int index) {
        super(api, cv);
        mIndex = index;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mClazzName = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (Type.getDescriptor(MActivity.class).equals(desc)) {
            mClazzNode = new AnnotationNode(desc);
            mClazzType = AtomVar.TYPE_ACTIVITY;
            return mClazzNode;
        }else if (Type.getDescriptor(MFragment.class).equals(desc)) {
            mClazzNode = new AnnotationNode(desc);
            mClazzType = AtomVar.TYPE_FRAGMENT;
            return mClazzNode;
        }else if (Type.getDescriptor(MViewGroup.class).equals(desc)) {
            mClazzNode = new AnnotationNode(desc);
            mClazzType = AtomVar.TYPE_VIEW;
            return mClazzNode;
        }
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions) {
        if(name.startsWith("atomOrgin_")){
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        methodVisitor = new AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc) {

            public boolean mAtomWorkThreadInject = false;
            public boolean mAtomUIThreadInject = false;
            public boolean mAtomSupressCodeInject = false;
            public AnnotationNode mAnnotationNode;

            /**
             * 读取注解
             * @param desc
             * @param visible
             * @return
             */
            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if (Type.getDescriptor(WorkThread.class).equals(desc)) {
                    mAnnotationNode = new AnnotationNode(desc);
                    mAtomWorkThreadInject = true;
                    mIndex ++;
                    return mAnnotationNode;
                }else if (Type.getDescriptor(UiThread.class).equals(desc)) {
                    mAnnotationNode = new AnnotationNode(desc);
                    mAtomUIThreadInject = true;
                    mIndex ++;
                    return mAnnotationNode;
                }else if (Type.getDescriptor(SupressCode.class).equals(desc)){
                    mAnnotationNode = new AnnotationNode(desc);
                    mAtomSupressCodeInject = true;
                    mIndex ++;
                    return mAnnotationNode;
                }
                return super.visitAnnotation(desc, visible);
            }

            //            /**
//             * 读取参数,如果是atom,则将其参数改为final
//             * @param name
//             * @param access
//             */
//            @Override
//            public void visitParameter(String name, int access) {
//                if(!mAtomInject){
//                    super.visitParameter(name, access);
//                }else {
//                    if (api < Opcodes.ASM5) {
//                        throw new RuntimeException();
//                    }
//                    if (mv != null) {
//                        mv.visitParameter(name, ACC_FINAL);
//                    }
//                }
//            }

            @Override
            public void visitCode() {
                super.visitCode();
                if(mAtomWorkThreadInject || mAtomUIThreadInject || mAtomSupressCodeInject){
                    if(mAtomNodes == null){
                        mAtomNodes = new ArrayList<>();
                    }
                    List<Type> paramsTypeClass = new ArrayList();
                    Type[] argsType = Type.getArgumentTypes(desc);
                    String typeDescrible = "";
                    for (Type type : argsType) {
                        paramsTypeClass.add(type);
                        typeDescrible = typeDescrible + type;
                    }
                    AtomNode atomNode = new AtomNode();
                    atomNode.mClazz = mClazzName;
                    atomNode.mTypes = paramsTypeClass;
                    atomNode.mTypeDescrible = typeDescrible;
                    atomNode.mMethodName = name;
                    atomNode.mdesc = desc;
                    atomNode.mAccess = access;
                    atomNode.mSignature = signature;
                    atomNode.mExceptions = exceptions;
                    atomNode.mAnnotation = mAnnotationNode;
                    atomNode.mIndex = mIndex;
                    atomNode.mReturnType = Type.getReturnType(methodDesc);
                    if(mAtomUIThreadInject){
                        atomNode.mNodeType = AtomVar.TYPE_UITHREAD;
                    }else if(mAtomWorkThreadInject){
                        atomNode.mNodeType = AtomVar.TYPE_WORKTHREAD;
                    }else if(mAtomSupressCodeInject){
                        atomNode.mNodeType = AtomVar.TYPE_SUPRESSCODE;
                    }
//                    atomNode.mNodeType = mAtomUIThreadInject ? AtomVar.TYPE_UITHREAD : AtomVar.TYPE_WORKTHREAD;
                    mAtomNodes.add(atomNode);
                }
            }

        };

        return methodVisitor;

    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
