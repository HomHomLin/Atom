package com.meiyou.atom.managers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.meiyou.atom.Atom;
import com.meiyou.atom.converts.LayoutInflaterConvert;

/**
 * Created by Linhh on 2017/10/12.
 */

public class LayoutInflaterManager {
    static class Holder{
        static LayoutInflaterManager layoutInflaterManager = new LayoutInflaterManager();
    }

    public static LayoutInflaterManager getIntance(){
        return Holder.layoutInflaterManager;
    }

    /**
     */
    public LayoutInflater getLayoutInflater(Context context){
        LayoutInflaterConvert layoutInflaterConvert = Atom.getAtom().getLayoutInflaterConvert();
        if(layoutInflaterConvert != null){
            return layoutInflaterConvert.getLayoutInflater(context);
        }else{
            return LayoutInflater.from(context);
        }
    }
}
