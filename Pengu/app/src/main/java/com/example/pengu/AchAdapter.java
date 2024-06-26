package com.example.pengu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AchAdapter extends RecyclerView.Adapter<AchAdapter.AchievementViewHolder> {
    private final List<Achievement> achievements;  // The list of all achievements
    private List<Integer> userAchievements;  // List of IDs for earned achievements
    private final StudentProfile context;

    public AchAdapter(StudentProfile context, List<Achievement> achievements) {
        this.context = context;
        this.achievements = achievements;
        this.userAchievements = new ArrayList<>();  // Initialize as empty
    }

    public void updateUserAchievements(List<Integer> userAchievements) {
        this.userAchievements = userAchievements;
        notifyDataSetChanged();  // Refresh the RecyclerView
    }

    @Override
    public AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ach_layout, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);

        // Bind data to the views
        holder.title.setText(achievement.getTitle());

        // Check if the achievement is earned
        if (userAchievements.contains(achievement.getId())) {
            // If earned, set to normal
            holder.title.setTextColor(context.getResources().getColor(R.color.black));  // Default color
            holder.imageView.setColorFilter(null);  // No tinting
        } else {
            // If unearned, dim the appearance
            holder.title.setTextColor(context.getResources().getColor(R.color.grey));  // Greyed-out text
            holder.imageView.setColorFilter(context.getResources().getColor(R.color.grey));  // Greyed-out icon
        }
        holder.itemView.setOnClickListener(v -> {
            // Show the dialog with the achievement description
            AchievementDescriptionDialog dialog = new AchievementDescriptionDialog(achievement.getDescription());
            dialog.show(context.getSupportFragmentManager(), "AchievementDescription");
        });
    }

    @Override
    public int getItemCount() {
        return achievements.size();  // Total number of achievements
    }

    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;

        public AchievementViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userName);
            imageView = itemView.findViewById(R.id.userProfile);
        }
    }
}
