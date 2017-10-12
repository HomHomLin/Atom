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
}
