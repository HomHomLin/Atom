package com.meetyou.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.meiyou.atom.AtomTaskNode;
import com.meiyou.atom.inject.MActivity;
import com.meiyou.atom.inject.SupressCode;
import com.meiyou.atom.inject.UiThread;
import com.meiyou.atom.inject.WorkThread;
import com.meiyou.atom.managers.TaskManager;

@MActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static final String DEBUG = "debug";
    public static final String YUNQI = "yunqi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Test.init();
        Object o = test2("test",this,1);
        t3("test",this,1);
        t2("test",this);
        test3("test3");
    }
    public void t1(MainActivity activity, String s, int q){
//        AtomMeta meta = new AtomMeta(this, activity,s,q);
//        TaskManager.getIntance().submitTask("delay;100;taskName;test;", meta, s);
    }

    public Object returnNull(){
        return false;
    }

    @UiThread(delay = 100)
    public Object t2( String s,MainActivity activity){
        Log.i("Test-Atom", "t2 is call");
        return 1;
    }

    @WorkThread(delay = 100)
    public MainActivity t3(String test, MainActivity activity, int q){
        Log.i("Test-Atom", "test3 is call");
        return null;
    }

    @WorkThread(delay = 100, taskName = "test")
    public Object test2(String test, MainActivity activity, int q){
        //......
        return new Object();
    }

//    public void test2n(String test, MainActivity activity, int q){
//        Log.i("Test-Atom", "test2 is call");
//        submit(new Runnable(){
//            @Override
//            public void run() {
//                //......
//                Eventbus.post
//            }
//        })
//    }

    @SupressCode("listload load")
    public boolean test3(String t){
        Log.i("Test-Atom", "test3 is call");
        return false;
//        return 1;
        /*
        boolean isProduct = ConfigManager.from(activity).isProduct();
        if (!isProduct) {
            Rocket.init(activity)
                  .inject(DeveloperConfig.class);
        }
         */
    }
}
