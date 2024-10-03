package com.example.citymanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private Context context;
    private String[] placeNames;
    private int[][] placeImages;  // 2D array to hold multiple images for each place
    private String[] placeDescriptions;
    private String[] placeCategories;

    public PlaceAdapter(Context context, String[] placeNames, int[][] placeImages, String[] placeDescriptions, String[] placeCategories) {
        this.context = context;
        this.placeNames = placeNames;
        this.placeImages = placeImages;
        this.placeDescriptions = placeDescriptions;
        this.placeCategories = placeCategories;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_place_list_item, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.placeNameTextView.setText(placeNames[position]);
        holder.placeCategoryTextView.setText(placeCategories[position]);

        // Set up ViewPager2 for image sliding
        List<Integer> imagesList = new ArrayList<>();
        for (int image : placeImages[position]) {
            imagesList.add(image);
        }

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(imagesList);
        holder.placeImageViewPager.setAdapter(imageSliderAdapter);

        // Auto-scroll feature for the ViewPager2
        holder.sliderHandler.postDelayed(holder.sliderRunnable, 3000);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlaceDetailActivity.class);
            intent.putExtra("PLACE_NAME", placeNames[position]);
            intent.putExtra("PLACE_IMAGES", placeImages[position]);
            intent.putExtra("PLACE_DESCRIPTION", placeDescriptions[position]);
            intent.putExtra("PLACE_CATEGORY", placeCategories[position]);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return placeNames.length;
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        ViewPager2 placeImageViewPager;
        TextView placeNameTextView;
        TextView placeCategoryTextView;
        Handler sliderHandler = new Handler(Looper.getMainLooper());
        Runnable sliderRunnable;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImageViewPager = itemView.findViewById(R.id.place_image_viewpager);
            placeNameTextView = itemView.findViewById(R.id.place_name);
            placeCategoryTextView = itemView.findViewById(R.id.place_category);

            // Runnable for auto-scrolling images
            sliderRunnable = new Runnable() {
                @Override
                public void run() {
                    int nextItem = placeImageViewPager.getCurrentItem() + 1;
                    if (nextItem >= placeImageViewPager.getAdapter().getItemCount()) {
                        nextItem = 0;  // Loop back to the first image
                    }
                    placeImageViewPager.setCurrentItem(nextItem);
                    sliderHandler.postDelayed(this, 3000);  // Continue auto-scrolling
                }
            };

            // Register a callback to stop and restart the auto-scroll on page change
            placeImageViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    sliderHandler.removeCallbacks(sliderRunnable);
                    sliderHandler.postDelayed(sliderRunnable, 3000);
                }
            });
        }
    }

    // Method to update the data in the adapter
    public void updateData(String[] newPlaceNames, int[][] newPlaceImages, String[] newPlaceDescriptions, String[] newPlaceCategories) {
        this.placeNames = newPlaceNames;
        this.placeImages = newPlaceImages;
        this.placeDescriptions = newPlaceDescriptions;
        this.placeCategories = newPlaceCategories;
        notifyDataSetChanged();
    }
}
