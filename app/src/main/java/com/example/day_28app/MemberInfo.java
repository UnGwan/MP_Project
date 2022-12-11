package com.example.day_28app;

public class MemberInfo {

    //새싹 이름
    private String name;

    //미션 내용
    private String[] mission = new String[4];

    //checkMissionWeeks : 새싹 아이콘 설정
    //값 : 0~4
    private int checkMissionWeeks;

    //미션 설정 체크 여부
    //값 : 0~3
    private int checkSetMission;

    //자동 로그인 체크 여부
    //값 : 0~1
    private int autoLogin;

    //아이디 저장 체크 여부
    //값 : 0~1
    private int saveId;

    //저장된 일기 갯수

    private int countDiary;


    public MemberInfo(String name , String mission1, String mission2, String mission3, String mission4, int checkSetMission, int checkMissionWeeks, int autoLogin , int saveId,int countDiary){
        this.name =name;
        this.mission[0] = mission1;
        this.mission[1] = mission2;
        this.mission[2] = mission3;
        this.mission[3] = mission4;
        this.checkSetMission = checkSetMission;
        this.checkMissionWeeks = checkMissionWeeks;
        this.autoLogin = autoLogin;
        this.saveId = saveId;
        this.countDiary = countDiary;
    }
    public MemberInfo(String name , String mission1, String mission2, String mission3, String mission4, int checkSetMission, int checkMissionWeeks,int countDiary){
        this.name =name;
        this.mission[0] = mission1;
        this.mission[1] = mission2;
        this.mission[2] = mission3;
        this.mission[3] = mission4;
        this.checkSetMission = checkSetMission;
        this.checkMissionWeeks = checkMissionWeeks;
        this.countDiary = countDiary;
    }
    public MemberInfo(String name , String mission1, String mission2, String mission3, String mission4, int checkSetMission, int checkMissionWeeks){
        this.name =name;
        this.mission[0] = mission1;
        this.mission[1] = mission2;
        this.mission[2] = mission3;
        this.mission[3] = mission4;
        this.checkSetMission = checkSetMission;
        this.checkMissionWeeks = checkMissionWeeks;
    }
    public MemberInfo(int autoLogin){
        this.autoLogin = autoLogin;
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

    public int getAutoLogin() {
        return this.autoLogin;
    }

    public void setAutoLogin(int autoLogin) {
        this.autoLogin = autoLogin;
    }

    public int getSaveId() {
        return this.saveId;
    }

    public void setSaveId(int saveId) {
        this.saveId = saveId;
    }

    public int getCountDiary(){
        return this.countDiary;
    }
    public void setCountDiary(int countDiary){
        this.countDiary = countDiary;
    }
}
