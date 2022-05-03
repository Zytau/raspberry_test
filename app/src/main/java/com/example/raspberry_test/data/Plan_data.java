package com.example.raspberry_test.data;


/**
 * @描述：树莓派中计划任务列表中的属性
 * */
public class Plan_data {
    //时间
    private static String plan_pi_time;
    //状态
    private static String plan_pi_condition;

    private static String [] plan_list;


    public static String getPlan_pi_time() {
        return plan_pi_time;
    }

    public static void setPlan_pi_time(String plan_pi_time) {
        Plan_data.plan_pi_time = plan_pi_time;
    }

    public static String[] getPlan_list() {
        return plan_list;
    }

    public static void setPlan_list(String[] plan_list) {
        Plan_data.plan_list = plan_list;
    }

    public static String getPlan_pi_condition() {
        return plan_pi_condition;
    }

    public static void setPlan_pi_condition(String plan_pi_condition) {
        Plan_data.plan_pi_condition = plan_pi_condition;
    }


}
