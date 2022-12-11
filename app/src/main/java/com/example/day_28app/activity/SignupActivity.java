package com.example.day_28app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.day_28app.MemberDiary;
import com.example.day_28app.MemberID;
import com.example.day_28app.MemberMission;
import com.example.day_28app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int dupCheckValue=0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signUp_sign_btn).setOnClickListener(onClickListener);
        findViewById(R.id.duplicate_check_btn).setOnClickListener(onClickListener);
        findViewById(R.id.signUp_cancel_btn).setOnClickListener(onClickListener);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.signUp_sign_btn:
                    signUP();
                    break;
                case R.id.signUp_cancel_btn:
                    finish();
                    break;
                case R.id.duplicate_check_btn:
                    duplicateCheckId();
                    break;

            }
        }
    };

    private void duplicateCheckId(){
        String email = ((EditText)findViewById(R.id.idEdittxt)).getText().toString();
        CollectionReference docRef = db.collection("userId");
        docRef.whereArrayContains("id", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("테스트", "뭐뜨는디: "+document.getData());
                                dupCheckValue = 2;
                            }
                        } else {
                            Log.d("테스트", "Error getting documents: ", task.getException());
                        }
                        if (dupCheckValue==2){
                            dupCheckValue=0;
                            startToast("중복된 이메일이 존재합니다 다른 이메일을 작성해주세요");
                        } else{
                            dupCheckValue=1;
                            startToast("사용 가능한 메일입니다");
                        }
                    }
                });
    }
    private void signUP(){

        //email , password
        String email = ((EditText)findViewById(R.id.idEdittxt)).getText().toString();
        String password = ((EditText)findViewById(R.id.signup_passwordEditTxt)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.passwordCheckEditTxt)).getText().toString();
            if(email.length()>0 && password.length()>0 && passwordCheck.length()>0){
                if(password.equals(passwordCheck)){
                    Log.d("check","체크좀: "+dupCheckValue);
                    if (dupCheckValue == 1){
                        Log.d("check","체크좀2: "+dupCheckValue);
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Log.d("check","체크좀3: "+dupCheckValue);
                                            Map<String, Object> defaultData = new HashMap<>();
                                            defaultData.put("diary", "오늘의 일기를 작성해주세요");
                                            defaultData.put("dayCheck", 0);
                                            defaultData.put("photoShotUrl","0");

                                            MemberDiary memberDiary = new MemberDiary(defaultData,defaultData,defaultData,defaultData,defaultData,defaultData,defaultData,0);
                                            if (user != null ){
                                                for (int i = 0 ; i< 4 ; i++){
                                                    db.collection("userDiary"+(i+1)+"weeks").document(user.getUid()).set(memberDiary)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("성공","성공ㅎ했습니다");
                                                                    String email = ((EditText)findViewById(R.id.idEdittxt)).getText().toString();
                                                                    MemberID memberID = new MemberID(Arrays.asList(email));
                                                                    DocumentReference washingtonRef = db.collection("userId").document("idList");
                                                                    washingtonRef.update("id", FieldValue.arrayUnion(email));
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.e("실패","실패했습니다");
                                                                }
                                                            });
                                                }
                                                Map<String, Object> defaultData2 = new HashMap<>();
                                                defaultData2.put("diary1", "");
                                                db.collection("saveDiary").document(user.getUid()).set(defaultData2)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });


                                            }
                                            startActivity(WelcomeSignupActivity.class);
                                            finish();
                                            // 성공 로직
                                        } else {
                                            if (task.getException()!= null){
                                                startToast( task.getException().toString());
                                            }
                                        }
                                    }
                                });
                    } else{
                        startToast("이메일 중복체크를 진행해주세요");
                    }
                }else {
                    startToast("비밀번호가 일치하지 않습니다.");
                }
            } else {
                startToast("빈칸이 존재하면 안됩니다");
            }
        }



    // 회원가입 성공시 데이터베이스 컬렉션 생성.
    private void setDB(){


        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("diary", "오늘의 일기를 작성해주세요");
        defaultData.put("dayCheck", 0);
        defaultData.put("photoShotUrl","0");

        MemberDiary memberDiary = new MemberDiary(defaultData,defaultData,defaultData,defaultData,defaultData,defaultData,defaultData,0);
        if (user != null ){
            for (int i = 0 ; i< 4 ; i++){
                db.collection("userDiary"+(i+1)+"weeks").document(user.getUid()).set(memberDiary)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("성공","성공ㅎ했습니다");
                                String email = ((EditText)findViewById(R.id.idEdittxt)).getText().toString();
                                MemberID memberID = new MemberID(Arrays.asList(email));
                                DocumentReference washingtonRef = db.collection("userId").document("idList");
                                washingtonRef.update("id", FieldValue.arrayUnion(memberID));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("실패","실패했습니다");
                            }
                        });
            }

            }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void startActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
