package com.example.raspberry_test.SSH;

import java.io.IOException;

public class Test01 {
    public static void main(String[] args) {
        boolean B=false;


    }

    public Boolean bool(String ip, String userName, String userPwd){
        boolean B=false;
        RemoteExecuteCommand remoteExecuteCommand = new RemoteExecuteCommand(ip,userName,userPwd);

        B = remoteExecuteCommand.login();
        System.out.println(B);
        return B;

    }
}