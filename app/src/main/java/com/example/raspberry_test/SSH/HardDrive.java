package com.example.raspberry_test.SSH;

import com.example.raspberry_test.data.User;

public class HardDrive {
    public static String hard(){
        int Tem=0;
        //User user=new User();
//        User.setIp("192.168.0.100");
//        User.setUserName("pi");
//        User.setPassword("pi2021");
        RemoteExecuteCommand rec = new RemoteExecuteCommand("192.168.0.100", "pi", "pi2021");//执行命令
        //  System.out.println(rec.execute("ls"));//执行脚本
        //rec.execute("ls");//这个方法与上面最大的区别就是，上面的方法，不管执行成功与否都返回，

        //  rec.executeSuccess("cat index.html");//这个方法，如果命令或者脚本执行错误将返回空字符串


        String s = "";

        s=rec.execute("sudo df -h /");


        return s;
    }
}
