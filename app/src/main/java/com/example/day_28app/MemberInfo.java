package com.example.day_28app;

public class MemberInfo {

    private String name;
    private int userNameCheck;

    public MemberInfo(String name , int userNameCheck){
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
