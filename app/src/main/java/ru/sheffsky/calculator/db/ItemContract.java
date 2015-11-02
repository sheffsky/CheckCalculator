package ru.sheffsky.calculator.db;

import android.provider.BaseColumns;

public class ItemContract {
    public static final String DB_NAME = "ru.sheffsky.barcounter.db.tasks";
    public static final int DB_VERSION = 8;
    public static final String TABLE = "checks";


    public class Columns {
        public static final String ITEM = "item";
        public static final String QTY = "qty";
        public static final String PRICE = "price";
        public static final String _ID = BaseColumns._ID;
        public static final String PERSONS = "persons";
    }

    public class Values {

        private Integer ItemId;
        private String Item;
        private Integer Qty;
        private Float Price;
        private Integer Persons;

        public Integer getItemId() {
            return ItemId;
        }

        public void setItemId(Integer itemId) {
            ItemId = itemId;
        }

        public String getItem() {
            return Item;
        }

        public void setItem(String item) {
            Item = item;
        }

        public Integer getQty() {
            return Qty;
        }

        public void setQty(Integer qty) {
            Qty = qty;
        }

        public Float getPrice() {
            return Price;
        }

        public void setPrice(Float price) {
            Price = price;
        }

        public Integer getPersons() {
            return Persons;
        }

        public void setPersons(Integer persons) {
            Persons = persons;
        }

    }

}

