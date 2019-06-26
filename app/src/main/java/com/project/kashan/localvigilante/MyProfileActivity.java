package com.project.kashan.localvigilante;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

public class MyProfileActivity extends AppCompatActivity {
    private int userId;
    DatabaseHelper myDb;
    Button save, export, delete;
    EditText oldPsw, newPsw, confNewPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        userId = getIntent().getIntExtra("userId", -1);
        myDb = new DatabaseHelper(this);

        save = findViewById(R.id.btnSave);
        export = findViewById(R.id.btnExport);
        delete = findViewById(R.id.btnDelete);
        oldPsw = findViewById(R.id.etOldPass);
        newPsw = findViewById(R.id.etNewPass);
        confNewPsw = findViewById(R.id.etConfNewPass);
        final User user = getUserData(userId);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPsw.getText().toString().equals(getPasswordById(userId))){
                    if(newPsw.getText().toString().equals(confNewPsw.getText().toString())){
                        boolean isUpdated = myDb.updateUser(user.getId(), user.getLastName(), user.getFirstName(),
                                user.getEmail(), newPsw.getText().toString(), user.getCity());
                        if(isUpdated == true)
                            Toast.makeText(getApplicationContext(), "Password successfully changed!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Password NOT updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "The passwords don't match!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_SHORT).show();
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File root = android.os.Environment.getExternalStorageDirectory();
                File dir = new File (root + "/Local_Vigilante");
                dir.mkdirs();
                File fileU = new File(dir, "users.txt");
                File fileP = new File(dir, "petitions.txt");
                try{
                    FileWriter writerU = new FileWriter(fileU);
                    Cursor res = myDb.getAllUsers();
                    while(res.moveToNext()) {
                        writerU.append("\n\n\nId: " + res.getInt(0));
                        writerU.append("\nLast Name: " + res.getString(1));
                        writerU.append("\nFirst Name: " + res.getString(2));
                        writerU.append("\nEmail: " + res.getString(3));
                        writerU.append("\nPassword: " + res.getString(4));
                        writerU.append("\nCity: " + res.getString(5));
                    }
                    writerU.flush();
                    writerU.close();
                    res.close();

                    FileWriter writerP = new FileWriter(fileP);
                    res = myDb.getAllPetitions();
                    while(res.moveToNext()) {
                        writerP.append("\n\n\nId: " + res.getInt(0));
                        writerP.append("\nName: " + res.getString(1));
                        writerP.append("\nBody: " + res.getString(2));
                        writerP.append("\nNr. of Signatures: " + res.getInt(3));
                        //writerP.append("\nImage: " + res.getBlob(4));
                        writerP.append("\nAddress: " + res.getString(5));
                        writerP.append("\nLatitude: " + res.getFloat(6));
                        writerP.append("\nLongitude: " + res.getFloat(7));
                        writerP.append("\nCreated by user id: " + res.getInt(8));
                    }
                    writerP.flush();
                    writerP.close();
                    Toast.makeText(getApplicationContext(), "Reports have been exported!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), dir.toString(), Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyProfileActivity.this);
                alert.setCancelable(true);
                alert.setTitle("Delete profile");
                alert.setMessage("Are you sure you want to delete your account?");
                alert.setPositiveButton("YES!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Integer del = myDb.deleteUser(userId);
                        if(del != 0) {
                            Toast.makeText(getApplicationContext(), "User deleted!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "User NOT deleted! :(", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("NO!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.show();
            }
        });
    }

    public String getPasswordById(int id){
        String pass = "";
        Cursor res = myDb.getPswById(id);
        while(res.moveToNext())
            pass = res.getString(0);
        return pass;
    }

    public User getUserData(int id){
        User user = new User();
        Cursor res = myDb.getUserById(id);
        while(res.moveToNext()){
            user.setId(Integer.parseInt(res.getString(0)));
            user.setLastName(res.getString(1));
            user.setFirstName(res.getString(2));
            user.setEmail(res.getString(3));
            user.setPassword(res.getString(4));
            user.setCity(res.getString(5));
        }
        return user;
    }
}
