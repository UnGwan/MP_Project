package com.example.day_28app;

public class MemberInfo2 {

    //새싹 이름
    private String name;

    //미션 내용
    private String[] mission = new String[4];

    //checkMissionWeeks : 처음 메인 프레그먼트에서 미션마다 프로그레스바 활성도 표시를 위한 변수
    //값 : 0~7
    private int checkMissionWeeks;

    //미션 설정 체크 여부
    //값 : 0~3
    private int checkSetMission;

    public MemberInfo2(String name ,String mission1,String mission2,String mission3,String mission4,int checkSetMission,int checkMissionWeeks){
        this.name =name;
        this.mission[0] = mission1;
        this.mission[1] = mission2;
        this.mission[2] = mission3;
        this.mission[3] = mission4;
        this.checkSetMission = checkSetMission;
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

    public int getCheckSetMission() {
        return this.checkSetMission;
    }

    public void setCheckSetMission(int checkSetMission) {
        this.checkSetMission = checkSetMission;
    }
}
