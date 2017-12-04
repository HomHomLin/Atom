package com.meiyou.atom;

import java.util.List;

/**
 * Created by Linhh on 2017/10/11.
 */

public class AtomClazzMaker {
    public static AtomClazzNode makeClazzNode(int type, List<Object> list){
        AtomClazzNode node = new AtomClazzNode();
        node.type = type;
        node.value = Integer.valueOf(list.get(1).toString());
        return node;
    }

    public static AtomTodoNode makeTodoClazzNode(int type, List<Object> list){
        AtomTodoNode node = new AtomTodoNode();
//        node.expried = Boolean.valueOf(list.get(2).toString());
        for(int i = 0; i < list.size(); i ++){
            if(list.get(i).equals("expried")){
                node.expried = Boolean.valueOf(list.get(i + 1).toString());
                i++;
            }else if(list.get(i).equals("msg")){
                node.msg = list.get(i + 1).toString();
                i++;
            }
        }

        return node;
    }
}
