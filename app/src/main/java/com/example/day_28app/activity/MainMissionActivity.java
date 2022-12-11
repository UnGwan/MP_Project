package com.example.day_28app.activity;

import static com.example.day_28app.fragment.MainHomeFragment.weeks;
import static com.example.day_28app.fragment.MissionSetDialogFragment.TAG_EVENT_DIALOG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.day_28app.R;
import com.example.day_28app.fragment.FlowerpotSetFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainMissionActivity extends AppCompatActivity {

    private static final String TAG = "Main_mission_1weeks_Activity";
    public static int checkingDay;
    private ImageView[] img = new ImageView[7];
    private TextView[] txt = new TextView[7];
    private LinearLayout[] layout = new LinearLayout[7];

    private int[] icon_id = {R.drawable.main1_icon_profile_0weeks,R.drawable.main1_icon_profile_1weeks,R.drawable.main1_icon_profile_2weeks,R.drawable.main1_icon_profile_3weeks,R.drawable.main1_icon_profile_4weeks};
    private int[] img_id = {R.id.mission_day1_img,R.id.mission_day2_img,R.id.mission_day3_img,R.id.mission_day4_img,R.id.mission_day5_img,R.id.mission_day6_img,R.id.mission_day7_img};
    private int[] txt_id ={R.id.mission_day1_txt,R.id.mission_day2_txt,R.id.mission_day3_txt,R.id.mission_day4_txt,R.id.mission_day5_txt,R.id.mission_day6_txt,R.id.mission_day7_txt};
    private int[] lay_id = {R.id.main_mission_1weeks_1day,R.id.main_mission_1weeks_2day,R.id.main_mission_1weeks_3day,R.id.main_mission_1weeks_4day,R.id.main_mission_1weeks_5day,R.id.main_mission_1weeks_6day,R.id.main_mission_1weeks_7day};
    private Dialog dialog,dialog2;

    //다이얼로그 변수
    ImageView preImg,nextImge;
    TextView dialogTxt;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

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
    protected void onStart() {
        super.onStart();
        Intent secondIntent = getIntent();
        //일주일 미션 완료시 실행되는 다이얼로그
        if (secondIntent.getIntExtra("check",0)==1){
        dialog = new Dialog(MainMissionActivity.this);       // Dialog 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog.setContentView(R.layout.dialog_mission_complete);
        showDialog();
        } else if (secondIntent.getIntExtra("check",0)==2){
            FlowerpotSetFragment m = FlowerpotSetFragment.getInstance();
            m.show(getSupportFragmentManager(),TAG_EVENT_DIALOG);
        }
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case 0: {
//                if (resultCode == Activity.RESULT_OK){
//                    String returnValue =data.getStringExtra("some_key");
//                }
//            }
//            break;
//        }
//    }

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

    //일주일 완료시 바뀐 새싹 아이콘 보여주기 함수
    private void showDialog(){

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    preImg = (ImageView) dialog.findViewById(R.id.mc_dialog_previous_img);
                    nextImge=(ImageView) dialog.findViewById(R.id.mc_dialog_next_img);
                    dialogTxt = (TextView) dialog.findViewById(R.id.mc_dialog_txt);
                    if (preImg != null && nextImge != null && dialogTxt != null){
                        preImg.setImageResource(icon_id[Integer.parseInt(document.getData().get("checkMissionWeeks").toString())]-1);
                        nextImge.setImageResource(icon_id[Integer.parseInt(document.getData().get("checkMissionWeeks").toString())]);
                        dialogTxt.setText(Integer.parseInt(document.getData().get("checkMissionWeeks").toString())+"주차 완료 새싹이 성장했어요!");
                    }
                }
            }
        });
        dialog.show();


//        dialogTxt.setTypeface(null, Typeface.BOLD);

    }

    //화면 넘어가기
    private void startActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void checkMissionComplete(){
        DocumentReference docRef = db.collection("userDiary"+(weeks+1)+"weeks").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    //해당 주차 미션이 완료된 일에 대한 위젯 변경
                    for (int i = 0; i< 7;i++){
                        String day = "diary"+(i+1);
                        if (Integer.parseInt(((HashMap<String, Object>) document.getData().get(day)).get("dayCheck").toString())==1){
                            img[i].setImageResource(R.drawable.color_orange);
                            txt[i].setText((weeks+1)+"주차"+" "+(i +1)+"일 일기 돌아보기!");
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
                    missionTxt.setText("<"+document.getData().get("mission"+(weeks+1)).toString()+">");
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}
