package com.meiyou.atom;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/TaskManager", "submitTask", "(Ljava/lang/String;Ljava/lang/Runnable;)V", false);
        }else if(node.mNodeType == AtomVar.TYPE_UITHREAD){
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/UIThreadManager", "submitTask", "(Ljava/lang/String;Ljava/lang/Runnable;)V", false);
        }else if(node.mNodeType == AtomVar.TYPE_SUPRESSCODE){
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/atom/managers/SupressCodeManager", "submitTask", "(Ljava/lang/String;Ljava/lang/Runnable;)V", false);
        }
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
}
