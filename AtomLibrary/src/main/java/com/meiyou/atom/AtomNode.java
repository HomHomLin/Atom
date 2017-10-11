package com.meiyou.atom;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.List;

/**
 * Created by Linhh on 2017/10/10.
 */

public class AtomNode {
    public String mClazz;
    public List<Type> mTypes;
    public String mMethodName;
    public String mdesc;
    public int mAccess;
    public String mSignature;
    public String[] mExceptions;
    public String mTaskName;
    public long mDelay;
    public AnnotationNode mAnnotation;
    public int mIndex = -1;
    public int mNodeType = -1;

    public String mTypeDescrible;

    @Override
    public String toString() {
        return "clazz:" + mClazz + ";" + mTypeDescrible;
    }
}
