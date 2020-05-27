package com.example.projetotrinsheira;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity<CurrentActivity> extends AppCompatActivity {

    Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnLogin= findViewById(R.id.btnLogin);
        btnRegister= findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent LoginPage  = new Intent(MainActivity.this,Login.class);

                MainActivity.this.startActivity(LoginPage);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent RegisterPage = new Intent(MainActivity.this, Register.class);

                MainActivity.this.startActivity(RegisterPage);
            }
        });


    }
}
