package com.meetyou.demo;

/**
 * Created by Linhh on 2017/10/10.
 */

public class AtomMeta implements Runnable{
    private MainActivity activity;
    private String s;

    public AtomMeta(MainActivity activity, String s) {
        this.activity = activity;
        this.s = s;
    }

    @Override
    public void run() {
        activity.t(activity,s);
    }
}
