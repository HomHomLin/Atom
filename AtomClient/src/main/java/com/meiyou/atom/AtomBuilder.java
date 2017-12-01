package com.meiyou.atom;

import com.meiyou.atom.converts.LayoutInflaterConvert;
import com.meiyou.atom.converts.SupressCodeConvert;
import com.meiyou.atom.converts.TaskConvert;
import com.meiyou.atom.converts.UIThreadConvert;

/**
 * Created by Linhh on 2017/10/11.
 */

public final class AtomBuilder {
    public TaskConvert taskConvert;
    public UIThreadConvert uiThreadConvert;
    public LayoutInflaterConvert layoutInflaterConvert;
    public SupressCodeConvert supressCodeConvert;

    public AtomBuilder setTaskConvert(TaskConvert taskConver){
        this.taskConvert = taskConver;
        return this;
    }

    public AtomBuilder setUIThreadConvert(UIThreadConvert uiThreadConvert){
        this.uiThreadConvert = uiThreadConvert;
        return this;
    }

    public AtomBuilder setLayoutInflaterConvert(LayoutInflaterConvert layoutInflaterConvert){
        this.layoutInflaterConvert = layoutInflaterConvert;
        return this;
    }

    public AtomBuilder setSupressCodeConvert(SupressCodeConvert supressCodeConvert){
        this.supressCodeConvert = supressCodeConvert;
        return this;
    }
}
