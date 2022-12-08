package com.example.day_28app;

public class MemberInfo {

    private String name;
    private int userNameCheck,checkMissionWeeks;

    public MemberInfo(String name , int userNameCheck,int checkMissionWeeks){
        this.name =name;
        this.userNameCheck = userNameCheck;
        this.checkMissionWeeks = checkMissionWeeks;
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

    public int getCheckMissionWeeks(){
        return this.checkMissionWeeks;
    }
    public void setCheckMissionWeeks(int checkMissionWeeks){
        this.checkMissionWeeks = checkMissionWeeks;
    }
}
