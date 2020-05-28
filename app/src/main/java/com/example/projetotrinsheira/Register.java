package com.example.projetotrinsheira;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Register extends AppCompatActivity {
    Button btnRegisterSubmission;
    EditText userName, email, password;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        //find text inputs & btns by ID
        userName = findViewById(R.id.userNameId);
        email= findViewById(R.id.emailId);
        password= findViewById(R.id.passwordId);
        btnRegisterSubmission= findViewById(R.id.btnSignUpId);
        btnRegisterSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = email.getText().toString();
                String passwordInput = password.getText().toString();

                if(emailInput.isEmpty()){
                    /*emailInput.setError("Please enter email");
                    emailInput.requestFocus();*/
                    Toast.makeText(Register.this, "email field are empty",Toast.LENGTH_SHORT).show();
                }
                else if(passwordInput.isEmpty()){
                    /*passwordInput.setError("Please enter password");
                    passwordInput.requestFocus();*/
                    Toast.makeText(Register.this, "password field are empty",Toast.LENGTH_SHORT).show();
                }
                else if(passwordInput.isEmpty() && emailInput.isEmpty()){
                    Toast.makeText(Register.this, "fields are empty",Toast.LENGTH_SHORT).show();
                }
                else if(!(passwordInput.isEmpty() && emailInput.isEmpty())){
                   // Toast.makeText(Register.this, "User Created",Toast.LENGTH_SHORT);
                    mAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if(!task.isSuccessful()){
                               Toast.makeText(Register.this, "Sign Up failed! Check if data is correct! ",Toast.LENGTH_SHORT).show();
                           }
                           else{
                               Toast.makeText(Register.this, "Sign Up completed ",Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(Register.this,LogOut.class));
                           }
                        }
                    });

                }
                else{
                    Toast.makeText(Register.this, "Error Occurred ",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
