package edu.gatech.cs2340.ratlab;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.cs2340.ratlab.controllers.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by tiffanyxu on 11/8/17.
 */

@RunWith(AndroidJUnit4.class)
public class UITest {
    @Rule
    public final ActivityRule<LoginActivity> main = new ActivityRule<>(LoginActivity.class);

    @Test
    public void shouldBeAbleToLogIn() {
        onView(withId(R.id.email)).perform(typeText("test2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password2"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
    }
}
