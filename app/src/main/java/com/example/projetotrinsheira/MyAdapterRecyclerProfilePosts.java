package com.example.projetotrinsheira;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MyAdapterRecyclerProfilePosts extends  RecyclerView.Adapter<MyAdapterRecyclerProfilePosts.ViewHolder> {

    private FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String userName="", userImage="";


    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtDesc,txtName,txtVotes,txtTimePost;
       protected ImageView imgCover,txtPhoto;



        public ViewHolder(View itemView) {
            super(itemView);

            txtDesc= itemView.findViewById(R.id.desc);
            imgCover=itemView.findViewById(R.id.postImg);
            txtName= itemView.findViewById(R.id.image_name);
            txtPhoto=itemView.findViewById(R.id.image);
            txtVotes=itemView.findViewById(R.id.vote);
            txtTimePost=itemView.findViewById(R.id.time_post);




        }
    }

    private ArrayList<postsProfileUser> myPostsList;
    public MyAdapterRecyclerProfilePosts(ArrayList<postsProfileUser> myPostsList) {
        this.myPostsList =myPostsList;
    }

    public MyAdapterRecyclerProfilePosts.ViewHolder onCreateViewHolder(@NonNull
                                                                   ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posts, viewGroup, false);
        return new ViewHolder(itemView);

    }
    @Override
    public void onBindViewHolder(@NonNull MyAdapterRecyclerProfilePosts.ViewHolder
                                         viewHolder, int i) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();





        postsProfileUser item = myPostsList.get(i);
        viewHolder.txtDesc.setText(item.getDesc());
        viewHolder.txtName.setText(item.getUsername());
        viewHolder.imgCover.setImageBitmap(StringToBitMap(item.getImage()));
       //viewHolder.txtPhoto.setImageBitmap(StringToBitMap(item.getImageUser()));
        viewHolder.txtVotes.setText(item.getVotos().toString()+" votos");
        viewHolder.txtTimePost.setText(item.getTempo());




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