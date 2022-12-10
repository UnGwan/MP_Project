package com.example.day_28app;

public class MemberInfo {

    private String name;
    //checkMissionWeeks : 처음 메인 프레그먼트에서 미션마다 프로그레스바 활성도 표시를 위한 변수
    private int checkMissionWeeks;

    public MemberInfo(String name , int userNameCheck,int checkMissionWeeks){
        this.name =name;
        this.checkMissionWeeks = checkMissionWeeks;
    }

    public String getName(){
        return this.name;

    }

    public void setName(String name){
        this.name = name;
    }

    public int getCheckMissionWeeks(){
        return this.checkMissionWeeks;
    }
    public void setCheckMissionWeeks(int checkMissionWeeks){
        this.checkMissionWeeks = checkMissionWeeks;
    }
}
