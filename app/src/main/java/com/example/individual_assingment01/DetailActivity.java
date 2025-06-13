package com.example.individual_assingment01;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView textViewMonth = findViewById(R.id.textViewMonth);
        TextView textViewDetails = findViewById(R.id.textViewDetails);

        // Get data from intent
        String month = getIntent().getStringExtra("MONTH");
        double finalCost = getIntent().getDoubleExtra("FINAL_COST", -1);

        if (month == null || finalCost == -1) {
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
            finish(); // Close activity
            return;
        }

        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getBillByMonth(month);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                double units = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COL_UNITS));
                double rebate = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COL_REBATE));
                double totalCharge = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COL_TOTAL));

                textViewMonth.setText("Month: " + month);
                textViewDetails.setText(
                        "Units Consumed: " + units + " kWh\n" +
                                "Total Charges: $" + String.format("%.2f", totalCharge) + "\n" +
                                "Rebate (%): " + rebate + "\n" +
                                "Final Cost: $" + String.format("%.2f", finalCost)
                );
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load bill details", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                finish();
            } finally {
                cursor.close();
            }
        } else {
            Toast.makeText(this, "No data found for " + month, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}