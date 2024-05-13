package com.example.travel_buddy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FeedDisplay extends AppCompatActivity {

    RecyclerView rvFeed;
    ArrayList<User> users;
    FeedAdapter adapter;

    Button btnExplore;
    FloatingActionButton fabProfile, fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed_display);

        fabAdd = findViewById(R.id.fabAdd);
        fabProfile = findViewById(R.id.fabProfile);
        btnExplore = findViewById(R.id.btnExplore);
        rvFeed = findViewById(R.id.rvFeed);
        rvFeed.setHasFixedSize(true);
        rvFeed.setLayoutManager(new LinearLayoutManager(this));
        String encodedImage = getIntent().getStringExtra("encodedImage");
        String profilePictureUrl = getIntent().getStringExtra("profilePictureUrl");

//        Log.d(encodedImage,"encodedImage");
//        Log.d(profilePictureUrl,"profilePictureUrl");

        Intent addPostIntent = new Intent(FeedDisplay.this, AddPost.class);
        Intent addPostIntentThruLogin = new Intent(FeedDisplay.this, AddPost.class);

        if (encodedImage != null && !encodedImage.isEmpty()) {
            addPostIntent.putExtra("encodedImage", encodedImage);

            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(addPostIntent);
                    //Log.d(encodedImage,"encodedImage");
                    finish();
                }
            });
        } else if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            addPostIntentThruLogin.putExtra("profilePictureUrl", profilePictureUrl);

            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(addPostIntentThruLogin);
                    //Log.d(profilePictureUrl,"profilePictureUrl");
                    finish();
                }
            });
        } else {
            addPostIntentThruLogin.putExtra("profilePictureUrl", profilePictureUrl);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(addPostIntentThruLogin);
                    //Log.d(profilePictureUrl,"profilePictureUrl");
                    finish();
                }
            });
            // Handle the case when both encodedImage and profilePictureUrl are empty
            // For example, show a toast or log a message
            Toast.makeText(FeedDisplay.this, "No profile picture available", Toast.LENGTH_SHORT).show();
        }



        users = new ArrayList<>(); // Initialize the users ArrayList

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Post");
        databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String name = String.valueOf(snapshot.child("Name").getValue());
                        String source = String.valueOf(snapshot.child("Source").getValue());
                        String bio = String.valueOf(snapshot.child("Bio").getValue());
                        String destination = String.valueOf(snapshot.child("Destination").getValue());
                        String phone = String.valueOf(snapshot.child("Phone").getValue());
                        String encodedImage = String.valueOf(snapshot.child("Profile Picture").getValue());

                        Bitmap decodedImage = FeedAdapter.getUserImage(encodedImage);

                        User user = new User(name, source, destination, bio, decodedImage,phone);

                        users.add(user);
                    }
                    // Initialize adapter once data is fetched
                    adapter = new FeedAdapter(users);
                    rvFeed.setAdapter(adapter);
                } else {
                    // Handle error
                    Log.e("FeedDisplay", "Error getting data", task.getException());
                }
            }
        });

        Intent ProfileIntent = new Intent(FeedDisplay.this, ProfileActivity.class);
        Intent ProfileIntentThruLogin = new Intent(FeedDisplay.this, ProfileActivity.class);
        Log.d(encodedImage,"encodedImage");
        Log.d(profilePictureUrl,"profilePictureUrl");
        if (encodedImage != null && !encodedImage.isEmpty()) {
            ProfileIntent.putExtra("encodedImage", encodedImage);

            fabProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(encodedImage,"encodedImage");
                    startActivity(ProfileIntent);
                }
            });
        } else if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            ProfileIntentThruLogin.putExtra("profilePictureUrl", profilePictureUrl);

            fabProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(profilePictureUrl,"profilePictureUrl");
                    startActivity(ProfileIntentThruLogin);
                }
            });
        }

        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedDisplay.this, MapsActivity.class);
                startActivity(intent);

            }
        });



        SearchView searchView = findViewById(R.id.searchView);

        // Set up a TextWatcher to listen for text changes in the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText); // Apply the filter
                return true;
            }
        });



    }
}
