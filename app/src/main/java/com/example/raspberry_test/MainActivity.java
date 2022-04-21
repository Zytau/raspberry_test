package com.example.raspberry_test;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.raspberry_test.SSH.Exec;
import com.example.raspberry_test.SSH.HardDrive;
import com.example.raspberry_test.SSH.RemoteExecuteCommand;
import com.example.raspberry_test.SSH.T;
import com.example.raspberry_test.SSH.Temperature;
import com.example.raspberry_test.SSH.Time;
import com.example.raspberry_test.data.User;
import com.example.raspberry_test.plan.DateTimePickDialogUtil;

import java.io.IOError;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Switch Water,T_Mopen,F_Mopen,Led;
    private Spinner mWaterSpinner;
    private Spinner mDianSpinner;
    private Spinner plan_spinner_repeat,plan_spinner_condition;
    private ImageView imageView_d,imageView_w,imageView_led;
    private TextView wendu,harddrive,OStime,ding_water_time,ding_dian_time,water_Gpio,dian_Gpio;
    private Button delet_button,cancel_button;
    private ImageButton imageButton;
    private EditText plan_getTime;
    private ListView plan_list;

    private int state;  //自定义 电机状态
    private int stop_time=0;  //倒计时 电机状态

   private String initStartDateTime;    //获取当前时间

    private ArrayList<String> data=new ArrayList<>();

    ArrayAdapter<String> adapter;

    //private ListView listview;
    private MyAdapter myAdapter;
    /**
     * 列表的数据源
     */
    private List<String> listData;
    /**
     * 记录选中item的下标
     */
    private List<Integer> checkedIndexList;
    /**
     * 保存每个item中的checkbox
     */
    private List<CheckBox> checkBoxList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       //Log.e("tag","获得运行时间"+Time.time());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initListData();
        initView();

        //实时响应树莓派温度  5分钟刷新一次
        //Startthread();
        SimpleDateFormat sdf=new SimpleDateFormat();
        //sdf.applyPattern("yyyy年MM月dd日HH时mm分ss秒");
        //2012年07月02日 16:45
        sdf.applyPattern("yyyy年MM月dd日 HH:mm");
        Date date=new Date();
        initStartDateTime=sdf.format(date);
        // initStartDateTime="2012年07月02日 16:45";

        //获取周几
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        String currSun = dateFm.format(date);
        sumday(currSun);


