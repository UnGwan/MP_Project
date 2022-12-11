package com.example.day_28app.fragment;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.day_28app.R;
import com.example.day_28app.activity.CameraActivity;
import com.example.day_28app.activity.GalleryActivity;
import com.example.day_28app.activity.MainMissionCompleteActivity;
import com.example.day_28app.activity.SaveDiaryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFeedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ImageView profileIcon;
    TextView top_txt;


    private int[] icon_id = {R.drawable.main1_icon_profile_0weeks,R.drawable.main1_icon_profile_1weeks,R.drawable.main1_icon_profile_2weeks,R.drawable.main1_icon_profile_3weeks,R.drawable.main1_icon_profile_4weeks};


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView diaryNameTxt;
    private ImageButton imageButton;

    public MainFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main_feed.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFeedFragment newInstance(String param1, String param2) {
        MainFeedFragment fragment = new MainFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main_feed, container, false);
        diaryNameTxt = rootView.findViewById(R.id.save_diary_txt1);
        profileIcon = rootView.findViewById(R.id.main_home_profile_icon);
        imageButton = rootView.findViewById(R.id.save_diary_btn1);
        imageButton.setOnClickListener(onClickListener);
        getDiaryName();
//        setInit();
        return rootView;
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.save_diary_btn1:
                    startActivity(SaveDiaryActivity.class);
            }
        }
    };

    private void startActivity(Class c){
        Intent intent = new Intent(getActivity(),c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void getDiaryName(){
        DocumentReference docRef = db.collection("saveDiary").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            if (((HashMap<String, Object>) document.getData().get("diary1")).get("일기이름")!= null){
                                imageButton.setVisibility(View.VISIBLE);
                                diaryNameTxt.setText(((HashMap<String, Object>) document.getData().get("diary1")).get("일기이름").toString());
                            }
                            else {
                            }
                        } else {
                            getActivity().finish();
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setInit(){
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //미션 설정여부 확인

                    //진행도에 따른 프로필 아이콘 설정
                    profileIcon.setImageResource(icon_id[Integer.parseInt(document.getData().get("checkMissionWeeks").toString())]);
                }
            }
        });
    }
}