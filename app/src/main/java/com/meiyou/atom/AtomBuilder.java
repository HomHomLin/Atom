package com.meiyou.atom;

import com.meiyou.atom.converts.TaskConvert;

/**
 * Created by Linhh on 2017/10/11.
 */

public final class AtomBuilder {
    public TaskConvert taskConvert;

    public AtomBuilder setTaskConver(TaskConvert taskConver){
        this.taskConvert = taskConver;
        return this;
    }
}
