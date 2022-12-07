package com.example.day_28app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Main1_Activity extends AppCompatActivity {
    private static final String TAG = "MemberInitActivity";

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Main_home fragmentHome = new Main_home();
    private Main_feed fragmentFeed = new Main_feed();
    private Main_mypage fragmentMyPage = new Main_mypage();
    private FirebaseAuth mAuth;

    //Main
    TextView top_txt;
    public static String username;

    //Feed

    //MyPage

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);


        //하단 네비게이션바 구현
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        //파이어베이스
        mAuth = FirebaseAuth.getInstance();
        setInit();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //fragment 전환
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_home:
                    setInit();
                    transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();
                    break;
                case R.id.menu_feed:
                    transaction.replace(R.id.menu_frame_layout, fragmentFeed).commitAllowingStateLoss();
                    break;
                case R.id.menu_my:
                    transaction.replace(R.id.menu_frame_layout, fragmentMyPage).commitAllowingStateLoss();
                    break;

            }

            return true;
        }
    }

    //public
    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //Main
    private void setInit(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //로그인 여부
        if (user == null) {
            startActivity(Intro_Activity.class);
            finish();
        } else {
//            startActivity(CameraActivity.class);
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                username = document.getData().get("name").toString();
                                top_txt = (TextView) findViewById(R.id.main_top_text);
                                top_txt.setText(username + "님 \n오늘도 반가워요 ");
                            } else {
                                finish();
                                Log.d(TAG, "No such document");
                                startActivity(Set_Username_Activity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }
    }
    //Feed

    //MyPage


}
