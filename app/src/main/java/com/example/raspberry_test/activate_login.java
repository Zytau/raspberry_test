package com.example.raspberry_test;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.raspberry_test.SSH.obtain_plan_list;
import com.example.raspberry_test.data.User;

public class activate_login extends AppCompatActivity {
    private Button loge;
    private Button btn1;
    private TextView mwendu;
    private Switch T_open,F_open;
    private EditText ip,port,password;


    public final static int RESULT_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_login);
        initView();




    }








    //初始化
    private void initView() {
        loge=findViewById(R.id.loge);
        ip=findViewById(R.id.ip);
        port=findViewById(R.id.port);
        password=findViewById(R.id.password);
    }



    /**
     * 登录验证
     * */
    public void Loge(View v) {
        String Ip="";
        String Port="";
        String Password="";
        Ip=ip.getText().toString();
        Port=port.getText().toString();
        Password=password.getText().toString();




    /*    Boolean B=t.bool(Ip,Port,Password);
        RemoteExecuteCommand remoteExecuteCommand = new RemoteExecuteCommand("10.0.0.25","pi","^K5F9L2!#^Gb%ekJ9&2xtE");

        B = remoteExecuteCommand.login();
        Log.e("tgp","登录"+B);*/

//
//        if(B){
//            Toast.makeText(activate_login.this, "验证成功", Toast.LENGTH_LONG).show();
//            //验证成功跳转回MainActivity
//            // Intent intent = new Intent(this,MainActivity.class);
//            Intent intent=new Intent(activate_login.this,MainActivity.class);
//            startActivity(intent);
//            //activate_login.this.setResult(RESULT_OK,intent);
//            //*结束本Activity*//*
//            activate_login.this.finish();
//
//        }else{
//            Toast.makeText(activate_login.this,"输入有误",Toast.LENGTH_SHORT).show();
//
//        }
        User user=new User();
        user.setIp(ip.getText().toString());
        user.setPassword(password.getText().toString());


        if(Ip.equals("")||Port.equals("")||Password.equals("")){
            Toast.makeText(activate_login.this,"输入有误",Toast.LENGTH_SHORT).show();
        }else if(Ip.equals("10.0.0.25")&&Port.equals("22")&&Password.equals("^K5F9L2!#^Gb%ekJ9&2xtE")){
            Toast.makeText(activate_login.this, "验证成功", Toast.LENGTH_LONG).show();
            //验证成功跳转回MainActivity
           // Intent intent = new Intent(this,MainActivity.class);
            Intent intent=new Intent(activate_login.this,MainActivity.class);
            startActivity(intent);
            //activate_login.this.setResult(RESULT_OK,intent);
            //结束本Activity
            activate_login.this.finish();

        }else{Toast.makeText(activate_login.this, "验证有误", Toast.LENGTH_LONG).show();
        }



    }









    /**
     * 按键一键清空EditText内的内容
     * */
    public void Clear(View v) {
        ip.setText("");
        port.setText("");
        password.setText("");
        Toast.makeText(activate_login.this, "清空", Toast.LENGTH_LONG).show();

    }

    /**
     * 自动获取ip，并直接赋值到ip的EditText
     * */
    public void Auto(View v){

        //缺少扫描ip的方法

        //ip.setText("10.0.0.25");
        ip.setText("10.0.0.25");
        port.setText("22");
        password.setText("^K5F9L2!#^Gb%ekJ9&2xtE");
        //Toast.makeText(activate_login.this, "添加成功", Toast.LENGTH_LONG).show();

        User.setIp(ip.getText().toString());
        User.setPassword(password.getText().toString());
        User.setUsername("pi");


        //user.setPassword("^K5F9L2!#^Gb%ekJ9&2xtE");
        Log.e("tag","添加成功");

    }



}