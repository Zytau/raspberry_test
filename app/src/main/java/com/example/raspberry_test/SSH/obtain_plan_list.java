package com.example.raspberry_test.SSH;

import java.io.IOException;
import java.util.ArrayList;

public class obtain_plan_list {
    public static void main(String[] args) {
        boolean B=false;

   /*   String s=  Exec.ssh("10.0.0.25","pi"," crontab -l");

      ArrayList<String> sites=new ArrayList<>();

      String [] the=s.split("\\r?\\n");
      for (String string:the){
         // System.out.println(string);
          sites.add(string);
      }
      for (String i:sites){
          System.out.println(i);
      }
        System.out.println(sites.size());

*/

        plan_list();
    }




    public static ArrayList<String> plan_list() {

        String s = Exec.ssh("10.0.0.25", "pi", " crontab -l");

        ArrayList<String> sites = new ArrayList<>();

        String[] the = s.split("\\r?\\n");
        for (String string : the) {
            // System.out.println(string);
            sites.add(string);
        }
      //  System.out.println(sites.size());

        return sites;
    }
}