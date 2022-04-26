package com.example.raspberry_test.SSH;

import com.example.raspberry_test.data.User;

/**
 * @描述：获取实时同步树莓派中的时间
 * */

public class Time {

    public static String time(){
        String s = "";
      s=  Exec.ssh("10.0.0.25","pi","cat /proc/uptime| awk -F. '{run_days=$1 / 86400;run_hour=($1 % 86400)/3600;run_minute=($1 % 3600)/60;run_second=$1 % 60;printf(\"%d天%d时%d分%d秒\",run_days,run_hour,run_minute,run_second)}'");
        System.out.println("asd"+s);
        return s;
    }

    public static void main(String[] args) {
        time();

    }
}
