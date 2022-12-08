package com.example.day_28app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signUp_sign_btn).setOnClickListener(onClickListener);
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
            }
        }
    };
    private void signUP(){

        //email , password
        String email = ((EditText)findViewById(R.id.idEdittxt)).getText().toString();
        String password = ((EditText)findViewById(R.id.signup_passwordEditTxt)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.passwordCheckEditTxt)).getText().toString();

        if(email.length()>0 && password.length()>0 && passwordCheck.length()>0){
            if(password.equals(passwordCheck)){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    setDB();
                                    startActivity(WelcomeSignupActivity.class);
                                    // 성공 로직
                                } else {
                                    if (task.getException()!= null){
                                        startToast( task.getException().toString());
                                    }
                                }
                            }
                        });
            }else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        } else {
            startToast("빈칸이 존재하면 안됩니다");
        }
    }

    // 회원가입 성공시 데이터베이스 컬렉션 생성.
    private void setDB(){


        String defaultDiary = "오늘의 일기를 작성해주세요";
        String defaultMission = "미션을 설정해주세요";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        MemberDay memberDiary_day = new MemberDay(0,0,0,0,0,0,0,0);
        MemberDiary memberDiary = new MemberDiary(defaultDiary,defaultDiary,defaultDiary,defaultDiary,defaultDiary,defaultDiary,defaultDiary);
        MemberMission memberMission = new MemberMission("1주차"+defaultMission,"2주차"+defaultMission,"3주차"+defaultMission,"4주차"+defaultMission,0);
        MemberInfo memberinfo = new MemberInfo("0",0);
        if (user != null ){
            for (int i = 0 ; i< 4 ; i++){
                db.collection("userDay"+(i+1)+"weeks").document(user.getUid()).set(memberDiary_day)
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
                db.collection("userDiary"+(i+1)+"weeks").document(user.getUid()).set(memberDiary)
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
            db.collection("userMission").document(user.getUid()).set(memberMission)
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
