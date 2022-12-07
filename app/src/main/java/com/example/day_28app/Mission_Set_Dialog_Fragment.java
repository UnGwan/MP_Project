package com.example.day_28app;

import static com.example.day_28app.Main_home.missionCheckPoint;
import static com.example.day_28app.Main_home.weeks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Mission_Set_Dialog_Fragment extends DialogFragment {

    private static final String TAG = "Mission_Set_Dialog_Fragment";
    public static final String TAG_EVENT_DIALOG = "dialog_event";


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText missionEdt;
    Button okBtn;
    Button cancelBtn;

    public Mission_Set_Dialog_Fragment() {
    }

    public static Mission_Set_Dialog_Fragment getInstance() {
        Mission_Set_Dialog_Fragment m = new Mission_Set_Dialog_Fragment();
        return m;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mission_set_dialog, container);

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
        String mission = missionEdt.getText().toString();
        DocumentReference washingtonRef = db.collection("userMission").document(user.getUid());
        washingtonRef
                .update("mission"+(weeks+1), mission,"checkPoint",weeks+1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getActivity(), Main_mission_1weeks_Activity.class);
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


    }
}
