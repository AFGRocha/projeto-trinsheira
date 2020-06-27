package com.example.projetotrinsheira;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mImagePosts = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mVotes = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();
    private Context mContext;
    String imageStringPost;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mImageNames, ArrayList<String> mImages, ArrayList<String> mImagePosts, ArrayList<String> mTime,ArrayList<String> mVotes, ArrayList<String> mDesc) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mImagePosts = mImagePosts;
        this.mTime = mTime;
        this.mVotes = mVotes;
        this.mDesc = mDesc;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.v("T", "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);
        Glide.with(mContext)
                .asBitmap()
                .load(StringToBitMap(mImagePosts.get(position)))
                .into(holder.imagePost);

        holder.imageName.setText(mImageNames.get(position));
        holder.time.setText(mTime.get(position));
        holder.votes.setText(mVotes.get(position));
        holder.desc.setText(mDesc.get(position));
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        ImageView imagePost;
        TextView imageName;
        TextView time;
        TextView votes;
        TextView desc;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            imagePost = itemView.findViewById(R.id.postImg);
            time = itemView.findViewById(R.id.time_post);
            votes = itemView.findViewById(R.id.vote);
            desc = itemView.findViewById(R.id.desc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
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
