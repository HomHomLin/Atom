package com.meiyou.atom;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public class TimeWatchUtils {


    public static final String TAG_TIME_WATCH = "TimeWatch";


    private static boolean isDebug = true;

    private int logMethodLevel = 0;

    /**
     * 程序全局开始时间
     */
    private long global_start_time = 0;

    private Map<String, Long> methodExecuteTimeMap = new HashMap<>();


    private static class TimeWatchUtilsHodler {

        private static final TimeWatchUtils INSTANCE = new TimeWatchUtils();
    }


    public static TimeWatchUtils getInstance() {
        return TimeWatchUtils.TimeWatchUtilsHodler.INSTANCE;
    }


    public int getLogMethodLevel() {
        return logMethodLevel;
    }

    /**
     * 程序入口调用该方法（一般在Application.onCreate）
     */
    public void globalBeginInit() {

        isDebug = true;

        if (isDebug) {
            logMethodLevel = 0;
            global_start_time = System.currentTimeMillis();
//            methodBegin("TimeWatchUtils", "global_start_time");
        }

    }

    public void globalInstrument(String className, String methodName) {
        if (isDebug) {
//            methodEnd("TimeWatchUtils", "global_start_time");
            String key = className + "." + methodName;
            Log.e(TAG_TIME_WATCH, "★★★★★★★★★★GlobalInstrument execute this point time --> " + key + " : " + (System.currentTimeMillis() - global_start_time) + "★★★★★★★★");

        }
    }


    /**
     * 方法开始执行
     */
    public void methodBegin() {

        if (isDebug) {

            methodBegin(getClassName(), getMethodName(), true, true);

        }


    }


    /**
     * 方法开始执行
     *
     * @param className  类名
     * @param methodName 方法名
     */
    public void methodBegin(String className, String methodName) {

        if (isDebug) {

            methodBegin(className, methodName, false);

        }

    }

    /**
     * 方法开始执行
     *
     * @param className  类名
     * @param methodName 方法名
     */
    public void methodBegin(String className, String methodName, boolean isPlusLevel) {

        if (isDebug) {

            methodBegin(className, methodName, false, isPlusLevel);

        }

    }

    /**
     * 方法开始执行
     *
     * @param className  类名
     * @param methodName 方法名
     */
    public void methodBegin(String className, String methodName, boolean isMethodBegin, boolean isPlusLevel) {

        if (isDebug) {

            if (isPlusLevel) {
                logMethodLevel++;
            }

            if (className != null && methodName != null) {

                String key = className + "." + methodName;

                methodExecuteTimeMap.put(key, System.currentTimeMillis());

                if (isMethodBegin) {


                    StringBuilder msg = new StringBuilder();

                    if (logMethodLevel > 1) {
                        msg.append("|");
                    }

                    for (int i = 1; i < logMethodLevel; i++) {
                        msg.append("---");
                    }

                    msg.append(key + " begin");

                    Log.e(TAG_TIME_WATCH, msg.toString());


                }

            }

        }

    }

    /**
     * 方法执行结束
     */
    public void methodEnd() {

        if (isDebug) {

            methodEnd(getClassName(), getMethodName(), "", true);

        }

    }

    /**
     * 方法执行结束
     *
     * @param className
     * @param methodName
     */
    public void methodEnd(String className, String methodName) {

        methodEnd(className, methodName, "", false);

    }

    /**
     * 方法执行结束
     *
     * @param className
     * @param methodName
     */
    public void methodEnd(String className, String methodName, String extMsg) {

        methodEnd(className, methodName, extMsg, false);

    }


    /**
     * 方法执行结束
     *
     * @param className
     * @param methodName
     */
    public void methodEnd(String className, String methodName, String extMsg, boolean isPlusLevel) {

        if (isDebug) {

            if (className != null && methodName != null) {

                if (isPlusLevel) {
                    logMethodLevel--;
                }


                String key = className + "." + methodName;

                Long beginTime = methodExecuteTimeMap.get(key);
                if (beginTime != null) {

                    long exeTime = System.currentTimeMillis() - beginTime;
                    methodExecuteTimeMap.put(key, exeTime);


                    StringBuilder msg = new StringBuilder();

                    if (logMethodLevel > 0) {
                        msg.append("|");
                    }

                    for (int i = 0; i < logMethodLevel; i++) {
                        msg.append("---");
                    }

                    msg.append((TextUtils.isEmpty(className) ? "#" : "") + key + " execute time " + extMsg + " : " + exeTime);

                    if (exeTime >= 10) {
                        Log.e(TAG_TIME_WATCH, msg.toString());
                    } else {
                        Log.i(TAG_TIME_WATCH, msg.toString());
                    }

                    methodExecuteTimeMap.remove(key);

//                    Long begin = System.currentTimeMillis();
//                    Log.d(TAG_TIME_WATCH, getClassNameMethodNameAndLineNumber());
//                    Log.d(TAG_TIME_WATCH, "" + (System.currentTimeMillis() - begin));
                }

            }

        }

    }


    public float watchTimeLog(String methodName, long endTime) {
        return watchTimeLog(methodName, global_start_time, endTime);
    }

    public float watchTimeLog(String methodName, long startTime, long endTime) {
        Log.e(TAG_TIME_WATCH, methodName + " , starttime: " + startTime + ", endtime : " + endTime + ", allTime : " + (endTime - startTime) / 1000F);
        return (endTime - startTime) / 1000F;
    }

    private static final int STACK_TRACE_LEVELS_UP = 4;

    /**
     * Get the current line number. Note, this will only work as called from
     * this class as it has to go a predetermined number of steps up the stack
     * trace. In this case 5.
     *
     * @return int - Current line number.
     * @author kvarela
     */
    private static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getLineNumber();
    }

    /**
     * Get the current class name. Note, this will only work as called from this
     * class as it has to go a predetermined number of steps up the stack trace.
     * In this case 5.
     *
     * @return String - Current line number.
     * @author kvarela
     */
    private static String getClassName() {
        String fileName = Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getFileName();

        // kvarela: Removing ".java" and returning class name
        return fileName.substring(0, fileName.length() - 5);
    }

    /**
     * Get the current method name. Note, this will only work as called from
     * this class as it has to go a predetermined number of steps up the stack
     * trace. In this case 5.
     *
     * @return String - Current line number.
     * @author kvarela
     */
    private static String getMethodName() {
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getMethodName();
    }

    /**
     * Returns the class name, method name, and line number from the currently
     * executing log call in the form <class_name>.<method_name>()-<line_number>
     *
     * @return String - String representing class name, method name, and line
     * number.
     * @author kvarela
     */
    private static String getClassNameMethodNameAndLineNumber() {
        return "[" + getClassName() + "." + getMethodName() + "()-" + getLineNumber() + "]: ";
    }

}
