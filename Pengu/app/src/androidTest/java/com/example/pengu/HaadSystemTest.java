package com.example.pengu;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HaadSystemTest {

//    @Rule
//    public ActivityScenarioRule<StudentDashboard> viewTeachersRule =
//            new ActivityScenarioRule<>(StudentDashboard.class);

//    @Rule
//    public ActivityScenarioRule<StudentLogIn> viewTeachersRule =
//            new ActivityScenarioRule<>(StudentLogIn.class);

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void testFetchStudentProfile() {
        onView(withId(R.id.studentTextView)).perform(click());

        onView(withId(R.id.TextMail)).perform(typeText("selim@email.com"), closeSoftKeyboard());
        onView(withId(R.id.TextPassword)).perform(typeText("safepwd"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click()); // Login button

        onView(withId(R.id.stuProfile)).perform(click());

        onView(withId(R.id.name))
                .check(matches(not(withText(""))));

        onView(withId(R.id.aboutMeBox))
                .check(matches(not(withText(""))));
    }

    @Test
    public void testFetchReports() {
        // Step 1: Open MainActivity and navigate to StudentLogIn
        onView(withId(R.id.studentTextView)).perform(click());

        // Step 2: Log in as a student
        onView(withId(R.id.TextMail)).perform(typeText("haad@email.com"), closeSoftKeyboard());
        onView(withId(R.id.TextPassword)).perform(typeText("safepwd"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click()); // Login button

        // Step 3: Navigate to ViewTeachersActivity from StudentDashboard
        onView(withId(R.id.button4)).perform(click());

        // Step 4: Check that the RecyclerView is displayed in ViewTeachersActivity
        onView(withId(R.id.ongoingReportsRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.doneReportsRecyclerView)).check(matches(isDisplayed()));

        // Step 5: Verify the RecyclerView has at least one item
        onView(withId(R.id.ongoingReportsRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0)) // Scroll to first item
                .check(matches(isDisplayed()));                   // Ensure it's visible

        onView(withId(R.id.ongoingReportsRecyclerView))
                .perform(swipeUp())  // Simulate swipe up
                .check(matches(isDisplayed()));

        onView(withId(R.id.doneReportsRecyclerView)).check(matches(isDisplayed()));

        onView(withId(R.id.doneReportsRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0)) // Scroll to first item
                .check(matches(isDisplayed()));                   // Ensure it's visible

        onView(withId(R.id.doneReportsRecyclerView))
                .perform(swipeUp())  // Simulate swipe up
                .check(matches(isDisplayed()));

    }

    @Test
    public void testFetchTeachersInRecyclerView() {
        // Step 1: Open MainActivity and navigate to StudentLogIn
        onView(withId(R.id.studentTextView)).perform(click());

        // Step 2: Log in as a student
        onView(withId(R.id.TextMail)).perform(typeText("selim@email.com"), closeSoftKeyboard());
        onView(withId(R.id.TextPassword)).perform(typeText("safepwd"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click()); // Login button

        // Step 3: Navigate to ViewTeachersActivity from StudentDashboard
        onView(withId(R.id.viewTeachersButton)).perform(click());

        // Step 4: Check that the RecyclerView is displayed in ViewTeachersActivity
        onView(withId(R.id.teacherRecyclerView)).check(matches(isDisplayed()));

        // Step 5: Verify the RecyclerView has at least one item
        onView(withId(R.id.teacherRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0)) // Scroll to first item
                .check(matches(isDisplayed()));                   // Ensure it's visible

        onView(withId(R.id.teacherRecyclerView))
                .perform(swipeUp())  // Simulate swipe up
                .check(matches(isDisplayed()));

    }

    @Test
    public void testFetchTeacherRating() {
        // Step 1: Open MainActivity and navigate to StudentLogIn
        onView(withId(R.id.studentTextView)).perform(click());

        // Step 2: Log in as a student
        onView(withId(R.id.TextMail)).perform(typeText("selim@email.com"), closeSoftKeyboard());
        onView(withId(R.id.TextPassword)).perform(typeText("safepwd"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click()); // Login button

        // Step 3: Navigate to ViewTeachersActivity from StudentDashboard
        onView(withId(R.id.viewTeachersButton)).perform(click());

        // Step 4: Check that the RecyclerView is displayed in ViewTeachersActivity
        onView(withId(R.id.teacherRecyclerView)).check(matches(isDisplayed()));

        // Step 5: Verify the RecyclerView has at least one item
        onView(withId(R.id.teacherRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0)) // Scroll to first item
                .check(matches(isDisplayed()));// Ensure it's visible

        onView(withId(R.id.teacherRecyclerView))
                .perform(swipeUp())  // Simulate swipe up
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.submitButton)).check(matches(isDisplayed()));

        onView(withId(R.id.ratingPage)).perform(click());

        try {
            // Capture the initial rating
            float initialRating = getRatingValue(R.id.ratingBar);

            // Simulate a click on the RatingBar
            onView(withId(R.id.ratingBar)).perform(click());

            // Check if the rating has changed
            float newRating = getRatingValue(R.id.ratingBar);

            // Verify that the initial and new rating are the same
            if (initialRating != newRating) {
                throw new AssertionError("RatingBar should be non-interactive, but it was changed.");
            }
        } catch (Exception e) {
            throw new AssertionError("RatingBar should be non-interactive, but interaction was possible.", e);
        }

        onView(withId(R.id.goToRateButton)).perform(click());

        onView(withId(R.id.communicationRatingBar)).check(matches(isDisplayed()));
        onView(withId(R.id.teachingRatingBar)).check(matches(isDisplayed()));
        onView(withId(R.id.recommendationRatingBar)).check(matches(isDisplayed()));

        // Step 3: Set ratings
        onView(withId(R.id.communicationRatingBar)).perform(CustomViewActions.setRating(4.0f)); // Set to 4 stars
        onView(withId(R.id.teachingRatingBar)).perform(CustomViewActions.setRating(5.0f));      // Set to 5 stars
        onView(withId(R.id.recommendationRatingBar)).perform(CustomViewActions.setRating(3.0f)); // Set to 3 stars

        // Step 4: Click the create button
        onView(withId(R.id.updateButton)).perform(click());
    }

    private float getRatingValue(int ratingBarId) {
        final float[] rating = new float[1]; // Store the rating value
        onView(withId(ratingBarId)).check((view, noViewFoundException) -> {
            if (view instanceof android.widget.RatingBar) {
                rating[0] = ((android.widget.RatingBar) view).getRating();
            } else {
                throw new AssertionError("Expected a RatingBar but found something else.");
            }
        });
        return rating[0];
    }

}
