package com.meiyou.atom;

import android.text.TextUtils;

/**
 * Created by Linhh on 2017/10/11.
 */

public class DescProcessor {
    public static AtomTaskNode excuteAtomTask(String desc){
        if(TextUtils.isEmpty(desc)){
            return null;
        }
        AtomTaskNode node = new AtomTaskNode();
        String[] strings = desc.split(";");
        for(int i = 0; i < strings.length; i ++){
            String key = strings[i];
            if(TextUtils.isEmpty(key)){
                continue;
            }
            if(key.trim().equals("delay")){
                node.delay = Long.valueOf(strings[i + 1]);
                i = i + 1;
                continue;
            }else if(key.trim().equals("taskName")){
                node.taskName = strings[i + 1];
                i = i + 1;
                continue;
            }else if(key.trim().equals("mode")){
                node.mode = strings[i + 1];
                i = i + 1;
                continue;
            }
        }
        return node;
    }
}
