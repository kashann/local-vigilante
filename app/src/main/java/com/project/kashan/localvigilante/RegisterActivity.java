package com.project.kashan.localvigilante;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    Context context = this;
    Button btn;
    EditText editDate, etFN, etLN, etE, etP, etCP;
    Spinner cityS;
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "dd.MM.yyyy";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
    DatabaseHelper myDb;
    CheckBox cbTC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDb = new DatabaseHelper(this);

        editDate = findViewById(R.id.etBDay);

        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        editDate.setText(dateString);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etFN = findViewById(R.id.etFirstName);
        etLN = findViewById(R.id.etLastName);
        etE = findViewById(R.id.etEmail);
        etP = findViewById(R.id.etPassword);
        etCP = findViewById(R.id.etConfPassword);
        cityS = findViewById(R.id.citySpinner);
        btn = findViewById(R.id.btnRegister);
        cbTC = findViewById(R.id.cbTerms);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etP.getText().toString().equals(etCP.getText().toString())) {
                    if(cbTC.isChecked()){
                        boolean success = myDb.insertUserData(etLN.getText().toString(), etFN.getText().toString(),
                                etE.getText().toString(), etP.getText().toString(), cityS.getSelectedItem().toString());
                        if(success)
                            Toast.makeText(getApplicationContext(), "You've successfully registered!",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Could not be registered!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Accept Terms & Conditions first!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Passwords do not match!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDate() {
        editDate.setText(sdf.format(myCalendar.getTime()));
    }
}
