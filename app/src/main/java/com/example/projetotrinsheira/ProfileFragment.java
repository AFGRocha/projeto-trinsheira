package com.example.projetotrinsheira;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projetotrinsheira.ui.profilefragment2.ProfileFragment2;

public class ProfileFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProfileFragment2.newInstance())
                    .commitNow();
        }
    }
}
