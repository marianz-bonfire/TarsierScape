package com.tarsier.marianz.tarsierscape.constant;

import com.tarsier.marianz.tarsierscape.database.Dictionaries;
import com.tarsier.marianz.tarsierscape.database.Histories;

public class DbConst {
    public static final String DATABASE_NAME = "tarsier_db";
    public static final int DATABASE_VERSION = 1;

    public static final String ACTION = "action";
    public static final String ID = "id";

    public static final String[] DICTIONARIES = {
            Dictionaries.COLUMN_ID,
            Dictionaries.COLUMN_WORD,
            Dictionaries.COLUMN_MEANING,
            Dictionaries.COLUMN_ACTIVE
    };

    public static final String[] HISTORIES = {
            Histories.COLUMN_ID,
            Histories.COLUMN_WORD,
            Histories.COLUMN_COUNT,
            Histories.COLUMN_ACTIVE
    };
}
