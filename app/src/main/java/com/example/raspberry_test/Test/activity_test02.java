package com.example.raspberry_test.Test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.raspberry_test.R;
import com.example.raspberry_test.SSH.Exec;
import com.example.raspberry_test.SSH.RemoteExecuteCommand;

public class activity_test02 extends AppCompatActivity {

    Handler mainHandler,workHandler;
    HandlerThread mHandlerThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);
//      Button button=findViewById(R.id.Test_link02);

      //创建于主线程关联
        mainHandler=new Handler();

        //步骤一：创建HandlerTread实例对象
        //传入参数=线程名字，作用=标记该线程

        mHandlerThread =new HandlerThread("handlerThread");

        //步骤二：启动线程
        mHandlerThread.start();

        /*
        * 步骤三：创建工作线程Handler &复写handleMessage（）
        * 作用：关联HandlerTread的Looper对象，实现消息处理操作&与其他线程进行通讯
        * 注：消息处理（HandlerMessage（））的执行线程=mHandlerThread所创建的工作线程中执行
        *
        * */

        workHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            // 消息处理的操作
            public void handleMessage(Message msg)
            {
                //设置了两种消息处理操作,通过msg来进行识别
                switch(msg.what){
                    // 消息1
                    case 1:
                        try {
                            //延时操作
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 通过主线程Handler.post方法进行在主线程的UI更新操作
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run () {
                               Exec exec=new Exec();
                               SSH("192.168.0.100","pi","pi2021","./Wopen.sh");
                              // exec.ssh("192.168.0.100","pi","./Wopen.sh");
                                Log.e("tag","点击了线程");

                            }
                        });
                        mHandlerThread.quit();
                        break;

                    default:
                        break;
                }
            }
        };




        /*
        * 步骤四： 使用工作线程Handler向工作线程的消息队列发送信息
        * 在工作线程中，当消息循环时取出对应消息&在工作线程执行相关操作
        * */

       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //通过sendMessage（）发送
                //a.定义要发送的消息
                Message msg=Message.obtain();
                msg.what=1; //消息的标识
                msg.obj="A";//消息的存放
                //b.通过Handler发送消息到其绑定的消息队列
                workHandler.sendMessage(msg);
             }
        });*/



    }

    public void  SSH(String ip, String name, String password, String order) {
        RemoteExecuteCommand remoteExecuteCommand = new RemoteExecuteCommand(ip, name, password);
        // List<String> S = new ArrayList<>();
        // S.add(remoteExecuteCommand.execute("./test.sh"));
        remoteExecuteCommand.execute(order);

    }

    public void Test_lin02(View view){}
}