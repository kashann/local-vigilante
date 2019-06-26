package com.project.kashan.localvigilante;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "localvigilante.db";
    public static final String TABLE_NAME_PET = "petition";
    public static final String PET_1 = "ID";
    public static final String PET_2 = "NAME";
    public static final String PET_3 = "BODY";
    //public static final String PET_4 = "DAY"; //not going to use this for the sake of simplicity
    public static final String PET_5 = "NOSIGNATURES";
    public static final String PET_6 = "USER_ID";
    public static final String PET_7 = "IMG";
    public static final String PET_8 = "ADDRESS";
    public static final String PET_9 = "LAT";
    public static final String PET_10 = "LNG";
    public static final String TABLE_NAME_USER = "user";
    public static final String USER_1 = "ID";
    public static final String USER_2 = "LAST_NAME";
    public static final String USER_3 = "FIRST_NAME";
    public static final String USER_4 = "EMAIL";
    public static final String USER_5 = "PASSWORD";
    //public static final String USER_6 = "BIRTHDAY"; //not going to use these for the sake of simplicity
    public static final String USER_7 = "CITY";
    //public static final String USER_8 = "GENDER";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME_USER + " (" + USER_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_2 + " TEXT, " + USER_3 + " TEXT, " + USER_4 + " TEXT, " + USER_5 + " TEXT, " + USER_7 + " TEXT);");
        db.execSQL("create table if not exists " + TABLE_NAME_PET + " (" + PET_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PET_2 + " TEXT, " + PET_3 + " TEXT, " + PET_5 + " INTEGER, " + PET_7 + " BLOB, " + PET_8 + " TEXT, "
                + PET_9 + " FLOAT, " + PET_10 + " FLOAT, " + PET_6 + " INTEGER, CONSTRAINT FK_USERS FOREIGN KEY ("
                + USER_1 + ") REFERENCES " + TABLE_NAME_USER + "(" + USER_1 + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PET);
        onCreate(db);
    }

    public boolean insertUserData(String lastName, String firstName, String email, String password, String city){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_2, lastName);
        contentValues.put(USER_3, firstName);
        contentValues.put(USER_4, email);
        contentValues.put(USER_5, password);
        contentValues.put(USER_7, city);
        long result = db.insert(TABLE_NAME_USER, null, contentValues);
        if(result == -1)
            return false;
        else return true;
    }

    public boolean updateUser(int id, String lastName, String firstName, String email, String password, String city){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_1, id);
        contentValues.put(USER_2, lastName);
        contentValues.put(USER_3, firstName);
        contentValues.put(USER_4, email);
        contentValues.put(USER_5, password);
        contentValues.put(USER_7, city);
        db.update(TABLE_NAME_USER, contentValues, "ID = ?", new String[]{ Integer.toString(id) });
        return true;
    }

    public boolean insertPetitionData(String name, String body, int noSign, String address, float latitude, float longitude, int userid, byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PET_2, name);
        contentValues.put(PET_3, body);
        contentValues.put(PET_5, noSign);
        contentValues.put(PET_8, address);
        contentValues.put(PET_9, latitude);
        contentValues.put(PET_10, longitude);
        contentValues.put(PET_6, userid);
        contentValues.put(PET_7, image);
        long result = db.insert(TABLE_NAME_PET, null, contentValues);
        if(result == -1)
            return false;
        else return true;
    }

    public Cursor getIdByEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + USER_1 + " from " + TABLE_NAME_USER + " where "
                + USER_4 + " = \"" + email.trim() + "\"", null);
        return res;
    }

    public Cursor getPasswordByEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + USER_5 + " from " + TABLE_NAME_USER + " where "
                + USER_4 + " = \"" + email.trim() + "\"", null);
        return res;
    }

    public int login(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            int i = 0;
            Cursor c = null;
            c = db.rawQuery("select * from " + TABLE_NAME_USER + " where " + USER_4 + " = \""
                    + email.trim() + "\" and " + USER_5 + " = \"" + password.trim() + "\"", null);
            c.moveToFirst();
            i = c.getCount();
            c.close();
            return i;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public Cursor getPetitionsByUserId(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_PET + " where "
                + PET_6 + " = \"" + id + "\"", null);
        return res;
    }

    public Cursor getAllPetitions(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_PET, null);
        return res;
    }

    public Cursor getUserById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_USER + " where "
                + USER_1 + " = \"" + id + "\"", null);
        return res;
    }

    public Cursor getPswById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + USER_5 + " from " + TABLE_NAME_USER + " where "
                + USER_1 + " = \"" + id + "\"", null);
        return res;
    }

    public Cursor getAllUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_USER, null);
        return res;
    }

    public Integer deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_USER, "ID = ?", new String[] {Integer.toString(id)});
    }
}