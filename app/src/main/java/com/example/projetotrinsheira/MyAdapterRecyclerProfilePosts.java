package com.example.projetotrinsheira;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MyAdapterRecyclerProfilePosts extends  RecyclerView.Adapter<MyAdapterRecyclerProfilePosts.ViewHolder> {

    private final Context mContext;
    private FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String userName="", userImage="";



    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtDesc,txtName,txtVotes,txtTimePost;
       protected ImageView imgCover,txtPhoto;
       Button votarBtn, comentarBtn;
         Context mContext;


        public ViewHolder(View itemView) {
            super(itemView);

            txtDesc= itemView.findViewById(R.id.desc);
            imgCover=itemView.findViewById(R.id.postImg);
            txtName= itemView.findViewById(R.id.image_name);
            txtPhoto=itemView.findViewById(R.id.image);
            txtVotes=itemView.findViewById(R.id.vote);
            txtTimePost=itemView.findViewById(R.id.time_post);
            votarBtn=itemView.findViewById(R.id.votarBtnId);
            comentarBtn=itemView.findViewById(R.id.comentarBtnId);




        }
    }

    private ArrayList<postsProfileUser> myPostsList;
    public MyAdapterRecyclerProfilePosts(Context mContext, ArrayList<postsProfileUser> myPostsList) {
        this.myPostsList =myPostsList;
        this.mContext=mContext;
    }

    public MyAdapterRecyclerProfilePosts.ViewHolder onCreateViewHolder(@NonNull
                                                                   ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posts, viewGroup, false);
        return new ViewHolder(itemView);

    }
    @Override
    public void onBindViewHolder(@NonNull final MyAdapterRecyclerProfilePosts.ViewHolder
                                         viewHolder, int i) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String userId = firebaseUser.getUid();





        postsProfileUser item = myPostsList.get(i);
        viewHolder.txtDesc.setText(item.getDesc());
        viewHolder.txtName.setText(item.getUsername());
        viewHolder.imgCover.setImageBitmap(StringToBitMap(item.getImage()));
        viewHolder.txtPhoto.setImageBitmap(StringToBitMap(item.getImageUser()));
        viewHolder.txtVotes.setText(item.getVotos()+" votos");
        viewHolder.txtTimePost.setText(item.getTempo());

        viewHolder.imgCover.setTag(item.getPostId());
        viewHolder.imgCover.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Log.d("onClick", "Post "  + v.getTag().toString());
                Intent intent = new Intent(mContext,posts.class);
                intent.putExtra("POST_ID", v.getTag().toString());
                mContext.startActivity(intent);
            }


        });

        final String postId= item.getPostId();//mPostsId.get(position);

        //FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //final String userId = firebaseUser.getUid();

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
                               // ClassLoader mContext;
                                if(votesArray.equals("")){
                                    Log.d("checkuserVotes", votesArray );
                                    viewHolder.votarBtn.setText("Votar");
                                    viewHolder.votarBtn.setTextColor(mContext.getResources().getColor(R.color.darkergrey));






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
                                        viewHolder.votarBtn.setText("Votado");
                                        //btn represents your button object

                                        // holder.votarBtn.setBackgroundColor(mContext.getResources().getColor(R.color.verde));
                                        viewHolder.votarBtn.setTextColor(mContext.getResources().getColor(R.color.verde));
                                    }
                                    else{
                                        viewHolder.votarBtn.setText("Votar");
                                        viewHolder.votarBtn.setTextColor(mContext.getResources().getColor(R.color.darkergrey));
                                    }
                                }

                            }
                        }
                    }
                });







///função onclickCommentbtn

        viewHolder.comentarBtn.setTag(item.getPostId());
        viewHolder.comentarBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Log.d("onClick", "Post "  + v.getTag().toString());
                Intent intent = new Intent(mContext,posts.class);
                intent.putExtra("POST_ID", v.getTag().toString());
                mContext.startActivity(intent);
            }


        });






        viewHolder.votarBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {










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

                                if(viewHolder.votarBtn.getText().equals("Votar")){
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

                                                            viewHolder.votarBtn.setText("Votado");
                                                            //btn represents your button object

                                                            // holder.votarBtn.setBackgroundColor(mContext.getResources().getColor(R.color.verde));
                                                            viewHolder.votarBtn.setTextColor(mContext.getResources().getColor(R.color.verde));

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
                                                            viewHolder.votarBtn.setText("Votar");
                                                            viewHolder.votarBtn.setTextColor(mContext.getResources().getColor(R.color.darkergrey));

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
                                                viewHolder.txtVotes.setText(numbVotes[0].toString()+" votos");
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















            }


        });




    }
    @Override
    public int getItemCount() {
        return myPostsList.size();
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