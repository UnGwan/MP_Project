package com.example.day_28app;

public class MemberMission {

    private String[] mission = new String[4];
    private int checkPoint;

    public MemberMission(String a,String b,String c,String d,int checkPoint){
        this.mission[0] = a;
        this.mission[1] = b;
        this.mission[2] = c;
        this.mission[3] = d;
        this.checkPoint = checkPoint;

    }
    public String getMission1() {
        return this.mission[0];
    }

    public void setMission1(String a) {
        this.mission[0] = a;
    }

    public String getMission2() {
        return this.mission[1];
    }

    public void setMission2(String b) {
        this.mission[1] = b;
    }


    public String getMission3() {
        return this.mission[2];
    }

    public void setMission3(String c) {
        this.mission[2] = c;
    }

    public String getMission4() {
        return this.mission[3];
    }

    public void setMission4(String d) {
        this.mission[3] = d;
    }

    public int getCheckPoint() {
        return this.checkPoint;
    }

    public void setCheckPoint(int checkPoint) {
        this.checkPoint = checkPoint;
    }

}
