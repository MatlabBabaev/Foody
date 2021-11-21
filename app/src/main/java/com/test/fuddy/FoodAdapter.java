package com.test.fuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private ArrayList<Food> foods;
    private OnNoteClickListener onNoteClickListener;

    public FoodAdapter(ArrayList<Food> foods) {
        this.foods = foods;
    }

    interface OnNoteClickListener {
        void onNoteClick(int position);

        void onNoteLongClick(int position);
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewMealTime;
        private TextView textViewMeal;
        private TextView textViewCalorie, textViewCarbohydrate, textViewProtein;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewMealTime = itemView.findViewById(R.id.textViewMealTime);
            textViewMeal = itemView.findViewById(R.id.textViewMeal);
            textViewCalorie = itemView.findViewById(R.id.textViewCalorie);
            textViewCarbohydrate = itemView.findViewById(R.id.textCarbohydrate);
            textViewProtein = itemView.findViewById(R.id.textProtein);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNoteClickListener != null) {
                        onNoteClickListener.onNoteClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onNoteClickListener != null) {
                        onNoteClickListener.onNoteLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new FoodViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foods.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        holder.textViewDate.setText(String.format("%s", sdf.format(food.getDate())));
        holder.textViewMealTime.setText(food.getMealTime());
        holder.textViewMeal.setText(food.getMeal());
        holder.textViewCalorie.setText(String.format("cal: %s", food.getCalorie()));
        holder.textViewCarbohydrate.setText(String.format("carb: %s", food.getCarbohydrate()));
        holder.textViewProtein.setText(String.format("prot: %s", food.getProtein()));

    }

    @Override
    public int getItemCount() {

        return foods.size();
    }

}
