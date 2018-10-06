package com.safframework.rxcache4a.demo;

import android.app.Application;

import com.tencent.mmkv.MMKV;

/**
 * @Author Aaron
 * @Date 2018/10/4
 * @Email aaron@magicwindow.cn
 * @Description
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
    }
}
