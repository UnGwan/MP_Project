package com.example.day_28app;

import static com.example.day_28app.MissionSetDialogFragment.TAG_EVENT_DIALOG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private int[] txt_id = {R.id.mission1_txt,R.id.mission2_txt,R.id.mission3_txt,R.id.mission4_txt};
    private int[] p_txt_id = {R.id.progress_1weeks_txt,R.id.progress_2weeks_txt,R.id.progress_3weeks_txt,R.id.progress_4weeks_txt};
    private int[] bar_id = {R.id.progressbar1,R.id.progressbar2,R.id.progressbar3,R.id.progressbar4};
    private int[] btn_id = {R.id.main_1weeks_Btn,R.id.main_2weeks_Btn,R.id.main_3weeks_Btn,R.id.main_4weeks_Btn};
    private int[] lay_id = {R.id.main_1weeks_layout,R.id.main_2weeks_layout,R.id.main_3weeks_layout,R.id.main_4weeks_layout};



    public static int weeks,missionCheckPoint;

    TextView[] missionTxt = new TextView[4];
    TextView[] progressTxt = new TextView[4];
    ProgressBar[] progressBars = new ProgressBar[4];
    RelativeLayout[] missionLayout = new RelativeLayout[4];




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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main_home, container, false);

        rootView.findViewById(R.id.main_1weeks_Btn).setOnClickListener(onClickListener);
        for (int i = 0 ;i<4;i++){
            missionTxt[i]= (TextView) rootView.findViewById(txt_id[i]);
            missionLayout[i] = (RelativeLayout)rootView.findViewById(lay_id[i]);
            progressTxt[i] = (TextView)rootView.findViewById(p_txt_id[i]);
            progressBars[i] = (ProgressBar)rootView.findViewById(bar_id[i]);

            rootView.findViewById(btn_id[i]).setOnClickListener(onClickListener);
        }

        setInit();
        updateBtnVisibility();
        return rootView;   // Inflate the layout for this fragment
    }

    @Override
    public void onStart() {
        super.onStart();
        setInit();
        updateBtnVisibility();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int i = 0; i<4;i++){
                if (view.getId() ==btn_id[i] ){
                        weeks = i;
                    if (weeks<missionCheckPoint){
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
    private void setInit(){
        DocumentReference docRef = db.collection("userMission").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    for (int i = 0 ;i<4;i++){
                        missionTxt[i].setText(document.getData().get("mission" + (i + 1)).toString());
                    }
                    missionCheckPoint = Integer.parseInt(document.getData().get("checkPoint").toString());

                }
            }
        });
    }

    private void updateBtnVisibility(){
        for (int i= 0 ;i<3;i++){
            DocumentReference docRef = db.collection("userDay"+(i+1)+"weeks").document(user.getUid());
            int finalI = i;
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(Integer.parseInt(document.getData().get("day7").toString())==1){
                            missionLayout[finalI +1].setVisibility(View.VISIBLE);
                        }
//                        progressTxt[weeks].setText(Integer.parseInt(document.getData().get("daySum").toString()));
                    }
                }
            });
        }

        for (int i = 0; i< 4; i++){
            DocumentReference docRef = db.collection("userDay"+(i+1)+"weeks").document(user.getUid());
            int finalI = i;
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        progressTxt[finalI].setText(document.getData().get("daySum").toString());
                        progressBars[finalI].setProgress(Integer.parseInt(document.getData().get("daySum").toString())*10);
                        if (Integer.parseInt(document.getData().get("daySum").toString()) == 7){
                            progressBars[finalI].setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FB6502")));
                            progressTxt[finalI].setTextColor(Color.BLACK);
                        }
                    }
                }
            });
        }


    }


}