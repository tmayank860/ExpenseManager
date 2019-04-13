package com.expensemanager.expensemanager.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.expensemanager.expensemanager.R;
import com.expensemanager.expensemanager.utils.IConstants;

public class StartupActivity extends AppCompatActivity {

    EditText editTextName, editTextBudget;
    Button buttonProceed;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        editTextName = findViewById(R.id.editText_name);
        editTextBudget = findViewById(R.id.editText_budget);
        buttonProceed = findViewById(R.id.button_proceed);

        sharedPreferences = getSharedPreferences(IConstants.SP_NAME, Context.MODE_PRIVATE);

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextName.getText().toString().equals("")) {
                    editTextName.setError(getString(R.string.required));
                } else if (editTextBudget.getText().toString().equals("")) {
                    editTextBudget.setError(getString(R.string.required));
                } else {
                    String name = editTextName.getText().toString();
                    float budget = Float.parseFloat(editTextBudget.getText().toString());

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(IConstants.NAME, name);
                    editor.putFloat(IConstants.BUDGET, budget);

                    editor.apply();

                    startActivity(new Intent(StartupActivity.this,
                            MainActivity.class));
                    finish();
                }
            }
        });
    }
}
