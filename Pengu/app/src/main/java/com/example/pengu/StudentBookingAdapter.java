package com.example.pengu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentBookingAdapter extends RecyclerView.Adapter<StudentBookingAdapter.StudentBookingViewHolder> {
    private List<StudentBookingItem> bookingItems;

    // Constructor for the adapter, takes a list of StudentBookingItem objects
    public StudentBookingAdapter(List<StudentBookingItem> bookingItems) {
        this.bookingItems = bookingItems;
    }

    @NonNull
    @Override
    public StudentBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_another_test, parent, false);
        return new StudentBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentBookingViewHolder holder, int position) {
        // Get the StudentBookingItem at the given position
        StudentBookingItem bookingItem = bookingItems.get(position);

        // Set the text in the TextView elements to display the booking information
        holder.subjectName.setText(bookingItem.getSubjectName());
        holder.teacherName.setText(bookingItem.getTeacherName());
        holder.dateTime.setText(bookingItem.getDate());
        holder.startTime.setText(bookingItem.getStartTime());
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return bookingItems.size();
    }

    // ViewHolder class to represent the views for each item in the RecyclerView
    static class StudentBookingViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView teacherName;
        TextView dateTime;
        TextView startTime;

        public StudentBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the TextView elements in the layout
            subjectName = itemView.findViewById(R.id.subjName);
            teacherName = itemView.findViewById(R.id.teacherName);
            dateTime = itemView.findViewById(R.id.dateTime);
            startTime = itemView.findViewById(R.id.startTime);
        }
    }
}
