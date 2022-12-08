package com.example.day_28app;

import static com.example.day_28app.MainHomeFragment.weeks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainMissionActivity extends AppCompatActivity {

    private static final String TAG = "Main_mission_1weeks_Activity";
    public static int checkingDay;
    private ImageView[] img = new ImageView[7];
    private TextView[] txt = new TextView[7];
    private LinearLayout[] layout = new LinearLayout[7];
    private int[] img_id = {R.id.mission_day1_img,R.id.mission_day2_img,R.id.mission_day3_img,R.id.mission_day4_img,R.id.mission_day5_img,R.id.mission_day6_img,R.id.mission_day7_img};
    private int[] txt_id ={R.id.mission_day1_txt,R.id.mission_day2_txt,R.id.mission_day3_txt,R.id.mission_day4_txt,R.id.mission_day5_txt,R.id.mission_day6_txt,R.id.mission_day7_txt};
    private int[] lay_id = {R.id.main_mission_1weeks_1day,R.id.main_mission_1weeks_2day,R.id.main_mission_1weeks_3day,R.id.main_mission_1weeks_4day,R.id.main_mission_1weeks_5day,R.id.main_mission_1weeks_6day,R.id.main_mission_1weeks_7day};
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_mission_1weeks);

        for (int i = 0; i<7;i++){
            img[i]=findViewById(img_id[i]);
            txt[i]=findViewById(txt_id[i]);
            layout[i] = findViewById(lay_id[i]);
            layout[i].setOnClickListener(onClickListener);
        }
        setTxt();
        checkMissionComplete();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0: {
                if (resultCode == Activity.RESULT_OK){
                    String returnValue =data.getStringExtra("some_key");
                }
            }
            break;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // 인증샷 화면으러 넘어가기
            for (int i =0 ; i<7; i++){
                if (view.getId() == lay_id[i]){
                    checkingDay = i;
                    startActivity(MainMissionCompleteActivity.class);
                }
            }
        }
    };

    private void startActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivityForResult(intent,0);
    }
    private void checkMissionComplete(){


        DocumentReference docRef = db.collection("userDay"+(weeks+1)+"weeks").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    for (int i = 0; i< 7;i++){
                        if (Integer.parseInt(document.getData().get("day"+(i+1)).toString())==1){
                            img[i].setImageResource(R.drawable.color_orange);
                            txt[i].setText((weeks+1)+"주차"+(i +1)+"일 미션완료!");
                            txt[i].setTextColor(Color.parseColor("#FFC107") );
                            if(i==6) {
                                break;
                            }
                            layout[i+1].setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setTxt(){
        TextView usernameTxt,missionTxt;
        usernameTxt = (TextView) findViewById(R.id.main_mission_username_txt);
        missionTxt = (TextView) findViewById(R.id.main_mission_mission_txt);
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    usernameTxt.setText(document.getData().get("name").toString()+" "+( weeks+1)+"주차 미션");
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        DocumentReference docRef2 = db.collection("userMission").document(user.getUid());
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    missionTxt.setText(document.getData().get("mission"+(weeks+1)).toString());
                } else {
                    Log.d(TAG, "get failed with2 ", task.getException());
                }
            }
        });
    }

}
