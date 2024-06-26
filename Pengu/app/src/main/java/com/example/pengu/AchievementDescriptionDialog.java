package com.example.pengu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AchievementDescriptionDialog extends DialogFragment {

    private String description;  // Achievement description

    // Constructor to pass the description
    public AchievementDescriptionDialog(String description) {
        this.description = description;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the dialog layout
        View view = inflater.inflate(R.layout.dialog_achievement_description, container, false);

        // Set the description
        TextView descriptionTextView = view.findViewById(R.id.achievementDescription);
        descriptionTextView.setText(description);

        return view;
    }
}
