package com.example.day_28app.fragment;

import static android.content.ContentValues.TAG;
import static com.example.day_28app.fragment.MissionSetDialogFragment.TAG_EVENT_DIALOG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.day_28app.MemberSaveDiary;
import com.example.day_28app.activity.IntroActivity;
import com.example.day_28app.activity.MainMissionActivity;
import com.example.day_28app.R;
import com.example.day_28app.activity.SetUsernameActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int[] m_txt_id = {R.id.mission1_txt,R.id.mission2_txt,R.id.mission3_txt,R.id.mission4_txt};
    private int[] p_txt_id = {R.id.progress_1weeks_txt,R.id.progress_2weeks_txt,R.id.progress_3weeks_txt,R.id.progress_4weeks_txt};
    private int[] mv_txt_id = {R.id.main_home_motivation_txt1,R.id.main_home_motivation_txt2,R.id.main_home_motivation_txt3,R.id.main_home_motivation_txt4};
    private int[] bar_id = {R.id.progressbar1,R.id.progressbar2,R.id.progressbar3,R.id.progressbar4};
    private int[] btn_id = {R.id.main_1weeks_Btn,R.id.main_2weeks_Btn,R.id.main_3weeks_Btn,R.id.main_4weeks_Btn};
    private int[] lay_id = {R.id.main_1weeks_layout,R.id.main_2weeks_layout,R.id.main_3weeks_layout,R.id.main_4weeks_layout};
    private int[] icon_id = {R.drawable.main1_icon_profile_0weeks,R.drawable.main1_icon_profile_1weeks,R.drawable.main1_icon_profile_2weeks,R.drawable.main1_icon_profile_3weeks,R.drawable.main1_icon_profile_4weeks};



    public static int weeks,missionCheckPoint;

    TextView[] missionTxt = new TextView[4];
    TextView[] progressTxt = new TextView[4];
    TextView[] motivationTxt = new TextView[4];

    ProgressBar[] progressBars = new ProgressBar[4];
    RelativeLayout[] missionLayout = new RelativeLayout[4];

    ImageView profileIcon;
    TextView top_txt;
    ViewGroup rootView;
    public static String username;
    private static int autoLoginPoint;

    private FirebaseAuth mAuth;

    public MainHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main_home.
     */
    // TODO: Rename and change types and number of parameters
    public static MainHomeFragment newInstance(String param1, String param2) {
        MainHomeFragment fragment = new MainHomeFragment();
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
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main_home, container, false);


        profileIcon = rootView.findViewById(R.id.main_home_profile_icon);

        for (int i = 0 ;i<4;i++){
            missionTxt[i]= (TextView) rootView.findViewById(m_txt_id[i]);
            progressTxt[i] = (TextView)rootView.findViewById(p_txt_id[i]);
            motivationTxt[i] = (TextView)rootView.findViewById(mv_txt_id[i]);

            missionLayout[i] = (RelativeLayout)rootView.findViewById(lay_id[i]);
            progressBars[i] = (ProgressBar)rootView.findViewById(bar_id[i]);

            rootView.findViewById(btn_id[i]).setOnClickListener(onClickListener);
        }

        setAccept();
//        setInit();
//        updateBtnVisibility();
        return rootView;   // Inflate the layout for this fragment
    }

    @Override
    public void onStart() {
        super.onStart();
        setAccept();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int i = 0; i<4;i++){
                if (view.getId() ==btn_id[i] ){
                        weeks = i;
                    if (weeks<missionCheckPoint){
                        Log.d("확인2","값:"+missionCheckPoint);
                        Intent intent = new Intent(getActivity(), MainMissionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        MissionSetDialogFragment m = MissionSetDialogFragment.getInstance();
                        m.show(getFragmentManager(),TAG_EVENT_DIALOG);
                    }
                }
            }
        }
    };
    private void startActivity(Class c){
        Intent intent = new Intent(getActivity(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void setInit(){
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    //미션 내용 확인
                    for (int i = 0 ;i<4;i++){
                        missionTxt[i].setText(document.getData().get("mission" + (i + 1)).toString());
                        missionTxt[i].setTypeface(null, Typeface.BOLD);
                    }
                    //미션 설정여부 확인
                    missionCheckPoint = Integer.parseInt(document.getData().get("checkSetMission").toString());

                    //진행도에 따른 프로필 아이콘 설정
                    profileIcon.setImageResource(icon_id[Integer.parseInt(document.getData().get("checkMissionWeeks").toString())]);
                }
            }
        });
    }
    public void onTaskRemoved(Intent rootIntent) {

        if (getActivity().isFinishing()){
            Log.d("포인트","로그인포인트"+autoLoginPoint);
            if (autoLoginPoint==0){
                FirebaseAuth.getInstance().signOut();
            }
        }
    }

    private void setAccept() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (user == null) {
            startActivity(IntroActivity.class);
            getActivity().finish();

        } else {
//            FirebaseAuth.getInstance().signOut();
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
                                top_txt = (TextView) rootView.findViewById(R.id.main_top_text);
                                //에러 1
                                top_txt.setText(username + "님 \n오늘도 반가워요 ");
                                DocumentReference docRef2 = db.collection("users").document(user.getUid());
                                docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            autoLoginPoint = Integer.parseInt(document.getData().get("autoLogin").toString());
                                            if (autoLoginPoint==0){
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                                setInit();
                                updateBtnVisibility();
                            } else {
                                getActivity().finish();
                                Log.d(TAG, "No such document");
                                startActivity(SetUsernameActivity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }
    }
    private void updateBtnVisibility(){

        for (int i= 0 ;i<4;i++){
            DocumentReference docRef = db.collection("userDiary"+(i+1)+"weeks").document(user.getUid());
            int finalI = i;
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        //각 주 프로그레스바 진행도 표시
                        progressTxt[finalI].setText(document.getData().get("daySum").toString());
                        progressBars[finalI].setProgress(Integer.parseInt(document.getData().get("daySum").toString())*10);

                        //해당주 해당일 일기를 작성시
                        if(Integer.parseInt(((HashMap<String, Object>) document.getData().get("diary7")).get("dayCheck").toString())==1){
                            // 한 주씩 미션을 성공할때마다 다음주 미션 버튼 활성화 로직
                            if (finalI <3){
                                missionLayout[finalI +1].setVisibility(View.VISIBLE);
                            }

                            //1주일 완성시 프로그레스바 색깔 변화 ,
                            if (Integer.parseInt(document.getData().get("daySum").toString()) == 7){
                                progressBars[finalI].setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FB6502")));
                                progressTxt[finalI].setTextColor(Color.BLACK);
                                motivationTxt[finalI].setText((finalI+1)+"주차 미션을 클리어 했습니다!!");
                                missionTxt[finalI].setTextColor(Color.parseColor("#F7A428"));
                            }
                        }
                    }
                }
            });
        }
    }
}