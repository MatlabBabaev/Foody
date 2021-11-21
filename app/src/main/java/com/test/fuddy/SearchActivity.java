package com.test.fuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {


    private Dialog dialogDetails;

    private RecyclerView recyclerViewNotes;
    private static final ArrayList<Food> foods = new ArrayList<>();
    private FoodAdapter adapter;
    private FoodDBHelper dbHelper;
    private SQLiteDatabase database;
    Calendar mCurrentDate;

    private EditText editTextDate;

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        dialogDetails = new Dialog(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        recyclerViewNotes = findViewById(R.id.recylerViewNotes);
        dbHelper = new FoodDBHelper(this);
        database = dbHelper.getWritableDatabase();
        editTextDate = findViewById(R.id.editTextDate);
        mCurrentDate = Calendar.getInstance();
        editTextDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        getData(editTextDate.getText().toString());
        adapter = new FoodAdapter(foods);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(adapter);
    }

    private void getData(String day) {
        foods.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM " + FoodDBHelper.TABLE_NAME + " WHERE " +
                FoodDBHelper.COLUMN_DATE + " = '" + day + "' ORDER BY " + FoodDBHelper.COLUMN_ID, null);
        //Cursor cursor = database.query(FoodDBHelper.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Date date = null;
            try {
                date = sdf.parse(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COLUMN_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int id = cursor.getInt(cursor.getColumnIndex(FoodDBHelper.COLUMN_ID));
            String mealTime = cursor.getString(cursor.getColumnIndex(FoodDBHelper.COLUMN_MEAL_TIME));
            String meal = cursor.getString(cursor.getColumnIndex(FoodDBHelper.COLUMN_MEAL));
            Double calorie = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COLUMN_CALORIE));
            Double fat = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COLUMN_FAT));
            Double carbohydrate = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COLUMN_CARBOHYDRATE));
            Double protein = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COLUMN_PROTEIN));

            Food food = new Food(id, date, mealTime, meal, calorie, fat, carbohydrate, protein);
            foods.add(food);
        }
        cursor.close();


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog mDatePicker = new DatePickerDialog(SearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        int month = selectedMonth + 1;
                        if (month < 10 && selectedDay < 10)
                            editTextDate.setText(selectedYear + "-0" + month + "-0" + selectedDay);
                        else if (month < 10)
                            editTextDate.setText(selectedYear + "-0" + month + "-" + selectedDay);
                        else if (selectedDay < 10)
                            editTextDate.setText(selectedYear + "-" + month + "-0" + selectedDay);
                        else
                            editTextDate.setText(selectedYear + "-" + month + "-" + selectedDay);
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);

                        getData(editTextDate.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                }, year, month, day);
                mDatePicker.show();
            }
        });
    }

    public void showPopup(View view) {
        TextView txtClose;
        Button btnFollow;
        dialogDetails.setContentView(R.layout.popup_window);
        txtClose = (TextView) dialogDetails.findViewById(R.id.txClose);
        btnFollow = (Button) dialogDetails.findViewById(R.id.btnReport);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDetails.dismiss();
            }
        });
        dialogDetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDetails.show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            Intent intent_hom = new Intent(SearchActivity.this, MainActivity.class);
                            startActivity(intent_hom);
                            break;
                        case R.id.nav_search:
                            break;
                        case R.id.nav_chart:
                            Intent intent_char = new Intent(SearchActivity.this, ChartActivity.class);
                            startActivity(intent_char);
                            break;
                    }
                    return true;
                }
            };
}
