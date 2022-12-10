package com.example.day_28app;

public class MemberMission {

    private String[] mission = new String[4];


    //미션 설정 체크 여부
    private int checkPoint;

    public MemberMission(String mission1,String mission2,String mission3,String mission4,int checkPoint){
        this.mission[0] = mission1;
        this.mission[1] = mission2;
        this.mission[2] = mission3;
        this.mission[3] = mission4;
        this.checkPoint = checkPoint;

    }
    public String getMission1() {
        return this.mission[0];
    }

    public void setMission1(String mission1) {
        this.mission[0] = mission1;
    }

    public String getMission2() {
        return this.mission[1];
    }

    public void setMission2(String mission2) {
        this.mission[1] = mission2;
    }


    public String getMission3() {
        return this.mission[2];
    }

    public void setMission3(String mission3) {
        this.mission[2] = mission3;
    }

    public String getMission4() {
        return this.mission[3];
    }

    public void setMission4(String mission4) {
        this.mission[3] = mission4;
    }

    public int getCheckPoint() {
        return this.checkPoint;
    }

    public void setCheckPoint(int checkPoint) {
        this.checkPoint = checkPoint;
    }

}
