package hr.fer.caloriecounter.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leo.searchablespinner.SearchableSpinner;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.sqlite.CalorieCounterPlanDbHelper;

public class RecyclerAdapterPlan extends RecyclerView.Adapter<RecyclerAdapterPlan.RecyclerViewHolder> {
    private Context ctx;
    private List<String> daysList = List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY");
    private SearchableSpinner spinnerAllFood;
    private SearchableSpinner spinnerPlannedFood;
    private CalorieCounterPlanDbHelper dbHelper;

    public RecyclerAdapterPlan(Context ctx) {
        this.ctx = ctx;

        dbHelper = new CalorieCounterPlanDbHelper(ctx);
    }

    public void setFood(ArrayList<String> foodList) {
        spinnerAllFood = new SearchableSpinner(ctx);
        spinnerAllFood.setSpinnerListItems(foodList);
        spinnerAllFood.setWindowTitle("Select consumed food");
        spinnerAllFood.setShowKeyboardByDefault(false);


        spinnerPlannedFood = new SearchableSpinner(ctx);
        spinnerPlannedFood.setSearchViewVisibility(SearchableSpinner.SpinnerView.GONE);
        spinnerPlannedFood.setWindowTitle("Select a meal to remove");
        spinnerPlannedFood.setShowKeyboardByDefault(false);
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerAdapterPlan.RecyclerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.plan_row, parent, false);

        return new RecyclerAdapterPlan.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerAdapterPlan.RecyclerViewHolder holder, int position) {
        holder.dayText.setText(daysList.get(position));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if(dayOfTheWeek.toLowerCase().equals(daysList.get(position).toLowerCase())) holder.dayText.setTextColor(Color.parseColor("#4fe37a"));

        holder.breakfastAddButton.setOnClickListener(view -> {
            spinnerAllFood.setOnItemSelectListener((positionSpinner, selectedString) -> {
                dbHelper.insert(daysList.get(position), selectedString, "BREAKFAST");
            });
            spinnerAllFood.show();
        });

        holder.lunchAddButton.setOnClickListener(view -> {
            spinnerAllFood.setOnItemSelectListener((positionSpinner, selectedString) -> {
                dbHelper.insert(daysList.get(position), selectedString, "LUNCH");
            });
            spinnerAllFood.show();
        });

        holder.lunchAddButton.setOnClickListener(view -> {
            spinnerAllFood.setOnItemSelectListener((positionSpinner, selectedString) -> {
                dbHelper.insert(daysList.get(position), selectedString, "DINNER");
            });
            spinnerAllFood.show();
        });

        holder.breakfastButton.setOnClickListener(view -> {
            ArrayList<String> planedMeals = new ArrayList<>();
            Cursor cursor = dbHelper.fetchEntries(daysList.get(position), "BREAKFAST");

            while (cursor.moveToNext()) {
                if (cursor.getString(3).contains("♥︎"))
                    planedMeals.add(cursor.getString(3).substring(0, cursor.getString(3).length() - 2));
                else
                    planedMeals.add(cursor.getString(3));
            }
            cursor.close();

            spinnerPlannedFood.setSpinnerListItems(planedMeals);

            spinnerPlannedFood.setOnItemSelectListener((positionSpinner, selectedString) -> {
                confirmDelete(selectedString, daysList.get(position), "BREAKFAST");
            });
            spinnerPlannedFood.show();
        });

        holder.lunchButton.setOnClickListener(view -> {
            ArrayList<String> planedMeals = new ArrayList<>();
            Cursor cursor = dbHelper.fetchEntries(daysList.get(position), "LUNCH");

            while (cursor.moveToNext()) {
                if (cursor.getString(3).contains("♥︎"))
                    planedMeals.add(cursor.getString(3).substring(0, cursor.getString(3).length() - 2));
                else
                    planedMeals.add(cursor.getString(3));
            }
            cursor.close();

            spinnerPlannedFood.setSpinnerListItems(planedMeals);

            spinnerPlannedFood.setOnItemSelectListener((positionSpinner, selectedString) -> {
                confirmDelete(selectedString, daysList.get(position), "LUNCH");
            });
            spinnerPlannedFood.show();
        });

        holder.dinnerButton.setOnClickListener(view -> {
            ArrayList<String> planedMeals = new ArrayList<>();
            Cursor cursor = dbHelper.fetchEntries(daysList.get(position), "DINNER");

            while (cursor.moveToNext()) {
                if (cursor.getString(3).contains("♥︎"))
                    planedMeals.add(cursor.getString(3).substring(0, cursor.getString(3).length() - 2));
                else
                    planedMeals.add(cursor.getString(3));
            }
            cursor.close();

            spinnerPlannedFood.setSpinnerListItems(planedMeals);

            spinnerPlannedFood.setOnItemSelectListener((positionSpinner, selectedString) -> {
                confirmDelete(selectedString, daysList.get(position), "DINNER");
            });
            spinnerPlannedFood.show();
        });
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    private void confirmDelete(String food, String day, String type) {
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("Remove a meal")
                .setMessage("Do you want to remove this meal: " + food)
                .setPositiveButton("YES", (dialogInterface, i) ->dbHelper.deleteEntry(food, day, type)).setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel()).create();
        dialog.show();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;
        ImageButton breakfastAddButton, lunchAddButton, dinnerAddButton;
        Button breakfastButton, lunchButton, dinnerButton;

        public RecyclerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.plan_day_text);
            breakfastAddButton = itemView.findViewById(R.id.plan_breakfast_add);
            lunchAddButton = itemView.findViewById(R.id.plan_lunch_add);
            dinnerAddButton = itemView.findViewById(R.id.plan_dinner_add);
            breakfastButton = itemView.findViewById(R.id.plan_breakfast_btn);
            lunchButton = itemView.findViewById(R.id.plan_lunch_btn);
            dinnerButton = itemView.findViewById(R.id.plan_dinner_btn);
        }
    }
}
