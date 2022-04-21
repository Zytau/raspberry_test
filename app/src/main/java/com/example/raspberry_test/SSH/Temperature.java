package com.example.raspberry_test.SSH;

import android.content.Intent;

import com.example.raspberry_test.data.User;

public class Temperature {

    public static String tem(){
        int Tem=0;
     /*   User user=new User();
        user.setIp("192.168.0.100");
        user.setUserName("pi");
        user.setPassword("pi2021");*/
//        RemoteExecuteCommand rec = new RemoteExecuteCommand("192.168.0.100", "pi", "pi2021");//执行命令
        //  System.out.println(rec.execute("ls"));//执行脚本
        //rec.execute("ls");//这个方法与上面最大的区别就是，上面的方法，不管执行成功与否都返回，
        //  rec.executeSuccess("cat index.html");//这个方法，如果命令或者脚本执行错误将返回空字符串


        String s = "";

//        s=rec.execute("vcgencmd measure_temp | head -1");

       s= Exec.ssh("10.0.0.25","pi","vcgencmd measure_temp | head -1");

        System.out.println(s);

        return s;
    }

    public static void main(String[] args) {

    tem();

    }
}
