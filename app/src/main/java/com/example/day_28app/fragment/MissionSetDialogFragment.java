package com.example.day_28app.fragment;

import static com.example.day_28app.fragment.MainHomeFragment.weeks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.day_28app.R;
import com.example.day_28app.activity.MainMissionActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MissionSetDialogFragment extends DialogFragment {

    private static final String TAG = "Mission_Set_Dialog_Fragment";
    public static final String TAG_EVENT_DIALOG = "dialog_event";



    EditText missionEdt;
    Button okBtn;
    Button cancelBtn;

    public MissionSetDialogFragment() {
    }

    public static MissionSetDialogFragment getInstance() {
        MissionSetDialogFragment m = new MissionSetDialogFragment();
        return m;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_mission_set, container);

        missionEdt = (EditText) v.findViewById(R.id.mission_set_edt);
        okBtn = (Button) v.findViewById(R.id.mission_set_ok_btn);
        cancelBtn = (Button) v.findViewById(R.id.mission_set_cancel_btn);

        cancelBtn.setOnClickListener(onClickListener);
        okBtn.setOnClickListener(onClickListener);

        setCancelable(false);
        return v;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mission_set_cancel_btn:
                    dismiss();
                    break;
                case R.id.mission_set_ok_btn:
                    okAction();
                    break;
            }
        }
    };

    private void okAction() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String mission = missionEdt.getText().toString();
        if (mission.length()>0){
            if (user != null){
                DocumentReference washingtonRef = db.collection("users").document(user.getUid());
                washingtonRef
                        .update("mission"+(weeks+1), mission,"checkSetMission",weeks+1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(getActivity(), MainMissionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("무야호", "Error updating document", e);
                            }
                        });
            } else {
                Log.e("왜","갑자기안되는데");
            }
        } else {
            Toast.makeText(getActivity(), "미션을 설정해주세요", Toast.LENGTH_SHORT).show();
        }

    }


}
