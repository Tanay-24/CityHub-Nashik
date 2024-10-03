package com.example.citymanagement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TableLayout busInfoTable;
    private MapView mapView;
    private IMapController mapController;
    private Double latitude, longitude;
    private Marker placeMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("places");

        // Initialize views
        ViewPager2 placeImageViewPager = findViewById(R.id.places_image_viewpager);
        TextView placeNameTextView = findViewById(R.id.places_name);
        TextView placeDescriptionTextView = findViewById(R.id.places_description);
        TextView placeDistanceTextView = findViewById(R.id.places_distance);
        TextView placeHistoryTextView = findViewById(R.id.places_history);
        TextView placeAddressTextView = findViewById(R.id.places_address);
        TextView placePhoneNumberTextView = findViewById(R.id.places_phone_number);
        TextView placeWebsiteTextView = findViewById(R.id.places_website);
        busInfoTable = findViewById(R.id.bus_info_table);
        mapView = findViewById(R.id.map_view);

        // Initialize map settings
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(15.0);

        // Make sure the MapView is clickable
        mapView.setClickable(true);
        mapView.setFocusable(true);

        // Get the place details from the intent
        String placeName = getIntent().getStringExtra("PLACE_NAME");
        Log.d("PlaceDetailActivity", "Received place name: " + placeName);

        // Fetch data from Firebase
        if (placeName != null) {
            databaseReference.child(placeName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        try {
                            // Get place data
                            String description = dataSnapshot.child("description").getValue(String.class);
                            String distance = dataSnapshot.child("distance").getValue(String.class);
                            String history = dataSnapshot.child("history").getValue(String.class);
                            String address = dataSnapshot.child("address").getValue(String.class);
                            String phoneNumber = dataSnapshot.child("phone_number").getValue(String.class);
                            String website = dataSnapshot.child("website").getValue(String.class);
                            List<String> images = (List<String>) dataSnapshot.child("images").getValue();
                            latitude = dataSnapshot.child("location").child("latitude").getValue(Double.class);
                            longitude = dataSnapshot.child("location").child("longitude").getValue(Double.class);

                            // Populate bus info
                            populateBusInfo(dataSnapshot.child("bus_info"));

                            // Set place details
                            placeNameTextView.setText(placeName);
                            placeDescriptionTextView.setText(description != null ? description : "N/A");
                            placeDistanceTextView.setText(distance != null ? distance : "N/A");
                            placeHistoryTextView.setText(history != null ? history : "N/A");
                            placeAddressTextView.setText(address != null ? address : "N/A");
                            placePhoneNumberTextView.setText(phoneNumber != null ? phoneNumber : "N/A");
                            placeWebsiteTextView.setText(website != null ? website : "N/A");

                            // Set up ViewPager2 for image sliding
                            List<Integer> imageList = new ArrayList<>();
                            if (images != null) {
                                for (String image : images) {
                                    int imageResId = getResources().getIdentifier(image, "drawable", getPackageName());
                                    if (imageResId != 0) {
                                        imageList.add(imageResId);
                                    }
                                }
                            }
                            ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(imageList);
                            placeImageViewPager.setAdapter(imageSliderAdapter);

                            // Update Map
                            if (latitude != null && longitude != null) {
                                updateMap(latitude, longitude);
                            }
                        } catch (Exception e) {
                            Log.e("PlaceDetailActivity", "Error processing data: " + e.getMessage());
                        }
                    } else {
                        Log.d("PlaceDetailActivity", "No data found for place: " + placeName);
                        // Handle case where place data is not found
                        placeDistanceTextView.setText("Distance: N/A");
                        placeHistoryTextView.setText("History: N/A");
                        placeAddressTextView.setText("Address: N/A");
                        placePhoneNumberTextView.setText("Phone: N/A");
                        placeWebsiteTextView.setText("Website: N/A");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseError", "DatabaseError: " + databaseError.getMessage());
                    placeDistanceTextView.setText("Error fetching data");
                    placeHistoryTextView.setText("Error fetching data");
                    placeAddressTextView.setText("Error fetching data");
                    placePhoneNumberTextView.setText("Error fetching data");
                    placeWebsiteTextView.setText("Error fetching data");
                }
            });
        }
    }

    private void updateMap(double latitude, double longitude) {
        try {
            org.osmdroid.util.GeoPoint geoPoint = new org.osmdroid.util.GeoPoint(latitude, longitude);
            mapController.setCenter(geoPoint);

            if (placeMarker != null) {
                mapView.getOverlays().remove(placeMarker);
            }

            placeMarker = new Marker(mapView);
            placeMarker.setPosition(geoPoint);
            placeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            placeMarker.setTitle("Place Location");
            placeMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    openRouteActivity(latitude, longitude);
                    return true; // Return true to indicate that we handled the click event
                }
            });
            mapView.getOverlays().add(placeMarker);
            mapView.invalidate();
        } catch (Exception e) {
            Log.e("PlaceDetailActivity", "Error updating map: " + e.getMessage());
        }
    }

    private void openRouteActivity(double destinationLat, double destinationLon) {
        Intent intent = new Intent(PlaceDetailActivity.this, RouteActivity.class);
       intent.putExtra("DESTINATION_LAT", destinationLat);
        intent.putExtra("DESTINATION_LON", destinationLon);
       startActivity(intent);
    }

    private void populateBusInfo(DataSnapshot busInfoSnapshot) {
        try {
            busInfoTable.removeAllViews(); // Clear previous data

            // Define colors
            int headerBackgroundColor = ContextCompat.getColor(this, R.color.background_color);
            int headerTextColor = ContextCompat.getColor(this, R.color.contact_details_color);
            int rowTextColor = ContextCompat.getColor(this, R.color.contact_details_color);

            // Add header row
            TableRow headerRow = new TableRow(this);
            headerRow.setBackgroundColor(headerBackgroundColor);

            TextView busNumberHeader = new TextView(this);
            busNumberHeader.setText("Bus Number");
            busNumberHeader.setTextColor(headerTextColor);
            busNumberHeader.setPadding(8, 8, 8, 8);
            busNumberHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            busNumberHeader.setTextSize(16);
            busNumberHeader.setTypeface(null, Typeface.BOLD);

            TextView lastStopHeader = new TextView(this);
            lastStopHeader.setText("Last Stop");
            lastStopHeader.setTextColor(headerTextColor);
            lastStopHeader.setPadding(8, 8, 8, 8);
            lastStopHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            lastStopHeader.setTextSize(16);
            lastStopHeader.setTypeface(null, Typeface.BOLD);

            TextView frequencyHeader = new TextView(this);
            frequencyHeader.setText("Frequency");
            frequencyHeader.setTextColor(headerTextColor);
            frequencyHeader.setPadding(8, 8, 8, 8);
            frequencyHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            frequencyHeader.setTextSize(16);
            frequencyHeader.setTypeface(null, Typeface.BOLD);

            headerRow.addView(busNumberHeader);
            headerRow.addView(lastStopHeader);
            headerRow.addView(frequencyHeader);

            busInfoTable.addView(headerRow);

            for (DataSnapshot snapshot : busInfoSnapshot.getChildren()) {
                String busNumber = snapshot.child("bus_number").getValue(String.class);
                String lastStop = snapshot.child("last_stop").getValue(String.class);
                String frequency = snapshot.child("frequency").getValue(String.class);

                TableRow row = new TableRow(this);

                TextView busNumberTextView = new TextView(this);
                busNumberTextView.setText(busNumber != null ? busNumber : "N/A");
                busNumberTextView.setTextColor(rowTextColor);
                busNumberTextView.setPadding(8, 8, 8, 8);
                busNumberTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                busNumberTextView.setTextSize(14);

                TextView lastStopTextView = new TextView(this);
                lastStopTextView.setText(lastStop != null ? lastStop : "N/A");
                lastStopTextView.setTextColor(rowTextColor);
                lastStopTextView.setPadding(8, 8, 8, 8);
                lastStopTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                lastStopTextView.setTextSize(14);

                TextView frequencyTextView = new TextView(this);
                frequencyTextView.setText(frequency != null ? frequency : "N/A");
                frequencyTextView.setTextColor(rowTextColor);
                frequencyTextView.setPadding(8, 8, 8, 8);
                frequencyTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                frequencyTextView.setTextSize(14);

                row.addView(busNumberTextView);
                row.addView(lastStopTextView);
                row.addView(frequencyTextView);

                busInfoTable.addView(row);
            }
        } catch (Exception e) {
            Log.e("PlaceDetailActivity", "Error populating bus info: " + e.getMessage());
        }
    }
}
