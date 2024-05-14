//package com.example.travel_buddy;
//
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class PostProfile extends AppCompatActivity {
//
//    private FirebaseAuth mAuth;
//    Context c;
//    String encodedImage, profilePictureUrl;
//    private DatabaseReference databaseReference;
//    TextView tvpname, tvEmail, tvpnumber,tvgender,tvage,tvdob,tvabout;
//    ImageView ivpp;
//    FloatingActionButton fabedit;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        c = this;
//        setContentView(R.layout.activity_post_profile);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        tvpname = findViewById(R.id.tvpname);
//
//        tvEmail = findViewById(R.id.tvEmail);
//        tvgender = findViewById(R.id.tvgender);
//        tvage = findViewById(R.id.tvage);
//        tvdob = findViewById(R.id.tvdob);
//        tvpnumber = findViewById(R.id.tvpnumber);
//        tvabout = findViewById(R.id.tvabout);
//        ivpp = findViewById(R.id.ivpp);
//
//        String userId = getIntent().getStringExtra("userId");
//        Log.d("UserId from intent", userId);
//
//        getUserProfile(userId);
//    }
//
//    private void getUserProfile(String userId) {
//
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Post");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Find the user profile with the specified userId
//                    DataSnapshot snapshot = dataSnapshot.child(String.valueOf(userId));
//
////                    Log.d("snapshot", snapshot.toString());
//                    for (DataSnapshot Dsnapshot : dataSnapshot.getChildren()) {
//                        String userid = Dsnapshot.child("UserId").getValue(String.class);
//
//                        if (userId != null && userid.equals(userId)) {
//                            Log.d(userid,"userid every");
//                            Log.d(userId,"userid ");
//
//
//                            //Log.d(snapshot.child("Name").getValue(String.class),"name");
//                            String name = Dsnapshot.child("Name").getValue(String.class);
//                            String phone = Dsnapshot.child("Phone").getValue(String.class);
//                            //String age = snapshot.child("Age").getValue(String.class);
//                            String about = Dsnapshot.child("Bio").getValue(String.class);
//                            //String gender = snapshot.child("Gender").getValue(String.class);
//                            //String dob = snapshot.child("Dob").getValue(String.class);
//                            String sivpp = ""; // Initialize with empty string
//
//                            Log.d(name, "name");
//                            Log.d(phone, "phone");
//                            Log.d(about, "bio");
//                            if (encodedImage != null) {
//                                sivpp = encodedImage;
//                            } else if (profilePictureUrl != null) {
//                                sivpp = profilePictureUrl;
//                            }
//                            // Update UI elements
//                            updateUI(name, phone, about, sivpp);
//
//                        }else{
//                                Toast.makeText(PostProfile.this, "User profile not found", Toast.LENGTH_SHORT).show();
//                            }
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled
//            }
//        });
//    }
//
//    private void updateUI(String name, String phone, String about, String encodedImage) {
//        if (name == null) {
//            Toast.makeText(PostProfile.this, "Name is null", Toast.LENGTH_SHORT).show();
//            return; // Exit method if name is null
//        }
//
//        Log.d(name,"name");
//        Log.d(phone,"phone");
//        Log.d(about,"bio");
//        tvpname.setText(name);
//        tvEmail.setText(mAuth.getCurrentUser().getEmail());
////        tvgender.setText(gender);
////        tvage.setText(age);
////        tvdob.setText(dob);
//        tvpnumber.setText(phone);
//        tvabout.setText(about);
//
//        // Decode encoded image string into Bitmap and set it to ImageView
//        if (encodedImage != null && !encodedImage.isEmpty()) {
//            Bitmap bitmap = FeedAdapter.getUserImage(encodedImage);
//            ivpp.setImageBitmap(bitmap);
//        }
//    }
//
//
//}