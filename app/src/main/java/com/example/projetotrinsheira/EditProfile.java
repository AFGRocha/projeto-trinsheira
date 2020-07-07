package com.example.projetotrinsheira;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projetotrinsheira.ui.profilefragment2.ProfileFragment2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText profileUsername, profileDesc, profileLocalidade, profilePassword;
    ImageView profileImg;
    public String userName="",userImg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();

        profileUsername= findViewById(R.id.NomeId);
        profileDesc = findViewById(R.id.descriptionId);
        profileLocalidade = findViewById(R.id.inputLocalId);
        profileImg = findViewById(R.id.editProfileImg);
        profilePassword = findViewById(R.id.editPassword);

        //get userId

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        Log.d("profileFragment","entrei");


        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        editProfile();
        //get user data

        db.collection("users")
                .whereEqualTo("userId",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot documentUser : task.getResult()) {

                                profileUsername.setText(documentUser.getString("username"));
                                userName=documentUser.getString("username");
                                if(documentUser.getString("description")!=null)
                                {
                                    profileDesc.setText(documentUser.getString("description"));

                                }
                                else{
                                    profileDesc.setText("Utilizador do Trinsheira App");

                                }
                                profileLocalidade.setText(documentUser.getString("local"));
                                userImg=documentUser.getString("photo");
                                profileImg.setImageBitmap(StringToBitMap(userImg));

                                Log.d("perfilUsername",documentUser.getString("username"));



                            }
                        }
                        else{
                        }}});



    }


    private void editProfile(){


        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String userId = firebaseUser.getUid();
        //DocumentReference userRef = db.collection("users").document();


        Button btnEdit = findViewById(R.id.btnAddPostId);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").whereEqualTo("userId", userId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.v("Yes", "Entrou 2");
                                    for (final QueryDocumentSnapshot documentPost : task.getResult()) {

                                        BitmapDrawable drawable = (BitmapDrawable) profileImg.getDrawable();
                                        Bitmap bitmap = drawable.getBitmap();
                                        Log.d("Bit","> " + BitMapToString(bitmap));

                                        DocumentReference userRef = db.collection("users").document(documentPost.getId());
                                        userRef.update("username", profileUsername.getText().toString(),
                                                "description", profileDesc.getText().toString(),
                                                "photo", BitMapToString(bitmap),
                                                "local", profileLocalidade.getText().toString());

                                        if(!profilePassword.getText().toString().equals("")){
                                            firebaseUser.updatePassword(profilePassword.getText().toString());
                                            Log.d("pass","MUDOU");
                                        }else{
                                            Log.d("pass","nao mudaste pass");
                                        }

                                        EditProfile.this.finish();

                                    }
                                }
                            }
                        });



            }});
    }

    public Bitmap StringToBitMap(String imageString){
        try{
            byte [] encodeByte = Base64.decode(imageString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                profileImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
