package com.project.kashan.localvigilante;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {
    private final int REGISTER_REQUEST_CODE = 2;
    private final int FORGOT_PASSWORD_REQUEST_CODE = 3;
    private final int HOME_REQUEST_CODE = 4;
    Button b1;
    CheckBox cb;
    EditText et1, et2;
    DatabaseHelper myDb;
    public int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        myDb = new DatabaseHelper(this);

        b1 = findViewById(R.id.btnSignin);
        cb = findViewById(R.id.cbRemember);
        et1 = findViewById(R.id.etEmail);
        et2 = findViewById(R.id.etPassword);

        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sharedPrefs.getBoolean("remembered", false)) {
            displayCred();
            cb.setChecked(true);
        }
        else {
            cb.setChecked(false);
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDb.login(et1.getText().toString(), et2.getText().toString()) != 0) {
                    if(cb.isChecked()) {
                        saveCred();
                    }
                    else {
                        deleteCred();
                    }
                    userId = getUserId(et1.getText().toString());
                    NavigateToHome();
                }else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void NavigateToRegister(View view) {
        Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_REQUEST_CODE);
    }

    public void NavigateToForgotPassword(View view) {
        Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
        startActivityForResult(intent, FORGOT_PASSWORD_REQUEST_CODE);
    }

    public void NavigateToHome() {
        Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, HOME_REQUEST_CODE);
    }

    public void saveCred() {
        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("email", et1.getText().toString());
        editor.putString("password", et2.getText().toString());
        editor.putBoolean("remembered", true);
        editor.apply();

        Toast.makeText(this, "Remembered!", Toast.LENGTH_SHORT).show();
    }

    public void displayCred() {
        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String email = sharedPrefs.getString("email", "");
        String passw = sharedPrefs.getString("password", "");

        et1.setText(email);
        et2.setText(passw);
    }

    public void deleteCred() {
        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("remembered", false);
        editor.apply();

        et1.setText("");
        et2.setText("");
    }

    public int getUserId(String email){
        int id = -1;
        Cursor res = myDb.getIdByEmail(email);
        if(res.getCount() == 0)
            Toast.makeText(this, "Email doesn't exist!", Toast.LENGTH_SHORT).show();
        while(res.moveToNext())
            id = res.getInt(0);
        return id;
    }
}
