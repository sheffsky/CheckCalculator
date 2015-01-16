package ru.sheffsky.calculator.db;

import android.provider.BaseColumns;

public class ItemContract {
    public static final String DB_NAME = "ru.sheffsky.barcounter.db.tasks";
    public static final int DB_VERSION = 7;
    public static final String TABLE = "checks";


    public class Columns {
        public static final String ITEM = "item";
        public static final String QTY = "qty";
        public static final String PRICE = "price";
        public static final String _ID = BaseColumns._ID;
    }
}

