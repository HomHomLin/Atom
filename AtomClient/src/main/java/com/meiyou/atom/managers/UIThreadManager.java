package com.meiyou.atom.managers;

import com.meiyou.atom.Atom;
import com.meiyou.atom.DescProcessor;
import com.meiyou.atom.converts.TaskConvert;
import com.meiyou.atom.converts.UIThreadConvert;

/**
 * Created by Linhh on 2017/10/11.
 */

public class UIThreadManager {
    static class Holder{
        static UIThreadManager taskManager = new UIThreadManager();
    }

    public static UIThreadManager getIntance(){
        return UIThreadManager.Holder.taskManager;
    }

    /**
     * 提交task,convert负责处理
     * @param desc 通信数据node
     * @param runnable
     */
    public void submitTask(String desc, Runnable runnable){
        UIThreadConvert taskConvert = Atom.getAtom().getUIThreadConvert();
        if(taskConvert != null){
            taskConvert.submitTask(DescProcessor.excuteAtomTask(desc), runnable);
        }
    }
}
