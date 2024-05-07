package com.example.travel_buddy;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AddPost extends AppCompatActivity {

    RoundedImageView riProfilePic;
    private FrameLayout layoutImage;
    Button btnSave, btnCancel;
    TextInputEditText etName, etBio, etSource, etDestination;
    TextInputLayout name;
    private String encodedImage = "";
    private TextView tvAddImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_post);
        name = findViewById(R.id.name);
        etName = findViewById(R.id.etName);
        etBio = findViewById(R.id.etBio);
        etSource = findViewById(R.id.etSource);
        etDestination = findViewById(R.id.etDestination);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        riProfilePic = findViewById(R.id.riProfilePic);
        layoutImage = findViewById(R.id.layoutImage);
        tvAddImage = findViewById(R.id.tvAddImage);

        final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            try {
                                if (imageUri != null) {
                                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    riProfilePic.setImageBitmap(bitmap);
                                    tvAddImage.setVisibility(View.GONE);
                                    encodedImage = FeedAdapter.encodeImage(bitmap);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });


        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                name.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPost.this, FeedDisplay.class);
                startActivity(intent);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bio = etBio.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String source = etSource.getText().toString().trim();
                String destination = etDestination.getText().toString().trim();

                if (!bio.isEmpty() && !name.isEmpty() && !source.isEmpty() && !destination.isEmpty()) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("Name", name);
                    data.put("Bio", bio);
                    data.put("Source", source);
                    data.put("Destination", destination);
                    data.put("Profile Picture", encodedImage);

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Post")
                            .push()
                            .updateChildren(data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(AddPost.this, "Note Added Successfully.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddPost.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddPost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}
