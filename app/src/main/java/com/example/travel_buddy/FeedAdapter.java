package com.example.travel_buddy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PostViewHolder> {
    private final ArrayList<User> users;

    public FeedAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_feed_design, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvName.setText(user.getName());
        holder.tvSource.setText(user.getSource());
        holder.tvDestination.setText(user.getDestination());
        holder.tvBio.setText(user.getBio());

        // Decode and set image
        Bitmap decodedImage = getUserImage(String.valueOf(user.getPfp()));
        holder.ivPFP.setImageBitmap(decodedImage);

//        // Encode and send image to Firebase (if needed)
//        String encodedImage = encodeImage(decodedImage);
//        // Send the encodedImage to Firebase...
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Decode
    public static Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    // Encode
    static String encodeImage(Bitmap bitmap) {
        int width = 150;
        int height = bitmap.getHeight() * width / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }



    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBio, tvDate, tvSource, tvDestination;
        RoundedImageView ivPFP;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvBio = itemView.findViewById(R.id.tvBio);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            ivPFP = itemView.findViewById(R.id.ivPFP);
        }
    }
}
