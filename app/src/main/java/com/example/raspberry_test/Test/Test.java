package com.example.raspberry_test.Test;

import com.example.raspberry_test.SSH.Exec;
import com.example.raspberry_test.data.User;

/**
 * @类名称: Test
 * @时间: 2021/7/26 21:58
 * @描述: TODO 类描述
 **/
public class Test {
    public static void main(String[] args) {
       /* User user=new User();
        user.setIp("192.168.0.100");
        user.setUserName("pi");
        user.setPassword("pi2021");*/
        //RemoteExecuteCommand rec = new RemoteExecuteCommand("10.0.0.25", "pi", "^K5F9L2!#^Gb%ekJ9&2xtE");//执行命令
      //  System.out.println(rec.execute("ls"));//执行脚本
        //rec.execute("ls");//这个方法与上面最大的区别就是，上面的方法，不管执行成功与否都返回，

   //  rec.executeSuccess("cat index.html");//这个方法，如果命令或者脚本执行错误将返回空字符串


       // String s = new String();

       // s=rec.execute("ls");
       // System.out.println(s.toString());
   /* while (true){
        Scanner scanner = new Scanner(System.in);

        List<String> list = new ArrayList<>();
        System.out.println("请输入命令:");
        String s = scanner.next();
        list.add(rec.execute(s));
        System.out.println(list);

        //rec.executeSuccess(s);  //返回执行指令的成效   但还会在执行一次命令

    }*/

        String s = Exec.ssh("10.0.0.25", "pi", " crontab -l");
        System.out.println(s);
    }
}