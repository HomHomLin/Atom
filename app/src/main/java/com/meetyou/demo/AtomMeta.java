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
//        Object o  = activity.t3(null,activity,1);
        return null;
    }
}
