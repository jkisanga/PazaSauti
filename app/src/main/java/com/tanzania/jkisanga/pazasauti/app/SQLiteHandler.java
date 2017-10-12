package com.tanzania.jkisanga.pazasauti.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by user on 1/28/2017.
 */


public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Names
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";




    public static final String KEY_ID = "id";
    public static final String KEY_PHONE_NO= "mobile_no";
    public static final String KEY_PHONE_ID= "mobile_ID";
    public static final String KEY_NAME = "name";







    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE_NO + " TEXT,"
                + KEY_PHONE_ID + " INTEGER" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

















    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }


    public void addUser(int user_id,  String name, String phone_no, String phone_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user_id);
        values.put(KEY_NAME, name);
        values.put(KEY_PHONE_NO, phone_no);
        values.put(KEY_PHONE_ID, phone_id);


        // Inserting Row
        long id = db.insertWithOnConflict(TABLE_USER, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(KEY_ID, cursor.getString(0));
            user.put(KEY_NAME, cursor.getString(1));
            user.put(KEY_PHONE_NO, cursor.getString(2));
            user.put(KEY_PHONE_ID, cursor.getString(3));


        }
        cursor.close();
        db.close();
        // return user

        return user;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();
    }



}
