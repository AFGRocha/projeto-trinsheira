package com.example.projetotrinsheira;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class AddPostTest extends AppCompatActivity {
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button btnAddImg, btnAddPost;
    ImageView imageViewPost;
    TextInputEditText inputName, inputDesc, inputLocal, inputCoordenadas;
    private LocationManager locationManager;
    private LocationListener listener;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_test);
        btnAddImg = findViewById(R.id.btnAddImgId);
        btnAddPost = findViewById(R.id.btnAddPostId);
        inputName = (TextInputEditText) findViewById(R.id.inputNameId);
        inputDesc = (TextInputEditText) findViewById(R.id.inputDescId);
        inputLocal = (TextInputEditText) findViewById(R.id.inputLocalId);
        inputCoordenadas = (TextInputEditText) findViewById(R.id.inputCoordenadasId);
        imageViewPost = findViewById(R.id.imageViewPostId);
        mAuth = FirebaseAuth.getInstance();

        //btnAddimg click
        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();

                //get values
                String userId = currentUser.getUid();
                String name = inputName.getText().toString();
                String desc = inputDesc.getText().toString();
                String local = inputLocal.getText().toString();
                String coordinates = inputCoordenadas.getText().toString();
                String image = BitMapToString(bitmap);


                //PostHelperClass helperClass = new PostHelperClass(name, desc,local,coordinates, image);
                Map<String, Object> post = new HashMap<>();
                post.put("name", name);
                post.put("description", desc);
                post.put("local", local);
                post.put("coordinates", coordinates);
                post.put("image", image);
                post.put("userId", userId);


                db.collection("posts").add(post);

            }
        });

/////// get current coordenates
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                inputCoordenadas.setText(location.getLongitude() + " " + location.getLatitude());
                inputCoordenadas.setText(location.getLongitude() + " " + location.getLatitude());


                Log.i("coordinates", location.getLongitude() + " " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {


            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else{
            locationManager.requestLocationUpdates("gps", 5000, 0, listener);
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        bitmap= (Bitmap)data.getExtras().get("data");
        imageViewPost.setImageBitmap(bitmap);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
