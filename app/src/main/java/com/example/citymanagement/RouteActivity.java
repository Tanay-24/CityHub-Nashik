package com.example.citymanagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RouteActivity extends AppCompatActivity {

    private MapView mapView;
    private double destinationLat;
    private double destinationLon;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        // Show the message overlay on the first open
        showLocationColorInfo();

        destinationLat = getIntent().getDoubleExtra("DESTINATION_LAT", 0);
        destinationLon = getIntent().getDoubleExtra("DESTINATION_LON", 0);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        mapView = findViewById(R.id.map);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint destinationPoint = new GeoPoint(destinationLat, destinationLon);
        mapController.setCenter(destinationPoint);

        Marker destinationMarker = new Marker(mapView);
        destinationMarker.setPosition(destinationPoint);
        destinationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        destinationMarker.setTitle(String.format(Locale.getDefault(), "Place Location: %.4f, %.4f", destinationLat, destinationLon));
        destinationMarker.setIcon(getColoredMarker(android.graphics.Color.RED));
        mapView.getOverlays().add(destinationMarker);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getUserLocation();
        }
    }

    private void showLocationColorInfo() {
        Toast.makeText(this, "Red is destination location and Blue is your location.", Toast.LENGTH_LONG).show();
    }

    private void getUserLocation() {
        android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
            if (location != null) {
                double userLat = location.getLatitude();
                double userLon = location.getLongitude();

                GeoPoint userPoint = new GeoPoint(userLat, userLon);
                Marker userMarker = new Marker(mapView);
                userMarker.setPosition(userPoint);
                userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                userMarker.setTitle(String.format(Locale.getDefault(), "Your Location: %.4f, %.4f", userLat, userLon));
                userMarker.setIcon(getColoredMarker(android.graphics.Color.BLUE));
                mapView.getOverlays().add(userMarker);

                IMapController mapController = mapView.getController();
                mapController.setCenter(userPoint);

                // Fetch route without travel times
                drawRoute(userPoint, new GeoPoint(destinationLat, destinationLon));
            }
        }
    }

    private void drawRoute(GeoPoint startPoint, GeoPoint endPoint) {
        List<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(startPoint);
        routePoints.add(new GeoPoint((startPoint.getLatitude() + endPoint.getLatitude()) / 2, (startPoint.getLongitude() + endPoint.getLongitude()) / 2));
        routePoints.add(endPoint);

        Polyline line = new Polyline(mapView);
        line.setTitle("Route to Destination");
        line.setWidth(10f);
        line.setColor(android.graphics.Color.GREEN);
        line.setPoints(routePoints);
        mapView.getOverlays().add(line);
        mapView.invalidate();
    }

    private Drawable getColoredMarker(int color) {
        Bitmap defaultMarker = BitmapFactory.decodeResource(getResources(), org.osmdroid.library.R.drawable.marker_default);
        Bitmap mutableMarker = defaultMarker.copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));

        Canvas canvas = new Canvas(mutableMarker);
        canvas.drawBitmap(mutableMarker, 0, 0, paint);

        return new BitmapDrawable(getResources(), mutableMarker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
