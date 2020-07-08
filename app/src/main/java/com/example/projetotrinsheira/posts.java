package com.example.projetotrinsheira;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class posts extends AppCompatActivity {
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mComment = new ArrayList<>();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_posts);
        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();

        //get xml items

        final TextView nameView = findViewById(R.id.nameViewId);
        final ImageView imageView = findViewById(R.id.imageView);
        final TextView votesView = findViewById(R.id.votes);
        final TextView descView = findViewById(R.id.desc);
        final TextView adressView = findViewById(R.id.adress);
        final ImageView imageViewautor = findViewById(R.id.imageViewautor);
        final TextView userNamePost = findViewById(R.id.author_name);
        final TextView userLocal = findViewById(R.id.author_local);
        final TextView userPoints = findViewById(R.id.author_points);

        final Button button = findViewById(R.id.btnInputComment);
        final Button buttonVote = findViewById(R.id.btnVote);
        final EditText editComment = findViewById(R.id.editComment);

        final Button buttonBack = findViewById(R.id.btnBack);


        final String postId = intent.getStringExtra("POST_ID");
        Log.v("Yes", " " + postId);
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
                                .whereEqualTo("userId", document.getString("userId"))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (final QueryDocumentSnapshot documentUser : task.getResult()) {


                                                nameView.setText(document.getString("name"));
                                                imageView.setImageBitmap(StringToBitMap(document.getString("image")));
                                                votesView.setText(document.getString("votes") + " votos");
                                                descView.setText(document.getString("description"));
                                                adressView.setText(document.getString("adress"));
                                                imageViewautor.setImageBitmap(StringToBitMap(documentUser.getString("photo")));
                                                userNamePost.setText(documentUser.getString("username"));
                                                userLocal.setText(documentUser.getString("local"));
                                                userPoints.setText(documentUser.get("perfilPoints") + "pp");

                                            }
                                        }
                                    }
                                });
                        db.collection("comments")
                                .whereEqualTo("postId", postId)
                                 .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                   @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                                Log.v("Yes", "Entrou");

                                                db.collection("users").whereEqualTo("userId", document.getString("userId"))
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.v("Yes", "Entrou 2");
                                                                    for (final QueryDocumentSnapshot documentUser : task.getResult()) {

                                                                        mNames.add(documentUser.getString("username"));
                                                                        mImages.add(documentUser.getString("photo"));
                                                                        mComment.add(document.getString("comment"));
                                                                        initRecyclerView();
                                                                        Log.v("Yes", " " + mNames);
                                                                    }
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    }
                                });

                    } else {
                        Log.d("postPage", "No such document");
                    }
                } else {
                    Log.d("postPage", "get failed with ", task.getException());
                }

                // Log.v("Yes", "Bro " + postComments);

            }
        });


        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String userId = firebaseUser.getUid();

        final String[] userIdField = new String[1];

        db.collection("users")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot documentUser : task.getResult()) {
                               // Log.v("Yes", "Entrou");

                                Log.d("docUserVotes", "documentUser votes) data: " + documentUser.getString("votes"));
                                String votesArray= documentUser.getString("votes");
                               // userIdField[0] =documentUser.getString("userId");
                                if(votesArray.equals("")){
                                    Log.d("checkuserVotes", votesArray );
                                    buttonVote.setText("Votar");




                                }
                                else{
                                    //votesArray.split()
                                    String[] arrOfStr = votesArray.split(";", votesArray.length());
                                    Log.d("checkuserVotes", "votesStringData" + arrOfStr[0]);
                                    Boolean idPostFound=false;
                                    for(int i=0;i<arrOfStr.length;i++){
                                        if(arrOfStr[i].equals(postId)){
                                            idPostFound=true;
                                            i=arrOfStr.length;
                                        }
                                        else{
                                            idPostFound=false;

                                        }
                                    }

                                    if (idPostFound.equals(true)) {
                                        buttonVote.setText("Cancelar Voto");
                                    }
                                    else{
                                        buttonVote.setText("Votar");
                                    }
                                }

                            }
                        }
                    }
                });



        //comments

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                String userId = firebaseUser.getUid();
                CommentsClass newComment = new CommentsClass(editComment.getText().toString(), userId, postId);
                Log.v("Yes", " " + editComment.getText().toString() + " " + userId + " " + postId);
                Toast.makeText(posts.this, "Comentário realizado", Toast.LENGTH_SHORT).show();
                db.collection("comments").add(newComment);

                mNames.clear();
                mImages.clear();
                mComment.clear();


                db.collection("comments")
                        .whereEqualTo("postId", postId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        Log.v("Yes", "Entrou");

                                        db.collection("users").whereEqualTo("userId", document.getString("userId"))
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.v("Yes", "Entrou 2");
                                                            for (final QueryDocumentSnapshot documentUser : task.getResult()) {

                                                                mNames.add(documentUser.getString("username"));
                                                                mImages.add(documentUser.getString("photo"));
                                                                mComment.add(document.getString("comment"));
                                                                initRecyclerView();
                                                                Log.v("Yes", " " + mNames);
                                                            }
                                                        }
                                                    }
                                                });




                                    }
                                }
                            }
                        });

                db.collection("users")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot documentUser : task.getResult()) {
                                        Log.v("Game", "Entrou");
                                        String expText = documentUser.getString("perfilPoints");
                                        Log.v("Game Text", ">" + expText);
                                        int exp = 0;
                                        try {
                                            exp = Integer.parseInt(expText);
                                        } catch(NumberFormatException nfe) {
                                            System.out.println("Could not parse " + nfe);
                                        }
                                        Log.v("Game Int", ">" + exp);
                                        exp = exp + 10;
                                        String expFinal = String.valueOf(exp);
                                        Log.v("Game Final", ">" + expFinal);
                                        DocumentReference userRef = db.collection("users").document(documentUser.getId());
                                        userRef.update("perfilPoints", expFinal);

                                    }
                                }
                            }
                        });


            }
        });


        //Go back

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(posts.this , MainActivityBottomNavigation.class);
                startActivity(intent);

            }
        });

        //VOTAR
        buttonVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            ///////////////////////////////////

                final String[] stringVotes = new String[1];
                final Integer[] numbVotes = new Integer[1];


                //função para adicionar voto
                DocumentReference docRef = db.collection("posts").document(postId);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("postPage", "DocumentSnapshot data: " + document.getString("name"));

                                /// get logged user votes


                                numbVotes[0] =Integer.parseInt(document.getString("votes"));

                                String btnTextString;

                                if(buttonVote.getText().equals("Votar")){
                                    numbVotes[0]= numbVotes[0]+1;

                                    db.collection("users")
                                            .whereEqualTo("userId", userId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (final QueryDocumentSnapshot documentUser : task.getResult()) {
                                                            // Log.v("Yes", "Entrou");

                                                            String votesStringOld = documentUser.getString("votes");
                                                            String votesStringNew = votesStringOld+";"+postId;

                                                            DocumentReference userRef = db.collection("users").document(documentUser.getId());
                                                            userRef.update("votes",votesStringNew);


                                                            String expText = documentUser.getString("perfilPoints");

                                                            int exp = 0;
                                                            try {
                                                                exp = Integer.parseInt(expText);
                                                            } catch(NumberFormatException nfe) {
                                                                System.out.println("Could not parse " + nfe);
                                                            }

                                                            exp = exp + 5;
                                                            String expFinal = String.valueOf(exp);

                                                            userRef.update("perfilPoints", expFinal);
                                                            buttonVote.setText("Cancelar Voto");


                                                        }
                                                    }
                                                }
                                            });

                                }
                                else{
                                    numbVotes[0]= numbVotes[0]-1;
                                    db.collection("users")
                                            .whereEqualTo("userId", userId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (final QueryDocumentSnapshot documentUser : task.getResult()) {
                                                            // Log.v("Yes", "Entrou");

                                                            String votesStringOld = documentUser.getString("votes");
                                                            String votesStringNew="";

                                                            String[] arrOfStr = votesStringOld.split(";", votesStringOld.length());

                                                            for(int i=0;i<arrOfStr.length;i++){
                                                                if(arrOfStr[i].equals(postId)){

                                                                }
                                                                else{
                                                                    votesStringNew=votesStringNew+";"+arrOfStr[i];
                                                                }
                                                            }

                                                            DocumentReference userRef = db.collection("users").document(documentUser.getId());
                                                            userRef.update("votes",votesStringNew);
                                                            String expText = documentUser.getString("perfilPoints");

                                                            int exp = 0;
                                                            try {
                                                                exp = Integer.parseInt(expText);
                                                            } catch(NumberFormatException nfe) {
                                                                System.out.println("Could not parse " + nfe);
                                                            }

                                                            exp = exp - 5;
                                                            String expFinal = String.valueOf(exp);

                                                            userRef.update("perfilPoints", expFinal);
                                                            buttonVote.setText("Votar");
                                                        }
                                                    }
                                                }
                                            });


                                }



                                                DocumentReference docPostRef = db.collection("posts").document(postId);

// Set the "isCapital" field of the city 'DC'

                                                docPostRef
                                                        .update("votes", numbVotes[0].toString())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                votesView.setText(numbVotes[0].toString()+" votos");
                                                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("TAG", "Error updating document", e);
                                                            }
                                                        });









                            } else {
                                Log.d("postPage", "No such document");
                            }
                        } else {
                            Log.d("postPage", "get failed with ", task.getException());
                        }

                        // Log.v("Yes", "Bro " + postComments);

                    }
                });










        ////////////////////////////////
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

    private void initRecyclerView(){
        Log.v("T", "initRecycler");
        //mProfile, ArrayList<String> mName, ArrayList<String> mTime, ArrayList<String> mVotes, ArrayList<String> mDesc, ArrayList<String> mPostImg, Context mContext
        RecyclerView recyclerView = findViewById(R.id.recycler_viewComment);
        CommentRecyclerViewAdapter adapter = new CommentRecyclerViewAdapter(getApplicationContext(), mNames, mImages,mComment);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

}
