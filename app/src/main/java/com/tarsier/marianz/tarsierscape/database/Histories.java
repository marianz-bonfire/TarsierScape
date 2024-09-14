package com.tarsier.marianz.tarsierscape.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tarsier.marianz.tarsierscape.constant.DbConst;
import com.tarsier.marianz.tarsierscape.models.History;
import com.tarsier.marianz.tarsierscape.models.WordInfo;

import java.util.ArrayList;
import java.util.List;

public class Histories {
    public static final String LOGTAG = Histories.class.getSimpleName();

    public static final String TABLE_HISTORY = "histories";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_COUNT = "total";
    public static final String COLUMN_ACTIVE = "active";

    public static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORD + " TEXT, " +
                    COLUMN_COUNT + " TEXT, " +
                    COLUMN_ACTIVE + " INTEGER DEFAULT 1 " +
                    ")";

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    public Histories(Context context) {
        dbHandler = new DatabaseHandler(context);
        if (database == null) {
            open();
        }
    }

    public void open() {
        Log.i(LOGTAG, "Dictionaries database opened");
        database = dbHandler.getWritableDatabase();

    }

    public void close() {
        Log.i(LOGTAG, "Dictionaries database closed");
        dbHandler.close();
    }

    public History add(History h) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, h.getWord());
        values.put(COLUMN_COUNT, h.getSearchCount());
        values.put(COLUMN_ACTIVE, h.isActive());

        long id = database.insert(TABLE_HISTORY, null, values);
        h.setId(id);
        return h;
    }

    public int update(History h) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, h.getWord());
        values.put(COLUMN_COUNT, getCount(h.getWord()) + 1);
        values.put(COLUMN_ACTIVE, h.isActive());
        return database.update(TABLE_HISTORY, values, COLUMN_ID + "=?", new String[]{String.valueOf(h.getId())});
    }

    public History getHistory(Cursor cursor) {
        History h = new History();
        h.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
        h.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
        h.setSearchCount(cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT)));
        h.setActive(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVE)).equals("1"));

        return h;
    }

    public History getHistory(String id) {
        Cursor cursor = database.query(TABLE_HISTORY, DbConst.HISTORIES, COLUMN_ID + "=?", new String[]{id}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        History h = getHistory(cursor);
        cursor.close();
        return h;
    }

    public boolean existCode(String word) {
        boolean exist = false;
        if (word != null) {
            String query = "SELECT * FROM " + TABLE_HISTORY + " WHERE " + COLUMN_WORD + " ='" + word + "'";
            Cursor cursor = database.rawQuery(query, null);
            exist = (cursor.getCount() > 0);
        }
        return exist;
    }

    public int getCount(String word) {
        boolean exist = false;
        if (word != null) {
            String query = "SELECT * FROM " + TABLE_HISTORY + " WHERE " + COLUMN_WORD + " ='" + word + "'";
            Cursor cursor = database.rawQuery(query, null);
            return cursor.getCount();
        }
        return 0;
    }

    public ArrayList<History> getHistories() {
        Cursor cursor = database.query(TABLE_HISTORY, DbConst.HISTORIES, null, null, null, null, null);
        ArrayList<History> histories = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                History h = getHistory(cursor);
                histories.add(h);
            }
        }
        return histories;
    }

    public ArrayList<WordInfo> getHistoryList() {
        if (!database.isOpen()) {
            //open();
        }
        Cursor cursor = database.query(TABLE_HISTORY, DbConst.HISTORIES, null, null, null, null, null);
        ArrayList<WordInfo> dicts = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                WordInfo wordInfo = new WordInfo();
                wordInfo.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
                dicts.add(wordInfo);
            }
        }
        cursor.close();
        return dicts;
    }

    public void addWord(String word){
        long count = getCount(word);
        History history = new History();
        history.setWord(word);
        history.setSearchCount(count+1);
        if(count>0){
            update(history);
        }else{
            add(history);
        }
    }
}


