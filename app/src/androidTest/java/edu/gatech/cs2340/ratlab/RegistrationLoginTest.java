package edu.gatech.cs2340.ratlab;

import android.support.test.espresso.action.ViewActions;

import org.junit.Rule;
import org.junit.Test;

import edu.gatech.cs2340.ratlab.controllers.RegistrationActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Espresso test that tests the UI of registration activity.
 */
public class RegistrationLoginTest {
    @Rule
    public final ActivityRule<RegistrationActivity> main =
            new ActivityRule<>(RegistrationActivity.class);

    /**
     * Method that performs actions on registration activity.
     */
    @Test
    public void shouldBeAbleToRegister() {
        onView(withId(R.id.nameTextBox)).perform(typeText("name"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        //Test that empty user name causes empty username toast to appear
        onView(withId(R.id.usernameTextBox)).perform(typeText("nameUser"),
                ViewActions.closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        //Tests that empty email toast is displayed when trying to register without inputting email
        onView(withId(R.id.emailTextBox)).perform(typeText("name@gmail.com"),
                ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordTextBox)).perform(typeText("123"),
                ViewActions.closeSoftKeyboard());
        //Tests that password input less than length 6 will cause password too
        // short toast to show up/onView(withText("Password too short"))
        // .inRoot(withDecorView(not(is(main.get().getWindow().getDecorView()))))
        // .check(matches(isDisplayed()));
        onView(withId(R.id.passwordTextBox)).perform(typeText("name123"),
                ViewActions.closeSoftKeyboard());
        onView(withId(R.id.accountTypeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("User"))).perform(click());
        onView(withId(R.id.registerButton)).perform(click());
    }

}
