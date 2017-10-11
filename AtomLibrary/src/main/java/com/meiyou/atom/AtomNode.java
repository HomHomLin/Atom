package com.meiyou.atom;

import org.objectweb.asm.Type;

import java.util.List;

/**
 * Created by Linhh on 2017/10/10.
 */

public class AtomNode {
    public String mClazz;
    public List<Type> mTypes;
    public String mMethodName;
    public String mdesc;

    public String mTypeDescrible;

    @Override
    public String toString() {
        return "clazz:" + mClazz + ";" + mTypeDescrible;
    }
}
