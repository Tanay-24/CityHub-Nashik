package com.example.citymanagement;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample data
        String[] cityNames = {"Nashik", "Mumbai","Pune","Thane","Nagpur"};
        int[] cityImages = {R.drawable.nashik, R.drawable.coming_soon,R.drawable.coming_soon,R.drawable.coming_soon,R.drawable.coming_soon};

        // Find RecyclerView and set adapter
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CityAdapter adapter = new CityAdapter(this, cityNames, cityImages);
        recyclerView.setAdapter(adapter);

    }
}
