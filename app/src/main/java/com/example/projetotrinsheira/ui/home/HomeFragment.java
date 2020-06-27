package com.example.projetotrinsheira.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetotrinsheira.MainActivity;
import com.example.projetotrinsheira.R;
import com.example.projetotrinsheira.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mPostImg = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mVotes = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();
    private ArrayList<QueryDocumentSnapshot> usersArray = new ArrayList<QueryDocumentSnapshot>();
    List<Map<String,List<String>>> list = new ArrayList<Map<String,List<String>>>();


    FirebaseFirestore db = FirebaseFirestore.getInstance();
public String userIdDoc;

    private RecyclerViewAdapter RecyclerViewAdapter;
   // private CatalogoViewModel mViewModel;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
         root = inflater.inflate(R.layout.fragment_home, container, false);


        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            //mAuth.signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            //startActivity(new Intent(getContext(), MainActivity.class));}

        }
        initPosts();



        return root;
    }
    private void initPosts(){



        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d( "posts" ,document.getId() + " => " + document.getString("name"));
                                mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
                                mPostImg.add(document.getString("image"));
                                mTime.add("2 sem");
                                mVotes.add(document.getString("votes"));
                              // mNames.add("Óscar Sousa");
                                mDesc.add(document.getString("description"));
                                userIdDoc=document.getString("userId");

                                db.collection("users")
                                        .whereEqualTo("userId",userIdDoc)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (final QueryDocumentSnapshot documentUser : task.getResult()) {


                                                            mNames.add(documentUser.getString("username"));
                                                            initRecyclerView();



                                                    }
                                                }
                                                else{
                                                }}});




                            }
                        } else {
                            Log.w("posts", "Error getting documents.", task.getException());
                        }
                    }
                });
        Log.v("T", "initPosts");

    }

    private void initRecyclerView(){
        Log.v("T", "initRecycler");
        //mProfile, ArrayList<String> mName, ArrayList<String> mTime, ArrayList<String> mVotes, ArrayList<String> mDesc, ArrayList<String> mPostImg, Context mContext
        RecyclerView recyclerView = root.findViewById(R.id.recyclerId);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mNames, mImageUrls,mPostImg, mTime, mVotes, mDesc);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