/*
        2022年4月18日23:26:08

         int TIME = 1000;  //每隔1s执行一次.

        Handler handlerTime=new Handler();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                // 此处执行任务 此处即使界面返回也会一直后台运行
                Log.i("djtest", "run: 该条打印信息仅测试锁屏情况下是否会执行task内容:"+Temperature.tem());
                wendu.setText(Temperature.tem());
                // 每5s重复一次
                handlerTime.postDelayed(this, 5 * 1000);//延迟5秒,再次执行task本身,实现了5s一次的循环效果
            }
        };
        handlerTime.postDelayed(task, 1000);
*/

        /**
         * 下拉框定义
         * */
        //获取下拉数据
        String[] mwater = getResources().getStringArray(R.array.water);
        //建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter_w=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mwater);
        adapter_w.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到对应的控件
        mWaterSpinner.setAdapter(adapter_w);


        String[] mdian = getResources().getStringArray(R.array.dian);
        ArrayAdapter<String> adapter_d=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mdian);
        adapter_d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDianSpinner.setAdapter(adapter_d);

        String[] spinner_repeat = getResources().getStringArray(R.array.plan_repeat);
        ArrayAdapter<String> adapter_repeat=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinner_repeat);
        adapter_repeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan_spinner_repeat.setAdapter(adapter_repeat);

        String[] spinner_condition = getResources().getStringArray(R.array.plan_condition);
        ArrayAdapter<String> adapter_condition=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinner_condition);
        adapter_condition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan_spinner_condition.setAdapter(adapter_condition);






        /**
         * 水泵定量下拉框
         * */
        mWaterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                imageView_w.setImageResource(R.drawable.water);
                AnimationDrawable animationDrawable_water=(AnimationDrawable) imageView_w.getDrawable();
                String[] languages = getResources().getStringArray(R.array.water);
                Toast.makeText(MainActivity.this, "你水泵定量选择:"+languages[pos], 100).show();

                switch (pos){
                    case 0:break;
                    

                    //150ml
                    case 1:
                        mDianSpinner.getParent();
                        SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time_water.sh "+1);
                        CountDownTimer timer=new CountDownTimer(1500,100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                animationDrawable_water.start();
                                ding_water_time.setText(millisUntilFinished/100.0+"s");
                            }

                            @Override
                            public void onFinish() {
                                ding_water_time.setText("无");
                                animationDrawable_water.stop();
                            }
                        };
                        timer.start();


                        //300ml
                    case 2:break;
                    //1000ml
                    case 3:break;
                    //水泵自定义
                    case 4:
                        AlertDialog.Builder customizeDialog_water=new AlertDialog.Builder(MainActivity.this);
                        final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.diy_water,null);
                        customizeDialog_water.setTitle("水泵自定义");
                        customizeDialog_water.setView(dialogView);
                        EditText editText_water=dialogView.findViewById(R.id.diy_edit_water);

                        imageView_w.setImageResource(R.drawable.water);
                        AnimationDrawable animationDrawable_diy_water=(AnimationDrawable) imageView_w.getDrawable();

                        customizeDialog_water.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str_water=editText_water.getText().toString();
                                SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time_water.sh "+str_water);
                                CountDownTimer timer_zheng=new CountDownTimer((long) (Float.parseFloat(str_water)*1000),1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        animationDrawable_diy_water.start();
                                        ding_water_time.setText(millisUntilFinished/1000+"ml");
                                    }

                                    @Override
                                    public void onFinish() {
                                        ding_water_time.setText("无");
                                        animationDrawable_diy_water.stop();
                                        Water.setEnabled(true);
                                    }
                                };
                                timer_zheng.start();
                                Water.setEnabled(false);
                            }
                        });
                        customizeDialog_water.show();
                        break;


                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        /**
         * 电机定量下拉框
         * */
        mDianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                imageView_d.setImageResource(R.drawable.dian_1);
                AnimationDrawable animationDrawable_dian=(AnimationDrawable) imageView_d.getDrawable();
                String[] languages = getResources().getStringArray(R.array.dian);
                Toast.makeText(MainActivity.this, "你电机点击的是:"+languages[pos], 100).show();

                switch(pos){

                    //初始  无
                    case 0:{ //Toast.makeText(MainActivity.this, "你电机点击的是:"+languages[pos], 100).show();
                    Log.e("tag","选择了"+languages[pos]);
                    break;
                    }
                    //正转： 5s
                    case 1:{ Toast.makeText(MainActivity.this, "你电机点击的是:"+languages[pos], 100).show();
                        imageButton.setVisibility(View.VISIBLE);
                        mDianSpinner.getParent();
                        SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time.sh "+0.5);
                        CountDownTimer timer=new CountDownTimer(30000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                animationDrawable_dian.start();
                                ding_dian_time.setText(millisUntilFinished/1000+"s");
                            }

                            @Override
                            public void onFinish() {
                                ding_dian_time.setText("无");
                                animationDrawable_dian.stop();
                                T_Mopen.setEnabled(true);
                                F_Mopen.setEnabled(true);
                            }
                        };
                        timer.start();

                        T_Mopen.setEnabled(false);
                        F_Mopen.setEnabled(false);

                    Log.e("tag","选择了5s");

                    break;
                         }

                         //反转：5秒
                    case 2:{ Toast.makeText(MainActivity.this, "你电机点击的是:"+languages[pos], 100).show();
                        imageButton.setVisibility(View.VISIBLE);

                        mDianSpinner.getParent();
                        imageView_d.setImageResource(R.drawable.dian_0);
                        AnimationDrawable animationDrawable_dian_fan=(AnimationDrawable) imageView_d.getDrawable();
                        SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time_fan.sh "+0.5);
                        CountDownTimer timer=new CountDownTimer(30000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                animationDrawable_dian_fan.start();
                                ding_dian_time.setText(millisUntilFinished/1000+"s");
                            }

                            @Override
                            public void onFinish() {
                                ding_dian_time.setText("无");
                                animationDrawable_dian_fan.stop();
                                T_Mopen.setEnabled(true);
                                F_Mopen.setEnabled(true);
                            }
                        };
                        timer.start();
                        T_Mopen.setEnabled(false);
                        F_Mopen.setEnabled(false);
                        Log.e("tag","选择了5s");
                        break;


                    }
                    //自定义
                    case 3:{
                        imageView_d.setImageResource(R.drawable.dian_1);
                        AnimationDrawable animationDrawable_dian_diy_zheng=(AnimationDrawable) imageView_d.getDrawable();

                        AlertDialog.Builder customizeDialog=new AlertDialog.Builder(MainActivity.this);
                        final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.diy_dian,null);
                            customizeDialog.setTitle("自定义");
                            customizeDialog.setView(dialogView);

                        RadioGroup radioGroup=(RadioGroup) dialogView.findViewById(R.id.radioGroup);
                        EditText editText=(EditText)dialogView.findViewById(R.id.diy_edit_dian);
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                Log.e("tag",""+checkedId);
                                switch (checkedId){
                                    case  R.id.btnZheng:
                                        Log.e("tag","选择了正");
                                        state=0;
                                        break;
                                    case R.id.btnFan:
                                        Log.e("tag","选择了反");
                                        state=1;
                                        break;
                                }
                            }
                        });


                        customizeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e("tag","输入框内容"+editText.getText());
                                    imageButton.setVisibility(View.VISIBLE);
                                    switch (state){
                                        case 0:
                                            String str_zheng=editText.getText().toString();
                                            SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time.sh "+str_zheng);
                                            CountDownTimer timer_zheng=new CountDownTimer((long) (Float.parseFloat(str_zheng)*60000),1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    animationDrawable_dian_diy_zheng.start();
                                                    ding_dian_time.setText(millisUntilFinished/1000+"s");
                                                }

                                                @Override
                                                public void onFinish() {
                                                    ding_dian_time.setText("无");
                                                    animationDrawable_dian_diy_zheng.stop();
                                                    T_Mopen.setEnabled(true);
                                                    F_Mopen.setEnabled(true);
                                                }
                                            };
                                            timer_zheng.start();
                                            T_Mopen.setEnabled(false);
                                            F_Mopen.setEnabled(false);
                                            if(timer_zheng!=null)
                                            if(stop_time==1){
                                                timer_zheng.cancel();

                                                ding_dian_time.setText("无");
                                                stop_time=0;

                                            }
                                            break;

                                        case 1:
                                            String str_fan=editText.getText().toString();
                                            imageView_d.setImageResource(R.drawable.dian_0);
                                            AnimationDrawable animationDrawable_dian_fan=(AnimationDrawable) imageView_d.getDrawable();
                                            SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time_fan.sh "+str_fan);
                                            CountDownTimer timer_fan=new CountDownTimer((long) (Float.parseFloat(str_fan) *60000),1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    animationDrawable_dian_fan.start();
                                                    ding_dian_time.setText(millisUntilFinished/1000+"s");
                                                }
                                                @Override
                                                public void onFinish() {
                                                    ding_dian_time.setText("无");
                                                    animationDrawable_dian_fan.stop();
                                                    T_Mopen.setEnabled(true);
                                                    F_Mopen.setEnabled(true);
                                                }
                                            };
                                            timer_fan.start();
                                            T_Mopen.setEnabled(false);
                                            F_Mopen.setEnabled(false);
