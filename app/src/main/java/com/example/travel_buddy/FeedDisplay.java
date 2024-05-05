package com.example.travel_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FeedDisplay extends AppCompatActivity {

    RecyclerView rvFeed;
    LinearLayoutManager manager;
    ArrayList<User> user;
    FeedAdapter adapter;
    FloatingActionButton fabDelete, fabAdd;
    int userid;

    ArrayList<User>users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed_display);

        fabAdd = findViewById(R.id.fabAdd);
        rvFeed = findViewById(R.id.rvFeed);
        rvFeed.setHasFixedSize(true);
        rvFeed.setLayoutManager(new LinearLayoutManager(this));

        users = new ArrayList<>(); // Initialize the users ArrayList

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("Post").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    // Handle error
                } else {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String name = String.valueOf(snapshot.child("Name").getValue());
                        String source = String.valueOf(snapshot.child("Source").getValue());
                        String bio = String.valueOf(snapshot.child("Bio").getValue());
                        String destination = String.valueOf(snapshot.child("Destination").getValue());
                        //String pfp = String.valueOf(snapshot.child("pfp").getValue());
                        User user = new User(name, source, destination, bio);
                        users.add(user);
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter of data changes
                }
            }
        });

        adapter = new FeedAdapter(users);
        rvFeed.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedDisplay.this, AddPost.class);
                startActivity(intent);
                finish();
            }
        });
    }


}