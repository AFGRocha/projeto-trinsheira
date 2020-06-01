package com.example.projetotrinsheira;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddPostTest extends AppCompatActivity {
    private static final int IMAGE_CAPTURE_CODE = 1001 ;
    Button btnAddImg, btnAddPost;
    ImageView imageViewPost;
    TextInputEditText inputName, inputDesc, inputLocal, inputCoordenadas;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_test);
        btnAddImg=findViewById(R.id.btnAddImgId);
        btnAddPost=findViewById(R.id.btnAddPostId);
        inputName=(TextInputEditText)findViewById(R.id.inputNameId);
        inputDesc=(TextInputEditText)findViewById(R.id.inputDescId);
        inputLocal= (TextInputEditText)findViewById(R.id.inputLocalId);
        inputCoordenadas= (TextInputEditText)findViewById(R.id.inputCoordenadasId);
        imageViewPost=findViewById(R.id.imageViewPostId);

        //btnAddimg click
        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //get values

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


                db.collection("posts").add(post);

            }
        });


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
