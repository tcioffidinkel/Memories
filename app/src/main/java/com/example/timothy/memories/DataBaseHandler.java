package com.example.timothy.memories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy on 12/12/2016.
 */

public class DataBaseHandler extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Camera_imagedb";
    public static final String TABLE_PICTURE = "Camera_picture";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOC = "location";
    public static final String KEY_IMAGE = "image";

    private String[] allColumns = {KEY_ID, KEY_NAME, KEY_LOC, KEY_IMAGE};

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PICTURE_TABLE = "CREATE TABLE " + TABLE_PICTURE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_LOC + " TEXT,"
                + KEY_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_PICTURE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURE);
        onCreate(db);
    }






}
