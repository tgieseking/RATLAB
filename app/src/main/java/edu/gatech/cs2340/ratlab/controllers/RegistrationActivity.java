package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.UserManager;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    // --Commented out by Inspection (11/8/2017 3:55 PM):private DatabaseReference mDatabase;
    private UserManager userManager;

    // UI references
    private Spinner accountTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        userManager = UserManager.getInstance();

        accountTypeSpinner = (Spinner) findViewById(R.id.accountTypeSpinner);
        ArrayAdapter<CharSequence> accountTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.account_types, android.R.layout.simple_spinner_item);
        accountTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(accountTypeAdapter);
    }

    /**
     * Close the registration activity and return to the previous activity
     *
     * @param view the cancel button
     */
    public void registrationCancel(View view) {
        finish();
    }

    /**
     * Attempts to create a new user using the email and password entered. If account creation is
     * successful, the app proceeds to the main activity. Otherwise, an explanatory error message
     * is shown.
     *
     * @param view the register button
     */
    public void createAccount(View view) {
        //TODO: Set a better way of responding to failures
        EditText emailView = (EditText) findViewById(R.id.emailTextBox);
        EditText passwordView = (EditText) findViewById(R.id.passwordTextBox);
        EditText usernameView = (EditText) findViewById(R.id.usernameTextBox);
        EditText nameView = (EditText) findViewById(R.id.nameTextBox);

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        final String username = usernameView.getText().toString();
        final String name = nameView.getText().toString();

        if (name.isEmpty()) {
            Toast nameEmpty = Toast.makeText(RegistrationActivity.this, "Empty name",
                    Toast.LENGTH_SHORT);
            nameEmpty.show();
            return;
        }
        if (username.isEmpty()) {
            Toast userEmpty = Toast.makeText(RegistrationActivity.this, "Empty username",
                    Toast.LENGTH_SHORT);
            userEmpty.show();
            return;
        }
        if (email.isEmpty()) {
            Toast emptyEmail = Toast.makeText(RegistrationActivity.this, "Empty email",
                    Toast.LENGTH_SHORT);
            emptyEmail.show();
            return;
        }
        if (password.length() < 6) {
            Toast passwordLength = Toast.makeText(RegistrationActivity.this, "Password too short",
                    Toast.LENGTH_SHORT);
            passwordLength.show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration is successful
                            userManager.addUserToDatabase(username, name,
                                    (String) accountTypeSpinner.getSelectedItem());
                            userManager.login();


                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Registration is unsuccessful
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(RegistrationActivity.this, "Email in use",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(RegistrationActivity.this, "Email is malformed",
                                        Toast.LENGTH_SHORT).show();
                                Log.d("Registration", task.getException().toString());
                            }catch (FirebaseException e) {
                                if (e.toString().contains("WEAK_PASSWORD")) {
                                    Toast.makeText(RegistrationActivity.this, "Weak password",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegistrationActivity.this, task.getException().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("Registration", task.getException().toString());
                                }
                            } catch (Exception e) {
                                Toast.makeText(RegistrationActivity.this, task.getException().toString(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d("Registration", task.getException().toString());
                            }
                        }

                        // ...
                    }
                });
    }
}
