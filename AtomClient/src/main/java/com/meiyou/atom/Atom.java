package com.meiyou.atom;

import com.meiyou.atom.converts.TaskConvert;
import com.meiyou.atom.converts.UIThreadConvert;

/**
 * Created by Linhh on 2017/10/11.
 */

public class Atom {
    private TaskConvert taskConvert;
    private UIThreadConvert uiThreadConvert;

    static class Holder{
        public static Atom mAtom = new Atom();
    }

    public static Atom getAtom(){
        return Holder.mAtom;
    }

    public static void init(AtomBuilder builder){
        Atom atom = getAtom();
        atom.taskConvert = builder.taskConvert;
        atom.uiThreadConvert = builder.uiThreadConvert;
    }

    public TaskConvert getTaskConvert(){
        return taskConvert;
    }

    public UIThreadConvert getUIThreadConvert(){
        return uiThreadConvert;
    }
}
