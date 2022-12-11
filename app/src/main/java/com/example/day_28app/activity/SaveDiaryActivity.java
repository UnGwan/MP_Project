package com.example.day_28app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.day_28app.R;


public class SaveDiaryActivity extends AppCompatActivity {



    private int[] btn_id = {R.id.save1,R.id.save2,R.id.save3,R.id.save4,R.id.save5,R.id.save6,R.id.save7,R.id.save8,R.id.save9,R.id.save10,R.id.save11,R.id.save12,R.id.save13,R.id.save14,R.id.save15,R.id.save16,R.id.save17,R.id.save18,R.id.save19,R.id.save20,R.id.save21,R.id.save22,R.id.save23,R.id.save24,R.id.save25,R.id.save26,R.id.save27,R.id.save28};
    public static int checkDayPoint,checkWeeksPoint;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savediary);

        for (int i =0 ;i<28;i++){
            findViewById(btn_id[i]).setOnClickListener(onClickListener);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int i =0 ;i<28;i++){
                if (view.getId() == btn_id[i]){
                    if (i<7){
                        checkDayPoint=i;
                        checkWeeksPoint = 1;
                    } else if (i>=7&&i<14){
                        checkDayPoint=i-6;
                        checkWeeksPoint = 2;
                    } else if (i>=14 && i<21){
                        checkDayPoint=i-13;
                        checkWeeksPoint = 3;
                    } else {
                        checkDayPoint=i-20;
                        checkWeeksPoint = 4;
                    }
                    startActivity(SaveDairyContentActivity.class);
                }
            }
        }
    };

    private void startActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
