package com.example.raspberry_test.SSH;

public class Attribute {
    private String ip;
    private String password;
    private String Name;

    public Attribute() {
    }

    public Attribute(String ip, String password, String name) {
        this.ip = ip;
        this.password = password;
        Name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
