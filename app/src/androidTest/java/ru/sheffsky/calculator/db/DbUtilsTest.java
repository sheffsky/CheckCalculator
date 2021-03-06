package ru.sheffsky.calculator.db;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class DbUtilsTest extends AndroidTestCase {

    protected Integer checkItemId;
    protected String checkItem = "Test iteM 1";
    protected Integer checkQty = 4;
    protected Float checkPrice = (float) 25.16;
    protected Integer checkPersons = 2;

    protected RenamingDelegatingContext context;
    protected final String TEST_FILE_PREFIX = "test_";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        context = new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);

        checkItemId = addItem();

    }

    protected Integer addItem() {

        ItemContract.Values itemValues;

        itemValues = new ItemContract().new Values();
        itemValues.setItem(checkItem);
        itemValues.setQty(checkQty);
        itemValues.setPersons(checkPersons);
        itemValues.setPrice(checkPrice);

        return DbUtils.addOrUpdateItem(context, itemValues);
    }

    public void testAddOrUpdateItemOnNullObject() {

        long returnValue;
        ItemContract.Values itemValues = new ItemContract().new Values();

        returnValue = DbUtils.addOrUpdateItem(context, itemValues);

        assertEquals("Returned value is incorrect.", -1, returnValue);

    }

    public void testAddOrUpdateItemWithUnsetValues() {

        long returnValue;
        ItemContract.Values itemValues = new ItemContract().new Values();

        returnValue = DbUtils.addOrUpdateItem(context, itemValues);

        assertEquals("Returned value is incorrect.", -1, returnValue);

    }


    public void testAddOrUpdateItemWithNonexistentId() {
        // when we're trying to update by nonexistent id, 0 rows will be affected
        long returnValue;
        ItemContract.Values itemValues;

        itemValues = new ItemContract().new Values();

        itemValues.setItemId(-50);
        returnValue = DbUtils.addOrUpdateItem(context, itemValues);
        assertEquals("Returned value is incorrect.", -1, returnValue);

        itemValues.setItemId(6000);
        returnValue = DbUtils.addOrUpdateItem(context, itemValues);
        assertEquals("Returned value is incorrect.", 0, returnValue);

    }

    public void testAddOrUpdateItemWithNullValuesButId() {

        long returnValue;
        ItemContract.Values itemValues;

        itemValues = new ItemContract().new Values();
        itemValues.setItemId(checkItemId);

        returnValue = DbUtils.addOrUpdateItem(context, itemValues);

        assertEquals("Returned value is incorrect. ", 1, returnValue);

    }

    public void testGetItemByIdNormally() {

        ItemContract.Values itemValues;
        itemValues = DbUtils.getItemById(context, checkItemId);

        assertEquals("Item name is incorrect. ", checkItem, itemValues.getItem());
        assertEquals("Item price is incorrect. ", checkPrice, itemValues.getPrice());
        assertEquals("Item qty is incorrect. ", checkQty, itemValues.getQty());
        assertEquals("Item persons is incorrect. ", checkPersons, itemValues.getPersons());

    }

    public void testUpdateItemNormally() {

        String newItem = "Item nAme / updated 1";
        Integer newPersons = 6;
        Integer newQty = 12;
        Float newPrice = (float) 258.16;
        long returnValue;
        ItemContract.Values itemValues;

        itemValues = new ItemContract().new Values();

        itemValues.setItemId(checkItemId);
        itemValues.setPersons(newPersons);
        itemValues.setQty(newQty);
        itemValues.setPrice(newPrice);
        itemValues.setItem(newItem);

        returnValue = DbUtils.addOrUpdateItem(context, itemValues);

        assertEquals("Item was not updated correctly.", 1, returnValue);

        itemValues = DbUtils.getItemById(context, checkItemId);

        assertEquals("Item name is incorrect. ", newItem, itemValues.getItem());
        assertEquals("Item price is incorrect. ", newPrice, itemValues.getPrice());
        assertEquals("Item qty is incorrect. ", newQty, itemValues.getQty());
        assertEquals("Item persons is incorrect. ", newPersons, itemValues.getPersons());

    }

    public void testGetTotalPrice() {

        ItemContract.Values itemValues;

        itemValues = new ItemContract().new Values();
        itemValues.setPersons(1);
        itemValues.setQty(3);
        itemValues.setPrice((float) 200);

        DbUtils.deleteAllItems(context);

        DbUtils.addOrUpdateItem(context, itemValues);

        double price = DbUtils.getTotalPrice(context);

        assertEquals((double) 200*3, price);

        itemValues.setPersons(3);
        itemValues.setQty(2);
        itemValues.setPrice((float) 321.15);

        DbUtils.addOrUpdateItem(context, itemValues);

        price = DbUtils.getTotalPrice(context);

        assertEquals((double) 200*3 + 321.15*2/3, price);

    }

    public void testGetTotalPriceAfterDeletion() {

        DbUtils.deleteAllItems(context);

        ItemContract.Values itemValues;

        itemValues = new ItemContract().new Values();
        itemValues.setPersons(1);
        itemValues.setQty(3);
        itemValues.setPrice((float) 200);

        DbUtils.deleteAllItems(context);

        Integer itemId1 = DbUtils.addOrUpdateItem(context, itemValues);

        double price = DbUtils.getTotalPrice(context);

        assertEquals((double) 200 * 3, price);

        itemValues.setPersons(3);
        itemValues.setQty(2);
        itemValues.setPrice((float) 321.15);

        Integer itemId2 = DbUtils.addOrUpdateItem(context, itemValues);

        DbUtils.deleteItemById(context, itemId1);

        price = DbUtils.getTotalPrice(context);

        assertEquals(321.15*2/3, price);

        DbUtils.deleteItemById(context, itemId2);

        price = DbUtils.getTotalPrice(context);

        assertEquals((double) 0, price);

    }



}
