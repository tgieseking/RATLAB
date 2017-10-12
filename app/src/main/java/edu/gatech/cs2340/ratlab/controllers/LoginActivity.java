package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.UserManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    // Reference to the Firebase authorization backend
    private FirebaseAuth mAuth;
    private UserManager userManager;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        userManager = userManager.getInstance();
    }

    /**
     * Checks if the inputted username and password are valid. If
     * they are valid, the user is allowed to continue to the MainActivity class. Otherwise, an
     * explanatory error message is shown.
     *
     * @param view the login button
     */
    public void checkLogin(View view) {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.length() == 0) {
            Toast.makeText(LoginActivity.this, "Empty email",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(LoginActivity.this, "Empty password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userManager.login();
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * The method called when the back button on the login activity is clicked.
     * When clicked, the screen starts up WelcomeActivity.
     *
     * @param view the back button
     */
    public void goBack(View view) {
        finish();
    }
}

