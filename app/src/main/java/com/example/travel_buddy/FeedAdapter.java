package com.example.travel_buddy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PostViewHolder> implements Filterable {

    private ArrayList<User> users;
    private ArrayList<User> usersFull; // This will contain the full list of users

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
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvName.setText(user.getName());
        holder.tvSource.setText(user.getSource());
        holder.tvDestination.setText(user.getDestination());
        holder.tvBio.setText(user.getBio());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.anim_one ));

        // Decode and set image
        Bitmap decodedImage = getUserImage(String.valueOf(user.getPfp()));
        holder.ivPFP.setImageBitmap(decodedImage);
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

    private Filter userFilter = new Filter() {
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
                    String name = user.getName().toLowerCase();
                    Log.d("Filter", "Name from Database: " + name); // Log the name fetched from the database

                    if (name.contains(filterPattern)) {
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
