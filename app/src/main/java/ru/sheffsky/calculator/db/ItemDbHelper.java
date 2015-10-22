package ru.sheffsky.calculator.db;

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
                                "%s DOUBLE DEFAULT 0, " +
                                "%s INTEGER DEFAULT 1, " +
                                "%s TEXT)",
                        ItemContract.TABLE,
                        ItemContract.Columns.QTY,
                        ItemContract.Columns.PRICE,
                        ItemContract.Columns.PERSONS,
                        ItemContract.Columns.ITEM);

        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        if (i <= 7) {
            sqlDB.execSQL("ALTER TABLE " + ItemContract.TABLE + " ADD COLUMN " + ItemContract.Columns.PERSONS + " INTEGER DEFAULT 1");
        }
    }
}


