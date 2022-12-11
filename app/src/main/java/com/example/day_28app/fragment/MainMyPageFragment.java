package com.example.day_28app.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.day_28app.MemberDiary;
import com.example.day_28app.MemberID;
import com.example.day_28app.R;
import com.example.day_28app.activity.LoginActivity;
import com.example.day_28app.activity.ResetActivity;
import com.example.day_28app.activity.SetUsernameActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class MainMyPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ViewGroup rootView;
    TextView nickName;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MainMyPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main_mypage.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMyPageFragment newInstance(String param1, String param2) {
        MainMyPageFragment fragment = new MainMyPageFragment();
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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main_mypage, container, false);
        rootView.findViewById(R.id.main_myPage_logout_btn).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.reset_btn).setOnClickListener(onClickListener);
        nickName = (TextView) rootView.findViewById(R.id.myPage_userName_txt);
        getUserName();

        return rootView;

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_myPage_logout_btn:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
                case R.id.reset_btn:
                    resetAll();
                    break;
            }
        }
    };

    private void resetAll(){
        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("diary", "오늘의 일기를 작성해주세요");
        defaultData.put("dayCheck", 0);
        defaultData.put("photoShotUrl","0");

        MemberDiary memberDiary = new MemberDiary(defaultData,defaultData,defaultData,defaultData,defaultData,defaultData,defaultData,0);
        for (int i = 0 ; i< 4 ; i++){
            db.collection("userDiary"+(i+1)+"weeks").document(user.getUid()).set(memberDiary)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("성공","성공ㅎ했습니다");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("실패","실패했습니다");
                        }
                    });
        }

        Intent intent = new Intent(getActivity(), ResetActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void getUserName(){
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String username = document.getData().get("name").toString();
                            nickName.setText(username);
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

}