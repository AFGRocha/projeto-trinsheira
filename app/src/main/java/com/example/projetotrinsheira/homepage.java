package com.example.projetotrinsheira;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class homepage extends AppCompatActivity {

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mPostImg = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mVotes = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initPosts();


    }

    private void initPosts(){
        Log.v("T", "initPosts");
        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mPostImg.add("https://thumbs.web.sapo.io/?W=800&H=0&delay_optim=1&epic=MmI2ZpV/gLgud+p5iGP34b+niaaWEjxYWgBiPg+1toVfVU2z1WD7YS8kOeV18THkYdtXQsc/hFVUOeISH/ILaNl2M/bT5u7gEETs+8mOh64fAAc=");
        mTime.add("2 sem");
        mVotes.add("37 Votes");
        mNames.add("Óscar Sousa");
        mDesc.add("Onda parque foi um dos parques aquaticos mais famosos");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mPostImg.add("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Solar_dos_Malafaias_Gralheira_St_Cruz_Trapa.JPG/1200px-Solar_dos_Malafaias_Gralheira_St_Cruz_Trapa.JPG");
        mTime.add("1 sem");
        mVotes.add("47 Votes");
        mNames.add("João Silva");
        mDesc.add("Situa-se á entrada da freguesia de Cruz de Trapa");

        initRecyclerView();
    }

    private void initRecyclerView(){
       /* Log.v("T", "initRecycler");
        //mProfile, ArrayList<String> mName, ArrayList<String> mTime, ArrayList<String> mVotes, ArrayList<String> mDesc, ArrayList<String> mPostImg, Context mContext
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls,mPostImg, mTime, mVotes, mDesc);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/

    }
}
