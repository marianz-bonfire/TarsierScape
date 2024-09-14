package com.tarsier.marianz.tarsierscape.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tarsier.marianz.tarsierscape.constant.DbConst;
import com.tarsier.marianz.tarsierscape.models.Dictionary;
import com.tarsier.marianz.tarsierscape.models.WordInfo;

import java.util.ArrayList;
import java.util.List;

public class Dictionaries {
    public static final String LOGTAG = Dictionaries.class.getSimpleName();

    public static final String TABLE_DICTIONARY = "dictionaries";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_MEANING = "meaning";
    public static final String COLUMN_ACTIVE = "active";

    public static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_DICTIONARY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORD + " TEXT, " +
                    COLUMN_MEANING + " TEXT, " +
                    COLUMN_ACTIVE + " INTEGER DEFAULT 1 " +
                    ")";

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    public Dictionaries(Context context) {
        dbHandler = new DatabaseHandler(context);
        if (database == null) {
            //  open();
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

    public Dictionary add(Dictionary dict) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, dict.getWord());
        values.put(COLUMN_MEANING, dict.getMeaning());
        values.put(COLUMN_ACTIVE, dict.isActive() ? 1 : 0);

        if (!database.isOpen()) {
            open();
        }
        long id = database.insert(TABLE_DICTIONARY, null, values);
        dict.setId(id);
        return dict;
    }

    public int update(Dictionary dict) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, dict.getWord());
        values.put(COLUMN_MEANING, dict.getMeaning());
        values.put(COLUMN_ACTIVE, dict.isActive());
        if (!database.isOpen()) {
            //open();
        }
        return database.update(TABLE_DICTIONARY, values, COLUMN_ID + "=?", new String[]{String.valueOf(dict.getId())});
    }

    public Dictionary getDictionary(Cursor cursor) {
        Dictionary dict = new Dictionary();
        dict.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
        dict.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
        dict.setMeaning(cursor.getString(cursor.getColumnIndex(COLUMN_MEANING)));
        dict.setActive(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVE)).equals("1"));

        return dict;
    }

    public Dictionary getDictionary(String id) {
        if (!database.isOpen()) {
            //open();
        }
        Cursor cursor = database.query(TABLE_DICTIONARY, DbConst.DICTIONARIES, COLUMN_ID + "=?", new String[]{id}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Dictionary dict = getDictionary(cursor);

        return dict;
    }

    public boolean existWord(String word) {
        boolean exist = false;
        try {
            if (word != null) {
                if (!database.isOpen()) {
                    //open();
                }
                String query = "SELECT * FROM " + TABLE_DICTIONARY + " WHERE " + COLUMN_WORD + " ='" + word + "'";
                Cursor cursor = database.rawQuery(query, null);
                exist = (cursor.getCount() > 0);
                cursor.close();
            }
        } catch (Exception e) {
            //Ignore exception
            Log.d("existWord", e.getMessage());
        }
        return exist;
    }

    public ArrayList<Dictionary> getDictionaries() {
        if (!database.isOpen()) {
            // open();
        }
        Cursor cursor = database.query(TABLE_DICTIONARY, DbConst.DICTIONARIES, null, null, null, null, null);
        ArrayList<Dictionary> dicts = new ArrayList<>();
        int count = cursor.getCount();
        if (count > 0) {
            while (cursor.moveToNext()) {
                Dictionary dict = getDictionary(cursor);
                dicts.add(dict);
            }
        }
        cursor.close();
        return dicts;
    }

    public ArrayList<WordInfo> getDictionaryList() {
        if (!database.isOpen()) {
            //open();
        }
        Cursor cursor = database.query(TABLE_DICTIONARY, DbConst.DICTIONARIES, null, null, null, null, null);
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
}


