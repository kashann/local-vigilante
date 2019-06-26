package com.project.kashan.localvigilante;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AllPetitionsActivity extends AppCompatActivity {
    private int userId;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_petitions);
        userId = getIntent().getIntExtra("userId", -1);
        myDb = new DatabaseHelper(this);
        List<PetitionInfo> list = getPetitions();

        ListView lv = findViewById(R.id.lvAllPetitions);
        PetitionCustomAdapter adapter = new PetitionCustomAdapter(getApplicationContext(), list);
        lv.setAdapter(adapter);
    }

    public ArrayList<PetitionInfo> getPetitions(){
        ArrayList<PetitionInfo> list = new ArrayList<PetitionInfo>();
        Cursor res = myDb.getAllPetitions();
        if(res.getCount() == 0)
            Toast.makeText(this, "No petitions found!", Toast.LENGTH_LONG).show();
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            PetitionInfo pet = new PetitionInfo();
            pet.setId(res.getInt(0));
            pet.setName(res.getString(1));
            pet.setBody(res.getString(2));
            pet.setNoSignatures(res.getInt(3));
            pet.setImg(res.getBlob(4));
            pet.setAddress(res.getString(5));
            pet.setLat(res.getFloat(6));
            pet.setLng(res.getFloat(7));
            pet.setUser_id(res.getInt(8));
            list.add(pet);
        }
        return list;
    }
}
