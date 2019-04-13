package com.expensemanager.expensemanager.database;

public class DatabaseContract {

    public static final class Transactions {
        public static final String TABLE_TRANSACTIONS = "transactions";
        public static final String COLUMN_TXN_ID = "txn_id";
        public static final String COLUMN_TRANSACTION_TYPE = "transaction_type";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_REF_IMAGE = "ref_image";
        public static final String COLUMN_STATUS = "status";
    }

    public static final class Category {
        public static final String TABLE_CATEGORY = "category";
        public static final String COLUMN_CATEGORY = "category_name";
    }
}
