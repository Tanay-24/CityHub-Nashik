package com.example.citymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class AppInfoActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100; // Unique request code for permissions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        // Find the App Info button
        Button appInfoButton = findViewById(R.id.btn_app_info);

        // Set an OnClickListener to navigate to MainActivity
        appInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for location permission
                if (checkLocationPermission()) {
                    Intent intent = new Intent(AppInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    requestLocationPermission(); // Request permission if not granted
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Clear all activities and stop the app from running in background
        finishAffinity();  // This method closes all activities
        System.exit(0);    // Optional: Forces the app to fully exit
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Close the app to prevent it from running in the background
        finishAffinity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop any services running in the background
        stopAllServices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop services and fully close the app
        stopAllServices();
    }

    private void stopAllServices() {
        // Stop any background services if they are running
        Intent stopServiceIntent = new Intent(this, AppInfoActivity.class);
        stopService(stopServiceIntent);
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, navigate to MainActivity
                Intent intent = new Intent(AppInfoActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // Permission denied
                showPermissionDeniedDialog();
            }
        }
    }

    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Required")
                .setMessage("This app requires location access to display maps and nearby places. Please grant permission to continue.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // User acknowledged the message
                    requestLocationPermission(); // Request permission again
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // User chose to not grant permission
                    Toast.makeText(this, "Location permission is required to access map features.", Toast.LENGTH_SHORT).show();
                })
                .setCancelable(false)
                .show();
    }
}