/*
                                            if(timer_fan!=null)
                                                if(stop_time==1){
                                                    timer_fan.cancel();

                                                    ding_dian_time.setText("无");
                                                    stop_time=0;

                                                }*/

                                            break;

                                    }



                                }
                            });
                        customizeDialog.show();
                        break;
                    }



                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        /**
         * 计划下拉框   --->重复
         * */
        plan_spinner_repeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] languages=getResources().getStringArray(R.array.plan_repeat);
                Log.i("tag","计划——重复选择了"+languages[position]);

                User.setplan_repeat(languages[position]);
                /*//获取周几
                SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
                String currSun = dateFm.format(date);
                sumday(currSun);*/

                //获取选择的时间 （年-月-日-时-分）
                String time=plan_getTime.getText().toString();
                String Year=time.substring(0,4);
                String H=time.substring(12,14);     //小时
                String M=time.substring(15,17);     //分钟
                String Moon=time.substring(5,7);     //月份
                String Day=time.substring(8,10);     //日

                User.setPlan_time_year(Year);
                User.setPlan_time_moon(Moon);       //传值：月份
                User.setPlan_time_day(Day);         //传值：日
                User.setPlan_time_hour(H);          //传值：小时
                User.setPlan_time_minute(M);        //传值：分钟
                //User.setPlan_time_week();    //传值：周几




                switch (position){
                        case 0:
                            Log.e("tag","重复：*");
                        break;
                    /**
                     * 每小时 ---  小时-分钟-（开关）
                     * */
                        case 1:
                            Log.e("tag","选择每天-获取时分"+"编号："+position+H+":"+M);
                            User.setPlan_time_hour(H);          //传值：小时
                            User.setPlan_time_minute(M);        //传值：分钟
                            User.setPlan_repeat_num(position);



                            /**
                             * 每天 ---  小时-分钟-（开关）
                             * */
                        case 2:

                           // User.setplan_repeat(languages[position]);
                            Log.e("tag","选择每天-获取时分，"+"编号："+position+H+":"+M);
                            User.setPlan_time_hour(H);          //传值：小时
                            User.setPlan_time_minute(M);        //传值：分钟
                            User.setPlan_repeat_num(position);

                        break;

                        /**
                         * 重复：每周   传值：周几-小时-分钟
                         * */
                        case 3:
                            Log.e("tag","选择每周-获取时分-周几"+"编号："+position+H+":"+M+""+currSun);
                            User.setPlan_time_hour(H);          //传值：小时
                            User.setPlan_time_minute(M);        //传值：分钟
                           // User.setPlan_time_week(currSun);    //传值：周几
                            User.setPlan_repeat_num(position);
                            break;


                    /**
                     * 重复：每月   传值：月-日-小时-分钟
                     * */
                        case 4:
                            Log.e("tag","选择每月-获取时分-周几"+"编号："+position+H+":"+M+"-"+currSun+"-月"+Moon+"-日"+Day);
                            User.setPlan_time_hour(H);          //传值：小时
                            User.setPlan_time_minute(M);        //传值：分钟
                            User.setPlan_time_moon(Moon);       //传值：月份
                            User.setPlan_time_day(Day);         //传值：日
                            User.setPlan_repeat_num(position);
                        break;



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * 计划下拉框   --->状态
         * */
        plan_spinner_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] languages=getResources().getStringArray(R.array.plan_condition);
                Log.i("tag","计划——状态选择了"+languages[position]);

                User.setplan_condition(languages[position]);

                switch (position){
                    case 0:
                        User.setPlan_conditon_num(position);
                        break;
                    case 1:
                        User.setPlan_conditon_num(position);
                        break;

                        case 2:
                        User.setPlan_conditon_num(position);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    //LED灯
       /* Led.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Toast.makeText(MainActivity.this, "打开led", Toast.LENGTH_SHORT).show();
                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Led.sh");

                }else{
                    Toast.makeText(MainActivity.this, "关闭led", Toast.LENGTH_SHORT).show();
                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Led_stop.sh");

                }
            }
        });*/



        // Switch Water=(Switch) findViewById(R.id.s);//水泵开关


//        Water.setEnabled(openBoolean);

        /**
         * 开关设置
         * */

        Water.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageView_w.setImageResource(R.drawable.water);
                AnimationDrawable animationDrawable_water=(AnimationDrawable) imageView_w.getDrawable();
                if(isChecked){
                    Toast.makeText(MainActivity.this, "打开水泵", Toast.LENGTH_SHORT).show();
//                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Wopen.sh");

                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "/home/pi/Code/on.sh");
                    Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/on.sh");
                    animationDrawable_water.start();

                    //Gpio状态
                    water_Gpio.setText("1");

                }else{
                    Toast.makeText(MainActivity.this, "关闭水泵", Toast.LENGTH_SHORT).show();
                    //SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Wstop.sh");
//                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "/home/pi/Code/off.sh");
                    Exec.ssh(User.getIp(), User.getUsername(),"/home/pi/Code/off.sh");
                    animationDrawable_water.stop();

                    //Gpio状态
                    water_Gpio.setText("0");
                }
            }
        });

        boolean T=true;
       // Switch T_Mopen=(Switch) findViewById(R.id.dian_01);//电机——正转
        //Switch F_Mopen=(Switch) findViewById(R.id.dian_00);//电机——反转

        //开始统一被控制无法使用，需要登录验证之后进行
        /*T_Mopen.setEnabled(openBoolean);
        F_Mopen.setEnabled(openBoolean);*/




        T_Mopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("tag","状态"+isChecked);
                imageView_d.setImageResource(R.drawable.dian_1);
                AnimationDrawable animationDrawable_dian=(AnimationDrawable) imageView_d.getDrawable();
                if(T_Mopen.isChecked()&&!F_Mopen.isChecked()){
                    F_Mopen.setEnabled(false);  //单一开启控制
                    Toast.makeText(MainActivity.this, "电机正转开始", Toast.LENGTH_SHORT).show();
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./T_Mopen.sh");
                    animationDrawable_dian.start();

                    //Gpio状态
                    dian_Gpio.setText("1");
                }else{
                    Toast.makeText(MainActivity.this, "电机正转结束", Toast.LENGTH_SHORT).show();
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./T_Mstop.sh");
                    animationDrawable_dian.stop();
                    F_Mopen.setEnabled(true);

                    //Gpio状态
                    dian_Gpio.setText("0");

                }
            }
        });


        F_Mopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageView_d.setImageResource(R.drawable.dian_0);
                AnimationDrawable animationDrawable_dian=(AnimationDrawable) imageView_d.getDrawable();
                if(!T_Mopen.isChecked()&&F_Mopen.isChecked()){
                    T_Mopen.setEnabled(false);
                    Toast.makeText(MainActivity.this, "电机反转开始", Toast.LENGTH_SHORT).show();
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./F_Mopen.sh");
                    animationDrawable_dian.start();

                    //Gpio状态
                    dian_Gpio.setText("1");
                }else{
                    T_Mopen.setEnabled(true);
                    Toast.makeText(MainActivity.this, "电机反转结束", Toast.LENGTH_SHORT).show();
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./F_Mstop.sh");
                    animationDrawable_dian.stop();

                    //Gpio状态
                    dian_Gpio.setText("0");
                }
            }
        });







        //获取时间：
        plan_getTime.setText(initStartDateTime);
        plan_getTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        MainActivity.this, initStartDateTime);
                dateTimePicKDialog.dateTimePicKDialog(plan_getTime);
                Log.v("tag","选择的时间是"+plan_getTime+"：："+plan_getTime.getText().toString());
                String str=plan_getTime.getText().toString().substring(5);
              Log.e("tag","拆分时间："+str);
                User.setPlan_time(str);
            }
        });



        //单选按钮
        RadioGroup radgroup = (RadioGroup) findViewById(R.id.radioGroup);
        //第一种获得单选按钮值的方法
        //为radioGroup设置一个监听器:setOnCheckedChanged()
        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                Toast.makeText(getApplicationContext(), "按钮组值发生改变,你选了" + radbtn.getText(), Toast.LENGTH_LONG).show();
                User.setRadio(radbtn.getText().toString());

                String str=radbtn.getText().toString();
                switch (str){
                    case "无":
                        User.setRadio_num(0);
                        break;
                    case "水泵":
                        User.setRadio_num(1);
                        break;
                    case "电机正转":
                        User.setRadio_num(2);
                        break;
                    case "电机反转":
                        User.setRadio_num(3);
                        break;
                }
            }
        });



    }

    //控件初始化
    private void initView() {
      //  btn1 = findViewById(R.id.bt_login);

        Water=findViewById(R.id.s);
        T_Mopen=findViewById(R.id.dian_01);
        F_Mopen=findViewById(R.id.dian_00);
        imageView_d=findViewById(R.id.dian);
        imageView_w=findViewById(R.id.water);
        mWaterSpinner=findViewById(R.id.spin_s);
        mDianSpinner=findViewById(R.id.spin_d);
        wendu=findViewById(R.id.wendu);
      //  harddrive=findViewById(R.id.harddrive);
        OStime=findViewById(R.id.OStime);
       // Led=findViewById(R.id.Led);
       // imageView_led=findViewById(R.id.imageView_led);
       // t=findViewById(R.id.but);
        ding_water_time=findViewById(R.id.ding_water_time);
        ding_dian_time=findViewById(R.id.ding_dian_time);
        imageButton=findViewById(R.id.dian_cancel);
        delet_button=findViewById(R.id.delet_Button);
        cancel_button = findViewById(R.id.cancel_Button);

        //Gpio状态显示
        water_Gpio=findViewById(R.id.water_Gpio);
        dian_Gpio=findViewById(R.id.dian_Gpio);


        //计划任务
        plan_getTime=findViewById(R.id.plan_getTime);
        plan_spinner_repeat=findViewById(R.id.plan_spinner_repeat);
        plan_spinner_condition=findViewById(R.id.plan_spinner_condition);
        plan_list=findViewById(R.id.plan_list);

        //列表  删除-取消按钮
        delet_button.setVisibility(View.GONE);
        cancel_button.setVisibility(View.GONE);


        myAdapter = new MyAdapter(getApplicationContext(), listData);
        plan_list.setAdapter(myAdapter);
        //监听listview的长按事件
        plan_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                //将checkbox设置为可见
                for (int i = 0; i < checkBoxList.size(); i++) {
                    checkBoxList.get(i).setVisibility(View.VISIBLE);
                }

                delet_button.setVisibility(View.VISIBLE);
                cancel_button.setVisibility(View.VISIBLE);
                return false;
            }
        });

        checkedIndexList = new ArrayList<Integer>();
        checkBoxList = new ArrayList<CheckBox>();
    }
    /**
     与树莓派连接调用封装方法
     * */
    private void  SSH(String ip, String name, String password, String order) {
        RemoteExecuteCommand remoteExecuteCommand = new RemoteExecuteCommand(ip, name, password);
        // List<String> S = new ArrayList<>();
        // S.add(remoteExecuteCommand.execute("./test.sh"));
        remoteExecuteCommand.execute01(order);

    }

