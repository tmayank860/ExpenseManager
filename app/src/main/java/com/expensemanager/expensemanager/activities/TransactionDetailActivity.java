package com.expensemanager.expensemanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemanager.expensemanager.R;
import com.expensemanager.expensemanager.database.DBHelper;
import com.expensemanager.expensemanager.models.TransactionModel;

public class TransactionDetailActivity extends AppCompatActivity {

    TextView textViewDate, textViewTransactionType,
            textViewStatus, textViewDescription, textViewCategory;
    EditText editTextAmount;
    TransactionModel transactionModel;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.transaction_detail);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        textViewDate = findViewById(R.id.textView_date);
        textViewTransactionType = findViewById(R.id.textView_transaction_type);
        textViewStatus = findViewById(R.id.textView_status);
        textViewDescription = findViewById(R.id.textView_description);
        textViewCategory = findViewById(R.id.textView_category);
        editTextAmount = findViewById(R.id.editText_amount);

        dbHelper = new DBHelper(TransactionDetailActivity.this);

        Intent intent = getIntent();
        if (intent != null) {
            transactionModel = (TransactionModel) intent.getSerializableExtra("TransactionModel");
        } else {
            finish();
            Intent intent2 = new Intent(TransactionDetailActivity.this, MainActivity.class);
            startActivity(intent2);
        }

        if (transactionModel == null) {
            finish();
            Intent intent2 = new Intent(TransactionDetailActivity.this, MainActivity.class);
            startActivity(intent2);
        } else {
            editTextAmount.setText(String.valueOf(transactionModel.getAmount()));
            textViewDate.setText(transactionModel.getDate());
            textViewTransactionType.setText(transactionModel.getTransactionType());
            textViewDescription.setText(transactionModel.getDescription());
            textViewCategory.setText(transactionModel.getCategory());
            textViewStatus.setText(transactionModel.getStatus());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transaction_detail_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(TransactionDetailActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.action_delete:
                if (transactionModel != null) {
                    dbHelper.deleteTransaction(transactionModel.getTxnId());
                }
                Toast.makeText(this, "Deleted.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(TransactionDetailActivity.this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(TransactionDetailActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
