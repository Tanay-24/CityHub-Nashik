package com.example.citymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class city_list_item extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list_item);
        Intent intent = new Intent(city_list_item.this, CityDetailActivity.class);
        startActivity(intent);
    }
}