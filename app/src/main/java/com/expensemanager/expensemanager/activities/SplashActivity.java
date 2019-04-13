package com.expensemanager.expensemanager.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.expensemanager.expensemanager.R;
import com.expensemanager.expensemanager.utils.IConstants;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(IConstants.SP_NAME,
                Context.MODE_PRIVATE);

        final String name = sharedPreferences.getString(IConstants.NAME, "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ("".equals(name)) {
                    startActivity(new Intent(SplashActivity.this,
                            StartupActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this,
                            MainActivity.class));
                    finish();
                }
            }
        }, 1500);
    }
}
