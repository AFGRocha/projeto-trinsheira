package com.example.projetotrinsheira;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projetotrinsheira.ui.map.MapFragment;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MapFragment.newInstance())
                    .commitNow();
        }
    }
}
