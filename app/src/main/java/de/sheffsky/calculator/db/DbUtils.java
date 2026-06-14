package de.sheffsky.calculator.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbUtils {

    public static Integer addOrUpdateItem(Context context, ItemContract.Values values) {
        if (values == null) {
            return -1;
        }

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            if (values.getItem() != null) cv.put(ItemContract.Columns.ITEM, values.getItem());
            if (values.getQty() != null) cv.put(ItemContract.Columns.QTY, values.getQty());
            if (values.getPrice() != null) cv.put(ItemContract.Columns.PRICE, values.getPrice());
            if (values.getPersons() != null) cv.put(ItemContract.Columns.PERSONS, values.getPersons());

            if (values.getItemId() != null) {
                if (values.getItemId() < 0) {
                    return -1;
                }
                return db.update(ItemContract.TABLE, cv, ItemContract.Columns._ID + "=?", new String[]{String.valueOf(values.getItemId())});
            } else {
                if (cv.size() == 0) {
                    return -1;
                }
                long id = db.insert(ItemContract.TABLE, null, cv);
                return (int) id;
            }
        } finally {
            if (db != null) db.close();
            helper.close();
        }
    }

    @SuppressLint("Range")
    public static ItemContract.Values getItemById(Context context, Integer id) {
        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(ItemContract.TABLE, null, ItemContract.Columns._ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                ItemContract.Values values = new ItemContract.Values();
                values.setItemId(cursor.getInt(cursor.getColumnIndex(ItemContract.Columns._ID)));
                values.setItem(cursor.getString(cursor.getColumnIndex(ItemContract.Columns.ITEM)));
                values.setQty(cursor.getInt(cursor.getColumnIndex(ItemContract.Columns.QTY)));
                values.setPrice(cursor.getFloat(cursor.getColumnIndex(ItemContract.Columns.PRICE)));
                values.setPersons(cursor.getInt(cursor.getColumnIndex(ItemContract.Columns.PERSONS)));
                return values;
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
            helper.close();
        }
        return null;
    }

    public static double getTotalPrice(Context context) {
        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        double total = 0;
        try {
            db = helper.getReadableDatabase();
            cursor = db.rawQuery("SELECT SUM(" + ItemContract.Columns.PRICE + " * " + ItemContract.Columns.QTY + " / " + ItemContract.Columns.PERSONS + ") FROM " + ItemContract.TABLE, null);
            if (cursor != null && cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
            helper.close();
        }
        return total;
    }

    public static void deleteAllItems(Context context) {
        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(ItemContract.TABLE, null, null);
        } finally {
            if (db != null) db.close();
            helper.close();
        }
    }

    public static void deleteItemById(Context context, Integer id) {
        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(ItemContract.TABLE, ItemContract.Columns._ID + "=?", new String[]{String.valueOf(id)});
        } finally {
            if (db != null) db.close();
            helper.close();
        }
    }
}