/*    //跳转到登录界面
    public void insert(View v) {
                Intent intent = new Intent(this,activate_login.class);
                startActivity(intent);
    }*/







    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            Handler mHandler = new Handler();
            mHandler.postDelayed(runnable, 10000);
            Message message = new Message( );
            message.what = 1;
            mHandler.sendMessage(message);
        }
    };


    private void Startthread(){
        new Thread(){
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                        Message message=new Message();
                        message.what=1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (true);
            }
        }.start();
    }

    //在主线程中进行数据处理
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                   String Tem=Temperature.tem();
                   Log.e("tag",Tem);
                 //  String Hard= HardDrive.hard();
                   String Ost= Time.time();

                    wendu.setText(Tem.toString());
//                    harddrive.setText(Hard);
                    OStime.setText(Ost.toString());
                   // SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time.sh");
                    break;
                case 2:
//                    SSH(User.getIp(), User.getUsername(), User.getPassword(), "./Time.sh");
                    break;



            }
        }
    };




    public void disconnect(View view) {
        Intent intent=new Intent(MainActivity.this,activate_login.class);
        startActivity(intent);
        //activate_login.this.setResult(RESULT_OK,intent);
        //结束本Activity
        MainActivity.this.finish();
    }


    public void dian_cancel_off(View view){
        imageView_d.setImageResource(R.drawable.dian_1);
        AnimationDrawable animationDrawable_dian=(AnimationDrawable) imageView_d.getDrawable();
        animationDrawable_dian.stop();
        F_Mopen.setEnabled(true);
        T_Mopen.setEnabled(true);
        ding_dian_time.setText("无");
        stop_time=1;
        SSH(User.getIp(), User.getUsername(), User.getPassword(), "./T_Mstop.sh");
        SSH(User.getIp(), User.getUsername(), User.getPassword(), "./F_Mstop.sh");
    }

    /**
     * 计划任务
     * */
    public void plan(View view) {

        Log.e("tag","计划任务"+"时间:"+User.getPlan_time()+"选择了："+User.getRadio()+"重复："+User.getplan_repeat()+"状态："+User.getplan_condition());
        String str="计划任务："+User.getPlan_time()+","+User.getRadio()+","+User.getplan_repeat()+","+User.getplan_condition();
       // adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,data);


        if(User.getPlan_time()!=null&&User.getRadio()!=null){

             // data.add(User.getRadio()+"->"+" ："+User.getplan_repeat()+" ："+User.getplan_condition()+" : "+User.getPlan_time());
                listData.add(str);

        }else{
            Log.e("tag","添加失败");

        }
       //
      //  adapter.notifyDataSetChanged();
        myAdapter.notifyDataSetChanged();
        plan_list.setAdapter(myAdapter);

        Log.e("tag","计划任务添加："+"单选编号："+User.getRadio_num()+",重复编号"+User.getPlan_repeat_num()+",状态编号"+User.getPlan_conditon_num());
        Log.e("tag",User.getPlan_time_year()+"-"+User.getPlan_time_moon()+"-"+User.getPlan_time_day()+"-"+User.getPlan_time_hour()+":"+User.getPlan_time_minute()+"-"+User.getPlan_time_week()+" "+User.getRadio_num()+" "+User.getPlan_repeat_num()+" "+User.getPlan_conditon_num());

    }

    /**
     * 初始化列表的数据源
     */
    public void initListData() {
        //静态赋值
        listData = new ArrayList<String>();
        for (int i = 0; i < 1; i++) {
            listData.add("item" + i);
        }
    }
    /**
     * 自定义listview的适配器
     */
    class MyAdapter extends BaseAdapter {
        private List<String> listData;
        private LayoutInflater inflater;

        public MyAdapter(Context context, List<String> listData) {
            this.listData = listData;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int arg0) {
            return listData.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.textview);
                viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                //将item中的checkbox放到checkBoxList中
                checkBoxList.add(viewHolder.checkbox);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv.setText(listData.get(position));
            viewHolder.checkbox.setOnCheckedChangeListener(new CheckBoxListener(position));
            return convertView;
        }
        class ViewHolder {
            TextView tv;
            CheckBox checkbox;
        }
    }

    /**
     * checkbox的监听器
     */
    class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        /**
         * 列表item的下标位置
         */
        int position;

        public CheckBoxListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
            if (isChecked) {
                checkedIndexList.add(position);
            } else {
                checkedIndexList.remove((Integer) position);
            }
        }
    }


    /**
     * 删除按钮的点击事件
     */
    public void click_deleteButton(View v) {
        //先将checkedIndexList中的元素从大到小排列,否则可能会出现错位删除或下标溢出的错误
        checkedIndexList = sortCheckedIndexList(checkedIndexList);
        for (int i = 0; i < checkedIndexList.size(); i++) {
            //需要强转为int,才会删除对应下标的数据,否则默认删除与括号中对象相同的数据
            listData.remove((int) checkedIndexList.get(i));
            checkBoxList.remove(checkedIndexList.get(i));
        }
        for (int i = 0; i < checkBoxList.size(); i++) {
            //将已选的设置成未选状态
            checkBoxList.get(i).setChecked(false);
            //将checkbox设置为不可见
            checkBoxList.get(i).setVisibility(View.INVISIBLE);
        }
        //更新数据源
        myAdapter.notifyDataSetChanged();
        //清空checkedIndexList,避免影响下一次删除
        checkedIndexList.clear();

        delet_button.setVisibility(View.GONE);
        cancel_button.setVisibility(View.GONE);
    }

    /**
     * 取消按钮的点击事件
     */
    public void click_cancelButton(View v) {
        for (int i = 0; i < checkBoxList.size(); i++) {
            //将已选的设置成未选状态
            checkBoxList.get(i).setChecked(false);
            //将checkbox设置为不可见
            checkBoxList.get(i).setVisibility(View.INVISIBLE);
            delet_button.setVisibility(View.GONE);
            cancel_button.setVisibility(View.GONE);
        }
    }

    /**
     * 对checkedIndexList中的数据进行从大到小排序
     */
    public List<Integer> sortCheckedIndexList(List<Integer> list) {
        int[] ass = new int[list.size()];//辅助数组
        for (int i = 0; i < list.size(); i++) {
            ass[i] = list.get(i);
        }
        Arrays.sort(ass);
        list.clear();
        for (int i = ass.length - 1; i >= 0; i--) {
            list.add(ass[i]);
        }
        return list;
    }



    public void  sumday(String  str){
        switch (str){
            case "星期一":User.setPlan_time_week("1");break;
            case "星期二":User.setPlan_time_week("2");break;
            case "星期三":User.setPlan_time_week("3");break;
            case "星期四":User.setPlan_time_week("4");break;
            case "星期五":User.setPlan_time_week("5");break;
            case "星期六":User.setPlan_time_week("6");break;
            case "星期日":User.setPlan_time_week("7");break;
        }

    }

    public String runtime(){
        String s="";
        Exec.ssh("10.0.0.25","pi","cat /proc/uptime| awk -F. '{run_days=$1 / 86400;run_hour=($1 % 86400)/3600;run_minute=($1 % 3600)/60;run_second=$1 % 60;printf(\"%d天%d时%d分%d秒\",run_days,run_hour,run_minute,run_second)}'");
        return s;
    }

}
