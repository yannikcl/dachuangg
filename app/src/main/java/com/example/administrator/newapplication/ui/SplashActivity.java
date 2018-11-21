package com.example.administrator.newapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.utils.ShareUtils;
import com.example.administrator.newapplication.utils.StaticClass;

//闪屏页
public class SplashActivity extends AppCompatActivity {


    /**
     * 1.延时2000毫秒
     * 2.判断程序是否第一次运行
     * 3.自定义字体
     * 4.Activity全屏主题
     */


    //延时
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否是第一次运行
                    if (ifFirst()){
                        startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                    }else {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }
    //初始化
    private void initView() {
        handler.sendEmptyMessageAtTime(StaticClass.HANDLER_SPLASH,2000);

        //初始化字体
        //Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/FONT.TTF");
        //tv_splash.setTypeface(typeface);
    }

    private boolean ifFirst() {
        boolean isFirst = ShareUtils.getBoolean(this,StaticClass.SHARE_IS_FIRST,true);
        if (isFirst){
            ShareUtils.putBoolean(this,StaticClass.SHARE_IS_FIRST,false);
            //是第一次运行
            return true;
        }else {
            return false;
        }
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }
}
