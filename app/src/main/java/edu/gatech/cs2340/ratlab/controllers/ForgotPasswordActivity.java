package edu.gatech.cs2340.ratlab.controllers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import edu.gatech.cs2340.ratlab.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    public void goBack(View view) {
        finish();
    }

    public void resetPassword(View view) {
        EditText emailView = (EditText) findViewById(R.id.emailEditText);
        String email = emailView.getText().toString();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.d("password_reset", "got text");

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
