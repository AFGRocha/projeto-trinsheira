package com.example.projetotrinsheira;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class posts extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_posts);
        Intent intent = getIntent();

        //get xml items

        final TextView nameView = findViewById(R.id.nameViewId);
        final ImageView imageView = findViewById(R.id.imageView);
        final TextView votesView = findViewById(R.id.votes);
        final TextView  descView = findViewById(R.id.desc);
        final TextView  adressView = findViewById(R.id.adress);
        final ImageView imageViewautor = findViewById(R.id.imageViewautor);
        final TextView  userNamePost = findViewById(R.id.author_name);




        String postId = intent.getStringExtra("POST_ID");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("posts").document(postId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("postPage", "DocumentSnapshot data: " + document.getString("name"));

                        db.collection("users")
                                .whereEqualTo("userId",document.getString("userId"))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (final QueryDocumentSnapshot documentUser : task.getResult()) {



                                                nameView.setText(document.getString("name"));
                                                imageView.setImageBitmap(StringToBitMap(document.getString("image")));
                                                votesView.setText(document.getString("votes")+" votos");
                                                descView.setText(document.getString("description"));
                                                adressView.setText(document.getString("adress"));
                                                imageViewautor.setImageBitmap(StringToBitMap(documentUser.getString("photo")));
                                                userNamePost.setText(documentUser.getString("username"));




                                            }
                                        }
                                    }});
                    } else {
                        Log.d("postPage", "No such document");
                    }
                } else {
                    Log.d("postPage", "get failed with ", task.getException());
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
