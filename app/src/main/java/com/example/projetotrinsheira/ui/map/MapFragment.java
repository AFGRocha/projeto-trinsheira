package com.example.projetotrinsheira.ui.map;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.projetotrinsheira.AddPostTest;
import com.example.projetotrinsheira.CustomInfoWindowAdapter;
import com.example.projetotrinsheira.Maps;
import com.example.projetotrinsheira.R;
import com.example.projetotrinsheira.posts;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mViewModel;
    private GoogleMap mMap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, String> mMarkerMap = new HashMap<>();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment gMapFragment = (SupportMapFragment)  getChildFragmentManager().findFragmentById(R.id.mapGoogle);
        gMapFragment.getMapAsync(this);


        ImageView buttonAdd;
        buttonAdd= v.findViewById(R.id.addbuttonMapId);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), AddPostTest.class));
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(1.0f);
        mMap.setMaxZoomPreference(20.0f);
        final Context context = getContext();
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter((context)));

        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.pointer_green);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 150, false);

        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {



                                LatLng test =  getLocationFromAddress(context,document.getString("adress"));
                                Log.v("Address", "Teste: " + document.getString("adress"));
                                Log.v("Text", "pls " + test);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(test)
                                        .title(document.getString("name"))
                                        .snippet(document.getString("votes") + ";" + document.getString("image") + ";" + document.getId())
                                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                mMarkerMap.put(marker.getId(), document.getId());
                            }
                        }
                        else{
                        }}});

        mMap.moveCamera(CameraUpdateFactory.newLatLng(getLocationFromAddress(context,"Portugal")));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(7),200, null);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String postId = mMarkerMap.get(marker.getId());
                Log.v("Info", "coiso: " + postId);
                Intent intent = new Intent(context, posts.class);
                intent.putExtra("POST_ID", postId);
                context.startActivity(intent);
            }
        });


    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

}
