package com.meetyou.demo;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.meiyou.atom.AtomTaskNode;
import com.meiyou.atom.inject.WorkThread;
import com.meiyou.atom.managers.TaskManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Test.init();
        test2("test",this,1);
        t3("test",this,1);
        test2("test",this,1);
    }
    public void t1(MainActivity activity, String s, int q){
        AtomMeta meta = new AtomMeta(this, activity,s,q);
        TaskManager.getIntance().submitTask("delay;100;taskName;test;", meta);
    }

    public void t2( String s,MainActivity activity){

    }

    @WorkThread(delay = 100, taskName = "test2")
    public void t3(String test, MainActivity activity, int q){
        Log.i("Test-Atom", "test3 is call");
    }

    @WorkThread(delay = 100, taskName = "test")
    public void test2(String test, MainActivity activity, int q){
        Log.i("Test-Atom", "test2 is call");
    }
}
