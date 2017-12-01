package com.meiyou.atom.converts;

import com.meiyou.atom.AtomMethod;
import com.meiyou.atom.AtomTaskNode;

/**
 * Created by Linhh on 2017/10/11.
 */

public interface TaskConvert {
    public Object submitTask(AtomTaskNode node, AtomMethod runnable, Class returnType);
}
