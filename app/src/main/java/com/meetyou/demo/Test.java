package com.meetyou.demo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.meiyou.atom.Atom;
import com.meiyou.atom.AtomBuilder;
import com.meiyou.atom.AtomMethod;
import com.meiyou.atom.AtomTaskNode;
import com.meiyou.atom.converts.LayoutInflaterConvert;
import com.meiyou.atom.converts.SupressCodeConvert;
import com.meiyou.atom.converts.TaskConvert;
import com.meiyou.atom.converts.UIThreadConvert;

/**
 * Created by Linhh on 2017/10/11.
 */

public class Test {
    public static void init(){
        Atom.init(new AtomBuilder().setTaskConvert(new TaskConvert() {
            @Override
            public Object submitTask(AtomTaskNode node, final AtomMethod runnable, Class returnType) {
                Log.i("Test-Atom", "Atom is called " + node.taskName + ";" + returnType);
               //submit (runnable.run())

//                submit(new Runnable(){
//                    @Override
//                    public void run() {
//                        //......
//                        eventbus.runnable.run();
//                    }
//                })
                long time = System.currentTimeMillis();
                Object o = runnable.run();
                Log.d("atom cost time", "" + (System.currentTimeMillis() - time));
                return o;
//                return null;
            }
        }).setLayoutInflaterConvert(new LayoutInflaterConvert() {
            @Override
            public LayoutInflater getLayoutInflater(Context context) {
                return LayoutInflater.from(context);
            }
        }).setSupressCodeConvert(new SupressCodeConvert() {
            @Override
            public Object submitTask(String info, AtomMethod runnable, Class returnType) {
//                if(info.equals("Rocket init 4 Product"))
//                {
//                    //
//                    runnable.run();
//                    //
////                    return;
//                }else{
//                    ///
//                }
                Log.i("Test-Atom", "Atom supresscode is called : " + info + returnType);
//                Object object = runnable.run();
                return runnable.run();
            }
        }).setUIThreadConvert(new UIThreadConvert() {
            @Override
            public Object submitTask(AtomTaskNode node, AtomMethod runnable, Class returnType) {
                Log.i("Test-Atom", "Atom ui is called by ui" + node.taskName + returnType);
                return runnable.run();
            }
        }));
    }
}
