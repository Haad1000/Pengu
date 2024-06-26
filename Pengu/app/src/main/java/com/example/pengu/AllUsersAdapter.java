package com.example.pengu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.UserViewHolder> {
    private List<AllUser> userList;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public AllUsersAdapter(List<AllUser> userList, OnDeleteClickListener onDeleteClickListener) {
        this.userList = userList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alluser_item, parent, false);
        return new UserViewHolder(view);
    }




    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        AllUser user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.emailTextView.setText(user.getEmail());
        holder.roleTextView.setText(user.getRole());

        // Set the correct icon based on whether the user is banned
        if (user.isBanned()) {
            holder.deleteImageView.setImageResource(R.drawable.unblock);  // Change to 'unblock' icon
            // More descriptive content description
            holder.deleteImageView.setContentDescription("Click to unban " + user.getName() + ", currently banned.");
        } else {
            holder.deleteImageView.setImageResource(R.drawable.block);  // Reset to default 'block' icon
            // More descriptive content description
            holder.deleteImageView.setContentDescription("Click to ban " + user.getName() + ", currently active.");
        }

        // Setting up click listener for the icon
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
                }
            }
        });
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        TextView roleTextView;
        ImageView deleteImageView;

        public UserViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            deleteImageView = itemView.findViewById(R.id.deleteBookingImageView);
        }
    }
}
