package com.example.pengu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<TimeSlot> timeSlots;
    private TimeSlotClickListener listener;




    public TimeSlotAdapter(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
    public void setListener(TimeSlotClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeslot_item, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        TimeSlot timeSlot = timeSlots.get(position);
        holder.startTimeTextView.setText(timeSlot.getStartTime());
        holder.endTimeTextView.setText(timeSlot.getEndTime());

        // Set click listener or any other view updates if needed
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView startTimeTextView;
        TextView endTimeTextView;
        ImageView addButton;
       // String token;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            startTimeTextView = itemView.findViewById(R.id.startTime);
            endTimeTextView = itemView.findViewById(R.id.endTime);
            addButton = itemView.findViewById(R.id.addButton);

            if (addButton != null) {
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getBindingAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            TimeSlot timeSlot = timeSlots.get(position);
                            int dateId = timeSlot.getDateId(); // Replace getDateId() with the actual method to get the date ID
                            int id = timeSlot.getId();
                            Log.d("Adapter", "Button clicked for position " + position + " with dateId " + dateId + " and timeSlotId " + id);
                            listener.onAddButtonClick(timeSlot, dateId, id);

                        }
                    }
                });
            } else {
                Log.e("Adapter", "addButton is null, check your layout file");
            }
        }

    }

    public interface TimeSlotClickListener {
        void onAddButtonClick(TimeSlot timeSlot, int dateId, int timeSlotId);
    }
    public void setData(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
        notifyDataSetChanged();
    }


}
