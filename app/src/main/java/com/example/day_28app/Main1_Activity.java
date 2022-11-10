package com.example.day_28app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Main1_Activity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Main_home fragmentHome= new Main_home();
    private Main_feed fragmentFeed = new Main_feed();
    private Main_mypage fragmentMyPage = new Main_mypage();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        //로그인이 되있으면 바로 메인 화면 아니면 처음 화면를 실행해주는 메소드
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startFirstActivity();
        }
    }
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_home:
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
    private void startFirstActivity(){
        Intent intent = new Intent(this,First_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
