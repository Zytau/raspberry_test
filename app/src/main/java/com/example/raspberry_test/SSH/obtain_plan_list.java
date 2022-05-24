package com.example.raspberry_test.SSH;

import com.example.raspberry_test.data.Plan_data;

import java.io.IOException;
import java.util.ArrayList;


/**
 * @描述：从树莓派中获取计划任务的列表
 * */
public class obtain_plan_list {
    public static void main(String[] args) {

//        plan_list() ;
    ArrayList<String> sList=plan_list();
        System.out.println("akjhsdkha:"+sList.size());

        for (String st :
                sList) {
            System.out.println("遍历结果："+st);
        }
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
        ArrayList<String> array_time=new ArrayList<>();
        ArrayList<String> array_conditon=new ArrayList<>();
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
            //获得时间端
            for (int j = 0; j <test_array_time.length ; j+=2) {
                sites_array_time.add(test_array_time[j]);
            }
            //状态
            for (int j = 5; j <test_array_condition.length ; j+=4) {
                sites_array_conditon.add(test_array_condition[j]);
            }
        }
        //时间段
        for (String st : sites_array_time) {
           // System.out.println("第一个"+st);
//            obtain_time(st);
          //  System.out.println(q);
            array_time.add(obtain_time(st).toString());
        }
        //状态
        for (String st : sites_array_conditon) {
//            System.out.println("第二个"+st);
            //obtain_condition(st);
            array_conditon.add(obtain_condition(st));
        }

        /**最后的集合*/
        for (int i = 0; i < array_time.size(); i++) {
            plan_array.add(array_time.get(i)+"---"+array_conditon.get(i));
        }
        System.out.println(array_conditon.size());
        System.out.println("获取sites_time个数"+sites_array_time.size());
        System.out.println("获取sites_condition个数"+sites_array_conditon.size());
        System.out.println(array_time.size());

 return plan_array;
    }

    /**
     * 每小时：看分钟          42 * * * *  ”每小时的42分钟执行“
     *
     * 每天：看时分           42 10 * * *  “每天的10:42执行”
     *
     * 每周：看时分 星期      42 10 * * 7  “每个星期天10:42执行”
     *
     * 每月：看时分 月份      42 10 01 * *  “每月的01号的10:42执行”
     * */


    public static String obtain_time(String s){
        String  str="";
        String test_time="";    //获取时间
        String  H="";           //小时
        String  M="";           //分钟
        String  m="";           //分钟
        String W="";            //星期
        ArrayList<String> arrayList_text_time=new ArrayList<>();
        for (String retval: s.split(" ")){
           // System.out.println(retval);
            str+=retval;
        }
        int size = str.indexOf("*");
        String substr = str.substring(str.length()-1);
       // System.out.println(str+"第"+size+"_"+"最后一个是"+substr);
        System.out.println(str);
        H=str.substring(2,4);
        M=str.substring(0,2);
        if(size==2){
            test_time="每小时"+"的"+M+"分钟";
        }else if(size==4&&substr.equals("*")){
            test_time="每天的"+H+":"+M;
        }else if (size==6&&substr.equals("*")){
            m=str.substring(4,6);
            test_time="每月的"+m+" "+H+":"+M;
        }else if (substr!="*"){
            W=week(str.substring(6));
            test_time="每周的"+W+" "+H+":"+M;
        }
        arrayList_text_time.add(test_time);
       //System.out.println(test_time);
return test_time;
    }

    private static String week(String substring) {
        String W="";
        switch (substring){
            case "1":
                W="星期一";break;
            case "2":
                W="星期二";break;
            case "3":
                W="星期三";break;
            case "4":
                W="星期四";break;
            case "5":
                W="星期五";break;
            case "6":
                W="星期六";break;
            case "7":
                W="星期七";break;
        }
        return W;
    }
    public  static String obtain_condition(String st) {
        String test_condition="";    //获取时间
//        System.out.println("获取的值："+st);
        switch (st){
            case "MotorPositive ":
                test_condition="电机正转-ON";
            break;
            case "MotorOff ":
                test_condition="电机-OFF";
                break;
            case "MotorReverse ":
                test_condition="电机反转-ON";
                break;
            case "bumpOn ":
                test_condition="水泵-ON";
                break;
            case "bumpOff ":
                    test_condition="水泵-OFF";
                break;
        }
//        System.out.println("测试"+test_condition);
        return test_condition;
    }
}