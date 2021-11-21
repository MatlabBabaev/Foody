package com.test.fuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Dialog dialogDetails;

    private RecyclerView recyclerViewNotes;
    private static final ArrayList<Food> foods = new ArrayList<>();
    private FoodAdapter adapter;
    private FoodDBHelper dbHelper;
    private SQLiteDatabase database;
    private TextView txtDate, txtClose, txtMealTime,
            txtMealContent, txtFat, txtCarbohydrate,
            txtProtein, txtCalorie;

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialogDetails = new Dialog(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //bottomNavigationView.setVisibility(View.INVISIBLE); visible or invisible
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        recyclerViewNotes = findViewById(R.id.recylerViewNotes);
        dbHelper = new FoodDBHelper(this);
        database = dbHelper.getWritableDatabase();
        getData();
        adapter = new FoodAdapter(foods);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(adapter);

        adapter.setOnNoteClickListener(new FoodAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                showOptions(MainActivity.this, position);
            }

            @Override
            public void onNoteLongClick(int position) {
                remove(position);
                Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    remove(viewHolder.getAdapterPosition());
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else
                    editFood(viewHolder.getAdapterPosition(), "edit");
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            break;
                        case R.id.nav_search:
                            Intent intent_fav = new Intent(MainActivity.this, SearchActivity.class);
                            startActivity(intent_fav);
                            break;
                        case R.id.nav_chart:
                            Intent intent_chart = new Intent(MainActivity.this, ChartActivity.class);
                            startActivity(intent_chart);
                            break;
                    }
                    return true;
                }
            };

    private void remove(int position) {
        int id = foods.get(position).getId();
        String[] args = new String[]{Integer.toString(id)};
        database.delete(FoodDBHelper.TABLE_NAME, FoodDBHelper.COLUMN_ID + " = ?", args);
        getData();
        adapter.notifyDataSetChanged();
    }

    private void getData() {
        foods.clear();
        //Cursor cursor = database.query(FoodDBHelper.TABLE_NAME, null, null, null, null, null, FoodDBHelper.COLUMN_DATE);
        Cursor cursor = database.rawQuery("SELECT * FROM " + FoodDBHelper.TABLE_NAME + " ORDER BY " + FoodDBHelper.COLUMN_DATE +
                " DESC, " + FoodDBHelper.COLUMN_ID + " DESC", null);
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
    }

    public void onClickAddFood(View view) {
        editFood(0, "add");
    }

    public void editFood(int position, String action) {

        Intent intent = new Intent(this, AddFoodActivity.class);
        ArrayList<String> parsel = new ArrayList<>();
        if (!foods.isEmpty()) {
            parsel.add(Integer.toString(foods.get(position).getId()));//0
            parsel.add(sdf.format(foods.get(position).getDate()));//1
            parsel.add(foods.get(position).getMealTime());//2
            parsel.add(foods.get(position).getMeal());//3
            parsel.add(Double.toString(foods.get(position).getCalorie()));//4
            parsel.add(Double.toString(foods.get(position).getFat()));//5
            parsel.add(Double.toString(foods.get(position).getCarbohydrate()));//6
            parsel.add(Double.toString(foods.get(position).getProtein()));//7
        }

        intent.putStringArrayListExtra("parcel", parsel);
        // }
        //int id = foods.get(position).getId();;
        //intent.putExtra("id", id);
        intent.putExtra("action", action);
        startActivity(intent);
    }

   /* public void showPopup(View view){
        TextView txtClose;
        Button btnFollow;
        dialogDetails.setContentView(R.layout.popup_window);
        txtClose =(TextView) dialogDetails.findViewById(R.id.txClose);
        btnFollow =(Button) dialogDetails.findViewById(R.id.btnReport);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDetails.dismiss();
            }
        });
        dialogDetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDetails.show();
    }*/

    private PopupWindow showOptions(Context mcon, int position) {
        try {
            LayoutInflater inflater = (LayoutInflater) mcon.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_window, null);


            final PopupWindow optionspu = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            optionspu.setAnimationStyle(R.style.popup_window_animation);
            optionspu.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            optionspu.setFocusable(true);
            optionspu.setOutsideTouchable(true);
            optionspu.update(0, 0, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            optionspu.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
            optionspu.setBackgroundDrawable(new ColorDrawable(
                    Color.TRANSPARENT));

            txtDate = (TextView) layout.findViewById(R.id.txtDay);
            txtClose = (TextView) layout.findViewById(R.id.txClose);
            txtMealTime = (TextView) layout.findViewById(R.id.txtMealTime);
            txtMealContent = (TextView) layout.findViewById(R.id.txtMealContent);
            txtFat = (TextView) layout.findViewById(R.id.txtFat);
            txtCarbohydrate = (TextView) layout.findViewById(R.id.txtCarbohydrate);
            txtProtein = (TextView) layout.findViewById(R.id.txtProtein);
            txtCalorie = (TextView) layout.findViewById(R.id.txtCalorie);

            txtDate.setText(sdf.format(foods.get(position).getDate()));
            txtMealTime.setText(foods.get(position).getMealTime());
            txtMealContent.setText(foods.get(position).getMeal());
            txtFat.setText(Double.toString(foods.get(position).getFat()));
            txtCarbohydrate.setText(Double.toString(foods.get(position).getCarbohydrate()));
            txtProtein.setText(Double.toString(foods.get(position).getProtein()));
            txtCalorie.setText(Double.toString(foods.get(position).getCalorie()));
            txtClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionspu.dismiss();
                }
            });
            return optionspu;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
