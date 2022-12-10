package com.example.day_28app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.day_28app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_login_btn).setOnClickListener(onClickListener);
        findViewById(R.id.login_forget_password_text).setOnClickListener(onClickListener);
        findViewById(R.id.login_sign_btn).setOnClickListener(onClickListener);
        mAuth = FirebaseAuth.getInstance();

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
        String email = ((EditText) findViewById(R.id.login_idEditTxt)).getText().toString();
        String password = ((EditText) findViewById(R.id.login_passwordEditTxt)).getText().toString();
        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
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

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void startActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
