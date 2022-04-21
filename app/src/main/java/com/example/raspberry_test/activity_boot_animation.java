package com.example.raspberry_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class activity_boot_animation extends AppCompatActivity {

    public activity_boot_animation() {
    }

    ProgressBar progressBar;
    int i = 0;
    int progressBarMax = 0;
    /* 创建Handler对象*/
 /*   Handler handler = new Handler() {
        @Override
        public void publish(LogRecord record) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在setContentView(R.layout.activity_boot_animation);之前将状态栏和标题栏隐藏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_boot_animation);

        //新建一个子线程
        Thread thread =new Thread(){
            @Override
            public void run() {
                try{
                    sleep(5000);//程序休眠四秒后启动MainActivity
                    Intent intent=new Intent(getApplicationContext(),activate_login.class);
                    startActivity(intent);
                    finish();//关闭当前活动，否则按返回键还能回到启动画面
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();//启动线程


        /**
         * 进度条提示线程部分
         *
         * */
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        /* 获取最大值*/
        progressBarMax = progressBar.getMax();
        /* 匿名内部类启动实现效果的线程*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (i++ < progressBarMax) {
                    //设置滚动条当前状态值
                    progressBar.setProgress(i);
                    try {
                        //使用暂停当前线程（调节进度条的速度）
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }






}