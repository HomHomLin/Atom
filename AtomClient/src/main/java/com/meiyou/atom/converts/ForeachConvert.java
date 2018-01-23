package com.meiyou.atom.converts;

/**
 * Created by Linhh on 2017/12/19.
 */

public interface ForeachConvert {
    public void onForeachEnter(String annotations, String owner, String clazz, String method, String desc);
    public void onForeachEnd(String annotations, String owner,  String clazz, String method, String desc);
    public void onForeachPoll(String annotations, String owner,  String clazz, String method, String desc);
    public void onForeachPush(String annotations, String owner, String clazz, String method, String desc);
}
