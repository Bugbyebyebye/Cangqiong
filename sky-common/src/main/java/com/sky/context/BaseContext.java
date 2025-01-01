package com.sky.context;

//线程上下文
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //存储当前用户的ID
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    //获取当前用户的ID
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    //移除当前用户
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
