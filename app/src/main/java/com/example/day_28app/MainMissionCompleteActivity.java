package com.example.day_28app;

import static com.example.day_28app.MainHomeFragment.weeks;
import static com.example.day_28app.MainMissionActivity.checkingDay;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainMissionCompleteActivity extends AppCompatActivity {

    private static final String TAG = "Complete_Mission_Page_Activity";
    private MemberDiary memberDiary;
    private String diary_content;
    private static int[] diaryCheck = new int[7];
    private static String[] diary = new String[7];
    private static int completeCheck;
    private Dialog dialog;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static EditText diary_edt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_complete);

        findViewById(R.id.cm_take_photo_btn).setOnClickListener(onClickListener);
        findViewById(R.id.mission_complete_btn).setOnClickListener(onClickListener);

        diary_edt = findViewById(R.id.mission_diary_edt);
        setInitial();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cm_take_photo_btn:
                    startActivity(CameraActivity.class);
                    break;
                case R.id.mission_complete_btn:
                    if (completeCheck == 1) {
                        finish();
                    } else {
                        missionCompleteUpdate();
                    }
            }
        }
    };

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void missionCompleteUpdate() {
        diary_content = ((EditText) findViewById(R.id.mission_diary_edt)).getText().toString();
        if (diary_content.length() > 0) {
            if (user != null) {
                DocumentReference washingtonRef = db.collection("userDiary" + (weeks + 1) + "weeks").document(user.getUid());
                washingtonRef
                        .update("diary" + (checkingDay + 1), diary_content)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("일기 등록이 완료되었습니다.");
                                updateDay();
                                startActivity(MainMissionActivity.class);
                                if (checkingDay==6){
                                    dialog = new Dialog(MainMissionCompleteActivity.this);       // Dialog 초기화
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                                    dialog.setContentView(R.layout.mission_complete_dialog);
                                    dialog.show();
                                    finish();
                                } else {
                                    startActivity(MainMissionActivity.class);
                                }
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("일기등록에 실패하였습니다.");
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        } else {
            startToast("일기를 조금이라도 작성해주세요 ㅠ");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void updateDay() {
        DocumentReference washingtonRef = db.collection("userDay" + (weeks + 1) + "weeks").document(user.getUid());
        washingtonRef
                .update("day" + (checkingDay + 1), 1,"daySum",checkingDay+1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("무야호:", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("무야호", "Error updating document", e);
                    }
                });
    }

    private void setInitial() {
        DocumentReference docRef = db.collection("userDay" + (weeks + 1) + "weeks").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    completeCheck = Integer.parseInt(document.getData().get("day" + (checkingDay + 1)).toString());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        DocumentReference docRef2 = db.collection("userDiary" + (weeks + 1) + "weeks").document(user.getUid());
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (completeCheck == 1) {
                            diary_edt.setText(document.getData().get("diary"+(checkingDay+1)).toString());
                            diary_edt.setEnabled(false);
                            diary_edt.setTextColor(Color.parseColor("#FFC107"));
                        Log.d(TAG, completeCheck + "입니다");
                    } else {
                        diary_edt.setHint(document.getData().get("diary" + (checkingDay + 1)).toString());
                        Log.e(TAG, completeCheck + "입니다2");
                    }
                    for (int i = 0; i < 7; i++) {
                        diary[i] = document.getData().get("diary" + (i + 1)).toString();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}
