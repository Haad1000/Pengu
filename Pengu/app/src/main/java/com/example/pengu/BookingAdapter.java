package com.example.pengu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import android.util.Log;
import android.widget.ImageView;
import android.content.Context;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
        void onClick();

    }


    private final Context context;
    private List<BookingInfo> bookingList;
    private Map<Integer, String> subjectIdToNameMap;;
    private final OnItemClickListener listener;


    public BookingAdapter(Context context, List<BookingInfo> bookingList, Map<Integer, String> subjectIdToNameMap, OnItemClickListener listener) {
        this.context = context;
        this.bookingList = bookingList;
        this.subjectIdToNameMap = subjectIdToNameMap;
        this.listener = listener; // Now the listener is being passed in and initialized
    }




    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_booking_item, parent, false);
        return new BookingViewHolder(itemView, context, bookingList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingInfo booking = bookingList.get(position);

        Log.d("BookingAdapter", "BookingId:"+booking.getId()+"Position: " + position +
                " SubjectID: " + booking.getSubjectID() +
                " TeacherID: " + booking.getTeacherID() +
                " StudentID: " + booking.getStudentID() +
                " Date: " + booking.getDate() +
                " Start Time: " + booking.getStartTime() +
                " End Time: " + booking.getEndTime());

        if (subjectIdToNameMap != null) {
            String subjectName = subjectIdToNameMap.get(booking.getSubjectID());
            holder.subjectNameTextView.setText(subjectName != null ? subjectName : "Unknown Subject");
        } else {
            holder.subjectNameTextView.setText("Subject Name Unavailable");
        }
        // Set other TextViews similarly...
        holder.editBookingImageView.setOnClickListener(view -> {
            // Get the current BookingInfo instance
            BookingInfo bookingToEdit = bookingList.get(holder.getAdapterPosition());

            // Launch the editing dialog; context should be passed in from the Activity
            if (context instanceof AdminBookingActivity) {
                ((AdminBookingActivity) context).showEditBookingDialog(bookingToEdit, holder.getAdapterPosition());
            }
        });

        holder.teacherNameTextView.setText(booking.getTeacherName());
        holder.studentNameTextView.setText(booking.getStudentName());
        holder.startTimeTextView.setText(booking.getStartTime());
        holder.endTimeTextView.setText(booking.getEndTime());
        holder.dateTextView.setText(booking.getDate());
        holder.deleteBookingImageView.setOnClickListener(v -> {
            // Use the listener to communicate with the Activity or Fragment
            listener.onDeleteClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final List<BookingInfo> bookingList;
        public TextView subjectNameTextView;
        public TextView teacherNameTextView;
        public TextView studentNameTextView;
        public TextView startTimeTextView;
        public TextView endTimeTextView;
        public TextView dateTextView;
        public ImageView deleteBookingImageView, editBookingImageView;




        public BookingViewHolder(View view, final Context context, final List<BookingInfo> bookingList, final OnItemClickListener listener) {
            super(view);
            this.context = context;
            this.bookingList = bookingList;
            deleteBookingImageView = view.findViewById(R.id.deleteBookingImageView);
            subjectNameTextView = view.findViewById(R.id.subjectNameTextView);
            teacherNameTextView = view.findViewById(R.id.teacherNameTextView);
            studentNameTextView = view.findViewById(R.id.studentNameTextView);
            startTimeTextView = view.findViewById(R.id.startTimeTextView);
            endTimeTextView = view.findViewById(R.id.endTimeTextView);
            dateTextView = view.findViewById(R.id.dateTextView);
            editBookingImageView= view.findViewById(R.id.editBookingImageView);



            // initialize other views...

            // Set the click listener in the constructor of your ViewHolder


            deleteBookingImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });
            editBookingImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Assuming you have a reference to AdminBookingActivity through a context or other means
                    if (context instanceof AdminBookingActivity) {
                        ((AdminBookingActivity) context).showEditBookingDialog(bookingList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }
    }
    public void deleteBooking(int bookingId, int position) {
        // Code for the DELETE request as previously shown...
        // After successful deletion:
        bookingList.remove(position);
        notifyItemRemoved(position);
        // Optionally, call notifyItemRangeChanged if you feel it's necessary
        // notifyItemRangeChanged(position, bookingList.size());
    }
}
