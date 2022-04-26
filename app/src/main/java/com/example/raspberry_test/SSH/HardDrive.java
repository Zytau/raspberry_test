package com.example.raspberry_test.SSH;

import com.example.raspberry_test.data.User;

public class HardDrive {
    public static String hard(){
        RemoteExecuteCommand rec = new RemoteExecuteCommand("192.168.0.100", "pi", "pi2021");//执行命令
        String s = "";
        s=rec.execute("sudo df -h /");


        return s;
    }
}
