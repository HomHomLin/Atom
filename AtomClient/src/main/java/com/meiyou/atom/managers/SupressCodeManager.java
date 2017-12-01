package com.meiyou.atom.managers;

import com.meiyou.atom.Atom;
import com.meiyou.atom.converts.SupressCodeConvert;

/**
 * Created by Linhh on 2017/11/30.
 */

public class SupressCodeManager {
    static class Holder{
        static SupressCodeManager taskManager = new SupressCodeManager();
    }

    public static SupressCodeManager getIntance(){
        return SupressCodeManager.Holder.taskManager;
    }

    /**
     * 提交task,convert负责处理
     * @param desc 通信数据node
     * @param runnable
     */
    public void submitTask(String desc, Runnable runnable){
        SupressCodeConvert taskConvert = Atom.getAtom().getSupressCodeConvert();
        if(taskConvert != null){
            taskConvert.submitTask(desc, runnable);
        }
    }
}