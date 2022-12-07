package com.example.day_28app;

public class Memberinfo {

    private String name;
    private int userNameCheck;

    public Memberinfo(String name , int userNameCheck){
        this.name =name;
        this.userNameCheck = userNameCheck;
    }

    public String getName(){
        return this.name;

    }

    public void setName(String name){
        this.name = name;
    }

    public int getUserNameCheck(){
        return this.userNameCheck;

    }

    public void setUserNameCheck(int userNameCheck){
        this.userNameCheck = userNameCheck;
    }
}
