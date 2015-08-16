package ru.sheffsky.calculator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.math.BigDecimal;

import ru.sheffsky.calculator.db.ItemContract;
import ru.sheffsky.calculator.db.ItemDbHelper;


public class MainActivity extends ListActivity {
    private ItemDbHelper helper;
    public Integer selectedItemId = 0;
    private CustomCursorAdapter cursorAdapter;
    private RelativeLayout lastShownAdditionalButtonsLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createListView();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        //super.onBackPressed(); // Comment this super call to avoid calling finish()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove_all:
                removeAll();
                return true;
            case R.id.action_add_item:
                showAddNewItem(getListView());
                return true;
            default:
                return false;
        }
    }

    public void showAddNewItem(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }

    public void removeAll() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.deleteAllCaption))
                .setMessage(getString(R.string.deleteAllBody))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        String sql = String.format("DELETE FROM %s",
                                ItemContract.TABLE);

                        helper = new ItemDbHelper(MainActivity.this);
                        SQLiteDatabase sqlDB = helper.getWritableDatabase();
                        sqlDB.execSQL(sql);
                        createListView();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void refreshListView() {
        helper = new ItemDbHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(ItemContract.TABLE,
                new String[]{ItemContract.Columns._ID,
                        ItemContract.Columns.ITEM,
                        ItemContract.Columns.QTY,
                        ItemContract.Columns.PRICE},
                null, null, null, null, null);

        this.cursorAdapter.setSelectedItemId(selectedItemId);
        this.cursorAdapter.changeCursor(cursor);

        updatePrice();
    }

    public void createListView() {
        helper = new ItemDbHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(ItemContract.TABLE,
                new String[]{ItemContract.Columns._ID,
                        ItemContract.Columns.ITEM,
                        ItemContract.Columns.QTY,
                        ItemContract.Columns.PRICE},
                null, null, null, null, null);


        ListAdapter listAdapter = new CustomCursorAdapter(
                this,
                R.layout.item_view,
                cursor,
                new String[]{
                        ItemContract.Columns._ID,
                        ItemContract.Columns.ITEM,
                        ItemContract.Columns.QTY,
                        ItemContract.Columns.PRICE},
                new int[]{}
        );

        this.cursorAdapter = (CustomCursorAdapter) listAdapter;

        this.setListAdapter(listAdapter);

        updatePrice();

    }

    private void updatePrice() {
        SQLiteDatabase sqlDB;
        Cursor cursor;
        Double totalPrice = (double) 0;

        String sql = String.format("SELECT SUM(%s * %s) FROM %s",
                ItemContract.Columns.PRICE,
                ItemContract.Columns.QTY,
                ItemContract.TABLE);


        helper = new ItemDbHelper(MainActivity.this);
        sqlDB = helper.getReadableDatabase();
        cursor = sqlDB.rawQuery(sql, null);
        if (cursor.moveToFirst()) {

            BigDecimal bd = new BigDecimal(Double.toString(cursor.getFloat(0)));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            totalPrice = bd.doubleValue();

        }

        TextView totalPriceView = (TextView) findViewById(R.id.totalPrice);
        totalPriceView.setText(getString(R.string.totalPriceText) + " " + totalPrice.toString());
    }


    public void onDeleteButtonClick(final View view) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.deleteItemCaption))
                .setMessage(getString(R.string.deleteItemBody))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        View v = (View) view.getParent().getParent();
                        TextView taskTextView = (TextView) v.findViewById(R.id.itemId);
                        Integer itemId = Integer.parseInt(taskTextView.getText().toString());

                        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                                ItemContract.TABLE,
                                ItemContract.Columns._ID,
                                itemId);


                        helper = new ItemDbHelper(MainActivity.this);
                        SQLiteDatabase sqlDB = helper.getWritableDatabase();
                        sqlDB.execSQL(sql);
                        refreshListView();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void onItemClick(View view) {
        View v = (View) view.getParent();

        TextView itemIdView = (TextView) v.findViewById(R.id.itemId);

        Integer previousItemId = selectedItemId;
        selectedItemId = Integer.parseInt(itemIdView.getText().toString());

        RelativeLayout additionalButtonsLayout = (RelativeLayout) v.findViewById(R.id.additionalButtonsLayout);
        if (selectedItemId > 0 && selectedItemId.equals(previousItemId)) {
            selectedItemId = 0;
            this.cursorAdapter.setSelectedItemId(selectedItemId);
            additionalButtonsLayout.setVisibility(View.GONE);
        } else {
            if (lastShownAdditionalButtonsLayout != null) {
                lastShownAdditionalButtonsLayout.setVisibility(View.GONE);
            }
            additionalButtonsLayout.setVisibility(View.VISIBLE);
            lastShownAdditionalButtonsLayout = additionalButtonsLayout;
        }
        refreshListView();
    }

    public void onItemOutsideClick(View view) {
        if (lastShownAdditionalButtonsLayout != null) {
            selectedItemId = 0;
            this.cursorAdapter.setSelectedItemId(selectedItemId);
            lastShownAdditionalButtonsLayout.setVisibility(View.GONE);
            refreshListView();
        }
    }

    public void onPlusButtonClick(View view) {
        View v = (View) view.getParent().getParent();

        TextView taskTextView = (TextView) v.findViewById(R.id.itemId);
        Integer itemId = Integer.parseInt(taskTextView.getText().toString());

        TextView qtyView = (TextView) v.findViewById(R.id.itemQty);
        int qty;
        if (qtyView.getText().toString().equals("")) {
            qty = 1;
        } else {
            qty = Integer.parseInt(qtyView.getText().toString());
            qty++;
        }

        if (qty > 99) {
            return;
        }

        String sql = String.format("UPDATE %s SET %s = %s WHERE %s = '%s'",
                ItemContract.TABLE,
                ItemContract.Columns.QTY,
                qty,
                ItemContract.Columns._ID,
                itemId);


        helper = new ItemDbHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);

        refreshListView();

    }

    public void onEditButtonClick(View view) {
        View v = (View) view.getParent().getParent();

        TextView itemIdTextView = (TextView) v.findViewById(R.id.itemId);
        Integer itemId = Integer.parseInt(itemIdTextView.getText().toString());

        TextView itemQtyTextView = (TextView) v.findViewById(R.id.itemQty);
        Integer itemQty = Integer.parseInt(itemQtyTextView.getText().toString());

        TextView itemNameTextView = (TextView) v.findViewById(R.id.itemName);
        String itemName = itemNameTextView.getText().toString();

        TextView itemPriceTextView = (TextView) v.findViewById(R.id.itemPrice);

        String itemPrice = itemPriceTextView.getText().toString();

        Intent intent = new Intent(this, AddItemActivity.class);

        intent.putExtra("itemId", itemId);
        intent.putExtra("itemName", itemName);
        intent.putExtra("itemPrice", itemPrice);
        intent.putExtra("itemQty", itemQty);

        startActivity(intent);

    }

    public void onMinusButtonClick(View view) {
        View v = (View) view.getParent().getParent();

        TextView taskTextView = (TextView) v.findViewById(R.id.itemId);
        Integer itemId = Integer.parseInt(taskTextView.getText().toString());

        TextView qtyView = (TextView) v.findViewById(R.id.itemQty);
        int qty;
        if (qtyView.getText().toString().equals("")) {
            qty = 0;
        } else {
            qty = Integer.parseInt(qtyView.getText().toString());
            qty--;
        }

        if (qty < 1) {
            return;
        }

        String sql = String.format("UPDATE %s SET %s = %s WHERE %s = '%s'",
                ItemContract.TABLE,
                ItemContract.Columns.QTY,
                qty,
                ItemContract.Columns._ID,
                itemId);


        helper = new ItemDbHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);

        refreshListView();

    }


}
