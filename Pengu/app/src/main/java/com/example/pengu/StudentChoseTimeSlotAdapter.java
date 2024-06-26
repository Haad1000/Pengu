package com.example.pengu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentChoseTimeSlotAdapter extends RecyclerView.Adapter<StudentChoseTimeSlotAdapter.ViewHolder> {
    private List<ChoseTimeSlot> timeSlots;
    private OnSlotClickListener listener;
    public interface OnSlotClickListener {
        void onSlotClick(ChoseTimeSlot slot);
    }

    public StudentChoseTimeSlotAdapter(List<ChoseTimeSlot> timeSlots, OnSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
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
        ChoseTimeSlot slot = timeSlots.get(position);
        holder.tvDate.setText(slot.getDate());
        holder.tvTimeRange.setText(slot.getStartTime() + " - " + slot.getEndTime());
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvTimeRange;

        public ViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.startTime);
            tvTimeRange = view.findViewById(R.id.endTime);

        }
        public void bind(ChoseTimeSlot slot, OnSlotClickListener listener) {
            // Set data to views here
            itemView.setOnClickListener(v -> listener.onSlotClick(slot));
        }
    }
//    public void updateSlots(List<ChoseTimeSlot> newSlots) {
//        timeSlots.clear();
//        timeSlots.addAll(newSlots);
//        notifyDataSetChanged();
//    }
}
