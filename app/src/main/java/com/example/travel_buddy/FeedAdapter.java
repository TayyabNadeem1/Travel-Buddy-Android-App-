package com.example.travel_buddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PostViewHolder> {

    private final ArrayList<User> users;

    public FeedAdapter( ArrayList<User> users) {

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
        holder.tvName.setText(users.get(position).getName());
        holder.tvSource.setText(users.get(position).getSource());
        holder.tvDestination.setText(users.get(position).getDestination());
        holder.tvBio.setText(users.get(position).getBio());



    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvBio, tvDate, tvSource, tvDestination;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);


            tvName = itemView.findViewById(R.id.tvName);
            tvBio = itemView.findViewById(R.id.tvBio);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDestination = itemView.findViewById(R.id.tvDestination);
        }
    }
}