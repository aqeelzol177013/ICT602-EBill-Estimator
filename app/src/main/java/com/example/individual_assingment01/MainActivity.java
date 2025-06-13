package com.example.individual_assingment01;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    EditText editTextMonth, editTextUnits, editTextRebate;
    TextView textViewTotal, textViewFinal;
    Button buttonCalculate, buttonAboutUs, buttonViewHistory;

    DBHelper dbHelper;
    ArrayList<Bill> billList;
    ArrayAdapter<Bill> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextMonth = findViewById(R.id.editTextMonth);
        editTextUnits = findViewById(R.id.editTextUnits);
        editTextRebate = findViewById(R.id.editTextRebate);
        textViewTotal = findViewById(R.id.textViewTotal);
        textViewFinal = findViewById(R.id.textViewFinal);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonAboutUs = findViewById(R.id.buttonAboutUs);
        buttonViewHistory = findViewById(R.id.buttonViewHistory);

        dbHelper = new DBHelper(this);
        billList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, billList);

        // Calculate button click listener
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndSaveBill();
            }
        });

        // About Us button click listener
        buttonAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        // View History button click listener
        buttonViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void calculateAndSaveBill() {
        String month = editTextMonth.getText().toString().trim();
        String unitStr = editTextUnits.getText().toString().trim();
        String rebateStr = editTextRebate.getText().toString().trim();

        if (month.isEmpty() || unitStr.isEmpty() || rebateStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double units = Double.parseDouble(unitStr);
        double rebatePercent = Double.parseDouble(rebateStr);

        if (units < 0 || rebatePercent < 0 || rebatePercent > 5) {
            Toast.makeText(this, "Invalid input: units must be ≥ 0 and rebate must be between 0% and 5%", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate total charges based on tiered pricing
        double totalCharge = calculateTotalCharge(units);

        // Apply rebate
        double finalCost = totalCharge * (1 - rebatePercent / 100);

        // Update UI
        textViewTotal.setText("Total Charges: $" + String.format("%.2f", totalCharge));
        textViewFinal.setText("Final Cost: $" + String.format("%.2f", finalCost));

        // Save to DB
        dbHelper.insertBill(month, units, rebatePercent, totalCharge, finalCost);
    }

    private double calculateTotalCharge(double units) {
        double totalCharge = 0;

        if (units <= 200) {
            totalCharge = units * 0.218;
        } else if (units <= 300) {
            totalCharge = (200 * 0.218) + ((units - 200) * 0.334);
        } else if (units <= 600) {
            totalCharge = (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
        } else {
            totalCharge = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
        }

        return totalCharge;
    }
}