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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetotrinsheira.ui.dashboard.DashboardViewModel;
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

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mPostsId = new ArrayList<>();

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mImagePosts = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mVotes = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();
   // private ArrayList<String> mPostsId= new ArrayList<>();
    private Context mContext;
    String imageStringPost;
    private FirebaseAuth mAuth;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mImageNames, ArrayList<String> mImages, ArrayList<String> mImagePosts, ArrayList<String> mTime,ArrayList<String> mVotes, ArrayList<String> mDesc, ArrayList<String> mPostsId ) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mImagePosts = mImagePosts;
        this.mTime = mTime;
        this.mVotes = mVotes;
        this.mDesc = mDesc;
        this.mContext = mContext;
        this.mPostsId = mPostsId;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Log.v("T", "onBindViewHolder: called.");

        /*Glide.with(mContext)
                .asBitmap()
                .load(StringToBitMap(mImages.get(position)))
                .into(holder.image);
       /* Glide.with(mContext)
                .asBitmap()
                .load(StringToBitMap(mImagePosts.get(position)))
                .into(holder.imagePost);*/

        holder.imagePost.setImageBitmap(StringToBitMap(mImagePosts.get(position)));
        holder.image.setImageBitmap(StringToBitMap(mImages.get(position)));

        holder.imageName.setText(mImageNames.get(position));
        holder.time.setText(mTime.get(position));
        holder.votes.setText(mVotes.get(position)+" votos");
        holder.desc.setText(mDesc.get(position));
        holder.imagePost.setTag(mPostsId.get(position));

        holder.imagePost .setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Log.d("onClick", "Post "  + v.getTag().toString());
                Intent intent = new Intent(mContext,posts.class);
                intent.putExtra("POST_ID", v.getTag().toString());
                mContext.startActivity(intent);
            }


        });

        final String postId= mPostsId.get(position);

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
                                    holder.votarBtn.setText("Votar");




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
                                        holder.votarBtn.setText("Cancelar Voto");
                                    }
                                    else{
                                        holder.votarBtn.setText("Votar");
                                    }
                                }

                            }
                        }
                    }
                });
















        holder.votarBtn.setOnClickListener(new View.OnClickListener() {
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

                                if(holder.votarBtn.getText().equals("Votar")){
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

                                                            holder.votarBtn.setText("Cancelar Voto");


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
                                                            holder.votarBtn.setText("Votar");
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
                                                holder.votes.setText(numbVotes[0].toString()+" votos");
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
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        CircleImageView image;
        ImageView imagePost, imageUser;
        TextView imageName;
        TextView time;
        TextView votes;
        TextView desc;
        RelativeLayout parentLayout;

        Button votarBtn, comentarBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            imagePost = itemView.findViewById(R.id.postImg);
            //imageUser= itemView.findViewById(R.id.image);
            //Button b = itemView.findViewById(R.id.btnPartilhar);
           /* imagePost .setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    Log.d("onClick", "Post" + getAdapterPosition() + " clicado.");
                    Intent intent = new Intent(mContext,posts.class);
                    mContext.startActivity(intent);
                }


            });*/
            time = itemView.findViewById(R.id.time_post);
            votes = itemView.findViewById(R.id.vote);
            desc = itemView.findViewById(R.id.desc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            votarBtn= itemView.findViewById(R.id.votarBtnId);
            comentarBtn= itemView.findViewById(R.id.comentarBtnId);
        }
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
