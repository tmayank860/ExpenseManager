package com.expensemanager.expensemanager.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.expensemanager.expensemanager.R;
import com.expensemanager.expensemanager.database.DBHelper;
import com.expensemanager.expensemanager.models.TransactionModel;
import com.expensemanager.expensemanager.utils.IConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    RadioGroup radioGroupTransactionType, radioGroupStatus;
    RadioButton radioButtonCredit, radioButtonDebit, radioButtonDone, radioButtonPending;
    TextView textViewDate, textViewTime;
    EditText editTextAmount, editTextDescription;
    Spinner spinnerCategory;
    Calendar calendar;
    int year, month, day, hour, min, sec;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.add_new_transaction);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DBHelper(AddTransactionActivity.this);

        radioGroupTransactionType = findViewById(R.id.radioGroup_transaction_type);
        radioGroupStatus = findViewById(R.id.radioGroup_status);
        radioButtonCredit = findViewById(R.id.radioButton_credit);
        radioButtonDebit = findViewById(R.id.radioButton_debit);
        radioButtonDone = findViewById(R.id.radioButton_done);
        radioButtonPending = findViewById(R.id.radioButton_pending);
        textViewDate = findViewById(R.id.textView_date);
        textViewTime = findViewById(R.id.textView_time);
        editTextAmount = findViewById(R.id.editText_amount);
        editTextDescription = findViewById(R.id.editText_description);
        spinnerCategory = findViewById(R.id.spinner_category);

        radioButtonDebit.setChecked(true);
        radioButtonDone.setChecked(true);

        List<String> allCategories = dbHelper.getAllCategories();

        spinnerCategory.setAdapter(new ArrayAdapter<String>(
                AddTransactionActivity.this,
                android.R.layout.simple_list_item_1,
                allCategories
        ));

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        sec = calendar.get(Calendar.SECOND);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddTransactionActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        textViewDate.setText(String.valueOf(day) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year));
                    }
                },
                year,
                month,
                day);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(
                AddTransactionActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        textViewTime.setText(String.valueOf(hour) + ":" + String.valueOf(min));
                    }
                },
                hour,
                min,
                false);

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        textViewDate.setText(String.valueOf(day) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year));
        textViewTime.setText(String.valueOf(hour) + ":" + String.valueOf(min));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AddTransactionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_save:
                saveData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_transaction_activity, menu);
        return true;
    }

    private void saveData() {
        if (editTextAmount.getText().toString().isEmpty()
                || Double.parseDouble(editTextAmount.getText().toString()) == 0) {
            editTextAmount.setError(getString(R.string.required));
        } else {
            TransactionModel transactionModel = new TransactionModel();

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

            String[] split = textViewDate.getText().toString()
                    .split("-");

            String[] split2 = textViewTime.getText().toString()
                    .split(":");

            Date date = new Date(
                    year - 1900,
                    Integer.parseInt(split[1]) - 1,
                    Integer.parseInt(split[0]),
                    Integer.parseInt(split2[0]),
                    Integer.parseInt(split2[1]));

            String dateString = simpleDateFormat.format(date);

            transactionModel.setTransactionType(radioGroupTransactionType
                    .getCheckedRadioButtonId() == R.id.radioButton_credit
                    ? IConstants.TRANSACTION_TYPE_CREDIT
                    : IConstants.TRANSACTION_TYPE_DEBIT);
            transactionModel.setDate("20-10-2018 18:40");
            transactionModel.setAmount(Double.parseDouble(editTextAmount.getText().toString()));
            transactionModel.setDescription(editTextDescription.getText().toString());
            transactionModel.setCategory(spinnerCategory.getSelectedItem().toString());
            transactionModel.setStatus(radioGroupStatus
                    .getCheckedRadioButtonId() == R.id.radioButton_done
                    ? IConstants.STATUS_DONE
                    : IConstants.STATUS_PENDING);
            transactionModel.setRefImage(null); // TODO: 18-08-2018 Save Image Pending

            boolean result = dbHelper.saveTransaction(transactionModel);

            if (result) {
                Toast.makeText(this, "Data Saved.", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(AddTransactionActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Try Again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddTransactionActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
