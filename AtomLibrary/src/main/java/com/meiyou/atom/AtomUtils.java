package com.meiyou.atom;

import com.meiyou.atom.inject.ForEachCode;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.Map;

/**
 * 初始化的类必须放置在com.meiyou.atom下,避免代码织入误伤
 * Created by Linhh on 2017/11/30.
 */

public class AtomUtils implements Opcodes{

    /**
     * 获得注解内容
     * @param key
     * @return
     */
    public static String getDescByForeach(Map<String, AnnotationNode> nodes, String key){
        try {
            if (nodes != null) {
                AnnotationNode node = nodes.get(key);
                if (node != null && node.values != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(node.values.get(1).toString());
//                for(Object o : node.values){
//                    stringBuilder.append(o.toString());
//                }
                    return stringBuilder.toString();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static MethodVisitor excuteForEachCode(Map<String, AnnotationNode> nodes, final boolean enabled,  MethodVisitor methodVisitor, String clazz, int access, String mName, String mDesc, String signature, String[] exceptions){
        MethodVisitor newmethod = new AdviceAdapter(Opcodes.ASM5, methodVisitor, access, mName, mDesc) {
            boolean mAtomForeachInject = false;
            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if(Type.getDescriptor(ForEachCode.class).equals(desc)){
                    mAtomForeachInject = true;
                }
                return super.visitAnnotation(desc, visible);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                if((mAtomForeachInject || enabled) && !clazz.contains("com/meiyou/atom")
                        && !owner.contains("com/meiyou/atom")){
                    String m = getDescByForeach(nodes, mName + mDesc);
                    if(m == null){
                        m = getDescByForeach(nodes, clazz);
                    }
                    if(m == null){
                        m = name;
                    }
                    mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/atom/managers/ForeachCodeManager", "getIntance", "()Lcom/meiyou/atom/managers/ForeachCodeManager;", false);
                    mv.visitLdcInsn(m);
                    mv.visitLdcInsn(owner.replaceAll("/","."));
                    mv.visitLdcInsn(clazz.replaceAll("/","."));
                    mv.visitLdcInsn(name);
                    mv.visitLdcInsn(getJavaArgumentDesc(desc));
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/ForeachCodeManager", "onForeachEnter", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
                }
                super.visitMethodInsn(opcode, owner, name, desc, itf);
                if((mAtomForeachInject || enabled) &&
                        !clazz.contains("com/meiyou/atom") &&
                        !owner.contains("com/meiyou/atom")){
                    String m = getDescByForeach(nodes, mName + mDesc);
                    if(m == null){
                        m = getDescByForeach(nodes, clazz);
                    }
                    if(m == null){
                        m = name;
                    }
                    mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/atom/managers/ForeachCodeManager", "getIntance", "()Lcom/meiyou/atom/managers/ForeachCodeManager;", false);
                    mv.visitLdcInsn(m);
                    mv.visitLdcInsn(owner.replaceAll("/","."));
                    mv.visitLdcInsn(clazz.replaceAll("/","."));
                    mv.visitLdcInsn(name);
                    mv.visitLdcInsn(getJavaArgumentDesc(desc));
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/ForeachCodeManager", "onForeachEnd", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
                }
            }

            @Override
            public void visitEnd() {

                super.visitEnd();
            }

            @Override
            protected void onMethodExit(int opcode) {
                super.onMethodExit(opcode);
                if((mAtomForeachInject || enabled) && !clazz.contains("com/meiyou/atom")){
                    String m = getDescByForeach(nodes, mName + mDesc);
                    if(m == null){
                        m = getDescByForeach(nodes, clazz);
                    }
                    if(m == null){
                        m = mName;
                    }
                    mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/atom/managers/ForeachCodeManager", "getIntance", "()Lcom/meiyou/atom/managers/ForeachCodeManager;", false);
                    mv.visitLdcInsn(m);
                    mv.visitLdcInsn(clazz.replaceAll("/","."));
                    mv.visitLdcInsn(clazz.replaceAll("/","."));
                    mv.visitLdcInsn(mName);
                    mv.visitLdcInsn(getJavaArgumentDesc(mDesc));
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/ForeachCodeManager", "onForeachPush", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
                }
            }


            @Override
            public void visitCode() {
                if((mAtomForeachInject || enabled) && !clazz.contains("com/meiyou/atom")){
                    String m = getDescByForeach(nodes, mName + mDesc);
                    if(m == null){
                        m = getDescByForeach(nodes, clazz);
                    }
                    if(m == null){
                        m = mName;
                    }
                    mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/atom/managers/ForeachCodeManager", "getIntance", "()Lcom/meiyou/atom/managers/ForeachCodeManager;", false);
                    mv.visitLdcInsn(m);
                    mv.visitLdcInsn(clazz.replaceAll("/","."));
                    mv.visitLdcInsn(clazz.replaceAll("/","."));
                    mv.visitLdcInsn(mName);
                    mv.visitLdcInsn(getJavaArgumentDesc(mDesc));
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/ForeachCodeManager", "onForeachPoll", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
                }
                super.visitCode();
            }
        };
        return newmethod;
    }

    public static String getJavaArgumentDesc(String desc){
        StringBuilder javaDesc = new StringBuilder();
        Type[] argsType = Type.getArgumentTypes(desc);
        for (int i = 0 ; i < argsType.length; i++) {
            javaDesc.append(getJavaNormalType(argsType[i].getDescriptor()));
            if(i + 1 != argsType.length) {
                javaDesc.append(",");
            }
        }
        javaDesc.append(";");
        Type returnType = Type.getReturnType(desc);
        javaDesc.append(getJavaNormalType(returnType.getDescriptor()));
        return javaDesc.toString();
    }

    /**
     * 执行对应的自动化方法
     * @param node
     * @param mv
     */
    public static void excuteForAtom(AtomNode node, MethodVisitor mv){
        if(node.mNodeType == AtomVar.TYPE_WORKTHREAD) {
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/TaskManager", "submitTask", "(Ljava/lang/String;Lcom/meiyou/atom/AtomMethod;Ljava/lang/Class;)Ljava/lang/Object;", false);
        }else if(node.mNodeType == AtomVar.TYPE_UITHREAD){
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/UIThreadManager", "submitTask", "(Ljava/lang/String;Lcom/meiyou/atom/AtomMethod;Ljava/lang/Class;)Ljava/lang/Object;", false);
        }else if(node.mNodeType == AtomVar.TYPE_SUPRESSCODE){
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/SupressCodeManager", "submitTask", "(Ljava/lang/String;Lcom/meiyou/atom/AtomMethod;Ljava/lang/Class;)Ljava/lang/Object;", false);
        }
        mv.visitVarInsn(ASTORE, 3);
        mv.visitVarInsn(ALOAD, 3);
        returnResult(mv,node.mReturnType);

    }

    public static void storeClazz(MethodVisitor mv, Type returnType){
        String typeS = returnType.getDescriptor();
        if("V".equals(typeS)){
            mv.visitFieldInsn(GETSTATIC, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
            return;
        }
        if("Z".equals(typeS)){
            mv.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
            return;
        }
        if("B".equals(typeS)){
            mv.visitFieldInsn(GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
            return;
        }
        if("C".equals(typeS)){
            mv.visitFieldInsn(GETSTATIC, "java/lang/Character", "TYPE", "Ljava/lang/Class;");
            return;
        }
        if("S".equals(typeS)){
            mv.visitFieldInsn(GETSTATIC, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
            return;
        }
        if("I".equals(typeS)){
            mv.visitFieldInsn(GETSTATIC, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
            return;
        }
        if("F".equals(typeS)){
            mv.visitFieldInsn(GETSTATIC, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
            return;
        }
        if("D".equals(typeS)){
            mv.visitFieldInsn(GETSTATIC, "java/lang/Double", "TYPE", "Ljava/lang/Class;");

            return;
        }
        if("J".equals(typeS)){
            mv.visitFieldInsn(GETSTATIC, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
            return;
        }
        mv.visitLdcInsn(returnType);
    }

    public static void returnResult(MethodVisitor mv, Type returnType){
        //判断是否有返回值，代码不同
        if("V".equals(returnType.getDescriptor())){
            mv.visitInsn(Opcodes.RETURN);
        }else{
            //强制转化类型
            if(!castPrimateToObj(mv, returnType.getDescriptor())){
                //这里需要注意，如果是数组类型的直接使用即可，如果非数组类型，就得去除前缀了,还有最终是没有结束符;
                //比如：Ljava/lang/String; ==》 java/lang/String
                String newTypeStr = null;
                int len = returnType.getDescriptor().length();
                if(returnType.getDescriptor().startsWith("[")){
                    newTypeStr = returnType.getDescriptor().substring(0, len);
                }else{
                    newTypeStr = returnType.getDescriptor().substring(1, len-1);
                }
                mv.visitTypeInsn(Opcodes.CHECKCAST, newTypeStr);
            }

            //这里还需要做返回类型不同返回指令也不同
            mv.visitInsn(getReturnTypeCode(returnType.getDescriptor()));
        }
    }

    public static boolean returnTypeFormat(MethodVisitor mv, String typeS){
        if("Z".equals(typeS)){
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
            return true;
        }
        if("B".equals(typeS)){
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
            return true;
        }
        if("C".equals(typeS)){
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
            return true;
        }
        if("S".equals(typeS)){
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
            return true;
        }
        if("I".equals(typeS)){
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            return true;
        }
        if("F".equals(typeS)){
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
            return true;
        }
        if("D".equals(typeS)){
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);

            return true;
        }
        if("J".equals(typeS)){
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);

            return true;
        }
        return false;
    }

    /**
     * 基本类型需要做对象类型分装
     * @param mv
     * @param typeS
     * @return
     */
    private static boolean castPrimateToObj(MethodVisitor mv, String typeS){
        if("Z".equals(typeS)){
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Boolean");//强制转化类型
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z",false);
            return true;
        }
        if("B".equals(typeS)){
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Byte");//强制转化类型
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B",false);
            return true;
        }
        if("C".equals(typeS)){
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Character");//强制转化类型
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Character", "intValue", "()C",false);
            return true;
        }
        if("S".equals(typeS)){
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Short");//强制转化类型
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S",false);
            return true;
        }
        if("I".equals(typeS)){
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Integer");//强制转化类型
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I",false);
            return true;
        }
        if("F".equals(typeS)){
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Float");//强制转化类型
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F",false);
            return true;
        }
        if("D".equals(typeS)){
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");//强制转化类型
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D",false);
            return true;
        }
        if("J".equals(typeS)){
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Long");//强制转化类型
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J",false);
            return true;
        }
        return false;
    }

    /**
     * 根据对应的atom数据获得自动化方法管理器
     * @param node
     * @param mv
     */
    public static void getInstatnceForAtom(AtomNode node, MethodVisitor mv){
        if(node.mNodeType == AtomVar.TYPE_WORKTHREAD) {
            mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/atom/managers/TaskManager", "getIntance", "()Lcom/meiyou/atom/managers/TaskManager;", false);
        }else if(node.mNodeType == AtomVar.TYPE_UITHREAD) {
            mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/atom/managers/UIThreadManager", "getIntance", "()Lcom/meiyou/atom/managers/UIThreadManager;", false);
        }else if(node.mNodeType == AtomVar.TYPE_SUPRESSCODE) {
            mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/atom/managers/SupressCodeManager", "getIntance", "()Lcom/meiyou/atom/managers/SupressCodeManager;", false);
        }
    }

    public static String getJavaNormalType(String typeS){
        boolean squeen = false;
        if(typeS.startsWith("[")){
            //说明是数组
            squeen = true;
            typeS = typeS.substring(0, typeS.length());
        }
        if("V".equals(typeS)){
            if(squeen){
                return "void[]";
            }
            return "void";
        }
        if("Z".equals(typeS)){
            if(squeen){
                return "boolean[]";
            }
            return "boolean";
        }
        if("B".equals(typeS)){
            if(squeen){
                return "byte[]";
            }
            return "byte";
        }
        if("C".equals(typeS)){
            if(squeen){
                return "char[]";
            }
            return "char";
        }
        if("S".equals(typeS)){
            if(squeen){
                return "short[]";
            }
            return "short";
        }
        if("I".equals(typeS)){
            if(squeen){
                return "int[]";
            }
            return "int";
        }
        if("F".equals(typeS)){
            if(squeen){
                return "float[]";
            }
            return "float";
        }
        if("D".equals(typeS)){
            if(squeen){
                return "double[]";
            }
            return "double";
        }
        if("J".equals(typeS)){
            if(squeen){
                return "long[]";
            }
            return "long";
        }
        if(typeS.startsWith("L")){
            typeS = typeS.substring(1, typeS.length()).replaceAll("/", ".").replaceAll(";","");
        }
        if(squeen){
            return typeS + "[]";
        }
        return typeS;
    }

    public static int getReturnTypeCode(String typeS){
        if("V".equals(typeS)){
            return Opcodes.RETURN;
        }
        if("Z".equals(typeS)){
            return Opcodes.IRETURN;
        }
        if("B".equals(typeS)){
            return Opcodes.IRETURN;
        }
        if("C".equals(typeS)){
            return Opcodes.IRETURN;
        }
        if("S".equals(typeS)){
            return Opcodes.IRETURN;
        }
        if("I".equals(typeS)){
            return Opcodes.IRETURN;
        }
        if("F".equals(typeS)){
            return Opcodes.FRETURN;
        }
        if("D".equals(typeS)){
            return Opcodes.DRETURN;
        }
        if("J".equals(typeS)){
            return Opcodes.LRETURN;
        }
        return Opcodes.ARETURN;
    }
}
