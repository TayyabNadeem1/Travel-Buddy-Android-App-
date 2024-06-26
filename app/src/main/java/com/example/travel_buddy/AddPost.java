package com.example.travel_buddy;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class AddPost extends AppCompatActivity {


    Button btnSave, btnCancel;
    TextInputEditText etName, etBio, etSource, etDestination,etPhone;
    TextInputLayout name;
    RoundedImageView riProfilePic;
    TextView getTvAddImage;
    FrameLayout layoutImage;
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
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        layoutImage = findViewById(R.id.layoutImage);
        tvAddImage = findViewById(R.id.tvAddImage);
        riProfilePic = findViewById(R.id.riProfilePic);

        encodedImage = getIntent().getStringExtra("encodedImage");
        profilePictureUrl = getIntent().getStringExtra("profilePictureUrl");

        if (encodedImage != null) {
            setEncodedImage(encodedImage);
            layoutImage.setVisibility(View.GONE);
        } else if (profilePictureUrl != null) {
            setEncodedImage(profilePictureUrl);
            layoutImage.setVisibility(View.GONE);
        } else if (profilePictureUrl == null && encodedImage == null) {

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
                                    encodedImage = encodeImage(bitmap);

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


    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    private void savePost() {
        String bio = Objects.requireNonNull(etBio.getText()).toString().trim();
        String name = Objects.requireNonNull(etName.getText()).toString().trim();
        String source = Objects.requireNonNull(etSource.getText()).toString().trim();
        String destination = Objects.requireNonNull(etDestination.getText()).toString().trim();
        String phone = Objects.requireNonNull(etPhone.getText()).toString().trim();
        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(AddPost.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bio.isEmpty() && !name.isEmpty() && !source.isEmpty() && !destination.isEmpty()&& !phone.isEmpty()) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                if (layoutImage.getVisibility() == View.VISIBLE && encodedImage == null && profilePictureUrl == null) {
                    Toast.makeText(AddPost.this, "Please upload an image", Toast.LENGTH_SHORT).show();
                } else {
                    String userId = user.getUid();

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("Name", name);
                    data.put("Bio", bio);
                    data.put("Source", source);
                    data.put("Destination", destination);
                    data.put("Phone", phone);

                        if (encodedImage != null) {
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
                }
            } else {
                Toast.makeText(AddPost.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddPost.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isValidPhoneNumber(String phoneNumber) {

        String phonePattern = "^[0-9]{11}$";
        return phoneNumber.matches(phonePattern);
    }

}
