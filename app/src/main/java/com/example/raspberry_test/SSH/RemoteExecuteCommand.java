package com.example.raspberry_test.SSH;


import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

//需要外置导包
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
/**
 * @类名称: SSH
 * @时间: 2022年4月25日15:16:23
 * @描述: TODO 类描述
 *  API说明：
 * 1.首先构造一个连接器，传入一个需要登陆的ip地址
 * Connection conn = new Connection(hostname);
 * 2.模拟登陆目的服务器 传入用户名和密码 ，
 * boolean isAuthenticated = conn.authenticateWithPassword(username, password);它会返回一个布尔值，true 代表成功登陆目的服务器，否则登陆失败
 * 3.打开一个session，有点象Hibernate的session ，执行你需要的linux 脚本命令 。
 * Session sess = conn.openSession();
 * sess.execCommand("last");
 * 4.接收目标服务器上的控制台返回结果，读取br中的内容
 * InputStream stdout = new StreamGobbler(sess.getStdout());
 * BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
 * 5.得到脚本运行成功与否的标志 ：0－成功 非0－失败
 * System.out.println("ExitCode: " + sess.getExitStatus());
 * 6.关闭session和connection
 *  sess.close();
 *  conn.close();
 **/

/**
 * 远程执行linux的shell script
 * @author Ickes
 * @since V0.1
 */
public class RemoteExecuteCommand {
    //字符编码默认是utf-8
    private static String DEFAULTCHART = "UTF-8";
    private Connection conn;
    private String ip;
    private String userName;
    private String userPwd;

    public RemoteExecuteCommand(String ip, String userName, String userPwd) {
        this.ip = ip;
        this.userName = userName;
        this.userPwd = userPwd;
    }


    public RemoteExecuteCommand() {

    }

    /**
     * 远程登录linux的主机
     *
     * @return 登录成功返回true，否则返回false
     * @author Ickes
     * @since V0.1
     */
    public Boolean login() {
        boolean flg = false;
        try {
            conn = new Connection(ip);  //构造一个连接器，传入一个需要登录的ip地址
            conn.connect();//连接
            flg = conn.authenticateWithPassword(userName, userPwd);//认证 它返回一个布尔值
            if (flg ) {
                System.out.println("登录成功");
            }else{
                System.out.println("验证失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flg;
    }

    /**
     * @param cmd 即将执行的命令
     * @return 命令执行完后返回的结果值
     * @author Ickes
     * 远程执行shll脚本或者命令
     * @since V0.1
     */
    public String execute(String cmd) {
        String result = "";
        try {
            if (login()) {

                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);//如果为得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isBlank(result)) {  //判断是否输入的命令是否为空 StringUtils.isBlank(result)
                   // System.out.println("输入的脚本或者命令有误！");
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String execute01(String cmd) {
        String result = "";
        try {
            if (login()) {

                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);//如果为得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isBlank(result)) {  //判断是否输入的命令是否为空 StringUtils.isBlank(result)
                    // System.out.println("输入的脚本或者命令有误！");
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * @param cmd 即将执行的命令
     * @return 命令执行成功后返回的结果值，如果命令执行失败，返回空字符串，不是null
     * @author Ickes
     * 远程执行shll脚本或者命令
     * @since V0.1
     */
    public String executeSuccess(String cmd) {
        String result = "";
        try {
            if (login()) {
                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                System.out.println(session.getExitStatus());
                System.out.println("执行结束");
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     * @author Ickes
     * @since V0.1
     */
    private String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in); //接受目标服务器上的控制台返回的结果传入到BufferedReader，最后通过读取BufferedReader对象中的内容
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {  //readLine()一次性从缓冲区中将内容全部读取进来
                buffer.append(line + "\n");  //Reader –>String  将缓冲区中的数据转换成String类型
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static void setCharset(String charset) {
        DEFAULTCHART = charset;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}