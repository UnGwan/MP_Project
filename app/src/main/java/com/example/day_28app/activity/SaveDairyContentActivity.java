package com.example.day_28app.activity;

import static com.example.day_28app.activity.MainMissionActivity.checkingDay;
import static com.example.day_28app.activity.SaveDiaryActivity.checkDayPoint;
import static com.example.day_28app.activity.SaveDiaryActivity.checkWeeksPoint;
import static com.example.day_28app.fragment.MainHomeFragment.weeks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.day_28app.Motivation;
import com.example.day_28app.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SaveDairyContentActivity extends AppCompatActivity {

    private static final String TAG = "Complete_Mission_Page_Activity";
    private String diary_content;
    private static int completeCheck,imgCompleteCheck=0;
    private Dialog dialog;
    private ImageView photoShot;
    private TextView movTxt;
    private String photoShotPath;
    Map<String, Object> dataMap = new HashMap<>();
    Motivation motivation = new Motivation();
    Uri downloadUri;

    private Button allBtn,diaryBtn,backBtn;



    private static EditText diary_edt;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_diary);

        photoShot = (ImageView) findViewById(R.id.as_photoShot_img);
        diary_edt = findViewById(R.id.as_edt);

        findViewById(R.id.as_btn).setOnClickListener(onClickListener);
        setInitial();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0: {
                if (resultCode == Activity.RESULT_OK){
                    imgCompleteCheck=1;
                    photoShotPath =data.getStringExtra("photoShotPath");
                    Glide.with(this).load(photoShotPath).centerCrop().override(500).into(photoShot);
                    Log.e("로그","photoShotPath"+photoShotPath);
                }
            }
            break;
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.as_btn:
                    finish();
                    break;
            }
        }
    };

    //일반 스타트 액티비티
    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    //일기 등록 로직

    //토스트 메세지
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //일주일동안 미션을 완료시 checkMissionWeeks 필드에 몇주차까지 완료했는지 데이터를 넘겨주는 함수

    //일기 등록 여부 확인 및 등록시 기존 일기 불러오기
    private void setInitial() {

        DocumentReference docRef = db.collection("saveDiary").document(user.getUid());
        docRef.get().addOnCompleteListener(this,new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //일기가 등록이 되어 있을시 미리 작성된 일기를 보여주기 위한 로직들
                    String diaryDay = checkWeeksPoint+"주"+(checkDayPoint+1)+"일diary";
                    String photoDay = checkWeeksPoint+"주"+(checkDayPoint+1)+"일photoShotUrl";
                    Log.e("몇주인데",diaryDay);
                    diary_edt.setText(((HashMap<String, Object>) document.getData().get("diary1")).get(diaryDay).toString());
                        diary_edt.setEnabled(false);
                        diary_edt.setTextColor(Color.parseColor("#FFC107"));
                        if (((HashMap<String, Object>) document.getData().get("diary1")).get(photoDay) != null){
                            Glide.with(SaveDairyContentActivity.this).load(((HashMap<String, Object>) document.getData().get("diary1")).get(photoDay).toString()).centerCrop().into(photoShot);
                            Log.d(TAG,  "설정입니다");
                        } else{
                            photoShot.setImageResource(R.drawable.replacephotshot);
                            Log.d(TAG,  "기본입니다");
                        }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}
