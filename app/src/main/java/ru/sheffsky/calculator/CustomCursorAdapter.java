package ru.sheffsky.calculator;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.sheffsky.calculator.db.ItemContract;

public class CustomCursorAdapter extends SimpleCursorAdapter {

    private int layout;

    private Integer selectedItemID;

    public CustomCursorAdapter(Context _context, int _layout, Cursor _cursor, String[] _from, int[] _to, Integer selectedItemId) {
        super(_context, _layout, _cursor, _from, _to);
        layout = _layout;
        selectedItemID = selectedItemId;
    }

    @Override
    public void bindView(View view, Context _context, Cursor _cursor) {

        TextView itemId = (TextView) view.findViewById(R.id.itemId);
        itemId.setText(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns._ID)));

        TextView itemName = (TextView) view.findViewById(R.id.itemName);
        itemName.setText(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns.ITEM)));

        TextView itemPrice = (TextView) view.findViewById(R.id.itemPrice);
        itemPrice.setText(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns.PRICE)));

        TextView itemQty = (TextView) view.findViewById(R.id.itemQty);
        itemQty.setText(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns.QTY)));

        if (selectedItemID == Integer.parseInt(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns._ID)))) {
            RelativeLayout additionalButtonsLayout = (RelativeLayout) view.findViewById(R.id.additionalButtonsLayout);
            additionalButtonsLayout.setVisibility(View.VISIBLE);
        } else {
            RelativeLayout additionalButtonsLayout = (RelativeLayout) view.findViewById(R.id.additionalButtonsLayout);
            additionalButtonsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public View newView(Context _context, Cursor _cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(layout, parent, false);
    }

}
