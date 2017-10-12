package com.meetyou.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.meiyou.atom.inject.MViewGroup;
import com.meiyou.atom.managers.LayoutInflaterManager;

/**
 * Created by Linhh on 2017/10/12.
 */
@MViewGroup(R.layout.activity_main)
public class TestView extends RelativeLayout{
    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(){}
}
