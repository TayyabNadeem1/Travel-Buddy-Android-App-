package com.example.travel_buddy;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class AddPost extends AppCompatActivity {


    Button btnSave, btnCancel;
    TextInputEditText etName, etBio, etSource, etDestination;
    TextInputLayout name;
    public String encodedImage = "";
    public String profilePictureUrl = "";
    private TextView tvAddImage;
    private FirebaseAuth mAuth;

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


        encodedImage = getIntent().getStringExtra("encodedImage");
        profilePictureUrl = getIntent().getStringExtra("profilePictureUrl");
        if(encodedImage != null){
            setEncodedImage(encodedImage);
            Log.d(encodedImage,"encodedImage");
        } else if (profilePictureUrl != null) {
            setEncodedImage(profilePictureUrl);
            Log.d(profilePictureUrl,"profilePictureUrl");
        }


        mAuth = FirebaseAuth.getInstance();


        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                name.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPost.this, FeedDisplay.class);
                startActivity(intent);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePost();
            }
        });
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    private void savePost() {
        String bio = Objects.requireNonNull(etBio.getText()).toString().trim();
        String name = Objects.requireNonNull(etName.getText()).toString().trim();
        String source = Objects.requireNonNull(etSource.getText()).toString().trim();
        String destination = Objects.requireNonNull(etDestination.getText()).toString().trim();

        if (!bio.isEmpty() && !name.isEmpty() && !source.isEmpty() && !destination.isEmpty()) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();

                HashMap<String, Object> data = new HashMap<>();
                data.put("Name", name);
                data.put("Bio", bio);
                data.put("Source", source);
                data.put("Destination", destination);
                if(encodedImage != null){
                    data.put("Profile Picture", encodedImage);
                } else if (profilePictureUrl != null) {
                    data.put("Profile Picture", profilePictureUrl);
                }

                data.put("UserId", userId); // Add user's UID

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Users")
                        .child("Post")
                        .push()
                        .updateChildren(data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddPost.this, "Note Added Successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddPost.this, FeedDisplay.class); // Navigate back to FeedDisplay
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
            } else {
                Toast.makeText(AddPost.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddPost.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

}
