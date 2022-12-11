package com.example.day_28app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.day_28app.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private ArrayList<String> localDataSet;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            cardView = (CardView) view.findViewById(R.id.cardView);
        }

        public CardView getCardView() {
            return cardView;
        }
    }
     public GalleryAdapter(Activity activity,ArrayList<String> dataSet) {
        localDataSet = dataSet;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ltem_galley, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(cardView );
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("photoShotPath",localDataSet.get(viewHolder.getAdapterPosition()));
                activity.setResult(Activity.RESULT_OK,resultIntent);
                activity.finish();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        TextView textView = viewHolder.cardView.findViewById(R.id.galley_txt);
//        textView.setText(localDataSet.get(position));
        CardView cardView = viewHolder.cardView;
        ImageView imageView = cardView.findViewById(R.id.galley_img);
//
        Glide.with(activity).load(localDataSet.get(position)).centerCrop().override(500).into(imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
