package com.example.citymanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CityDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaceAdapter adapter;
    private String[] placeNames;
    private int[][] placeImages;
    private String[] placeDescriptions;
    private String[] placeCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        // Get the city name from the intent
        String cityName = getIntent().getStringExtra("CITY_NAME");

        // Initialize arrays for places based on the selected city
        if (cityName.equals("Nashik")) {
            placeNames = new String[]{
                    "Harihar", "Pandavleni Caves", "Sula Vineyards", "Magnum Multispeciality Hospital Nashik Road",
                    "The Coffee Bean", "Barista", "Cafe Central", "Coffee World", "SIES High School",
                    "Kendriya Vidyalaya", "Dr. Annie Besant School", "KTHM College", "Nashik City College",
                    "HPT Arts & RYK Science College", "BYK College of Commerce",
                    "R H Sapat College of Engineering And Management Studies And Research",
                    "RNC Arts, JDB Commerce & NSC Science College"
            };
            placeImages = new int[][]{
                    {R.drawable.harihar, R.drawable.harihar_2, R.drawable.harihar_3},
                    {R.drawable.pandavleni_1, R.drawable.pandavleni_3},
                    {R.drawable.sula_vineyards_1, R.drawable.sula_vineyards_2, R.drawable.sula_vineyards_3},
                    {R.drawable.coming_soon},
                    {R.drawable.coming_soon},
                    {R.drawable.coffee_shop_2, R.drawable.coffee_shop_1},
                    {R.drawable.coming_soon},
                    {R.drawable.coming_soon},
                    {R.drawable.coming_soon},
                    {R.drawable.kendriya_vidyalaya_1, R.drawable.kendriya_vidyalaya_2, R.drawable.kendriya_vidyalaya_3},
                    {R.drawable.dr_annie_besant_school_1},
                    {R.drawable.kthm_college_1, R.drawable.kthm_college_2, R.drawable.kthm_college_3},
                    {R.drawable.nashik_city_college_1},
                    {R.drawable.coming_soon},  // Placeholder for HPT Arts & RYK Science College
                    {R.drawable.coming_soon},  // Placeholder for BYK College of Commerce
                    {R.drawable.sapat_college_1, R.drawable.sapat_college_2}  // Images for Sapat College
            };
            placeDescriptions = new String[]{
                    "A famous temple dedicated to Lord Shiva.",
                    "Ancient Buddhist caves with stunning architecture.",
                    "A popular vineyard and winery.",
                    "A renowned multispeciality hospital.",
                    "A popular coffee shop.",
                    "A popular coffee chain with a wide range of coffee options.",
                    "A cozy cafe serving a variety of coffee and snacks.",
                    "A popular coffee shop with a wide range of coffee blends.",
                    "One of the oldest and most reputed schools.",
                    "A central government school.",
                    "A co-educational school.",
                    "A prominent educational institution offering various undergraduate and postgraduate courses.",
                    "Well-known for its academic excellence and range of courses.",
                    "Offering various programs in arts and science disciplines.",
                    "Specializes in commerce education, offering a variety of courses.",
                    "Focuses on engineering and management programs.",
                    "Offers programs in arts, commerce, and science."
            };
            placeCategories = new String[]{
                    "Temple", "Cave", "Winery", "Hospital", "Coffee Shop", "Coffee Shop", "Cafe", "Coffee Shop",
                    "School", "School", "School", "College", "College", "College", "College", "College"
            };
        } else {
            placeNames = new String[]{"Unknown Place"};
            placeImages = new int[][]{{R.drawable.coming_soon}};
            placeDescriptions = new String[]{"Description not available."};
            placeCategories = new String[]{"Unknown"};
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_view_places);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAdapter(this, placeNames, placeImages, placeDescriptions, placeCategories);
        recyclerView.setAdapter(adapter);

        // Set up Spinner for category filtering
        Spinner categorySpinner = findViewById(R.id.category_spinner);

        // Set the prompt or hint for spinner heading
        categorySpinner.setPrompt("Select a Category");

        // Set up Spinner adapter with sorted categories
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getSortedCategories());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        // Set listener for spinner item selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                filterPlacesByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Show all places if nothing is selected
                adapter.updateData(placeNames, placeImages, placeDescriptions, placeCategories);
            }
        });

        // Display all places initially
        adapter.updateData(placeNames, placeImages, placeDescriptions, placeCategories);
    }

    // Get unique categories sorted alphabetically
    private String[] getSortedCategories() {
        List<String> uniqueCategories = new ArrayList<>();
        for (String category : placeCategories) {
            if (!uniqueCategories.contains(category)) {
                uniqueCategories.add(category);
            }
        }

        // Sort categories alphabetically
        Collections.sort(uniqueCategories);

        // Add "Sort" as the first option to show all places by default
        uniqueCategories.add(0, "Sort:All");

        return uniqueCategories.toArray(new String[0]);
    }

    // Filter places by the selected category
    private void filterPlacesByCategory(String category) {
        if (category.equals("Sort:All")) {
            adapter.updateData(placeNames, placeImages, placeDescriptions, placeCategories);
            return;
        }

        List<String> filteredNames = new ArrayList<>();
        List<int[]> filteredImages = new ArrayList<>();
        List<String> filteredDescriptions = new ArrayList<>();
        List<String> filteredCategories = new ArrayList<>();

        for (int i = 0; i < placeCategories.length; i++) {
            if (placeCategories[i].equals(category)) {
                filteredNames.add(placeNames[i]);
                filteredImages.add(placeImages[i]);
                filteredDescriptions.add(placeDescriptions[i]);
                filteredCategories.add(placeCategories[i]);
            }
        }

        adapter.updateData(
                filteredNames.toArray(new String[0]),
                filteredImages.toArray(new int[0][0]),
                filteredDescriptions.toArray(new String[0]),
                filteredCategories.toArray(new String[0])
        );
    }
}
