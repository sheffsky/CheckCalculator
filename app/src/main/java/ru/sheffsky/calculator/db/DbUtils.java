package ru.sheffsky.calculator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;

public class DbUtils {

    public enum qtyAction {PLUS, MINUS}

    public static Integer addOrUpdateItem(Context context, ItemContract.Values itemValues) {

        if (itemValues == null) {
            return -1;
        }

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        int valuesCount = 0;

        Integer itemId = -1;
        if (itemValues.getItemId() != null) {
            itemId = itemValues.getItemId();

            if (itemId > 0) {
                values.put(ItemContract.Columns._ID, itemId);
                valuesCount++;
            }
        }

        if (itemValues.getItem() != null) {
            values.put(ItemContract.Columns.ITEM, itemValues.getItem());
            valuesCount++;
        }

        if (itemValues.getPrice() != null) {
            values.put(ItemContract.Columns.PRICE, itemValues.getPrice());
            valuesCount++;
        }

        if (itemValues.getQty() != null) {
            values.put(ItemContract.Columns.QTY, itemValues.getQty());
            valuesCount++;
        }

        if (itemValues.getPersons() != null) {
            values.put(ItemContract.Columns.PERSONS, itemValues.getPersons());
            valuesCount++;
        }

        if (valuesCount == 0) {
            return -1;
        }

            if (itemId > 0) {
                return db.update(ItemContract.TABLE, values, ItemContract.Columns._ID + "=" + itemId.toString(), null);
            } else {
                return ((int) db.insertWithOnConflict(ItemContract.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE));
            }

    }

    public static ItemContract.Values getItemById(Context context, Integer itemId) {

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();

        Cursor cursor = sqlDB.rawQuery("SELECT * FROM " + ItemContract.TABLE
                + " WHERE " + ItemContract.Columns._ID
                + " = " + itemId.toString(), new String[]{});

        ItemContract.Values values = new ItemContract().new Values();

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            values.setItem(cursor.getString(cursor.getColumnIndex(ItemContract.Columns.ITEM)));
            values.setPrice(cursor.getFloat(cursor.getColumnIndex(ItemContract.Columns.PRICE)));
            values.setQty(cursor.getInt(cursor.getColumnIndex(ItemContract.Columns.QTY)));
            values.setPersons(cursor.getInt(cursor.getColumnIndex(ItemContract.Columns.PERSONS)));

        }

        cursor.close();

        return values;

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

    public static void changeQty(Context context, Integer itemId, qtyAction action ) {

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();

        String sql = String.format("UPDATE %s SET %s = %s %s 1 WHERE %s = '%s'",
                ItemContract.TABLE,
                ItemContract.Columns.QTY,
                ItemContract.Columns.QTY,
                action == qtyAction.PLUS ? "+" : "-",
                ItemContract.Columns._ID,
                itemId);
        sqlDB.execSQL(sql);


    }

}
