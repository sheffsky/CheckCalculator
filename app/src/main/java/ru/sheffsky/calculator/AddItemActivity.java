package ru.sheffsky.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;

import ru.sheffsky.calculator.db.ItemContract;
import ru.sheffsky.calculator.db.DbUtils;
import ru.sheffsky.calculator.utils.InterfaceUtils;


public class AddItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

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
                toggleQtyPicker(v);
            }
        });

        EditText itemQtyEdit = (EditText) findViewById(R.id.qtyNumber);
        itemQtyEdit.setFilters(new InputFilter[]{new InputFilterMinMax(InterfaceUtils.settingMinQty, InterfaceUtils.settingMaxQty, false)});

        EditText personQtyEdit = (EditText) findViewById(R.id.personQty);
        personQtyEdit.setFilters(new InputFilter[]{new InputFilterMinMax(InterfaceUtils.settingMinPersons, InterfaceUtils.settingMaxPersons, false)});

        EditText itemPriceEdit = (EditText) findViewById(R.id.newItemPrice);
        itemPriceEdit.setFilters(new InputFilter[]{new InputFilterMinMax(InterfaceUtils.settingMinPrice, InterfaceUtils.settingMaxPrice, true)});
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

            Map<String, String> itemMap;
            EditText itemNameEdit = (EditText) findViewById(R.id.newItemName);

            itemMap = DbUtils.getItemById(this, getIntent().getIntExtra("itemId", -1));

            itemNameEdit.setText(itemMap.get(ItemContract.Columns.ITEM));
            itemPriceEdit.setText(itemMap.get(ItemContract.Columns.PRICE));
            itemQtyEdit.setText(itemMap.get(ItemContract.Columns.QTY));
            personQtyEdit.setText(itemMap.get(ItemContract.Columns.PERSONS));

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

    public void toggleQtyPicker(View view) {

        hideKeyboard();

        ImageButton showPickerButton = (ImageButton) findViewById(R.id.showPickerButton);
        RelativeLayout qtyLayout = (RelativeLayout) findViewById(R.id.quantityLayout);

        if (qtyLayout.getVisibility() == View.GONE) {
            qtyLayout.setVisibility(View.VISIBLE);
            showPickerButton.setImageResource(R.drawable.ic_chevron_up_grey600_36dp);
        } else {
            qtyLayout.setVisibility(View.GONE);
            showPickerButton.setImageResource(R.drawable.ic_chevron_down_grey600_36dp);
        }
    }

    public void onAddButtonClick(View view) {

        EditText newItemName = (EditText) findViewById(R.id.newItemName);
        TextView newItemQty = (TextView) findViewById(R.id.qtyNumber);
        TextView newItemPersons = (TextView) findViewById(R.id.personQty);
        EditText newItemPrice = (EditText) findViewById(R.id.newItemPrice);

        Float itemPrice;

        try {
            itemPrice = Float.parseFloat(newItemPrice.getText().toString());
        } catch (NumberFormatException e) {
            itemPrice = (float) 0;
        }

        HashMap<String, Object> itemMap = new HashMap<>();

        itemMap.put(ItemContract.Columns._ID,       getIntent().getIntExtra("itemId", -1));
        itemMap.put(ItemContract.Columns.ITEM,      newItemName.getText());
        itemMap.put(ItemContract.Columns.PRICE,     itemPrice);
        itemMap.put(ItemContract.Columns.QTY,       newItemQty.getText());
        itemMap.put(ItemContract.Columns.PERSONS,   newItemPersons.getText());

        DbUtils.addOrUpdateItem(this, itemMap);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}
