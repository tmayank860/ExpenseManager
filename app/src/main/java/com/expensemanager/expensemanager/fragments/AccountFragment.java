package com.expensemanager.expensemanager.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.expensemanager.expensemanager.R;
import com.expensemanager.expensemanager.adapters.TransactionListAdapter;
import com.expensemanager.expensemanager.database.DBHelper;
import com.expensemanager.expensemanager.models.TransactionModel;
import com.expensemanager.expensemanager.utils.IConstants;

import java.util.List;

public class AccountFragment extends Fragment {

    TextView textViewBudget;
    SharedPreferences sharedPreferences;
    Spinner spinnerCategory;
    ListView listView;

    float budget;

    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,
                container,
                false);

        sharedPreferences = getActivity().getSharedPreferences(IConstants.SP_NAME,
                Context.MODE_PRIVATE);

        budget = sharedPreferences.getFloat(IConstants.BUDGET, 0);

        textViewBudget = view.findViewById(R.id.textView_budget);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        listView = view.findViewById(R.id.listView);

        textViewBudget.setText("Monthly Budget" + "\n" + String.valueOf(budget));

        dbHelper = new DBHelper(getActivity());

        final List<String> allCategories = dbHelper.getAllCategories();

        spinnerCategory.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, allCategories));

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String categoryName = allCategories.get(i);

                List<TransactionModel> allTransactionByCategory =
                        dbHelper.getAllTransactionByCategory(categoryName);

                listView.setAdapter(new TransactionListAdapter(getActivity(),
                        allTransactionByCategory));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
}
