package com.meetyou.demo;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.meiyou.atom.inject.WorkThread;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void t(MainActivity activity, String s){

    }

    public int add(int x){
        return x+1;
    }

//    @UiThread("delay = 100, mode = fix")
    public void test(String test, MainActivity activity, int q){
        //EVENT

        //A(work)
        //A....
        //B....
        //A....
        // -> B(mix:work)
    }

    @WorkThread
    public void test2(String test, MainActivity activity, int q){
        //wangluo
        //....something
        test(test,activity,q);
    }
}
