package com.tarsier.marianz.tarsierscape.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tarsier.marianz.tarsierscape.constant.DbConst;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, DbConst.DATABASE_NAME, null, DbConst.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Dictionaries.TABLE_CREATE);
        db.execSQL(Histories.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Dictionaries.TABLE_DICTIONARY);
        db.execSQL("DROP TABLE IF EXISTS " + Histories.TABLE_HISTORY);
        db.execSQL(Dictionaries.TABLE_CREATE);
        db.execSQL(Histories.TABLE_CREATE);
    }
}
