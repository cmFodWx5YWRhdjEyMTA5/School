package com.video.aashi.school.adapters.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DateLocal extends SQLiteOpenHelper {


    public static final String DB_NAME = "OrderItems";
    public static final String TABLE_NAME = "items";
    public static final String COLUMN_ID = "id";
    public static final String DATE = "date";
    public static final String VARIABLE = "variable";
    private static final int DB_VERSION = 1;
    public DateLocal(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE+
                " VARCHAR, " + VARIABLE +
                " VARCHAR " +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS OrderItems";
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor getDates() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean addItems(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE,id);
        contentValues.put(VARIABLE, status);
         db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=  "DELETE FROM "+ TABLE_NAME ;
        db.execSQL(sql);
        db.close();
    }

}
