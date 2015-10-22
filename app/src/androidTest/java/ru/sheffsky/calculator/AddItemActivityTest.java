package ru.sheffsky.calculator;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.NumberPicker;


public class AddItemActivityTest extends ActivityInstrumentationTestCase2 {

    private AddItemActivity addItemActivity;
    private EditText newItemName;
    private EditText newItemPrice;
    private EditText newItemQty;

    public AddItemActivityTest() {
        super(AddItemActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);

        addItemActivity = (AddItemActivity) getActivity();

        newItemName = (EditText) addItemActivity.findViewById(R.id.newItemName);
        newItemPrice = (EditText) addItemActivity.findViewById(R.id.newItemPrice);
        newItemQty = (EditText) addItemActivity.findViewById(R.id.qtyNumber);

    }

    public void testControlsCreated() {
        assertNotNull(addItemActivity);
    }

    public void testStartingEmpty() {
        assertEquals("newItemName is not empty", "", newItemName.getText()
                .toString());
        assertEquals("newItemPrice is not empty", "", newItemPrice
                .getText().toString());
        assertEquals("newItemQty value is not 1", 1, Integer.parseInt(newItemQty.getText().toString()));
    }

}