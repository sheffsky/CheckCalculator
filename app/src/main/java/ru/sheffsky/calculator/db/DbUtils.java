package ru.sheffsky.calculator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DbUtils {

    public static void addOrUpdateItem(Context context, HashMap itemMap) {

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        Integer itemId = -1;
        if (!Objects.equals(itemMap.get(ItemContract.Columns._ID).toString(), "")) {
            itemId = Integer.parseInt(itemMap.get(ItemContract.Columns._ID).toString());
        }

        if (itemId > 0) {
            values.put(ItemContract.Columns._ID, itemId);
        }

        if (itemMap.get(ItemContract.Columns.ITEM) != null) {
            values.put(ItemContract.Columns.ITEM, itemMap.get(ItemContract.Columns.ITEM).toString());
        }

        if (itemMap.get(ItemContract.Columns.PRICE) != null) {
            try {
                values.put(ItemContract.Columns.PRICE, Float.parseFloat(itemMap.get(ItemContract.Columns.PRICE).toString()));
            } catch (NumberFormatException nfe) {
                values.put(ItemContract.Columns.PRICE, 0);
            }
        }

        if (itemMap.get(ItemContract.Columns.QTY) != null) {
            try {
                values.put(ItemContract.Columns.QTY, Integer.parseInt(itemMap.get(ItemContract.Columns.QTY).toString()));
            } catch (NumberFormatException nfe) {
                values.put(ItemContract.Columns.QTY, 1);
            }
        }

        if (itemMap.get(ItemContract.Columns.PERSONS) != null) {
            try {
                values.put(ItemContract.Columns.PERSONS, Integer.parseInt(itemMap.get(ItemContract.Columns.PERSONS).toString()));
            } catch (NumberFormatException nfe) {
                values.put(ItemContract.Columns.PERSONS, 1);
            }
        }

        if (itemId > 0) {
            db.update(ItemContract.TABLE, values, ItemContract.Columns._ID + "=" + itemId.toString(), null);
        } else {
            db.insertWithOnConflict(ItemContract.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }

    }

    public static Map<String, String> getItemById(Context context, Integer itemId) {

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();

        Cursor cursor = sqlDB.rawQuery("SELECT * FROM " + ItemContract.TABLE
                + " WHERE " + ItemContract.Columns._ID
                + " = " + itemId.toString(), new String[]{});

        Map<String, String> itemMap = new HashMap<>();

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            itemMap.put(ItemContract.Columns.ITEM,      cursor.getString(cursor.getColumnIndex(ItemContract.Columns.ITEM)));
            itemMap.put(ItemContract.Columns.PRICE,     cursor.getString(cursor.getColumnIndex(ItemContract.Columns.PRICE)));
            itemMap.put(ItemContract.Columns.QTY,       cursor.getString(cursor.getColumnIndex(ItemContract.Columns.QTY)));
            itemMap.put(ItemContract.Columns.PERSONS,   cursor.getString(cursor.getColumnIndex(ItemContract.Columns.PERSONS)));

        }

        cursor.close();

        return itemMap;

    }

    public static double getTotalPrice(Context context) {

        Double totalPrice = (double) 0;

        String sql = String.format("SELECT SUM(%s * %s / %s) FROM %s",
                ItemContract.Columns.PRICE,
                ItemContract.Columns.QTY,
                ItemContract.Columns.PERSONS,
                ItemContract.TABLE);

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery(sql, null);
        if (cursor.moveToFirst()) {

            BigDecimal bd = new BigDecimal(Double.toString(cursor.getFloat(0)));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            totalPrice = bd.doubleValue();

        }
        cursor.close();

        return totalPrice;

    }

    public static void deleteItemById(Context context, Integer itemId) {

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                ItemContract.TABLE,
                ItemContract.Columns._ID,
                itemId);

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);

    }

    public static void deleteAllItems(Context context) {

        ItemDbHelper helper = new ItemDbHelper(context);

        String sql = String.format("DELETE FROM %s",
                ItemContract.TABLE);

        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        sqlDB.close();
    }

    public static void changeQty(Context context, Integer itemId, Boolean isIncreased) {

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();

        String sql = String.format("UPDATE %s SET %s = %s %s 1 WHERE %s = '%s'",
                ItemContract.TABLE,
                ItemContract.Columns.QTY,
                ItemContract.Columns.QTY,
                isIncreased ? "+" : "-",
                ItemContract.Columns._ID,
                itemId);
        sqlDB.execSQL(sql);



    }

}
