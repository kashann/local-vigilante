package com.project.kashan.localvigilante;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private final int LOG_IN_REQUEST_CODE = 123;
    private final int HOME_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void NavigateToLogIn(View view){
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivityForResult(intent, LOG_IN_REQUEST_CODE);
    }

    public void NavigateToHome(View view){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivityForResult(intent, HOME_REQUEST_CODE);
    }
}
