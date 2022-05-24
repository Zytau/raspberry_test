package com.example.raspberry_test.SSH;

import android.content.Intent;

import com.example.raspberry_test.data.User;


/**
 * @描述：获得实时树莓派CPU的温度
 * */
public class Temperature {

    public static String tem(){

        String s = "";
        String Tem="";
       s= Exec.ssh("10.0.0.25","pi","vcgencmd measure_temp | head -1").substring(5);
        String the_splist [] =s.split("\\r?\\n");
        for (String S :
                the_splist) {
            Tem+=S;
        }
        return Tem;
    }
    public static void main(String[] args) {
        tem();
    }
}
