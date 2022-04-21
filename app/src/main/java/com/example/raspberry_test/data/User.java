package com.example.raspberry_test.data;

public class User {
    //树莓派ip
    private static String ip;
    //树莓派密码
    private  static String password;
    //树莓派名字
    private static String username;

    //获取计划的时间
    private static String plan_time;
    private static  String plan_time_year;  //年
    private static String plan_time_moon;   //月
    private static String plan_time_day;    //日
    private static String plan_time_hour;   //小时
    private static String plan_time_minute; //分钟
    private static String plan_time_week;   //星期

    //重复-状态
    private static String plan_condition;
    private static String plan_repeat;
    private static String radio;


    private static int plan_repeat_num;  //重复编号
    private static int plan_conditon_num;//状态编号

    public static int getPlan_repeat_num() {
        return plan_repeat_num;
    }

    public static void setPlan_repeat_num(int plan_repeat_num) {
        User.plan_repeat_num = plan_repeat_num;
    }

    public static int getPlan_conditon_num() {
        return plan_conditon_num;
    }

    public static void setPlan_conditon_num(int plan_conditon_num) {
        User.plan_conditon_num = plan_conditon_num;
    }

    public static int getRadio_num() {
        return radio_num;
    }

    public static void setRadio_num(int radio_num) {
        User.radio_num = radio_num;
    }

    private static int radio_num;        //硬件编号（单选）

    public static String getRadio() {
        return radio;
    }

    public static void setRadio(String radio) {
        User.radio = radio;
    }

    public static String getplan_condition() {
        return plan_condition;
    }

    public static void setplan_condition(String plan_condition) {
        User.plan_condition = plan_condition;
    }

    public static String getplan_repeat() {
        return plan_repeat;
    }

    public static void setplan_repeat(String plan_repeat) {
        User.plan_repeat = plan_repeat;
    }

    public static String getPlan_dian_repeat() {
        return plan_dian_repeat;
    }

    public static void setPlan_dian_repeat(String plan_dian_repeat) {
        User.plan_dian_repeat = plan_dian_repeat;
    }

    public static String getPlan_water_repeat() {
        return plan_water_repeat;
    }

    public static void setPlan_water_repeat(String plan_water_repeat) {
        User.plan_water_repeat = plan_water_repeat;
    }

    //重复
    private static String plan_dian_repeat;
    private static String plan_water_repeat;






    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        User.ip = ip;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static String getPlan_time() {
        return plan_time;
    }

    public static void setPlan_time(String plan_time) {
        User.plan_time = plan_time;
    }

    public static String getPlan_time_year() {
        return plan_time_year;
    }

    public static void setPlan_time_year(String plan_time_year) {
        User.plan_time_year = plan_time_year;
    }

    public static String getPlan_time_moon() {
        return plan_time_moon;
    }

    public static void setPlan_time_moon(String plan_time_moon) {
        User.plan_time_moon = plan_time_moon;
    }

    public static String getPlan_time_day() {
        return plan_time_day;
    }

    public static void setPlan_time_day(String plan_time_day) {
        User.plan_time_day = plan_time_day;
    }

    public static String getPlan_time_hour() {
        return plan_time_hour;
    }

    public static void setPlan_time_hour(String plan_time_hour) {
        User.plan_time_hour = plan_time_hour;
    }

    public static String getPlan_time_minute() {
        return plan_time_minute;
    }

    public static void setPlan_time_minute(String plan_time_minute) {
        User.plan_time_minute = plan_time_minute;
    }


    public static String getPlan_time_week() {
        return plan_time_week;
    }

    public static void setPlan_time_week(String plan_time_week) {
        User.plan_time_week = plan_time_week;
    }
}
