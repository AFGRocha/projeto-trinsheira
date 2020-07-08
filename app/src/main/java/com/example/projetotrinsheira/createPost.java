package com.example.projetotrinsheira;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.projetotrinsheira.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class createPost extends AppCompatActivity {

    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button btnAddImg, btnAddPost, btnBack;
    ImageView imageViewPost,imageBtnPost;
    EditText inputName, inputDesc, inputLocal, inputCoordenadas;
    private LocationManager locationManager;
    private LocationListener listener;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);



        //btnAddImg = findViewById(R.id.btnAddImgId);
        imageBtnPost=findViewById(R.id.imageBtnPost);
        btnAddPost = findViewById(R.id.btnAddPostId);
        inputName = findViewById(R.id.NomeId);
        inputDesc = findViewById(R.id.descId);
        inputLocal = findViewById(R.id.inputLocalId);
        //inputCoordenadas = (TextInputEditText) findViewById(R.id.inputCoordenadasId);
        //imageViewPost = findViewById(R.id.imageViewPostId);
        mAuth = FirebaseAuth.getInstance();

        //btnAddimg click
        imageBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        btnBack = findViewById(R.id.btnBackPub);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost.this.finish();
            }
        });
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final String formattedDate = df.format(c);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();

                //get values
                String userId = currentUser.getUid();
                String name = inputName.getText().toString();
                String desc = inputDesc.getText().toString();
                String local = inputLocal.getText().toString();
               // String coordinates = inputCoordenadas.getText().toString();
                String image = BitMapToString(bitmap);


                PostHelperClass post = new PostHelperClass();
                ///*Map<String, Object> post = new HashMap<>();

                ArrayList<CommentsClass> comments=  new ArrayList<CommentsClass>();
                post.setName(name);
                post.setDescription(desc);
                post.setAdress(local);
               // post.setCoordinates(coordinates);
                post.setImage(image);
                post.setUserId( userId);
                post.setComments(comments);
                post.setVotes("0");


                Log.v("Data",formattedDate);
                post.setPostDate(formattedDate);


                db.collection("posts").add(post);


                //gamificaçao
                db.collection("users")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot documentUser : task.getResult()) {
                                        Log.v("Game", "Entrou");
                                        String expText = documentUser.getString("perfilPoints");
                                        Log.v("Game Text", ">" + expText);
                                        int exp = 0;
                                        try {
                                            exp = Integer.parseInt(expText);
                                        } catch(NumberFormatException nfe) {
                                            System.out.println("Could not parse " + nfe);
                                        }
                                        Log.v("Game Int", ">" + exp);
                                        exp = exp + 20;
                                        String expFinal = String.valueOf(exp);
                                        Log.v("Game Final", ">" + expFinal);
                                        DocumentReference userRef = db.collection("users").document(documentUser.getId());
                                        userRef.update("perfilPoints", expFinal);

                                    }
                                }
                            }
                        });



                Intent intent = new Intent(createPost.this , MainActivityBottomNavigation.class);
                startActivity(intent);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        bitmap= (Bitmap)data.getExtras().get("data");
        imageBtnPost.setImageBitmap(bitmap);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
