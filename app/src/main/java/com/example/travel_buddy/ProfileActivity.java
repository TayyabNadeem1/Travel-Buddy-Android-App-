package com.example.travel_buddy;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Context c;
    private DatabaseReference databaseReference;
    TextView tvpname, tvEmail, tvpnumber,tvgender,tvage,tvcountry,tvabout;
    RoundedImageView ivpp;
    FrameLayout pplayout;
    private String encodedImage = "";
    FloatingActionButton fabedit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Post");

        getUserProfile();
        tvpname = findViewById(R.id.tvpname);
        fabedit = findViewById(R.id.fabedit);
        tvEmail = findViewById(R.id.tvEmail);
        tvgender = findViewById(R.id.tvgender);
        tvage = findViewById(R.id.tvage);
        tvcountry = findViewById(R.id.tvcountry);
        tvpnumber = findViewById(R.id.tvpnumber);
        tvabout = findViewById(R.id.tvabout);
        ivpp=findViewById(R.id.ivpp);
        pplayout=findViewById(R.id.pplayout);
        String cuser="";


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
                                    ivpp.setImageBitmap(bitmap);
                                    ivpp.setBackground(null);

                                    encodedImage = encodeImage(bitmap);
                                    setimage(encodedImage);

                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        pplayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
                return false;
            }

            @Override
            public boolean onLongClickUseDefaultHapticFeedback(@NonNull View v) {

                return View.OnLongClickListener.super.onLongClickUseDefaultHapticFeedback(v);
            }

        });

        fabedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setView(inflater.inflate(R.layout.activity_edit_form, null))
                        .setCancelable(true);  // Allows dismissal by touching outside

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                TextInputEditText editDob, editAbout,editAge,editPhone,editName;
                AutoCompleteTextView editGender,editCountry;
