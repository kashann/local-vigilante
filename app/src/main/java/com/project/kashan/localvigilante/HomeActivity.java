package com.project.kashan.localvigilante;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private final int ADD_PETITION_REQUEST_CODE = 5;
    private final int MY_PETITION_REQUEST_CODE = 7;
    private final int ALL_PETITION_REQUEST_CODE = 11;
    private final int MY_PROFILE_REQUEST_CODE = 12;
    private int userId;
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userId = getIntent().getIntExtra("userId", -1);

        btn1 = findViewById(R.id.btnMyProfile);
        btn2 = findViewById(R.id.btnMyPet);
        if(userId == -1) {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
        }
    }

    public void NavigateToAddPetition(View view){
        Intent intent = new Intent(HomeActivity.this, AddPetitionActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, ADD_PETITION_REQUEST_CODE);
    }

    public void NavigateToMyPetitions(View view){
        Intent intent = new Intent(HomeActivity.this, MyPetitionsActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, MY_PETITION_REQUEST_CODE);
    }

    public void NavigateToAllPetitions(View view){
        Intent intent = new Intent(HomeActivity.this, AllPetitionsActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, ALL_PETITION_REQUEST_CODE);
    }

    public void NavigateToMyProfile(View view){
        Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, MY_PROFILE_REQUEST_CODE);
    }
}
