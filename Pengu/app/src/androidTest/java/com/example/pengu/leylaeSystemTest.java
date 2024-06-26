package com.example.pengu;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;





@RunWith(AndroidJUnit4.class)
@LargeTest
public class leylaeSystemTest {

    @Rule
    public ActivityScenarioRule<TeacherLogin> activityRule =
            new ActivityScenarioRule<>(TeacherLogin.class);


    @Rule
    public ActivityScenarioRule<AdminBookingActivity> adminActivityRule =
            new ActivityScenarioRule<>(AdminBookingActivity.class);

    private CountingIdlingResource idlingResource;



    @Before
    public void registerIdlingResource() {
        idlingResource = new CountingIdlingResource("Login");
        IdlingRegistry.getInstance().register(idlingResource);
    }





    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test

    public void testLoginInputs() {
        // Clear text in the email and password fields
        onView(withId(R.id.Textmail)).perform(replaceText(""));
        onView(withId(R.id.Textpassword)).perform(replaceText(""));

        // Enter text in the email and password fields
        onView(withId(R.id.Textmail)).perform(replaceText("merve@email.com"));
        onView(withId(R.id.Textpassword)).perform(replaceText("safepwd"));

        // Click the login button
        onView(withId(R.id.button)).perform(click());

        // Check if the TeacherDashboard or an error message is displayed
        // This might need adjustment based on actual result handling in the app
        onView(withId(R.id.button2)).check(matches(isDisplayed()));

        onView(withId(R.id.button2)).perform(click());

//        onView(withId(R.id.subjectsRecyclerView)).check(matches(isDisplayed()));
////        onView(withId(R.id.subjectsRecyclerView))
////                .check(matches(hasDescendant(withText("arts"))))
////                .check(matches(hasDescendant(withText("01:00"))))
////                .check(matches(hasDescendant(withText("2222/02/02")))).perform(click());
//
        onView(withId(R.id.subjectsRecyclerView))
                .check(matches(hasDescendant(withText("soccer"))));

        onView(withId(R.id.SelectASubjectRecycle))
                .check(matches(hasDescendant(withText("+")))).perform(click());
//
        onView(withId(R.id.selectASubject)).check(matches(isDisplayed()));

        onView(withId(R.id.subjectsListRecyclerView))
                .check(matches(hasDescendant(withText("arts"))));
        onView(withId(R.id.subjectsListRecyclerView))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText("arts"))))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("arts")), click()));

  //onView(withId(R.id.button2)).perform(click());
//
//        onView(withId(R.id.subjectsRecyclerView))
//                .check(matches(hasDescendant(withText("arts"))));











    }


    @Test
    public void testAdminLogin() {
        // Clear text in the email and password fields
        onView(withId(R.id.Textmail)).perform(replaceText(""));
        onView(withId(R.id.Textpassword)).perform(replaceText(""));

        // Enter text in the email and password fields
        onView(withId(R.id.Textmail)).perform(replaceText("haad@email.com"));
        onView(withId(R.id.Textpassword)).perform(replaceText("safepwd"));

        // Click the login button
        onView(withId(R.id.button)).perform(click());
        // Add any necessary waits or idling resource handling if there's async work

        // For example:
        onView(withId(R.id.button3)).check(matches(isDisplayed()));
        onView(withId(R.id.button3)).perform(click());




    }

    @Test

    public void testTeachernputs() {
        // Clear text in the email and password fields


        // Click the login button
        onView(withId(R.id.textViewOpenNewpage)).perform(click());


        onView(withId(R.id.TextName)).perform(replaceText("merve"));
        onView(withId(R.id.TextMail)).perform(replaceText("merve@email.com"));
        onView(withId(R.id.TextPassword)).perform(replaceText("safepwd"));

        // Check if the TeacherDashboard or an error message is displayed
        // This might need adjustment based on actual result handling in the app
        onView(withId(R.id.button)).check(matches(isDisplayed()));

        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(withId(R.id.textView)).perform(click());



    }

    @Test
    public void testAdminBooking() {
        // Clear text in the email and password fields
        onView(withId(R.id.Textmail)).perform(replaceText(""));
        onView(withId(R.id.Textpassword)).perform(replaceText(""));

        // Enter text in the email and password fields
        onView(withId(R.id.Textmail)).perform(replaceText("haad@email.com"));
        onView(withId(R.id.Textpassword)).perform(replaceText("safepwd"));

        // Click the login button
        onView(withId(R.id.button)).perform(click());
        // Add any necessary waits or idling resource handling if there's async work

        // For example:
        onView(withId(R.id.button3)).check(matches(isDisplayed()));
        onView(withId(R.id.button3)).perform(click());

        // Wait for the new page to load (you can replace this with an idling resource if available)
//        try {
//            Thread.sleep(1000); // 1 second delay, adjust as needed
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        onView(withId(R.id.addSubjectButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addSubjectButton)).perform(click());

        onView(withId(R.id.editStudentID)).perform(replaceText("2"));
        onView(withId(R.id.editTeacherID)).perform(replaceText("5"));
        onView(withId(R.id.editSubjectName)).perform(replaceText("arts"));
        onView(withId(R.id.editEndTime)).perform(replaceText("02:00"));
        onView(withId(R.id.editStartTime)).perform(replaceText("01:00"));
        onView(withId(R.id.editDate)).perform(replaceText("2222/02/02"));

        //Thread.sleep(500); // Wait for 500 milliseconds
        onView(withText("Create")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.subjectsRecyclerView))
                .check(matches(hasDescendant(withText("arts"))));


        onView(withId(R.id.subjectsRecyclerView))
                .check(matches(hasDescendant(withText("arts"))))
                .check(matches(hasDescendant(withText("01:00"))))
                .check(matches(hasDescendant(withText("2222/02/02"))));
//
//        onView(withId(R.id.subjectsRecyclerView)) // Replace with your RecyclerView's ID
//                .perform(RecyclerViewActions.scrollTo(
//                        hasDescendant(allOf(
//                                withId(R.id.deleteBookingImageView),
//                                isDisplayed())))
//                )
//                .perform(RecyclerViewActions.actionOnItem(
//                        hasDescendant(withId(R.id.deleteBookingImageView)), click()));




    }


}








