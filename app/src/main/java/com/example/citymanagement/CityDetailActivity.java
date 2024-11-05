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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaceAdapter adapter;
    private String[] placeNames;
    private int[][] placeImages;
    private String[] placeDescriptions;
    private String[] placeCategories;

    private Spinner categorySpinner;
    private Spinner subcategorySpinner;
    private Map<String, String[]> subcategoryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        String cityName = getIntent().getStringExtra("CITY_NAME");
        setupNashikPlaces(cityName); // Now it accepts cityName

        recyclerView = findViewById(R.id.recycler_view_places);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAdapter(this, placeNames, placeImages, placeDescriptions, placeCategories);
        recyclerView.setAdapter(adapter);

        setupSpinners();
    }

    // Method to set up places for Nashik
    private void setupNashikPlaces(String cityName) {
        // Here you can customize the logic for different cities based on cityName
        placeNames = new String[]{
                "Harihar", "Pandavleni Caves", "Sula Vineyards", "Magnum Multispeciality Hospital Nashik Road",
                "The Coffee Bean", "Barista", "Cafe Central", "Coffee World", "SIES High School",
                "Kendriya Vidyalaya", "Dr. Annie Besant School", "KTHM College", "Nashik City College",
                "HPT Arts & RYK Science College", "BYK College of Commerce",
                "R H Sapat College of Engineering And Management Studies And Research",
                "RNC Arts, JDB Commerce & NSC Science College",
                "Barbeque Nation", "The Great Punjab", "Rude Lounge",
                "Barbeque Ville", "Bistro Grill", "Little Italy", "Ghar Ka Chula",
                "Shahi Durbar", "Monginis", "Cafe Coffee Day", "Sushi & More",
                "Haveli Restaurant", "Taste of Punjab", "KFC", "Domino's Pizza",
                "Gandhi Park", "Pandav Leni Park", "Sula Vineyards Garden", "Nashik Garden",
                "Nashik Lake Garden", "Chetna Park", "Sula Vineyards", "Bhandardara Park",
                "Khandala Park", "Kumar Park"
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
                {R.drawable.sapat_college_1, R.drawable.sapat_college_2}, // Images for Sapat College
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
                {R.drawable.coming_soon},
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
                "Offers programs in arts, commerce, and science.",
                "A popular chain known for its buffet grills.",
                "North Indian cuisine with a great ambiance.",
                "Famous coffee house with a variety of brews.",
                "Casual dining with a lively atmosphere.",
                "Specializes in BBQ dishes and grills.",
                "Known for its grilled delicacies.",
                "Authentic Italian cuisine and pizzas.",
                "Homely food with traditional Indian flavors.",
                "Offers Mughlai and North Indian dishes.",
                "Bakery famous for its cakes and pastries.",
                "Popular coffee shop chain with a cozy vibe.",
                "Serves sushi and Japanese cuisine.",
                "Traditional Indian restaurant with a cultural touch.",
                "Delicious Punjabi dishes in a cozy setting.",
                "Fast food chain known for its fried chicken.",
                "Popular pizza delivery and dine-in chain.",
                "A beautifully landscaped park ideal for morning walks.",
                "A scenic park near the Pandav Leni caves, perfect for picnics.",
                "Garden area within Sula Vineyards, offering a lovely view.",
                "A well-maintained garden in the heart of Nashik.",
                "A serene lakeside garden, perfect for relaxation.",
                "A local park with facilities for children and families.",
                "Known for its picturesque vineyards and gardens.",
                "A peaceful park in the Bhandardara region.",
                "A small park perfect for families and kids.",
                "A community park with recreational facilities."
        };

        placeCategories = new String[]{
                "Visitor", "Visitor", "Visitor", "Visitor", "Visitor", "Visitor",
                "Visitor", "Visitor", "Visitor", "Visitor", "Visitor", "Visitor",
                "Visitor", "Visitor", "Local", "Local", "Local", "Local",
                "Local", "Local", "Local", "Local", "Local", "Local", "Local",
                "Local", "Local", "Local", "Local", "Local", "Local", "Local",
                "Local", "Local", "Local", "Local", "Local", "Local"
        };
    }

    private void setupSpinners() {
        categorySpinner = findViewById(R.id.category_spinner);
        subcategorySpinner = findViewById(R.id.subcategory_spinner);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"All", "Visitor", "Student", "Local"});
        categorySpinner.setAdapter(categoryAdapter);

        setupSubcategories();
        categorySpinner.setOnItemSelectedListener(new CategorySelectionListener());
        subcategorySpinner.setOnItemSelectedListener(new SubcategorySelectionListener());

        adapter.updateData(placeNames, placeImages, placeDescriptions, placeCategories);
    }

    private void setupSubcategories() {
        subcategoryMap = new HashMap<>();
        subcategoryMap.put("Visitor", new String[]{"Attractions", "Parks"});
        subcategoryMap.put("Student", new String[]{"Schools", "Colleges"});
        subcategoryMap.put("Local", new String[]{"Cafes", "Restaurants","Colleges","Schools"});
    }

    private class CategorySelectionListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedCategory = (String) parent.getItemAtPosition(position);
            if ("All".equals(selectedCategory)) {
                subcategorySpinner.setVisibility(View.GONE);
                adapter.updateData(placeNames, placeImages, placeDescriptions, placeCategories);
            } else {
                subcategorySpinner.setVisibility(View.VISIBLE);
                setupSubcategoryAdapter(selectedCategory);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            subcategorySpinner.setVisibility(View.GONE);
        }
    }

    private class SubcategorySelectionListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedSubcategory = (String) parent.getItemAtPosition(position);
            filterPlacesBySubcategory(selectedSubcategory);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            adapter.updateData(placeNames, placeImages, placeDescriptions, placeCategories);
        }
    }

    private void setupSubcategoryAdapter(String category) {
        String[] subcategories = subcategoryMap.get(category);
        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subcategories);
        subcategorySpinner.setAdapter(subcategoryAdapter);
    }

    private void filterPlacesBySubcategory(String subcategory) {
        List<String> filteredNames = new ArrayList<>();
        List<int[]> filteredImages = new ArrayList<>();
        List<String> filteredDescriptions = new ArrayList<>();
        List<String> filteredCategories = new ArrayList<>();

        for (int i = 0; i < placeCategories.length; i++) {
            boolean matches = false;
            switch (subcategory) {
                case "Attractions":
                    matches = (placeCategories[i].equals("Local") || placeCategories[i].equals("Visitor")) && (placeNames[i].equals("Pandavleni Caves") ||
                            placeNames[i].equals("Sula Vineyards") ||
                            placeNames[i].equals("Harihar"));
                    break;
                case "Parks":
                    matches = (placeCategories[i].equals("Local") || placeCategories[i].equals("Visitor")) &&
                            (placeNames[i].equals("Gandhi Park") ||
                                    placeNames[i].equals("Pandav Leni Park") ||
                                    placeNames[i].equals("Nashik Garden") ||
                                    placeNames[i].equals("Nashik Lake Garden") ||
                                    placeNames[i].equals("Chetna Park") ||
                                    placeNames[i].equals("Bhandardara Park") ||
                                    placeNames[i].equals("Khandala Park") ||
                                    placeNames[i].equals("Kumar Park"));
                    break;
                case "Schools":
                    matches = (placeCategories[i].equals("Local") || placeCategories[i].equals("Student")) &&
                            (placeNames[i].equals("SIES High School") ||
                                    placeNames[i].equals("Kendriya Vidyalaya") ||
                                    placeNames[i].equals("Dr. Annie Besant School"));
                    break;
                case "Colleges":
                    matches = (placeCategories[i].equals("Local") || placeCategories[i].equals("Student")) &&
                            (placeNames[i].equals("KTHM College") ||
                                    placeNames[i].equals("Nashik City College") ||
                                    placeNames[i].equals("HPT Arts & RYK Science College") ||
                                    placeNames[i].equals("BYK College of Commerce") ||
                                    placeNames[i].equals("R H Sapat College of Engineering And Management Studies And Research") ||
                                    placeNames[i].equals("RNC Arts, JDB Commerce & NSC Science College"));
                    break;
                case "Cafes":
                    matches = (placeCategories[i].equals("Local") || placeCategories[i].equals("Visitor")) &&
                            (placeNames[i].equals("The Coffee Bean") ||
                                    placeNames[i].equals("Barista") ||
                                    placeNames[i].equals("Cafe Central") ||
                                    placeNames[i].equals("Coffee World") ||
                                    placeNames[i].equals("Barbeque Nation") ||
                                    placeNames[i].equals("Little Italy") ||
                                    placeNames[i].equals("Ghar Ka Chula"));
                    break;
                case "Restaurants":
                    matches = (placeCategories[i].equals("Local") || placeCategories[i].equals("Visitor")) &&
                            (placeNames[i].equals("Taste of Punjab") ||
                                    placeNames[i].equals("KFC") ||
                                    placeNames[i].equals("Domino's Pizza") ||
                                    placeNames[i].equals("Shahi Durbar") ||
                                    placeNames[i].equals("Monginis") ||
                                    placeNames[i].equals("Sushi & More") ||
                                    placeNames[i].equals("Barbeque Ville") ||
                                    placeNames[i].equals("Rude Lounge") ||
                                    placeNames[i].equals("The Great Punjab") ||
                                    placeNames[i].equals("Bistro Grill"));
                    break;
            }

            if (matches) {
                filteredNames.add(placeNames[i]);
                filteredImages.add(placeImages[i]);
                filteredDescriptions.add(placeDescriptions[i]);
                filteredCategories.add(placeCategories[i]);
            }
        }

        adapter.updateData(
                filteredNames.toArray(new String[0]),
                filteredImages.toArray(new int[0][]),
                filteredDescriptions.toArray(new String[0]),
                filteredCategories.toArray(new String[0])
        );
    }

}
