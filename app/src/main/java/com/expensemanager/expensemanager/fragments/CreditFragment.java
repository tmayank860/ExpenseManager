package com.expensemanager.expensemanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.expensemanager.expensemanager.R;
import com.expensemanager.expensemanager.adapters.TransactionListAdapter;
import com.expensemanager.expensemanager.database.DBHelper;
import com.expensemanager.expensemanager.interfaces.TransactionClickListener;
import com.expensemanager.expensemanager.models.TransactionModel;
import com.expensemanager.expensemanager.utils.IConstants;

import java.util.List;

public class CreditFragment extends Fragment {

    ListView listView;
    DBHelper dbHelper;

    private TransactionClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit, container, false);

        listView = view.findViewById(R.id.listView);

        dbHelper = new DBHelper(getActivity());

        final List<TransactionModel> allTransaction = dbHelper.getAllTransaction(IConstants.TRANSACTION_TYPE_CREDIT);

        listView.setAdapter(new TransactionListAdapter(getActivity(), allTransaction));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onTransactionClick(allTransaction.get(i));
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TransactionClickListener) getActivity();
    }
}
