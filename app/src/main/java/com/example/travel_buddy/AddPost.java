package com.example.travel_buddy;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddPost extends AppCompatActivity {

    Button btnSave, btnCancel;
    TextInputEditText etName, etBio, etSource, etDestination;
    TextInputLayout name;
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


        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                name.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bio = etBio.getText().toString().trim();
                String name = etName.getText().toString().trim();

                String source = etSource.getText().toString().trim();
                String destination = etDestination.getText().toString().trim();

                if(!bio.isEmpty() && !name.isEmpty() && !source.isEmpty() && !destination.isEmpty() )
                {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("Name", name);
                    data.put("Bio", bio);

                    data.put("Source", source);
                    data.put("Destination", destination);
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