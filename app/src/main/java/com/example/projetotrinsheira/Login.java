package com.example.projetotrinsheira;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {

    Button btnLoginSubmission;
    EditText  email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();


        email= findViewById(R.id.emailId);
        password= findViewById(R.id.passwordId);
        btnLoginSubmission= findViewById(R.id.btnLoginId);
        btnLoginSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = email.getText().toString();
                String passwordInput = password.getText().toString();

                if(emailInput.isEmpty()){
                    /*emailInput.setError("Please enter email");
                    emailInput.requestFocus();*/
                    Toast.makeText(Login.this, "email field are empty",Toast.LENGTH_SHORT).show();
                }
                else if(passwordInput.isEmpty()){
                    /*passwordInput.setError("Please enter password");
                    passwordInput.requestFocus();*/
                    Toast.makeText(Login.this, "password field are empty",Toast.LENGTH_SHORT).show();
                }
                else if(passwordInput.isEmpty() && emailInput.isEmpty()){
                    Toast.makeText(Login.this, "fields are empty",Toast.LENGTH_SHORT).show();
                }
                else if(!(passwordInput.isEmpty() && emailInput.isEmpty())){
                   // Toast.makeText(Login.this, "User Created",Toast.LENGTH_SHORT);
                    mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Login.this, "Sign In failed! Check if data are correct! ",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Login.this, "Sign In completed ",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this,MainActivityBottomNavigation.class));
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(Login.this, "Error Occurred ",Toast.LENGTH_SHORT);
                }
            }
        });

    }
}
