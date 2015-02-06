package ru.sheffsky.calculator;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.sheffsky.calculator.db.ItemContract;
import ru.sheffsky.calculator.db.ItemDbHelper;


public class AddItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        NumberPicker np = (NumberPicker) findViewById(R.id.newItemQty);
        np.setMaxValue(99);
        np.setMinValue(1);
        np.setValue(1);

        final Button button = (Button) findViewById(R.id.okButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onAddButtonClick(v);
            }
        });

        RelativeLayout addItemLayout = (RelativeLayout) findViewById(R.id.addItemLayout);
        addItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOuterClick(v);
            }
        });

        ImageButton showPickerButton = (ImageButton) findViewById(R.id.showPickerButton);
        showPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQtyPicker(v);
            }
        });

        EditText itemPriceEdit = (EditText) findViewById(R.id.newItemPrice);
        itemPriceEdit.setFilters(new InputFilter[]{new InputFilterMinMax("1", "999999")});
        itemPriceEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    button.setFocusableInTouchMode(true);
                    button.requestFocus();
                    button.setFocusableInTouchMode(false); //else this button needs to be clicked twice
                }
            }
        });

        if (getIntent().getIntExtra("itemId", -1) > 0) {
            EditText itemNameEdit = (EditText) findViewById(R.id.newItemName);
            itemNameEdit.setText(getIntent().getStringExtra("itemName"));

            itemPriceEdit.setText(getIntent().getStringExtra("itemPrice"));

            np.setValue(getIntent().getIntExtra("itemQty", 1));

            setTitle(getString(R.string.updateHeader));

            button.setText(getString(R.string.updateButton));
        }
    }


    public void onOuterClick(View view) {

        hideKeyboard();

    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void showQtyPicker(View view) {

        hideKeyboard();

        ImageButton showPickerButton = (ImageButton) findViewById(R.id.showPickerButton);
        showPickerButton.setVisibility(View.GONE);

        TextView qtyHeader = (TextView) findViewById(R.id.textView3);
        qtyHeader.setVisibility(View.VISIBLE);

        NumberPicker qtyPicker = (NumberPicker) findViewById(R.id.newItemQty);
        qtyPicker.setVisibility(View.VISIBLE);
    }

    public void onAddButtonClick(View view) {

        EditText newItemName = (EditText) findViewById(R.id.newItemName);
        String itemName = newItemName.getText().toString();

        Float itemPrice;

        EditText newItemPrice = (EditText) findViewById(R.id.newItemPrice);

        try {
            itemPrice = Float.parseFloat(newItemPrice.getText().toString());
        } catch (NumberFormatException e) {
            itemPrice = (float) 0;
        }
        NumberPicker newItemQty = (NumberPicker) findViewById(R.id.newItemQty);
        Integer itemQty = newItemQty.getValue();

        ItemDbHelper helper = new ItemDbHelper(AddItemActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.clear();
        if (getIntent().getIntExtra("itemId", -1) > 0) {
            values.put(ItemContract.Columns._ID, getIntent().getIntExtra("itemId", -1));
        }
        values.put(ItemContract.Columns.ITEM, itemName);
        values.put(ItemContract.Columns.PRICE, itemPrice);
        values.put(ItemContract.Columns.QTY, itemQty);

        db.insertWithOnConflict(ItemContract.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}
