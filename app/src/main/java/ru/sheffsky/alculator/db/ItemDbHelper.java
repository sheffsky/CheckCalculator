package ru.sheffsky.alculator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDbHelper extends SQLiteOpenHelper {

    public ItemDbHelper(Context context) {
        super(context, ItemContract.DB_NAME, null, ItemContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s INTEGER DEFAULT 1, " +
                                "%s DOUBLE DEFAULT 0," +
                                "%s TEXT)",
                        ItemContract.TABLE,
                        ItemContract.Columns.QTY,
                        ItemContract.Columns.PRICE,
                        ItemContract.Columns.ITEM);

        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + ItemContract.TABLE);
        onCreate(sqlDB);
    }
}


