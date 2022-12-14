package com.example.day_28app.activity;

import static com.example.day_28app.fragment.MainHomeFragment.weeks;
import static com.example.day_28app.activity.MainMissionActivity.checkingDay;

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
import com.example.day_28app.MemberDiary;
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

public class MainMissionCompleteActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_mission_complete);

        photoShot = (ImageView) findViewById(R.id.mc_photoShot_img);
        findViewById(R.id.cm_take_photo_btn).setOnClickListener(onClickListener);
        findViewById(R.id.mission_all_complete_btn).setOnClickListener(onClickListener);
        findViewById(R.id.mission_diary_complete_btn).setOnClickListener(onClickListener);
        findViewById(R.id.mission_back_btn).setOnClickListener(onClickListener);

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
                    imgCompleteCheck=1;
                    photoShotPath =data.getStringExtra("photoShotPath");
                    Glide.with(this).load(photoShotPath).centerCrop().override(500).into(photoShot);
                    Log.e("??????","photoShotPath"+photoShotPath);
                }
            }
            break;
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                    //?????? ????????????
                case R.id.cm_take_photo_btn:
                    startActivity3(CameraActivity.class);
                    break;

                    // ????????? ??????
                case R.id.cm_get_photo_btn:
                    //????????? ?????????
                    if (ContextCompat.checkSelfPermission(
                            MainMissionCompleteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainMissionCompleteActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                        // You can use the API that requires the permission.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainMissionCompleteActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                            ActivityCompat.requestPermissions(MainMissionCompleteActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                        }else {
                            startToast("????????? ????????? ?????????");
                        }
                    }//????????? ?????????
                    else{
                        startActivity3(GalleryActivity.class);
                    }
                    break;
                case R.id.mission_all_complete_btn:
                    missionCompleteUpdate();
                    break;

                case R.id.mission_diary_complete_btn:
                    dataMap.put("photoShotUrl",null);
                    upload();
                    break;
                case R.id.mission_back_btn:
                    Log.e("???","?????????");
                    finish();
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
                    startActivity3(GalleryActivity.class);
                }  else {
                    startToast("????????? ????????? ?????????");
                }
                return;
        }
    }

    //?????? ????????? ????????????
    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //????????? ?????? ????????? ?????????????????? ????????? ?????? ???????????? ?????? ???????????? ????????? ????????????
    private void startActivity2(Class c,int val) {
        Intent intent = new Intent(this, c);
        intent.putExtra("check",val);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //????????? ?????? ????????? ????????????
    private void startActivity3(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);
    }

    private void missionCompleteUpdate() {
        diary_content = ((EditText) findViewById(R.id.mission_diary_edt)).getText().toString();
        //???????????? ????????? ?????? ????????????
        if (diary_content.length() > 0) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            // Create a storage reference from our app
//            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/+"+user.getUid()+"/"+(weeks+1)+"???"+checkingDay+"???"+"PhotoShot.jpg");

            //????????? ????????? ????????? ?????????
            if (photoShotPath == null){
                dataMap.put("photoShotUrl",null);
                upload();
            } else{
                //????????? ????????? ????????? ?????? ?????????
                try {
                    InputStream stream = new FileInputStream(new File(photoShotPath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.e("??????","??????");
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadUri = task.getResult();
                                dataMap.put("photoShotUrl",downloadUri.toString());
                                upload();
                                Log.e("??????","??????: "+downloadUri);
                            } else {
                                startToast("?????? ???????????? ?????? ???????????????");
                            }
                        }
                    });
                }
                catch (FileNotFoundException e){
                    Log.e("??????","??????"+e.toString());
                }
            }
        } else {
            startToast("????????? ??????????????? ?????????????????? ???");
        }
    }

    //?????? ?????? ??????
    private void upload(){
        diary_content = ((EditText) findViewById(R.id.mission_diary_edt)).getText().toString();
        dataMap.put("diary", diary_content);
        dataMap.put("dayCheck", 1);
        if (diary_content.length() > 0) {
        DocumentReference washingtonRef = db.collection("userDiary" + (weeks + 1) + "weeks").document(user.getUid());
        washingtonRef
                .update("diary"+(checkingDay+1), dataMap,"daySum",checkingDay+1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("?????? ????????? ?????????????????????.");
                        updateDay();
                        if (checkingDay==6){
                            if (weeks==3){
                                startActivity2(MainMissionActivity.class,2);
                            } else {

                                startActivity2(MainMissionActivity.class,1);
                            }
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
                        startToast("??????????????? ?????????????????????.");
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        } else {
            startToast("????????? ??????????????? ?????????????????? ???");
        }
    }

    //????????? ?????????
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //??????????????? ????????? ????????? checkMissionWeeks ????????? ??????????????? ??????????????? ???????????? ???????????? ??????
    private void updateDay() {
        if (checkingDay+1 == 7){
            DocumentReference washingtonRef = db.collection("users").document(user.getUid());
            washingtonRef
                    .update("checkMissionWeeks",weeks+1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("?????????:", "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("?????????", "Error updating document", e);
                        }
                    });
        }
    }

    //?????? ?????? ?????? ?????? ??? ????????? ?????? ?????? ????????????
    private void setInitial() {
        String day = "diary"+(checkingDay+1);
        DocumentReference docRef = db.collection("userDiary" + (weeks + 1) + "weeks").document(user.getUid());
        docRef.get().addOnCompleteListener(this,new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    completeCheck = Integer.parseInt(((HashMap<String, Object>) document.getData().get(day)).get("dayCheck").toString());
                    //????????? ????????? ?????? ????????? ?????? ????????? ????????? ???????????? ?????? ?????????
                    if (completeCheck == 1) {
                        findViewById(R.id.mission_back_btn).setVisibility(View.VISIBLE);
//                        movTxt.setText(motivation.getMotivation());
                        movTxt = findViewById((R.id.mission_motivation_txt));
                        movTxt.setVisibility(View.VISIBLE);
                        movTxt.setText(motivation.getMotivation());
                        findViewById(R.id.mission_diary_complete_btn).setVisibility(View.INVISIBLE);
                        findViewById(R.id.mission_all_complete_btn).setVisibility(View.INVISIBLE);
                        findViewById(R.id.cm_get_photo_btn).setVisibility(View.INVISIBLE);
                        findViewById(R.id.cm_take_photo_btn).setVisibility(View.INVISIBLE);

                        diary_edt.setText(((HashMap<String, Object>) document.getData().get(day)).get("diary").toString());
                        diary_edt.setEnabled(false);
                        diary_edt.setTextColor(Color.parseColor("#FFC107"));

                        if (((HashMap<String, Object>) document.getData().get(day)).get("photoShotUrl") != null){
                            Glide.with(MainMissionCompleteActivity.this).load(((HashMap<String, Object>) document.getData().get(day)).get("photoShotUrl").toString()).centerCrop().into(photoShot);
                            Log.d(TAG,  "???????????????");
                        } else{
//                            Glide.with(MainMissionCompleteActivity.this).load(R.drawable.replacephotshot).into(MainMissionCompleteActivity.this.photoShot);
                            photoShot.setImageResource(R.drawable.replacephotshot);
                            Log.d(TAG,  "???????????????");
                        }
                    } else {
                        diary_edt.setHint(((HashMap<String, Object>) document.getData().get(day)).get("diary").toString());
                        Log.e(TAG, completeCheck + "?????????22222");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}
