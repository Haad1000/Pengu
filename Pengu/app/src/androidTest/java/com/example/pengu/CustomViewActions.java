package com.example.pengu;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;


import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;

public class CustomViewActions {
    public static ViewAction setRating(final float rating) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(android.widget.RatingBar.class);
            }

            @Override
            public String getDescription() {
                return "Set RatingBar to " + rating;
            }

            @Override
            public void perform(UiController uiController, View view) {
                // Check if the view is a RatingBar
                if (view instanceof android.widget.RatingBar) {
                    ((android.widget.RatingBar) view).setRating(rating); // Set the specified rating
                } else {
                    throw new IllegalArgumentException("View must be a RatingBar");
                }
            }
        };
    }
}


