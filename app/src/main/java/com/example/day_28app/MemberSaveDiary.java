package com.example.day_28app;

import java.util.HashMap;
import java.util.Map;

public class MemberSaveDiary {
    private Map<String, Object> diary;

    public MemberSaveDiary(Map<String, Object> diary){
        this.diary = diary;
    }

    public Map<String, Object> getDiary1(){return this.diary;}
    public void setDiary1(Map<String, Object> diary) {
        this.diary = diary;
    }


}
