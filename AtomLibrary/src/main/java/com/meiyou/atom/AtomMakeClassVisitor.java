package com.meiyou.atom;

import com.meiyou.atom.inject.WorkThread;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linhh on 2017/10/10.
 */

public class AtomMakeClassVisitor extends ClassVisitor {
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
}
