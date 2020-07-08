package com.example.projetotrinsheira.ui.profilefragment2;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projetotrinsheira.EditProfile;
import com.example.projetotrinsheira.LogOut;
import com.example.projetotrinsheira.MainActivity;
import com.example.projetotrinsheira.MyAdapterRecyclerProfilePosts;
import com.example.projetotrinsheira.PostHelperClass;
import com.example.projetotrinsheira.R;
import com.example.projetotrinsheira.RecyclerViewAdapter;
import com.example.projetotrinsheira.Statistics;
import com.example.projetotrinsheira.postsProfileUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileFragment2 extends Fragment {

    private ProfileFragment2ViewModel mViewModel;

    public static ProfileFragment2 newInstance() {
        return new ProfileFragment2();
    }


    private FirebaseAuth mAuth;
    TextView profileUsername, profileDesc, profileLocalidade, profilePoints, noPostsText;
    ImageView profileImg, badge;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /////posts config
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mPostImg = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mVotes = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();
    View view;



    RecyclerView rvPosts;
    private ArrayList<postsProfileUser> posts;
    public String userName="",userImg="";
    private MyAdapterRecyclerProfilePosts postsProfileAdapter;
    private Integer postsCount=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ///get user data
         view= inflater.inflate(R.layout.profile_fragment2_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();

        //profile campos
        profileUsername= view.findViewById(R.id.usernameId);
        profileDesc=view.findViewById(R.id.descId);
        profileLocalidade=view.findViewById(R.id.localidadeId);
        profileImg=view.findViewById(R.id.imageViewProfile);
        profilePoints= view.findViewById(R.id.textPerfilPoints);
        noPostsText= view.findViewById(R.id.textNoPosts);

        //recyclerView and Layoutmanager

        rvPosts = view.findViewById(R.id.ReciclerId);
        rvPosts.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(llm);

        badge = view.findViewById(R.id.badge);

        badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Statistics.class);
                startActivity(intent);

            }
        });



        //get userId

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        Log.d("profileFragment","entrei");

        //get user data
        /*
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
                                profilePoints.setText(documentUser.get("perfilPoints").toString()+"pp");
                                userImg=documentUser.getString("photo");
                                profileImg.setImageBitmap(StringToBitMap(userImg));

                            Log.d("perfilUsername",documentUser.getString("username"));



                            }
                        }
                        else{
                        }}}); */


        //button logout
        ImageView btnEdit = (ImageView) view.findViewById(R.id.profileEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
        }});


        //button edit
        ImageView btnSearch= (ImageView) view.findViewById(R.id.logOutBtnId);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mAuth != null)
                {
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    //startActivity(new Intent(getContext(), MainActivity.class));}

                }
            }});

        //final Integer postsCount=0;
        ///print user posts
        db.collection("posts")
                .whereEqualTo("userId",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                posts= new ArrayList<postsProfileUser>();
                                //(String name, String desc, String local, String coordinates, String image)
                                final String userId=document.getString("userId");
                                final String[] userName = {null};
                                final String[] userImg = {null};
                                final Integer[] votos = {0};

                                db.collection("users")
                                        .whereEqualTo("userId",userId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (final QueryDocumentSnapshot documentUser : task.getResult()) {

                                                      userName[0] =documentUser.getString("username");
                                                      userImg[0] =documentUser.getString("photo");
                                                     // votos[0] = Integer.parseInt((String) document.get("votes"));
                                                        posts.add( new postsProfileUser(userName[0],document.getString("description"), userImg[0],document.getString("image"), document.get("votes").toString() ,""));
                                                        Log.w("posts", "username"+userName[0]);
                                                        postsProfileAdapter = new MyAdapterRecyclerProfilePosts(posts);

                                                        rvPosts.setAdapter(postsProfileAdapter);

                                                        if(posts.size()==0){
                                                            noPostsText.setText("Sem publicações feitas!");
                                                        }
                                                        else{
                                                            noPostsText.setText("");
                                                        }

                                                    }
                                                }
                                            }});



                            }

                        } else {
                            Log.w("posts", "Error getting documents.", task.getException());
                            //noPostsText.setText("Sem publicações feitas!");


                        }
                    }
                });



        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileFragment2ViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        Log.v("Boy","We turn it back");


        mAuth = FirebaseAuth.getInstance();

        //profile campos
        profileUsername= view.findViewById(R.id.usernameId);
        profileDesc=view.findViewById(R.id.descId);
        profileLocalidade=view.findViewById(R.id.localidadeId);
        profileImg=view.findViewById(R.id.imageViewProfile);
        profilePoints= view.findViewById(R.id.textPerfilPoints);
        noPostsText= view.findViewById(R.id.textNoPosts);

        //recyclerView and Layoutmanager

        rvPosts = view.findViewById(R.id.ReciclerId);
        rvPosts.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(llm);




        //get userId

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        Log.d("profileFragment","entrei");

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
                                profilePoints.setText(documentUser.get("perfilPoints").toString()+"pp");
                                userImg=documentUser.getString("photo");
                                profileImg.setImageBitmap(StringToBitMap(userImg));

                                Log.v("Boy","We turn it INNNNN");



                            }
                        }
                        else{
                        }}});

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
