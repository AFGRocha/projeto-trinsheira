package com.example.projetotrinsheira;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogOut extends AppCompatActivity {
    Button  logOutbtn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);
        mAuth = FirebaseAuth.getInstance();

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        logOutbtn = findViewById(R.id.logOutBtn);
        logOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null)
                { mAuth.signOut();
               startActivity(new Intent(LogOut.this,MainActivity.class));}
            }
        });
    }
}
