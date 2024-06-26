package com.example.pengu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChoseTimeSlotAdapter extends RecyclerView.Adapter<ChoseTimeSlotAdapter.ViewHolder> {
    private List<ChoseTimeSlot> timeSlots;  // Ensure the type here matches the list type.

    public ChoseTimeSlotAdapter(List<ChoseTimeSlot> timeSlots) {  // The parameter type should match the field type.
        this.timeSlots = timeSlots;
    }
    public void updateSlots(List<ChoseTimeSlot> newSlots) {
        this.timeSlots.clear();
        this.timeSlots.addAll(newSlots);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chosen_timeslots_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChoseTimeSlot slot = timeSlots.get(position);  // Fixed the variable name here.
        holder.tvDate.setText(slot.getDate());
        holder.tvTimeRange.setText(slot.getStartTime() + " - " + slot.getEndTime());
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();  // Fixed the variable name here.
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvTimeRange;

        public ViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.startTime);
            tvTimeRange = view.findViewById(R.id.endTime);
        }
    }
}
