package com.example.day_28app;

import java.util.HashMap;
import java.util.Map;

public class MemberDiary {

    private Map<String, Object>[] diaryDay = new HashMap[7];

    // 그 주차에 미션을 며칠 수행했는지 알기 위한 변수
    // 값:0~7
    private int daySum;
    public MemberDiary(Map<String, Object> diary1, Map<String, Object> diary2, Map<String, Object> diary3, Map<String, Object> diary4, Map<String, Object> diary5, Map<String, Object> diary6, Map<String, Object> diary7,int daySum) {
        this.diaryDay[0] = diary1;
        this.diaryDay[1] = diary2;
        this.diaryDay[2] = diary3;
        this.diaryDay[3] = diary4;
        this.diaryDay[4] = diary5;
        this.diaryDay[5] = diary6;
        this.diaryDay[6] = diary7;
        this.daySum = daySum;
    }

    public Map<String, Object> getDiary1(){return this.diaryDay[0];}
    public void setDiary1(Map<String, Object> diary1) {
        this.diaryDay[0] = diary1;
    }

    public Map<String, Object>  getDiary2() {return this.diaryDay[1];}
    public void setDiary2(Map<String, Object>  diary2) {
        this.diaryDay[1] = diary2;
    }

    public Map<String, Object>  getDiary3() {
        return this.diaryDay[2];
    }
    public void setDiary3(Map<String, Object>  diary3) {
        this.diaryDay[2] = diary3;
    }

    public Map<String, Object>  getDiary4() {
        return this.diaryDay[3];
    }
    public void setDiary4(Map<String, Object>  diary4) {
        this.diaryDay[3] = diary4;
    }

    public Map<String, Object>  getDiary5() {
        return this.diaryDay[4];
    }
    public void setDiary5(Map<String, Object>  diary5) {
        this.diaryDay[4] = diary5;
    }

    public Map<String, Object>  getDiary6() {
        return this.diaryDay[5];
    }
    public void setDiary6(Map<String, Object>  diary6) {
        this.diaryDay[5] = diary6;
    }

    public Map<String, Object>  getDiary7() {
        return this.diaryDay[6];
    }
    public void setDiary7(Map<String, Object>  diary7) {
        this.diaryDay[6] = diary7;
    }

    public int getDaySum(){
        return this.daySum;
    }
    public void setDiaryDaySum(int daySum) {
        this.daySum = daySum;
    }
}

