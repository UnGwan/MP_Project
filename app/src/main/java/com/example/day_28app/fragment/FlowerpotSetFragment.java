package com.example.day_28app.fragment;

import static com.example.day_28app.activity.MainMissionActivity.checkingDay;
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

import com.example.day_28app.MemberID;
import com.example.day_28app.MemberSaveDiary;
import com.example.day_28app.R;
import com.example.day_28app.activity.MainActivity;
import com.example.day_28app.activity.MainMissionActivity;
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

public class FlowerpotSetFragment extends DialogFragment {

    private static final String TAG = "Mission_Set_Dialog_Fragment";
    public static final String TAG_EVENT_DIALOG = "dialog_event";



    EditText nameEdt;
    Button okBtn;
    Button cancelBtn;



    public FlowerpotSetFragment() {
    }

    public static FlowerpotSetFragment getInstance() {
        FlowerpotSetFragment m = new FlowerpotSetFragment();
        return m;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.dialog_flowerpot_set, container);

        nameEdt = (EditText) v.findViewById(R.id.flowerpot_name_set_edt);
        okBtn = (Button) v.findViewById(R.id.flowerpot_set_ok_btn);
        cancelBtn = (Button) v.findViewById(R.id.flowerpot_set_cancel_btn);

        cancelBtn.setOnClickListener(onClickListener);
        okBtn.setOnClickListener(onClickListener);


        setCancelable(false);
        return v;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.flowerpot_set_cancel_btn:
                    break;
                case R.id.flowerpot_set_ok_btn:
                    okAction();
                    startActivity(MainActivity.class);
                    startToast("저장소에 일기가 등록되었습니다.");
                    break;
            }
        }
    };

    private void okAction() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> defaultData = new HashMap<>();
        String diaryName = nameEdt.getText().toString();
        for (int i = 1 ; i<5; i++){
            DocumentReference docRef = db.collection("userDiary"+i+"weeks").document(user.getUid());
            int finalI = i;
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //저장된 일기 갯수 받아오기
                            for (int j = 1;j<8;j++){
                                String day = "diary"+(j);
                                defaultData.put(+(finalI)+"주"+(j)+"일"+"diary",((HashMap<String, Object>) document.getData().get(day)).get("diary").toString());
                                if (((HashMap<String, Object>) document.getData().get(day)).get("photoShotUrl") != null){
                                    defaultData.put(+(finalI)+"주"+(j)+"일"+"photoShotUrl",((HashMap<String, Object>) document.getData().get(day)).get("photoShotUrl").toString());
                                } else {
                                    defaultData.put(+(finalI)+"주"+(j)+"일"+"photoShotUrl","https://firebasestorage.googleapis.com/v0/b/mp-project-c22a2.appspot.com/o/users%2F%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202022-12-07%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%206.18.21.png?alt=media&token=dbc189c5-01f9-4ed7-95fd-c8f44cc84394");
                                }
                            }

                            if (diaryName.length()>0){
                                if (user != null){
                                    DocumentReference docRef = db.collection("users").document(user.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    int countDiary = Integer.parseInt(document.getData().get("countDiary").toString());
                                                    defaultData.put("일기이름",diaryName);
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                    DocumentReference washingtonRef = db.collection("saveDiary").document(user.getUid());
                                                    washingtonRef
                                                            .update("diary"+countDiary,defaultData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    DocumentReference washingtonRef = db.collection("users").document(user.getUid());
                                                                    washingtonRef
                                                                            .update("countDiary",countDiary+1)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Log.d("무야호:", "DocumentSnapshot successfully updated!");
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Log.w("무야호", "Error updating document", e);
                                                                                }
                                                                            });
                                                                    Log.d("무야호:", "DocumentSnapshot successfully updated!");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("무야호", "Error updating document", e);
                                                                }
                                                            });
                                                } else {
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });

                                } else {
                                }
                            } else {
                                Toast.makeText(getActivity(), "미션을 설정해주세요", Toast.LENGTH_SHORT).show();
                            }

                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }


    }
    private void startActivity(Class c){
        Intent intent = new Intent(getActivity(),c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void startToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


}
