package com.example.projetotrinsheira;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Statistics extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    public String userImg="";
    public int comments = 0;
    public int posts = 0;
    TextView userVotes,userComents,userPosts;

    ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get userId
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        profileImg = findViewById(R.id.imageViewProfile2);
        userVotes = findViewById(R.id.userVotes);
        userComents = findViewById(R.id.userComents);
        userPosts = findViewById(R.id.userPosts);


        //get user data

        db.collection("users")
                .whereEqualTo("userId",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot documentUser : task.getResult()) {

                                Log.d("Entrou", "Antes da photo");
                                userImg = documentUser.getString("photo");
                                Log.d("Entrou", "Antes de set Bitmap");
                                profileImg.setImageBitmap(StringToBitMap(userImg));
                                Log.d("Entrou", "Depois de set Bitmap");
                                String votesArray= documentUser.getString("votes");
                                Log.d("Entrou", "Antes de verificar array");
                                if(votesArray.equals("")){
                                    userVotes.setText("0");



                                }
                                else{
                                    //votesArray.split()
                                    String[] arrOfStr = votesArray.split(";", votesArray.length());

                                    userVotes.setText("" + (arrOfStr.length - 1));

                                }


                            }
                        }
                        else{
                        }}});


        db.collection("comments")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                comments++;
                                userComents.setText("" + comments);


                            }
                        }
                    }
                });

        db.collection("posts")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                posts++;
                                userPosts.setText("" + posts);


                            }
                        }
                    }
                });


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
}
