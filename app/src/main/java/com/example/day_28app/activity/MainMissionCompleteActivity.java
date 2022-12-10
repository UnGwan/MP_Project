package com.example.day_28app.activity;

import static com.example.day_28app.fragment.MainHomeFragment.weeks;
import static com.example.day_28app.activity.MainMissionActivity.checkingDay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.day_28app.MemberDiary;
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

public class MainMissionCompleteActivity extends AppCompatActivity {

    private static final String TAG = "Complete_Mission_Page_Activity";
    private MemberDiary memberDiary;
    private String diary_content;
    private static int[] diaryCheck = new int[7];
    private static String[] diary = new String[7];
    private static int completeCheck;
    private Dialog dialog;
    private ImageView photoShot;
    private String photoShotPath;
    Map<String, Object> nestedData = new HashMap<>();
    String testString ="https://firebasestorage.googleapis.com/v0/b/mp-project-c22a2.appspot.com/o/users%2F%2BRKJyrs0YGfcf1GaN7OE7OnORTjU2%2FPhoto-shot2.jpg?alt=media&token=efafdd5d-6c19-4164-829c-ff89e703d30d" ;


    private static EditText diary_edt;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_complete);

        photoShot = (ImageView) findViewById(R.id.mc_photoShot_img);
        findViewById(R.id.cm_take_photo_btn).setOnClickListener(onClickListener);
        findViewById(R.id.mission_complete_btn).setOnClickListener(onClickListener);
        findViewById(R.id.cm_get_photo_btn).setOnClickListener(onClickListener);

        diary_edt = findViewById(R.id.mission_diary_edt);
        setInitial();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0: {
                if (resultCode == Activity.RESULT_OK){
                    photoShotPath =data.getStringExtra("photoShotPath");
                    Log.e("로그","photoShotPath"+photoShotPath);
                    Bitmap bmp = BitmapFactory.decodeFile(photoShotPath);
                    photoShot.setImageBitmap(bmp);
                    photoShot.setRotation(90 );
                }
            }
            break;
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                    //직접 사진찍기
                case R.id.cm_take_photo_btn:
                    startActivity3(CameraActivity.class);
                    break;

                    // 갤러리 접근
                case R.id.cm_get_photo_btn:
                    //권한이 없을때
                    if (ContextCompat.checkSelfPermission(
                            MainMissionCompleteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        // You can use the API that requires the permission.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainMissionCompleteActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                            ActivityCompat.requestPermissions(MainMissionCompleteActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                        }else {
                            ActivityCompat.requestPermissions(MainMissionCompleteActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                        }
                    }//권한이 있을때
                    else{
                        startActivity(GalleryActivity.class);
                    }
                    break;
                case R.id.mission_complete_btn:
                    if (completeCheck == 1) {
                        finish();
                    } else {
                        missionCompleteUpdate();
                    }
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(GalleryActivity.class);
                }  else {
                    startToast("권한을 허용해 주세요");
                }
                return;
        }
    }

    //일반 스타트 액티비티
    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //일주일 미션 완료시 다이얼로그를 띄우기 위한 인텐트에 값을 넘겨주는 스타트 액티비티
    private void startActivity2(Class c) {
        Intent intent = new Intent(this, c);
        intent.putExtra("check",1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //카메라 전용 스타트 액티비₩
    private void startActivity3(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);
    }

    private void missionCompleteUpdate() {

        diary_content = ((EditText) findViewById(R.id.mission_diary_edt)).getText().toString();
        //완료버튼 클리식 일기 업데이트
        if (diary_content.length() > 0) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            // Create a storage reference from our app
//            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/+"+user.getUid()+"/"+(weeks+1)+"주"+checkingDay+"일"+"PhotoShot.jpg");

            //사진이 없을때 일기만 업로드
            if (photoShotPath == null){
                nestedData.put("photoShotUrl",testString.toString());
                upload();
            } else{
                try {
                    InputStream stream = new FileInputStream(new File(photoShotPath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.e("실패","실패");
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
//                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    upload();
                                Log.e("성공","성공: "+downloadUri);
                            } else {
                                startToast("일기 업로드에 실패 하였습니다");
                            }
                        }
                    });
                }
                catch (FileNotFoundException e){
                    Log.e("로그","에러"+e.toString());
                }
            }
        } else {
            startToast("일기를 조금이라도 작성해주세요 ㅠ");
        }
    }

    private void upload(){
        nestedData.put("diary", diary_content);
        nestedData.put("dayCheck", 1);
        DocumentReference washingtonRef = db.collection("userDiary" + (weeks + 1) + "weeks").document(user.getUid());
        washingtonRef
                .update("diary"+(checkingDay+1), nestedData,"daySum",checkingDay+1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("일기 등록이 완료되었습니다.");
                        updateDay();
                        if (checkingDay==6){
                            startActivity2(MainMissionActivity.class);

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

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //일주일동안 미션을 완료시 checkMissionWeeks 필드에 몇주차까지 완료했는지 데이터를 넘겨주는 함수
    private void updateDay() {
        if (checkingDay+1 == 7){
            DocumentReference washingtonRef = db.collection("users").document(user.getUid());
            washingtonRef
                    .update("checkMissionWeeks",weeks+1)
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
    }

    //일기 등록 여부 확인 및 등록시 기존 일기 불러오기
    private void setInitial() {
        String day = "diary"+(checkingDay+1);

        DocumentReference docRef2 = db.collection("userDiary" + (weeks + 1) + "weeks").document(user.getUid());
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    completeCheck = Integer.parseInt(((HashMap<String, Object>) document.getData().get(day)).get("dayCheck").toString());
                    if (completeCheck == 1) {
                        diary_edt.setText(((HashMap<String, Object>) document.getData().get(day)).get("diary").toString());
                            diary_edt.setEnabled(false);
                            diary_edt.setTextColor(Color.parseColor("#FFC107"));
                        Log.d(TAG, completeCheck + "입니다");
                    } else {
                        diary_edt.setHint(((HashMap<String, Object>) document.getData().get(day)).get("diary").toString());
                        Log.e(TAG, completeCheck + "입니다22222");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}
