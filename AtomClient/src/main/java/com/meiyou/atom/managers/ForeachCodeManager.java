package com.meiyou.atom.managers;

import com.meiyou.atom.Atom;
import com.meiyou.atom.converts.ForeachConvert;

/**
 * Created by Linhh on 2017/12/19.
 */

public class ForeachCodeManager {
    static class Holder{
        static ForeachCodeManager taskManager = new ForeachCodeManager();
    }

    public static ForeachCodeManager getIntance(){
        return ForeachCodeManager.Holder.taskManager;
    }

    /**
     * 提交task,convert负责处理
     * @param desc 通信数据node
     * @param clazz
     */
    public void onForeachEnter(String an,String owner,  String clazz, String method, String desc){
        ForeachConvert foreachConvert = Atom.getAtom().getForeachConvert();
        if(foreachConvert != null){
            foreachConvert.onForeachEnter(an, owner, clazz, method,desc);
        }
    }

    public void onForeachEnd(String an, String owner, String clazz, String method, String desc){
        ForeachConvert foreachConvert = Atom.getAtom().getForeachConvert();
        if(foreachConvert != null){
            foreachConvert.onForeachEnd(an, owner, clazz, method, desc);
        }
    }

    public void onForeachPoll(String an, String owner, String clazz, String method, String desc){
        ForeachConvert foreachConvert = Atom.getAtom().getForeachConvert();
        if(foreachConvert != null){
            foreachConvert.onForeachPoll(an, owner, clazz, method, desc);
        }
    }

    public void onForeachPush(String an, String owner, String clazz, String method, String desc){
        ForeachConvert foreachConvert = Atom.getAtom().getForeachConvert();
        if(foreachConvert != null){
            foreachConvert.onForeachPush(an, owner, clazz, method, desc);
        }
    }
}
