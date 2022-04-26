package com.example.raspberry_test.SSH;

import com.example.raspberry_test.data.Plan_data;

import java.io.IOException;
import java.util.ArrayList;


/**
 * @描述：从树莓派中获取计划任务的列表
 * */
public class obtain_plan_list {
    public static void main(String[] args) {

        plan_list();
    }
    public static ArrayList<String> plan_list() {
        //获取树莓派中的计划任务
        String s = Exec.ssh("10.0.0.25", "pi", " crontab -l");

        //计划任务列表集合
        ArrayList<String> sites = new ArrayList<>();
        //计划任务时间集合
        ArrayList<String> sites_array_time = new ArrayList<>();
        //计划任务状态集合
        ArrayList<String> sites_array_conditon = new ArrayList<>();

        //最后转义集合
        ArrayList<String> plan_array = new ArrayList<>();

        if(s==null){
            return null;
        }

        String[] the = s.split("\\r?\\n");      //切割Linux 换行
        for (String string : the) {
           //  System.out.println(string);
            sites.add(string);                        //sites集合中树莓派计划任务的列表    12 16 * * 7 /home/pi/Code/switch/MotorPositive


        }

        //对计划任务集合遍历
        for (int i = 0; i <sites.size() ; i++) {
           // System.out.println(sites.get(i));        //遍历打印计划任务列表

            //获取计划集合的一条进行拆分
            String test=sites.get(i);
            String [] test_array_time=test.split(" "+"/|\\\\\\\\");  //拆分 空格键
            String [] test_array_condition=test.split("/|\\\\\\\\");  //拆分 Linux路径


            //遍历 拆解“空格键”的集合
            /*
             12 16 * * 7
             home/pi/Code/switch/MotorPositive
            * */
            for (String st : test_array_time) {
                System.out.println(st);
                sites_array_time.add(st);
            }


            //遍历拆解“Linux路径”的集合
            /*
            * 12 16 * * 7
            * home
            * pi
            * Code
            * switch
            * MotorPositive
             * */
            for (String st : test_array_condition) {
//                System.out.println(st);
                sites_array_conditon.add(st);
            }

        }


        ArrayList<String> time=new ArrayList<>();
        ArrayList<String> condition=new ArrayList<>();
        

        //获取时间列表
        for (int i = 0; i < sites_array_time.size(); i+=2) {
//            System.out.println("获得时间:"+sites_array_time.get(i));

            String str=sites_array_time.get(i);
            String [] list_time=str.split(" ");

            for (String S  :list_time
                    ) {
                System.out.println("拆分 ："+S);
                time.add(S);
            }


        }
        for (String time_str :
                time) {
            System.out.println(time_str);
        }

        System.out.println("拆分的时间："+time.size());

        //

        for (int i = 1; i < sites_array_conditon.size(); i+=2) {
            System.out.println("获得路径:"+sites_array_time.get(i));
        }

//        System.out.println(sites_array_time.size());
//        System.out.println(sites_array_time.get(1));




        /**              54 * * * * /home/pi/Code/switch/MotorPositive 4
         * 计划任务格式： 分 时 日 月 周
         * */

        for (int i = 0; i <sites_array_time.size() ; i++) {
        if(sites_array_time.get(0)!="*"){
            if (sites_array_time.get(1).equals("*")||sites_array_time.get(2).equals("*")||sites_array_time.get(3).equals("*")||sites_array_time.get(4).equals("*")) {
                Plan_data.setPlan_pi_time("每小时的" + sites_array_time.get(0) + "分钟");
//                System.out.println("每小时的" + sites_array_time.get(0) + "分钟");
            }
        }
        if(sites_array_time.get(0)!="*"&&sites_array_time.get(1)!="*"&&sites_array_time.get(2)=="*"&&sites_array_time.get(3)=="*"&&sites_array_time.get(4)=="*"){
            Plan_data.setPlan_pi_time("每天的"+sites_array_time.get(1)+"："+sites_array_time.get(0));
            System.out.println("每天的"+sites_array_time.get(1)+"："+sites_array_time.get(0));
        }
        if(sites_array_time.get(0)!="*"&&sites_array_time.get(1)!="*"&&sites_array_time.get(2)=="*"&&sites_array_time.get(3)=="*"&&sites_array_time.get(4)=="*"){
            Plan_data.setPlan_pi_time("每天的"+sites_array_time.get(1)+"："+sites_array_time.get(0));
            System.out.println("每天的"+sites_array_time.get(1)+"："+sites_array_time.get(0));
        }
        if(sites_array_time.get(0)!="*"&&sites_array_time.get(1)!="*"&&sites_array_time.get(2)=="*"&&sites_array_time.get(3)=="*"&&sites_array_time.get(4)!="*"){

            Plan_data.setPlan_pi_time("每周的星期"+sites_array_time.get(4)+" "+sites_array_time.get(1)+"："+sites_array_time.get(0));
            System.out.println("每周的星期"+sites_array_time.get(4)+" "+sites_array_time.get(1)+"："+sites_array_time.get(0));
        }
        if(sites_array_time.get(0)!="*"&&sites_array_time.get(1)!="*"&&sites_array_time.get(2)!="*"&&sites_array_time.get(3)=="*"&&sites_array_time.get(4)=="*"){
            Plan_data.setPlan_pi_time("每月的"+sites_array_time.get(2)+"号 "+sites_array_time.get(1)+"："+sites_array_time.get(0));
            System.out.println("每月的"+sites_array_time.get(2)+"号 "+sites_array_time.get(1)+"："+sites_array_time.get(0));
        }
        }

        switch (sites_array_conditon.get(5)){
            case "bumpOn" :
                Plan_data.setPlan_pi_condition("水泵——开启");
                System.out.println("水泵——开启");
                break;
            case "bumOff":
                Plan_data.setPlan_pi_condition("水泵——关闭");
                System.out.println("水泵——关闭");
                break;
            case "MotorPositive":
                Plan_data.setPlan_pi_condition("电机正转——开启");
                System.out.println("电机正转——开启");
                break;
            case "MotorReverse":
                Plan_data.setPlan_pi_condition("电机反转——开启");
                System.out.println("电机反转——开启");
                break;
            case "MotorOff":
                Plan_data.setPlan_pi_condition("电机——关闭");
                System.out.println("电机——关闭");
                break;
        }

        plan_array.add(Plan_data.getPlan_pi_time()+Plan_data.getPlan_pi_condition());





        System.out.println(sites.size());



 /* *//*
       System.out.println(sites.get(0));*//*
        ArrayList<String> sites_array_time = new ArrayList<>();
        ArrayList<String> sites_array_conditon = new ArrayList<>();
        ArrayList<String> plan_array = new ArrayList<>();
        for (int j = 0; j < sites.size(); j++) {


       String test=sites.get(0);

       String [] test_array_time=test.split(" ");

        String [] test_array_condition=test.split("/|\\\\\\\\");




        for (String st :
                test_array_time) {
            System.out.println(st);
            sites_array_time.add(st);
        }
        System.out.println("---------------");
        for (String st :
                test_array_condition) {
            System.out.println(st);
            sites_array_conditon.add(st);
        }
        System.out.println("获得时间"+sites_array_time.get(0));
     //   System.out.println("获得状态"+sites_array_time.get(0));

        */
 return sites;
    }



}