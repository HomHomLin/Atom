package com.meetyou.demo;

/**
 * Created by Linhh on 2017/10/10.
 */

public class AtomMeta implements Runnable{
    private MainActivity activity;

    public AtomMeta(MainActivity activity,MainActivity activity2, String s, int q) {
        this.activity = activity;
    }

    @Override
    public void run() {
        activity.t2(null,activity);
    }
}
