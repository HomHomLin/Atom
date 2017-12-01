package com.meiyou.atom;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Created by Linhh on 2017/11/30.
 */

public class AtomUtils implements Opcodes{

    /**
     * 执行对应的自动化方法
     * @param node
     * @param mv
     */
    public static void excuteForAtom(AtomNode node, MethodVisitor mv){
        if(node.mNodeType == AtomVar.TYPE_WORKTHREAD) {
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/TaskManager", "submitTask", "(Ljava/lang/String;Lcom/meiyou/atom/AtomMethod;Ljava/lang/String;)Ljava/lang/Object;", false);
        }else if(node.mNodeType == AtomVar.TYPE_UITHREAD){
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/UIThreadManager", "submitTask", "(Ljava/lang/String;Lcom/meiyou/atom/AtomMethod;Ljava/lang/String;)Ljava/lang/Object;", false);
        }else if(node.mNodeType == AtomVar.TYPE_SUPRESSCODE){
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/SupressCodeManager", "submitTask", "(Ljava/lang/String;Lcom/meiyou/atom/AtomMethod;Ljava/lang/String;)Ljava/lang/Object;", false);
        }
        mv.visitVarInsn(ASTORE, 3);
        mv.visitVarInsn(ALOAD, 3);
        returnResult(mv,node.mReturnType);

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
