package com.test.fuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class ChartActivity extends AppCompatActivity {

    private EditText dateFrom, dateTo;
    Calendar calendar;
    Button btnBarChart, btnPieChart, btnSendReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        btnBarChart = findViewById(R.id.btnBarChart);
        btnPieChart = findViewById(R.id.btnPieChart);
        btnSendReport = findViewById(R.id.btnReport);
        dateFrom = findViewById(R.id.editTextDateFrom);
        dateTo = findViewById(R.id.editTextDateTo);

        btnBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateFrom.getText().toString().trim().isEmpty() || dateTo.getText().toString().trim().isEmpty()) {
                    new AlertDialog.Builder(ChartActivity.this)
                            .setTitle("Empty fields")
                            .setMessage("Please select date from and to")
                            .show();
                } else {
                    Intent I = new Intent(ChartActivity.this, LineChartActivity.class);
                    I.putExtra("SelectedDate", " BETWEEN '" + dateFrom.getText().toString().trim() + "' AND '" +
                            dateTo.getText().toString().trim() + "'");
                    startActivity(I);
                }
            }
        });

        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateFrom.getText().toString().trim().isEmpty() || dateTo.getText().toString().trim().isEmpty()) {
                    new AlertDialog.Builder(ChartActivity.this)
                            .setTitle("Empty fields")
                            .setMessage("Please select date from and to")
                            .show();
                } else {
                    Intent I = new Intent(ChartActivity.this, PieChartActivity.class);
                    I.putExtra("SelectedDate", " BETWEEN '" + dateFrom.getText().toString().trim() + "' AND '" +
                            dateTo.getText().toString().trim() + "'");
                    startActivity(I);
                }
            }
        });

        btnSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateFrom.getText().toString().trim().isEmpty() || dateTo.getText().toString().trim().isEmpty()) {
                    new AlertDialog.Builder(ChartActivity.this)
                            .setTitle("Empty fields")
                            .setMessage("Please select date from and to")
                            .show();
                } else {
                    Intent I = new Intent(ChartActivity.this, SendReportActivity.class);
                    I.putExtra("SelectedDate", " BETWEEN '" + dateFrom.getText().toString().trim() + "' AND '" +
                            dateTo.getText().toString().trim() + "'");
                    startActivity(I);
                }
            }
        });
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog spFrom = new DatePickerDialog(ChartActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        int month = selectedMonth + 1;
                        if (month < 10 && selectedDay < 10)
                            dateFrom.setText(selectedYear + "-0" + month + "-0" + selectedDay);
                        else if (month < 10)
                            dateFrom.setText(selectedYear + "-0" + month + "-" + selectedDay);
                        else if (selectedDay < 10)
                            dateFrom.setText(selectedYear + "-" + month + "-0" + selectedDay);
                        else
                            dateFrom.setText(selectedYear + "-" + month + "-" + selectedDay);
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                    }
                }, yy, mm, dd);
                spFrom.show();
            }
        });
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog spFrom = new DatePickerDialog(ChartActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        int month = selectedMonth + 1;
                        if (month < 10 && selectedDay < 10)
                            dateTo.setText(selectedYear + "-0" + month + "-0" + selectedDay);
                        else if (month < 10)
                            dateTo.setText(selectedYear + "-0" + month + "-" + selectedDay);
                        else if (selectedDay < 10)
                            dateTo.setText(selectedYear + "-" + month + "-0" + selectedDay);
                        else
                            dateTo.setText(selectedYear + "-" + month + "-" + selectedDay);
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                    }
                }, yy, mm, dd);
                spFrom.show();
            }
        });
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog spFrom = new DatePickerDialog(ChartActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        int month = selectedMonth + 1;
                        if (month < 10 && selectedDay < 10)
                            dateTo.setText(selectedYear + "-0" + month + "-0" + selectedDay);
                        else if (month < 10)
                            dateTo.setText(selectedYear + "-0" + month + "-" + selectedDay);
                        else if (selectedDay < 10)
                            dateTo.setText(selectedYear + "-" + month + "-0" + selectedDay);
                        else
                            dateTo.setText(selectedYear + "-" + month + "-" + selectedDay);
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                    }
                }, yy, mm, dd);
                spFrom.show();
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            Intent intent_hom = new Intent(ChartActivity.this, MainActivity.class);
                            startActivity(intent_hom);
                            break;
                        case R.id.nav_search:
                            Intent intent_sear = new Intent(ChartActivity.this, SearchActivity.class);
                            startActivity(intent_sear);
                            break;
                        case R.id.nav_chart:
                            break;
                    }
                    return true;
                }
            };

}
