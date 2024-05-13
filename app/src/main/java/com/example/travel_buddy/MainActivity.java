package com.example.travel_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnSignup;
    EditText email, password;
    FirebaseAuth mAuth;
    private String profilePictureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();

            }
        });
    }


    private void loginUser() {
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString().trim();

        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
            Toast.makeText(MainActivity.this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Retrieve logged-in user's ID
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();

                                // Search for user ID in the "Post" node of Firebase Database
                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Post");
                                postRef.orderByChild("UserId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            //Log.d("userid", userId);
                                            // User ID found in "Post" node
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                // Retrieve profile picture URL from the snapshot
                                                 profilePictureUrl = snapshot.child("Profile Picture").getValue(String.class);
                                                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                                    Intent intent = new Intent(MainActivity.this, FeedDisplay.class);
                                                    intent.putExtra("profilePictureUrl", profilePictureUrl);
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    // Profile picture URL not found for the user
//                                                    Toast.makeText(MainActivity.this, "Profile picture URL not found", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MainActivity.this, FeedDisplay.class);
                                                    intent.putExtra("profilePictureUrl", profilePictureUrl);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        } else if(task.isSuccessful()){
                                            // User ID not found in "Post" node
                                            Intent intent = new Intent(MainActivity.this, FeedDisplay.class);
                                            intent.putExtra("profilePictureUrl", profilePictureUrl);
                                            startActivity(intent);
                                            finish();
//                                            Toast.makeText(MainActivity.this, "User ID not found in 'Post' node", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle error
                                        Toast.makeText(MainActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "Current user is null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
