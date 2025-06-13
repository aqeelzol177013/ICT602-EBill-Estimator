package com.example.individual_assingment01;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryActivity extends Activity {

    ListView listViewHistory;
    ArrayList<String> historyList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewHistory = findViewById(R.id.listViewHistory);
        historyList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item_history, R.id.textViewItem, historyList);

        listViewHistory.setAdapter(adapter);

        loadHistoryFromDB();

        // Item click listener to view details
        listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = historyList.get(position);
                String[] parts = item.split(": ");
                String month = parts[0];
                String costStr = parts[1].replace("$", "").trim();
                double finalCost = Double.parseDouble(costStr);

                Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                intent.putExtra("MONTH", month);
                intent.putExtra("FINAL_COST", finalCost); // already parsed
                startActivity(intent);
            }
        });
    }

    private void loadHistoryFromDB() {
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getAllBills();
        historyList.clear();

        if (cursor.moveToFirst()) {
            do {
                String month = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_MONTH));
                double finalCost = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COL_FINAL));
                historyList.add(month + ": $" + String.format("%.2f", finalCost));
            } while (cursor.moveToNext());
        }

        adapter.notifyDataSetChanged();
    }
}