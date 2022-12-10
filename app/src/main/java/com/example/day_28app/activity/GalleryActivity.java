package com.example.day_28app.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.day_28app.R;
import com.example.day_28app.adapter.GalleryAdapter;

public class GalleryActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] myDataset = {"강아지","고양이","드래곤","치킨"};
        mAdapter = new GalleryAdapter(myDataset);
        recyclerView.setAdapter(mAdapter );
    }
}
