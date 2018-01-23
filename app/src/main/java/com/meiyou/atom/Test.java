package com.meiyou.atom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.meiyou.atom.converts.ForeachConvert;
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
        }).setForeachConvert(new ForeachConvert() {
            @Override
            public void onForeachEnter(String annotations,String owner,  String clazz, String method, String desc) {
                String[] c = clazz.split("\\.");
                if(c.length > 0) {
                    clazz = c[c.length - 1];
                }
                String[] o = owner.split("\\.");
                if(o.length > 0) {
                    owner = o[o.length - 1];
                }
                String[] d = desc.split(";");
                String d0 = d[0];
                String[] ddot = d0.split(",");
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < ddot.length; i++){
                    stringBuilder.append(getSimpleName(ddot[i]));
                    if(i + 1 < ddot.length){
                        stringBuilder.append(",");
                    }
                }

                Log.w("onforeach", "AtomLog_W==>"  + clazz + "    " + owner + "." + method + "("+ stringBuilder.toString() + ")");
                TimeWatchUtils.getInstance().methodBegin("AtomLog_W_" + clazz, method);
            }

            @Override
            public void onForeachEnd(String annotations,String owner,  String clazz, String method, String desc) {
                String[] c = clazz.split("\\.");
                if(c.length > 0) {
                    clazz = c[c.length - 1];
                }
                String[] o = owner.split("\\.");
                if(o.length > 0) {
                    owner = o[o.length - 1];
                }
                String[] d = desc.split(";");
                String d0 = d[0];
                String[] ddot = d0.split(",");
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < ddot.length; i++){
                    stringBuilder.append(getSimpleName(ddot[i]));
                    if(i + 1 < ddot.length){
                        stringBuilder.append(",");
                    }
                }

                Log.w("onforeach", "AtomLog_W<=="  + clazz + "    " + owner + "." + method + "("+ stringBuilder.toString() + ")");
                TimeWatchUtils.getInstance().methodEnd("AtomLog_W_" + clazz, method);

            }

            @Override
            public void onForeachPoll(String annotations,String owner,  String clazz, String method, String desc) {
                String[] c = clazz.split("\\.");
                if(c.length > 0) {
                    clazz = c[c.length - 1];
                }
                String[] o = owner.split("\\.");
                if(o.length > 0) {
                    owner = o[o.length - 1];
                }
                String[] d = desc.split(";");
                String d0 = d[0];
                String[] ddot = d0.split(",");
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < ddot.length; i++){
                    stringBuilder.append(getSimpleName(ddot[i]));
                    if(i + 1 < ddot.length){
                        stringBuilder.append(",");
                    }
                }

                Log.e("onforeach", "AtomLog_E==>"  + clazz + "    " + method);
                TimeWatchUtils.getInstance().methodBegin("AtomLog_E_" + clazz, method);


                Log.d("onforeach", "AtomLog_D==>"  + clazz + "    " + owner + "." + method + "("+ stringBuilder.toString() + ")");
            }

            @Override
            public void onForeachPush(String annotations, String owner, String clazz, String method, String desc) {
                String[] c = clazz.split("\\.");
                if(c.length > 0) {
                    clazz = c[c.length - 1];
                }
                String[] o = owner.split("\\.");
                if(o.length > 0) {
                    owner = o[o.length - 1];
                }
                String[] d = desc.split(";");
                String d0 = d[0];
                String[] ddot = d0.split(",");
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < ddot.length; i++){
                    stringBuilder.append(getSimpleName(ddot[i]));
                    if(i + 1 < ddot.length){
                        stringBuilder.append(",");
                    }
                }

                Log.e("onforeach", "AtomLog_E<=="  + clazz + "    " + method);
                TimeWatchUtils.getInstance().methodEnd("AtomLog_E_" + clazz, method);


                Log.d("onforeach", "AtomLog_D<=="  + clazz + "    " + owner + "." + method + "("+ stringBuilder.toString() + ")");
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

    public static String getSimpleName(String clazz){
        String[] c = clazz.split("\\.");
        if(c.length > 0) {
            clazz = c[c.length - 1];
        }
        return clazz;
    }
}
