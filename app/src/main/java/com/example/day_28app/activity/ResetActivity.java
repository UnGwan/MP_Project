package com.example.day_28app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.day_28app.MemberInfo;
import com.example.day_28app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResetActivity extends AppCompatActivity {
    private static final String TAG = "MemberInitActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);

        findViewById(R.id.set_username_btn).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.set_username_btn:
                    profileUpdate();

                    break;
            }

        }
    };

    private void profileUpdate() {
        String username = ((EditText) findViewById(R.id.set_username_editTxt)).getText().toString();
        String defaultMission = "미션을 설정해주세요";

        if (username.length() > 0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            MemberInfo memberinfo = new MemberInfo(username,"1주차"+defaultMission,"2주차"+defaultMission,"3주차"+defaultMission,"4주차"+defaultMission,0,0);
            if (user != null ){
                DocumentReference washingtonRef = db.collection("users").document(user.getUid());
                washingtonRef
                        .update("checkMissionWeeks",0,"checkSetMission",0,"mission1","1주차"+defaultMission,"mission2","2주차"+defaultMission,"mission3","3주차"+defaultMission,"mission4","4주차"+defaultMission,"name",username)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d("무야호:", "DocumentSnapshot successfully updated!");
                                startToast("새싹의 이름이 정해졌습니다");
                                finish();
                                startActivity(MainActivity.class);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("무야호", "Error updating document", e);
                                startToast("새싹의 이름 등록을 실패했습니다");
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        } else {
            startToast("새싹의 이름을 정해주세요");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
