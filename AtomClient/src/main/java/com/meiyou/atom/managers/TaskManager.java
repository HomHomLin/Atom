package com.meiyou.atom.managers;

import com.meiyou.atom.Atom;
import com.meiyou.atom.AtomMethod;
import com.meiyou.atom.DescProcessor;
import com.meiyou.atom.converts.TaskConvert;

/**
 * Created by Linhh on 2017/10/11.
 */

public class TaskManager {
    static class Holder{
        static TaskManager taskManager = new TaskManager();
    }

    public static TaskManager getIntance(){
        return Holder.taskManager;
    }

    /**
     * 提交task,convert负责处理
     * @param desc 通信数据node
     * @param runnable
     */
    public Object submitTask(String desc, AtomMethod runnable, Class returnType){
        TaskConvert taskConvert = Atom.getAtom().getTaskConvert();
        if(taskConvert != null){
            return taskConvert.submitTask(DescProcessor.excuteAtomTask(desc), runnable, returnType);
        }
        return runnable.run();
    }
}
