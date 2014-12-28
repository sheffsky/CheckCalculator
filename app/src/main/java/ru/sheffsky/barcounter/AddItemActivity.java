package ru.sheffsky.barcounter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import ru.sheffsky.barcounter.db.ItemContract;
import ru.sheffsky.barcounter.db.ItemDbHelper;


public class AddItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        NumberPicker np = (NumberPicker) findViewById(R.id.newItemQty);
        np.setMaxValue(99);
        np.setMinValue(1);
        np.setValue(1);

        Button button = (Button) findViewById(R.id.okButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onAddButtonClick(v);
            }
        });

        if (getIntent().getIntExtra("itemId",-1) > 0) {
            EditText itemNameEdit = (EditText) findViewById(R.id.newItemName);
            itemNameEdit.setText(getIntent().getStringExtra("itemName"));

            EditText itemPriceEdit = (EditText) findViewById(R.id.newItemPrice);
            itemPriceEdit.setText(getIntent().getStringExtra("itemPrice"));

            np.setValue(getIntent().getIntExtra("itemQty", 1));

            setTitle(getString(R.string.title_activity_add_item));

            button.setText(getString(R.string.updateButton));

        }

    }

    public void onAddButtonClick(View view) {

        EditText newItemName = (EditText) findViewById(R.id.newItemName);
        String itemName = newItemName.getText().toString();

        EditText newItemPrice = (EditText) findViewById(R.id.newItemPrice);
        Float itemPrice = Float.parseFloat(newItemPrice.getText().toString());

        NumberPicker newItemQty = (NumberPicker) findViewById(R.id.newItemQty);
        Integer itemQty = newItemQty.getValue();

        ItemDbHelper helper = new ItemDbHelper(AddItemActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.clear();
        if (getIntent().getIntExtra("itemId",-1) > 0) {
            values.put(ItemContract.Columns._ID, getIntent().getIntExtra("itemId",-1));
        }
        values.put(ItemContract.Columns.ITEM, itemName);
        values.put(ItemContract.Columns.PRICE, itemPrice);
        values.put(ItemContract.Columns.QTY, itemQty);

        db.insertWithOnConflict(ItemContract.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