//                editDob=dialog.findViewById(R.id.editDob);
                editAbout=dialog.findViewById(R.id.editAbout);
                editGender=dialog.findViewById(R.id.editGender);
                editAge=dialog.findViewById(R.id.editAge);
                editPhone=dialog.findViewById(R.id.editPhone);
                editCountry = dialog.findViewById(R.id.editCountry);
                editName=dialog.findViewById(R.id.editName);
                Button saveButton = dialog.findViewById(R.id.saveButton);
                String[] genderOptions = getResources().getStringArray(R.array.gender_array);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity.this,
                        android.R.layout.simple_dropdown_item_1line, genderOptions);
                editGender.setAdapter(adapter);
                editGender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            editGender.showDropDown();
                        }
                    }
                });





                ArrayAdapter<String> countryAdapter = getStringArrayAdapter();

                editCountry.setAdapter(countryAdapter);
                editCountry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        {

                            editCountry.showDropDown();

                        }
                    }
                });









                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Post");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String userId = snapshot.child("UserId").getValue(String.class);
                                if (userId != null && userId.equals(mAuth.getCurrentUser().getUid())) {
                                    editName.setText(snapshot.child("Name").getValue(String.class));
                                    editPhone.setText(snapshot.child("Phone").getValue(String.class));
                                    editAge.setText(snapshot.child("Age").getValue(String.class));
                                    editGender.setText(snapshot.child("Gender").getValue(String.class), false);
                                    editAbout.setText(snapshot.child("About").getValue(String.class));
                                    editCountry.setText(snapshot.child("Country").getValue(String.class));

                                    break; // assuming you only need one match
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAuth.getCurrentUser() != null) {
                            String currentUserID = mAuth.getCurrentUser().getUid();
                            databaseReference.orderByChild("UserId").equalTo(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        String postId = "";
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            postId = snapshot.getKey(); // Assume there's only one matching post
                                            break;
                                        }

                                        if (!postId.isEmpty()) {
                                            Map<String, Object> userUpdates = new HashMap<>();
                                            userUpdates.put("Name", editName.getText().toString());
                                            userUpdates.put("Phone", editPhone.getText().toString());
                                            userUpdates.put("Age", editAge.getText().toString());
                                            userUpdates.put("Gender", editGender.getText().toString());
                                            userUpdates.put("About", editAbout.getText().toString());
                                            userUpdates.put("Country", editCountry.getText().toString());

                                            databaseReference.child(postId).updateChildren(userUpdates)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                                                        // Immediately update the UI to reflect changes
                                                        tvpname.setText(editName.getText().toString());

                                                        tvgender.setText(editGender.getText().toString());
                                                        tvage.setText(editAge.getText().toString());
//
                                                        tvcountry.setText(editCountry.getText().toString());
                                                        tvpnumber.setText(editPhone.getText().toString());
                                                        tvabout.setText(editAbout.getText().toString());
                                                        dialog.dismiss();
                                                    })
                                                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_LONG).show());
                                        }
                                    } else {
                                        Toast.makeText(ProfileActivity.this, "No matching user post found", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(ProfileActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });






            }
        });


    }

    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter() {
        TreeSet<String> countries = new TreeSet<>();
        String[] isoCountries = Locale.getISOCountries();
        for (String countryISO : isoCountries) {
            Locale locale = new Locale("", countryISO);
            String countryName = locale.getDisplayCountry();
            if (!countryName.isEmpty()) {
                countries.add(countryName);  // TreeSet automatically sorts and removes duplicates
            }
        }



        return new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(countries));
    }

    @NonNull
    private void getUserProfile() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Post");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name="";
                String phone="";
                String age="";
                String gender="";
                String about="";
                String country="";
                String epp="";
                Bitmap dpp = null;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.child("UserId").getValue(String.class);
                        if (userId != null && userId.equals(mAuth.getCurrentUser().getUid())) {
                            name = snapshot.child("Name").getValue(String.class);
                            phone= snapshot.child("Phone").getValue(String.class);
                            age= snapshot.child("Age").getValue(String.class);
                            about= snapshot.child("About").getValue(String.class);
                            gender= snapshot.child("Gender").getValue(String.class);
                            country= snapshot.child("Country").getValue(String.class);
                            epp= snapshot.child("Profile Picture").getValue(String.class);
                            if (epp!=null)
                            {
                                dpp = decodeFromBase64(epp);
                                if (dpp == null) {
                                    Toast.makeText(ProfileActivity.this, "Failed to decode bitmap.",Toast.LENGTH_SHORT);
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Bitmap decoded successfully.",Toast.LENGTH_SHORT);
                                    ivpp.setBackground(null);
                                }

                            }

                            break; // assuming you only need one match
                        }
                    }

                    ivpp.setImageBitmap(dpp);





                    tvpname.setText(name);
                    tvEmail.setText(mAuth.getCurrentUser().getEmail());
                    tvgender.setText(gender);
                    tvage.setText(age);
                    tvcountry.setText(country);
                    tvpnumber.setText(phone);
                    tvabout.setText(about);
                    // Repeat for other fields
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DatabaseError", databaseError.getMessage());
            }
        });
    }

    private void setimage(String encodedImage)
    {
        if (mAuth.getCurrentUser() != null) {
            String currentUserID = mAuth.getCurrentUser().getUid();
            databaseReference.orderByChild("UserId").equalTo(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String postId = "";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            postId = snapshot.getKey(); // Assume there's only one matching post
                            break;
                        }

                        if (!postId.isEmpty()) {
                            Map<String, Object> userUpdates = new HashMap<>();
                            userUpdates.put("Profile Picture", encodedImage);


                            databaseReference.child(postId).updateChildren(userUpdates)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ProfileActivity.this, "Profile Picture updated successfully!", Toast.LENGTH_SHORT).show();

                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to update profile Picture " , Toast.LENGTH_LONG).show());
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "No matching user post found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private Bitmap decodeFromBase64(String epp) {
        byte[] decodedBytes = Base64.decode(epp, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private String encodeImage(Bitmap bitmap) {
        int width = 150;
        int height = bitmap.getHeight() * width / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}