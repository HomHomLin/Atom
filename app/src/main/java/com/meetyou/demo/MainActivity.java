package com.meetyou.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.meiyou.atom.Test;
import com.meiyou.atom.inject.ForEachCode;
import com.meiyou.atom.inject.MActivity;
import com.meiyou.atom.inject.MTodo;
import com.meiyou.atom.inject.SupressCode;
import com.meiyou.atom.inject.UiThread;
import com.meiyou.atom.inject.WorkThread;


@MActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static final String DEBUG = "debug";
    public static final String YUNQI = "yunqi";

    @ForEachCode
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.activity_main).setOnClickListener(new View.OnClickListener() {
            @ForEachCode
            @Override
            public void onClick(View v) {
                t1(MainActivity.this,"sssss", 111);
            }
        });
        Test.init();
        Object o = test2("test",this,1);
        t3("test",this,1);
        t2("test",this);
        test3("test3");
    }

    @ForEachCode("METHOD_TEST")
    public void t1(MainActivity activity, String s, int q){
        //
        test3("ttte");
        int i = 0;
        if(getI() == 0) {
            test2("tttt", this, 11);
        }
        t2("ssss",this);
    }

    public int getI(){
        return 1;
    }


    @MTodo(expried = false, msg = "请修复这个地方，谢谢")
    public Object returnNull(){
        return false;
    }

    @MTodo(expried = false, msg = "这个警告可以")
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
