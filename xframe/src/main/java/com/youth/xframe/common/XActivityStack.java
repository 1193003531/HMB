package com.youth.xframe.common;

import android.app.Activity;
import android.content.Context;

import com.youth.xframe.utils.XFileUtils;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;


/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
final public class XActivityStack {
    private static final XActivityStack instance = new XActivityStack();

    private Set<Activity> allActivities = new HashSet<>();
    private WeakReference<Activity> currentAtivity;


    private XActivityStack() {
    }

    public static XActivityStack getInstance() {
        return instance;
    }

    /**
     * 设置当前运行的Activity。
     */
    public void setCurrentAtivity(Activity currentAtivity) {
        this.currentAtivity = new WeakReference<>(currentAtivity);
    }

    /**
     * 获取当前运行的Activity,有可能返回null
     */
    public Activity getCurrentAtivity() {
        return currentAtivity.get();
    }

    /**
     * 添加Activity到管理
     */
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    /**
     * 从管理器移除Activity，一般在Ondestroy移除，防止强引用内存泄漏
     */
    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }


    /**
     * 关闭所有Activity
     */
    public void finishiAll() {
        try {
            if (allActivities != null) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 关闭所有Activity 除了对应的activity
     */
    public void finishiAllExcept(Activity activity) {
        try {
            if (allActivities != null) {
                for (Activity act : allActivities) {
                    if (act != activity) {
                        act.finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 应用程序退出
     */
    public void appExit() {
        try {
            finishiAll();
            //退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(0);
            //从操作系统中结束掉当前程序的进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            System.exit(-1);
        }
    }


}