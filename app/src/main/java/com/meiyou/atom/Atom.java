package com.meiyou.atom;

import com.meiyou.atom.converts.TaskConvert;

/**
 * Created by Linhh on 2017/10/11.
 */

public class Atom {
    private TaskConvert taskConvert;

    static class Holder{
        public static Atom mAtom = new Atom();
    }

    public static Atom getAtom(){
        return Holder.mAtom;
    }

    public static void init(AtomBuilder builder){
        Atom atom = getAtom();
        atom.taskConvert = builder.taskConvert;
    }

    public TaskConvert getTaskConvert(){
        return taskConvert;
    }
}
