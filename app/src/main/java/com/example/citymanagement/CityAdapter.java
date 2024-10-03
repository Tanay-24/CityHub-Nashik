// src/main/java/com/example/citymanagement/CityAdapter.java
package com.example.citymanagement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private Context context;
    private String[] cityNames;
    private int[] cityImages;

    public CityAdapter(Context context, String[] cityNames, int[] cityImages) {
        this.context = context;
        this.cityNames = cityNames;
        this.cityImages = cityImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_city_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cityName.setText(cityNames[position]);
        holder.cityImage.setImageResource(cityImages[position]);

        // Set an onClick listener to navigate to the next activity when the image is clicked
        holder.cityImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, CityDetailActivity.class);
            // Optionally, you can pass data to the next activity if needed
            intent.putExtra("CITY_NAME", cityNames[position]);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cityNames.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cityImage;
        TextView cityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityImage = itemView.findViewById(R.id.city_image);
            cityName = itemView.findViewById(R.id.city_name);
        }
    }
}
