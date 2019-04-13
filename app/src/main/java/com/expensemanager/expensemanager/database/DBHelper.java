package com.expensemanager.expensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.expensemanager.expensemanager.models.TransactionModel;

import java.util.ArrayList;
import java.util.List;

import static com.expensemanager.expensemanager.database.DatabaseContract.Category;
import static com.expensemanager.expensemanager.database.DatabaseContract.Transactions;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "expmgr.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " + Transactions.TABLE_TRANSACTIONS +
            " (" +
            Transactions.COLUMN_TXN_ID + " INTEGER PRIMARY KEY, " +
            Transactions.COLUMN_TRANSACTION_TYPE + " VARCHAR(1), " +
            Transactions.COLUMN_DATE + " DATETIME, " +
            Transactions.COLUMN_AMOUNT + " REAL, " +
            Transactions.COLUMN_DESCRIPTION + " VARCHAR(500), " +
            Transactions.COLUMN_CATEGORY + " VARCHAR(100), " +
            Transactions.COLUMN_REF_IMAGE + " BLOB, " +
            Transactions.COLUMN_STATUS + " VARCHAR(1)" +
            ");";

    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + Category.TABLE_CATEGORY +
            " (" +
            Category.COLUMN_CATEGORY + " VARCHAR(100)" +
            ");";

    private static final String INSERT_INTO_CATEGORY = "INSERT INTO " +
            Category.TABLE_CATEGORY + " VALUES " +
            "('Education')," +
            "('Entertainment')," +
            "('Clothing')," +
            "('Food')," +
            "('Travel')," +
            "('Personal')," +
            "('Shopping');";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TRANSACTIONS);
        sqLiteDatabase.execSQL(CREATE_TABLE_CATEGORY);
        sqLiteDatabase.execSQL(INSERT_INTO_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    public boolean saveTransaction(TransactionModel transactionModel) {
        boolean success = false;
        ContentValues contentValues = new ContentValues();

        contentValues.put(Transactions.COLUMN_TRANSACTION_TYPE, transactionModel.getTransactionType());
        contentValues.put(Transactions.COLUMN_DATE, transactionModel.getDate());
        contentValues.put(Transactions.COLUMN_AMOUNT, transactionModel.getAmount());
        contentValues.put(Transactions.COLUMN_DESCRIPTION, transactionModel.getDescription());
        contentValues.put(Transactions.COLUMN_CATEGORY, transactionModel.getCategory());
        contentValues.put(Transactions.COLUMN_REF_IMAGE, transactionModel.getRefImage());
        contentValues.put(Transactions.COLUMN_STATUS, transactionModel.getStatus());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            try {
                sqLiteDatabase.insertOrThrow(Transactions.TABLE_TRANSACTIONS, null, contentValues);
                success = true;
            } catch (Exception e) {
                //Ignored
            } finally {
                sqLiteDatabase.close();
            }
        }
        return success;
    }

    public List<TransactionModel> getAllTransaction(String transactionType) {
        List<TransactionModel> list = new ArrayList<>();

        String sql = "SELECT *" +
                " FROM " + Transactions.TABLE_TRANSACTIONS +
                " WHERE " + Transactions.COLUMN_TRANSACTION_TYPE + " = '" + transactionType + "';";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        if (sqLiteDatabase != null) {

            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);

                    TransactionModel transactionModel = new TransactionModel();

                    transactionModel.setTxnId(cursor.getInt(cursor.getColumnIndex(Transactions.COLUMN_TXN_ID)));
                    transactionModel.setTransactionType(transactionType);
                    transactionModel.setDate(cursor.getString(cursor.getColumnIndex(Transactions.COLUMN_DATE)));
                    transactionModel.setAmount(cursor.getDouble(cursor.getColumnIndex(Transactions.COLUMN_AMOUNT)));
                    transactionModel.setDescription(cursor.getString(cursor.getColumnIndex(Transactions.COLUMN_DESCRIPTION)));
                    transactionModel.setCategory(cursor.getString(cursor.getColumnIndex(Transactions.COLUMN_CATEGORY)));
                    transactionModel.setStatus(cursor.getString(cursor.getColumnIndex(Transactions.COLUMN_STATUS)));
                    transactionModel.setRefImage(cursor.getBlob(cursor.getColumnIndex(Transactions.COLUMN_REF_IMAGE)));

                    list.add(transactionModel);
                }
                cursor.close();
            }
            sqLiteDatabase.close();
        }

        return list;
    }

    public void deleteTransaction(int txnId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.delete(
                    Transactions.TABLE_TRANSACTIONS,
                    Transactions.COLUMN_TXN_ID + " = ?",
                    new String[]{String.valueOf(txnId)}
            );
            sqLiteDatabase.close();
        }
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();

        categories.add("Other");

        String sql = "SELECT * FROM " + Category.TABLE_CATEGORY +
                " ORDER BY " + Category.COLUMN_CATEGORY + ";";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        if (sqLiteDatabase != null) {

            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() != 0) {

                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {

                    cursor.moveToPosition(i);

                    String categoryName = cursor.getString(cursor.getColumnIndex(Category.COLUMN_CATEGORY));

                    categories.add(categoryName);
                }
                cursor.close();
            }
            sqLiteDatabase.close();
        }

        return categories;
    }

    public double getTotal(String txnType) {
        double total = 0;

        String sql = "SELECT SUM(" + Transactions.COLUMN_AMOUNT + ") AS TOTAL FROM " +
                Transactions.TABLE_TRANSACTIONS +
                " WHERE STRFTIME('%m', " + Transactions.COLUMN_DATE + ") = STRFTIME('%m', DATETIME('now'))" +
                " AND " + Transactions.COLUMN_TRANSACTION_TYPE + " = '" + txnType + "';";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        if (sqLiteDatabase != null) {

            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() != 0) {

                total = cursor.getDouble(cursor.getColumnIndex("TOTAL"));

                cursor.close();
            }
            sqLiteDatabase.close();
        }

        return total;
    }

    public List<TransactionModel> getAllTransactionByCategory(String category) {
        List<TransactionModel> list = new ArrayList<>();

        String sql = "SELECT *" +
                " FROM " + Transactions.TABLE_TRANSACTIONS +
                " WHERE " + Transactions.COLUMN_CATEGORY + " = '" + category + "';";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        if (sqLiteDatabase != null) {

            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);

                    TransactionModel transactionModel = new TransactionModel();

                    transactionModel.setTxnId(cursor.getInt(cursor.getColumnIndex(Transactions.COLUMN_TXN_ID)));
                    transactionModel.setTransactionType(cursor.getString(cursor.getColumnIndex(Transactions.COLUMN_CATEGORY)));
                    transactionModel.setDate(cursor.getString(cursor.getColumnIndex(Transactions.COLUMN_DATE)));
                    transactionModel.setAmount(cursor.getDouble(cursor.getColumnIndex(Transactions.COLUMN_AMOUNT)));
                    transactionModel.setDescription(cursor.getString(cursor.getColumnIndex(Transactions.COLUMN_DESCRIPTION)));
                    transactionModel.setCategory(category);
                    transactionModel.setStatus(cursor.getString(cursor.getColumnIndex(Transactions.COLUMN_STATUS)));
                    transactionModel.setRefImage(cursor.getBlob(cursor.getColumnIndex(Transactions.COLUMN_REF_IMAGE)));

                    list.add(transactionModel);
                }
                cursor.close();
            }
            sqLiteDatabase.close();
        }

        return list;
    }
}
