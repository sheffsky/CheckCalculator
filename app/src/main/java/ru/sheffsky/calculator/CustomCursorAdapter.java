package ru.sheffsky.calculator;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ru.sheffsky.calculator.db.ItemContract;

public class CustomCursorAdapter extends SimpleCursorAdapter {

    private int layout;
    private Integer selectedItemID = 0;

    public CustomCursorAdapter(Context _context, int _layout, Cursor _cursor, String[] _from, int[] _to) {
        super(_context, _layout, _cursor, _from, _to);
        LayoutInflater mInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = _layout;
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

        TextView itemQty = (TextView) view.findViewById(R.id.itemQty);
        itemQty.setText(itemQtyText);

        if (selectedItemID == Integer.parseInt(_cursor.getString(_cursor.getColumnIndex(ItemContract.Columns._ID)))) {
            RelativeLayout additionalButtonsLayout = (RelativeLayout) view.findViewById(R.id.additionalButtonsLayout);
            additionalButtonsLayout.setVisibility(View.VISIBLE);
        } else {
            RelativeLayout additionalButtonsLayout = (RelativeLayout) view.findViewById(R.id.additionalButtonsLayout);
            additionalButtonsLayout.setVisibility(View.GONE);
        }

        ImageButton btnMinus = (ImageButton) view.findViewById(R.id.minusButton);
        Drawable originalMinusIcon = view.getResources().getDrawable(R.drawable.ic_minus_circle_outline_grey600_36dp);
        Drawable mutatedMinusIcon = originalMinusIcon.mutate();

        if (Integer.parseInt(itemQtyText) == 1) {
            mutatedMinusIcon.setColorFilter(Color.parseColor("#535353"), PorterDuff.Mode.SRC_IN); //Grey
            btnMinus.setEnabled(false);
        } else {
            mutatedMinusIcon.setColorFilter(Color.parseColor("#A05555"), PorterDuff.Mode.SRC_IN); //Red 915555
            btnMinus.setEnabled(true);
        }

        btnMinus.setImageDrawable(mutatedMinusIcon);

        ImageButton btnPlus = (ImageButton) view.findViewById(R.id.plusButton);
        Drawable originalPlusIcon = view.getResources().getDrawable(R.drawable.ic_plus_circle_outline_grey600_36dp);
        Drawable mutatedPlusIcon = originalPlusIcon.mutate();

        if (Integer.parseInt(itemQtyText) == 99) {
            mutatedPlusIcon.setColorFilter(Color.parseColor("#535353"), PorterDuff.Mode.SRC_IN); //Grey
            btnPlus.setEnabled(false);
        } else {
            mutatedPlusIcon.setColorFilter(Color.parseColor("#55A055"), PorterDuff.Mode.SRC_IN); //Green 3C753F
            btnPlus.setEnabled(true);
        }

        btnPlus.setImageDrawable(mutatedPlusIcon);

    }


}
