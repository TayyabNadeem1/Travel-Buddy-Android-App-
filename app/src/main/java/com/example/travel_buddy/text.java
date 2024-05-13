//package com.example.travel_buddy;
//
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ProfileActivity extends AppCompatActivity {
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
//        setContentView(R.layout.activity_profile);
//
//        mAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Post");
//
//        getUserProfile();
//        tvpname = findViewById(R.id.tvpname);
//        fabedit = findViewById(R.id.fabedit);
//        tvEmail = findViewById(R.id.tvEmail);
//        tvgender = findViewById(R.id.tvgender);
//        tvage = findViewById(R.id.tvage);
//        tvdob = findViewById(R.id.tvdob);
//        tvpnumber = findViewById(R.id.tvpnumber);
//        tvabout = findViewById(R.id.tvabout);
//        String cuser="";
//
//        encodedImage = getIntent().getStringExtra("encodedImage");
//        profilePictureUrl = getIntent().getStringExtra("profilePictureUrl");
//
//        Log.d(encodedImage,"encodedImage");
//        Log.d(profilePictureUrl,"profilePictureUrl");
//
//
//        fabedit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
//                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
//                builder.setView(inflater.inflate(R.layout.activity_edit_form, null))
//                        .setCancelable(true);  // Allows dismissal by touching outside
//
//                // Create and show the dialog
//                AlertDialog dialog = builder.create();
//                dialog.show();
//
//                TextInputEditText editDob, editAbout,editAge,editPhone,editName;
//                AutoCompleteTextView editGender;
//                editDob=dialog.findViewById(R.id.editDob);
//                editAbout=dialog.findViewById(R.id.editAbout);
//                editGender=dialog.findViewById(R.id.editGender);
//                editAge=dialog.findViewById(R.id.editAge);
//                editPhone=dialog.findViewById(R.id.editPhone);
//                editName=dialog.findViewById(R.id.editName);
//                Button saveButton = dialog.findViewById(R.id.saveButton);
//                String[] genderOptions = getResources().getStringArray(R.array.gender_array);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, genderOptions);
//                editGender.setAdapter(adapter);
//                editGender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View v, boolean hasFocus) {
//                        if (hasFocus) {
//                            editGender.showDropDown();
//                        }
//                    }
//                });
//
//                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Post");
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
//                {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                String userId = snapshot.child("UserId").getValue(String.class);
//                                if (userId != null && userId.equals(mAuth.getCurrentUser().getUid())) {
//                                    editName.setText(snapshot.child("Name").getValue(String.class));
//                                    editPhone.setText(snapshot.child("Phone").getValue(String.class));
//                                    editAge.setText(snapshot.child("Age").getValue(String.class));
//                                    editGender.setText(snapshot.child("Gender").getValue(String.class), false);
//                                    editAbout.setText(snapshot.child("About").getValue(String.class));
//                                    editDob.setText(snapshot.child("DOB").getValue(String.class));
//
//                                    break; // assuming you only need one match
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//
//                saveButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mAuth.getCurrentUser() != null) {
//                            String currentUserID = mAuth.getCurrentUser().getUid();
//                            databaseReference.orderByChild("UserId").equalTo(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                    if (dataSnapshot.exists()) {
//                                        String postId = "";
//                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                            postId = snapshot.getKey(); // Assume there's only one matching post
//                                            break;
//                                        }
//
//                                        if (!postId.isEmpty()) {
//                                            Map<String, Object> userUpdates = new HashMap<>();
//                                            userUpdates.put("Name", editName.getText().toString());
//                                            userUpdates.put("Phone", editPhone.getText().toString());
//                                            userUpdates.put("Age", editAge.getText().toString());
//                                            userUpdates.put("Gender", editGender.getText().toString());
//                                            userUpdates.put("About", editAbout.getText().toString());
//                                            userUpdates.put("DOB", editDob.getText().toString());
//
//                                            databaseReference.child(postId).updateChildren(userUpdates)
//                                                    .addOnSuccessListener(aVoid -> {
//                                                        Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
//                                                        // Immediately update the UI to reflect changes
//                                                        tvpname.setText(editName.getText().toString());
//                                                        tvEmail.setText(editPhone.getText().toString()); // Assuming this should show phone
//                                                        tvgender.setText(editGender.getText().toString());
//                                                        tvage.setText(editAge.getText().toString());
////                                                        tvdob.setText(editDob.getText().toString());
//                                                        tvdob.setText(editDob.getText().toString());
//                                                        tvpnumber.setText(editPhone.getText().toString());
//                                                        tvabout.setText(editAbout.getText().toString());
//                                                        dialog.dismiss();
//                                                    })
//                                                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_LONG).show());
//                                        }
//                                    } else {
//                                        Toast.makeText(ProfileActivity.this, "No matching user post found", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                    Toast.makeText(ProfileActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    }
//                });
//
////
////
//                editDob.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final Calendar c = Calendar.getInstance();
//                        int year = c.get(Calendar.YEAR);
//                        int month = c.get(Calendar.MONTH);
//                        int day = c.get(Calendar.DAY_OF_MONTH);
//
//                        DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this,
//                                new DatePickerDialog.OnDateSetListener() {
//                                    @Override
//                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                        editDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                    }
//                                }, year, month, day);
//                        datePickerDialog.show();
//                    }
//                });
////                dialog.dismiss();  // Dismiss the dialog after saving
//            }
//        });
//
//
//    }
//
//    private void getUserProfile() {
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Post");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String name="";
//                String phone="";
//                String age="";
//                String gender="";
//                String about="";
//                String dob="";
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String userId = snapshot.child("UserId").getValue(String.class);
//                        if (userId != null && userId.equals(mAuth.getCurrentUser().getUid())) {
//                            name = snapshot.child("Name").getValue(String.class);
//                            phone= snapshot.child("Phone").getValue(String.class);
//                            age= snapshot.child("Age").getValue(String.class);
//                            about= snapshot.child("About").getValue(String.class);
//                            gender= snapshot.child("Gender").getValue(String.class);
//                            dob= snapshot.child("Dob").getValue(String.class);
//                            String sivpp = ""; // Initialize with empty string
//                            if (encodedImage != null) {
//                                sivpp = encodedImage;
//                            } else if (profilePictureUrl != null) {
//                                sivpp = profilePictureUrl;
//                            }
//                            // Update UI elements
//                            updateUI(name, phone, age, about, gender, dob, sivpp);
//                            break; // Assuming you only need one match
//                        }
//                    }
//                    if (name == null) {
//                        Toast.makeText(ProfileActivity.this, "ProfileActivity Name is null", Toast.LENGTH_SHORT).show();
//                    }
//                    ivpp = findViewById(R.id.ivpp);
////                ivpp.setImageResource();
//
//                    tvpname.setText(name);
//                    tvEmail.setText(mAuth.getCurrentUser().getEmail());
//                    tvgender.setText(gender);
//                    tvage.setText(age);
//                    tvdob.setText(dob);
//                    tvpnumber.setText(phone);
//                    tvabout.setText(about);
//                    // Repeat for other fields
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("DatabaseError", databaseError.getMessage());
//            }
//        });
//    }
//
//    private void updateUI(String name, String phone, String age, String about, String gender, String dob, String encodedImage) {
//        if (name == null) {
//            Toast.makeText(ProfileActivity.this, "Name is null", Toast.LENGTH_SHORT).show();
//            return; // Exit method if name is null
//        }
//
//        tvpname.setText(name);
//        tvEmail.setText(mAuth.getCurrentUser().getEmail());
//        tvgender.setText(gender);
//        tvage.setText(age);
//        tvdob.setText(dob);
//        tvpnumber.setText(phone);
//        tvabout.setText(about);
//
//        // Decode encoded image string into Bitmap and set it to ImageView
//        if (encodedImage != null && !encodedImage.isEmpty()) {
//            Bitmap bitmap = FeedAdapter.getUserImage(encodedImage);
//            ivpp.setImageBitmap(bitmap);
//        }
//    }
//}