package com.example.administrator.newapplication.application;

import android.app.Application;

import com.example.administrator.newapplication.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APPID, true);
        //初始化Bomb
        Bmob.initialize(this, StaticClass.BOMB_APPID);
    }

}
