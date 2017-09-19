package edu.gatech.cs2340.ratlab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logout(View view) {
        // TODO: reset the currently logged in user attribute of the model
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
