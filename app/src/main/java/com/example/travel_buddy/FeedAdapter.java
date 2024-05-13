package com.example.travel_buddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PostViewHolder> implements Filterable {

    private final ArrayList<User> users;
    private FirebaseAuth mAuth;
    private final ArrayList<User> usersFull; // This will contain the full list of users

    // Constructor
    public FeedAdapter(ArrayList<User> users) {
        this.users = users;
        this.usersFull = new ArrayList<>(users); // Copy the original list
    }



    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_feed_design, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = users.get(position);

        holder.tvName.setText(user.getName());
        holder.tvSource.setText(user.getSource());
        holder.tvDestination.setText(user.getDestination());
        holder.tvBio.setText(user.getBio());
        holder.ivPFP.setImageBitmap(user.getPfp());
        //holder.tvConnect.setText(user.getPhone());
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.anim_one ));

        mAuth = FirebaseAuth.getInstance();
        // Set click listener for the profile picture
        holder.ivPFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to PostProfileActivity

                Intent intent = new Intent(holder.itemView.getContext(), PostProfile.class);

                FirebaseUser currentUser = mAuth.getCurrentUser();
                String userId;

                userId = currentUser.getUid();

                // Pass the user ID of the user who posted the feed post as an intent extra
                intent.putExtra("userId", userId);

                // Start the activity
                holder.itemView.getContext().startActivity(intent);
            }
        });



        holder.llConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = users.get(position);
                String phoneNumber = user.getPhone();
                Log.d("FeedAdapter", "Phone number: " + phoneNumber);
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"+user.getPhone()));
                    holder.itemView.getContext().startActivity(intent);

                } else {

                    Toast.makeText(holder.itemView.getContext(), "Phone number not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private final Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // If search query is empty, return the full list
                filteredList.addAll(usersFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.d("Filter", "Search Text: " + filterPattern); // Log the search text

                // Iterate through the full list and add matching items to filteredList
                for (User user : usersFull) {
                    String destination = user.getDestination().toLowerCase();
                    Log.d("Filter", "Name from Database: " + destination); // Log the name fetched from the database

                    if (destination.contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users.clear(); // Clear the current list
            users.addAll((List<User>) results.values); // Add filtered items to the list
            notifyDataSetChanged(); // Notify RecyclerView of data changes
        }
    };


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBio, tvDate, tvSource, tvDestination, tvConnect;
        RoundedImageView ivPFP;
        ImageView ivConnect;
        LinearLayout llConnect;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvBio = itemView.findViewById(R.id.tvBio);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvConnect = itemView.findViewById(R.id.tvConnect);
            ivConnect = itemView.findViewById(R.id.ivConnect);
            llConnect = itemView.findViewById(R.id.llConnect);
            ivPFP = itemView.findViewById(R.id.ivPFP);
        }


    }
}
