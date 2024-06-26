package com.example.pengu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.util.Log;
import android.util.Log;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<Review> reviewList;

    public ReviewsAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);

        // Log the username and professor's name
        Log.d("ReviewAdapter", "Username at position " + position + " is: " + review.getUsername());
        Log.d("ReviewAdapter", "Professor's name at position " + position + " is: " + review.getProfessorName());

        // Set the username and professor's name in the TextViews
        if (review.getUsername() != null && !review.getUsername().isEmpty()) {
            holder.usernameTextView.setText(review.getUsername());
        } else {
            holder.usernameTextView.setText("Anonymous"); // or any default text you prefer
        }

//        if (review.getProfessorName() != null && !review.getProfessorName().isEmpty()) {
//            holder.professorNameTextView.setText(review.getProfessorName());
//        } else {
//            holder.professorNameTextView.setText("Unknown Professor"); // or any default text you prefer
//        }


        // Set the review text
        holder.reviewTextView.setText(review.getReviewText());
    }


    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewTextView;
        TextView usernameTextView;
//        TextView professorNameTextView;



        public ViewHolder(View itemView) {
            super(itemView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
//            professorNameTextView = itemView.findViewById(R.id.professorNameTextView);

        }
    }
}