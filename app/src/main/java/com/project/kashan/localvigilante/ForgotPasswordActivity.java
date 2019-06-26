package com.project.kashan.localvigilante;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button b1;
    EditText et;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        myDb = new DatabaseHelper(this);

        b1 = findViewById(R.id.btnResetPassword);
        et = findViewById(R.id.etEmailResetPassword);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please insert an email!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "The password is: " +
                            getUserPassword(et.getText().toString()), Toast.LENGTH_LONG).show();
                    et.setText("");
                    finish();
                }
            }
        });
    }

    public String getUserPassword(String email){
        Cursor res = myDb.getPasswordByEmail(email);
        if(res.getCount() == 0)
            Toast.makeText(this, "Email doesn't exist!", Toast.LENGTH_SHORT).show();
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
            buffer.append(res.getString(0));
        return buffer.toString();
    }
}