package com.expensemanager.expensemanager.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemanager.expensemanager.R;
import com.expensemanager.expensemanager.fragments.AccountFragment;
import com.expensemanager.expensemanager.fragments.CreditFragment;
import com.expensemanager.expensemanager.fragments.DebitFragment;
import com.expensemanager.expensemanager.interfaces.TransactionClickListener;
import com.expensemanager.expensemanager.models.TransactionModel;
import com.expensemanager.expensemanager.utils.IConstants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TransactionClickListener {

    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;

    boolean backPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.navigation);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new AccountFragment(), AccountFragment.class.getSimpleName())
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_credit:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new CreditFragment(), CreditFragment.class.getSimpleName())
                                .commit();
                        return true;
                    case R.id.action_account:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new AccountFragment(), AccountFragment.class.getSimpleName())
                                .commit();
                        return true;
                    case R.id.action_debit:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new DebitFragment(), DebitFragment.class.getSimpleName())
                                .commit();
                        return true;
                }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences(IConstants.SP_NAME,
                Context.MODE_PRIVATE);

        String name = sharedPreferences.getString(IConstants.NAME, "");

        if ("".equals(name)) {
            finish();
        } else {
            TextView textViewUserName = navigationView
                    .getHeaderView(0)
                    .findViewById(R.id.textView_user_name);
            if (textViewUserName != null) {
                textViewUserName.setText("Hello, " + name);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed) {
                super.onBackPressed();
            } else {
                backPressed = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressed = false;
                    }
                }, 2000);
                Toast.makeText(MainActivity.this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
            }
        }

//        if (getSupportFragmentManager().getBackStackEntryCount()==0){
//            finish();
//        }else {
//            getSupportFragmentManager().popBackStack();
//        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this,
                    StartupActivity.class));
            finish();
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTransactionClick(TransactionModel transactionModel) {
        Intent intent = new Intent(MainActivity.this, TransactionDetailActivity.class);
        intent.putExtra("TransactionModel", transactionModel);
        startActivity(intent);
        finish();
    }
}
