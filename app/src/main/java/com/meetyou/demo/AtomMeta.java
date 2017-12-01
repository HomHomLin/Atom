package com.meetyou.demo;

import com.meiyou.atom.AtomMethod;

/**
 * Created by Linhh on 2017/10/10.
 */

public class AtomMeta implements AtomMethod{
    private MainActivity activity;

    public AtomMeta(MainActivity activity,MainActivity activity2, String s, int q) {
        this.activity = activity;
    }

    @Override
    public Object run() {
        if(activity.getClass() ==  MainActivity.class){
            return new Object();
        }

        Class cl = int.class;
        Class cl2 = boolean.class;
        Class cl3 = MainActivity.class;
//        Object o  = activity.t3(null,activity,1);
        return cl;
    }
}
