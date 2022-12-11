package com.example.day_28app.activity;

import static android.content.ContentValues.TAG;
import static com.example.day_28app.fragment.MainHomeFragment.weeks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.day_28app.MemberInfo;
import com.example.day_28app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email,password;
    EditText email_edt;

    private CheckBox autologin,saveId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_login_btn).setOnClickListener(onClickListener);
        findViewById(R.id.login_forget_password_text).setOnClickListener(onClickListener);
        findViewById(R.id.login_sign_btn).setOnClickListener(onClickListener);
        autologin = (CheckBox) findViewById(R.id.autoLoginCB);
        saveId = (CheckBox) findViewById(R.id.idRemCB);
        mAuth = FirebaseAuth.getInstance();
        email_edt = (EditText) findViewById(R.id.login_idEditTxt);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        checkId();

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_login_btn:
                    login();
                    break;
                case R.id.login_sign_btn:
                    startActivity(SignupActivity.class);
                    break;
                case R.id.login_forget_password_text:
                    startActivity(PasswordResetActivity.class);
                    break;
            }
        }
    };

    //로그인 메소드
    private void login() {
        email = ((EditText) findViewById(R.id.login_idEditTxt)).getText().toString();
        password = ((EditText) findViewById(R.id.login_passwordEditTxt)).getText().toString();
        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                MemberInfo memberinfo = new MemberInfo(1);
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                //자동 로그인 체크시
                                if (autologin.isChecked()){
                                    DocumentReference docRef = db.collection("users").document(user.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null) {
                                                    if (document.exists()) {
                                                        DocumentReference washingtonRef = db.collection("users").document(user.getUid());
                                                        washingtonRef
                                                                .update("autoLogin",1)
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
                                                    } else {
                                                        MemberInfo memberinfo = new MemberInfo(1);
                                                        if (user != null ){
                                                            db.collection("users").document(user.getUid()).set(memberinfo)
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
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                                //자동 로그인 체크 false 시
                                else{
                                    DocumentReference docRef = db.collection("users").document(user.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null) {
                                                    if (document.exists()) {
                                                        DocumentReference washingtonRef = db.collection("users").document(user.getUid());
                                                        washingtonRef
                                                                .update("autoLogin",0)
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
                                                    } else {
                                                        MemberInfo memberinfo = new MemberInfo(0);
                                                        if (user != null ){
                                                            db.collection("users").document(user.getUid()).set(memberinfo)
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
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                                if (saveId.isChecked()){
                                    DocumentReference washingtonRef2 = db.collection("userId").document("saveId");
                                    washingtonRef2
                                            .update("Id",email)
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
                                } else {
                                    DocumentReference washingtonRef2 = db.collection("userId").document("saveId");
                                    washingtonRef2
                                            .update("Id","이메일")
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
                                startActivity(MainActivity.class);
                               } else {
                                if (task.getException() != null) {
                                    startToast(task.getException().toString());
                                }
                            }
                        }
                    });
        }
        else {
            startToast("빈칸이 존재하면 안됩니다");
        }
    }
    private void checkId(){
        DocumentReference docRef = db.collection("userId").document("saveId");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.getData().get("Id").toString().length()>4){
                        email_edt.setText(document.getData().get("Id").toString());
                    }
                } else {
                }
            }
        });
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
