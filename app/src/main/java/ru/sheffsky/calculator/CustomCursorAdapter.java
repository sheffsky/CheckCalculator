package ru.sheffsky.calculator;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import ru.sheffsky.calculator.utils.InterfaceUtils;

import ru.sheffsky.calculator.db.ItemContract;

public class CustomCursorAdapter extends SimpleCursorAdapter {

    //private int layout;
    private Integer selectedItemID = 0;

    public CustomCursorAdapter(Context _context, int _layout, Cursor _cursor, String[] _from, int[] _to) {
        super(_context, _layout, _cursor, _from, _to);
        LayoutInflater mInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //layout = _layout;
    }

    public void setSelectedItemId(Integer itemId) {
        this.selectedItemID = itemId;
    }

    @Override
    public void bindView(View view, Context _context, Cursor _cursor) {

        TextView itemId = (TextView) view.findViewById(R.id.itemId);
        itemId.setText(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns._ID)));

        TextView itemName = (TextView) view.findViewById(R.id.itemName);
        itemName.setText(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns.ITEM)));

        TextView itemPrice = (TextView) view.findViewById(R.id.itemPrice);
        itemPrice.setText(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns.PRICE)));

        String itemQtyText = _cursor.getString(_cursor.getColumnIndex(ItemContract.Columns.QTY));
        Integer personsQty = _cursor.getInt(_cursor.getColumnIndex(ItemContract.Columns.PERSONS));

        TextView itemQty = (TextView) view.findViewById(R.id.itemQty);
        if (personsQty == 1) {
            itemQty.setText(itemQtyText);
        } else {
            itemQty.setText(itemQtyText + "/" + Integer.toString(personsQty));
        }

        if (selectedItemID == Integer.parseInt(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns._ID)))) {
            RelativeLayout additionalButtonsLayout = (RelativeLayout) view.findViewById(R.id.additionalButtonsLayout);
            additionalButtonsLayout.setVisibility(View.VISIBLE);
        } else {
            RelativeLayout additionalButtonsLayout = (RelativeLayout) view.findViewById(R.id.additionalButtonsLayout);
            additionalButtonsLayout.setVisibility(View.GONE);
        }

        ImageButton btnMinus = (ImageButton) view.findViewById(R.id.minusButton);

        if (Integer.parseInt(itemQtyText) == InterfaceUtils.settingMinQty) {
            InterfaceUtils.setImageButtonColor(view, InterfaceUtils.buttonType.MINUS, btnMinus, InterfaceUtils.buttonColor.GRAY);
            btnMinus.setEnabled(false);
        } else {
            InterfaceUtils.setImageButtonColor(view, InterfaceUtils.buttonType.MINUS, btnMinus, InterfaceUtils.buttonColor.RED);
            btnMinus.setEnabled(true);
        }

        ImageButton btnPlus = (ImageButton) view.findViewById(R.id.plusButton);

        if (Integer.parseInt(itemQtyText) == InterfaceUtils.settingMaxQty) {
            InterfaceUtils.setImageButtonColor(view, InterfaceUtils.buttonType.PLUS, btnPlus, InterfaceUtils.buttonColor.GRAY);
            btnPlus.setEnabled(false);
        } else {
            InterfaceUtils.setImageButtonColor(view, InterfaceUtils.buttonType.PLUS, btnPlus, InterfaceUtils.buttonColor.GREEN);
            btnPlus.setEnabled(true);
        }

    }

}
