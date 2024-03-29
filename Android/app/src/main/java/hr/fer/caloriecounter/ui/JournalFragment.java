package hr.fer.caloriecounter.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.leo.searchablespinner.SearchableSpinner;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.FoodApi;
import hr.fer.caloriecounter.api.MealApi;
import hr.fer.caloriecounter.model.Food;
import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.UserDetail;
import hr.fer.caloriecounter.model.enums.MealType;
import hr.fer.caloriecounter.sqlite.CalorieCounterFoodDbHelper;
import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@NoArgsConstructor
public class JournalFragment extends Fragment implements DatePickerFragment.IDateSetListener {
    private View view;
    private Bundle bundle;
    private UserDetail user;
    private LocalDate selectedDate;
    private List<Meal> mealsToday;
    private TextView caloriesConsumed;
    private Button dateView;
    private TextView breakfastCalories;
    private TextView lunchCalories;
    private TextView dinnerCalories;
    private ImageButton addBreakfastBtn;
    private ImageButton addLunchBtn;
    private ImageButton addDinnerBtn;
    private ImageButton forwardDateBtn;
    private ImageButton backDateBtn;
    private Button foodBreakfastBtn;
    private Button foodLunchBtn;
    private Button foodDinnerBtn;
    private int caloriesConsumedDay;
    private int caloriesConsumedBreakfast;
    private int caloriesConsumedLunch;
    private int caloriesConsumedDinner;
    private SearchableSpinner spinnerAllFood;
    private SearchableSpinner spinnerConsumedFood;
    private CalorieCounterFoodDbHelper dbHelper;
    private ArrayList<String> allFoodList;
    private ArrayList<String> faveFoodList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_journal, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        user = (UserDetail) bundle.getSerializable("user");
        selectedDate = LocalDate.now();

        caloriesConsumedDay = 0;
        caloriesConsumedBreakfast = 0;
        caloriesConsumedLunch = 0;
        caloriesConsumedDinner = 0;
        mealsToday = new ArrayList<>();

        dbHelper = new CalorieCounterFoodDbHelper(getContext());
        allFoodList = new ArrayList<>();
        faveFoodList = new ArrayList<>();
        Cursor cursor = dbHelper.fetchEntries();

        while (cursor.moveToNext()) {
            faveFoodList.add(cursor.getString(1) + "♥︎");
        }
        cursor.close();
        mealsRetrofit();
    }

    private void initUI() {
        dateView = view.findViewById(R.id.home_date);
        dateView.setText(selectedDate.toString());

        caloriesConsumed = view.findViewById(R.id.kcal_consumed_value);
        caloriesConsumed.setText(String.valueOf(caloriesConsumedDay));

        breakfastCalories = view.findViewById(R.id.home_breakfast_text);
        breakfastCalories.setText("  BREAKFAST\n  consumed calories: " + caloriesConsumedBreakfast);

        lunchCalories = view.findViewById(R.id.home_lunch_text);
        lunchCalories.setText("  LUNCH\n  consumed calories: " + caloriesConsumedLunch);

        dinnerCalories = view.findViewById(R.id.home_dinner_text);
        dinnerCalories.setText("  DINNER\n  consumed calories: " + caloriesConsumedDinner);

        addBreakfastBtn = view.findViewById(R.id.home_breakfast_button);
        addLunchBtn = view.findViewById(R.id.home_lunch_button);
        addDinnerBtn = view.findViewById(R.id.home_dinner_button);

        forwardDateBtn = view.findViewById(R.id.date_forward);
        backDateBtn = view.findViewById(R.id.date_back);

        foodBreakfastBtn = view.findViewById(R.id.home_consumed_breakfast);
        foodLunchBtn = view.findViewById(R.id.home_consumed_lunch);
        foodDinnerBtn = view.findViewById(R.id.home_consumed_dinner);

        spinnerAllFood = new SearchableSpinner(view.getContext());
        spinnerAllFood.setSpinnerListItems(allFoodList);
        spinnerAllFood.setWindowTitle("Select a food to add");
        spinnerAllFood.setShowKeyboardByDefault(false);

        spinnerConsumedFood = new SearchableSpinner(view.getContext());
        spinnerConsumedFood.setSearchViewVisibility(SearchableSpinner.SpinnerView.GONE);
        spinnerConsumedFood.setWindowTitle("Select a meal to remove");
        spinnerConsumedFood.setShowKeyboardByDefault(false);
    }

    private void initListeners() {
        DatePickerFragment newFragemnt = new DatePickerFragment();
        ((DatePickerFragment) newFragemnt).setIDateSetListener(this);

        dateView.setOnClickListener(view -> newFragemnt.show(getActivity().getSupportFragmentManager(), "datePicker"));

        backDateBtn.setOnClickListener(view -> {
            selectedDate = selectedDate.minusDays(1);
            dateView.setText(String.valueOf(selectedDate));
            caloriesConsumedDay = 0;
            caloriesConsumedBreakfast = 0;
            caloriesConsumedLunch = 0;
            caloriesConsumedDinner = 0;
            mealsToday = new ArrayList<>();
            allFoodList = new ArrayList<>();
            mealsRetrofit();
        });

        forwardDateBtn.setOnClickListener(view -> {
            selectedDate = selectedDate.plusDays(1);
            dateView.setText(String.valueOf(selectedDate));
            caloriesConsumedDay = 0;
            caloriesConsumedBreakfast = 0;
            caloriesConsumedLunch = 0;
            caloriesConsumedDinner = 0;
            mealsToday = new ArrayList<>();
            allFoodList = new ArrayList<>();
            mealsRetrofit();
        });

        addBreakfastBtn.setOnClickListener(view -> {
            spinnerAllFood.setOnItemSelectListener((position, selectedString) -> {
                Intent switchActivity = new Intent(view.getContext(), FoodDetailActivity.class);
                if (selectedString.contains("♥︎"))
                    switchActivity.putExtra("foodName", selectedString.substring(0, selectedString.length() - 2));
                else
                    switchActivity.putExtra("foodName", selectedString);
                switchActivity.putExtra("user", user);
                switchActivity.putExtra("date", String.valueOf(selectedDate));
                switchActivity.putExtra("type", "BREAKFAST");
                startActivity(switchActivity);
            });
            spinnerAllFood.show();
        });

        addLunchBtn.setOnClickListener(view -> {
            spinnerAllFood.setOnItemSelectListener((position, selectedString) -> {
                Intent switchActivity = new Intent(view.getContext(), FoodDetailActivity.class);
                if (selectedString.contains("♥︎"))
                    switchActivity.putExtra("foodName", selectedString.substring(0, selectedString.length() - 2));
                else
                    switchActivity.putExtra("foodName", selectedString);
                switchActivity.putExtra("user", user);
                switchActivity.putExtra("date", String.valueOf(selectedDate));
                switchActivity.putExtra("type", "LUNCH");
                startActivity(switchActivity);
            });
            spinnerAllFood.show();
        });

        addDinnerBtn.setOnClickListener(view -> {
            spinnerAllFood.setOnItemSelectListener((position, selectedString) -> {
                Intent switchActivity = new Intent(view.getContext(), FoodDetailActivity.class);
                if (selectedString.contains("♥︎"))
                    switchActivity.putExtra("foodName", selectedString.substring(0, selectedString.length() - 2));
                else
                    switchActivity.putExtra("foodName", selectedString);
                switchActivity.putExtra("user", user);
                switchActivity.putExtra("date", String.valueOf(selectedDate));
                switchActivity.putExtra("type", "DINNER");
                startActivity(switchActivity);
            });
            spinnerAllFood.show();
        });

        foodBreakfastBtn.setOnClickListener(view -> {
            spinnerConsumedFood.setSpinnerListItems(new ArrayList<>(mealsToday.stream().filter(m -> m.getType() == MealType.BREAKFAST).map(m -> m.getFood().getName() + " (" + (int) (m.getQuantity() * m.getFood().getCalories()) + ")").collect(Collectors.toList())));

            spinnerConsumedFood.setOnItemSelectListener((i, s) -> {
                Meal meal = mealsToday.stream().filter(m -> m.getType() == MealType.BREAKFAST).collect(Collectors.toList()).get(i);
                confirmDelete(meal.getId(), meal.getFood().getName());
            });

            spinnerConsumedFood.show();
        });

        foodLunchBtn.setOnClickListener(view -> {
            spinnerConsumedFood.setSpinnerListItems(new ArrayList<>(mealsToday.stream().filter(m -> m.getType() == MealType.LUNCH).map(m -> m.getFood().getName() + " (" + (int) (m.getQuantity() * m.getFood().getCalories()) + ")").collect(Collectors.toList())));

            spinnerConsumedFood.setOnItemSelectListener((i, s) -> {
                Meal meal = mealsToday.stream().filter(m -> m.getType() == MealType.LUNCH).collect(Collectors.toList()).get(i);
                confirmDelete(meal.getId(), meal.getFood().getName());
            });

            spinnerConsumedFood.show();
        });

        foodDinnerBtn.setOnClickListener(view -> {
            spinnerConsumedFood.setSpinnerListItems(new ArrayList<>(mealsToday.stream().filter(m -> m.getType() == MealType.DINNER).map(m -> m.getFood().getName() + " (" + (int) (m.getQuantity() * m.getFood().getCalories()) + ")").collect(Collectors.toList())));

            spinnerConsumedFood.setOnItemSelectListener((i, s) -> {
                Meal meal = mealsToday.stream().filter(m -> m.getType() == MealType.DINNER).collect(Collectors.toList()).get(i);
                confirmDelete(meal.getId(), meal.getFood().getName());
            });

            spinnerConsumedFood.show();
        });
    }

    private void mealsRetrofit() {
        Retrofit retrofit = NetworkClient.retrofit();

        MealApi mealApi = retrofit.create(MealApi.class);
        Call<List<Meal>> call = mealApi.getMeals(user.getId(), selectedDate.toString());

        call.enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
                if (response.code() == 200) {
                    mealsToday = response.body();
                    countConsumedCalories();
                    initUI();
                    foodRetrofit();
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Meal>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void deleteMealRetrofit(Long mealId) {
        Retrofit retrofit = NetworkClient.retrofit();

        MealApi mealApi = retrofit.create(MealApi.class);
        Call<Void> call = mealApi.deleteMeal(mealId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(view.getContext(), "Meal has been removed", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void foodRetrofit() {
        Retrofit retrofit = NetworkClient.retrofit();

        FoodApi foodApi = retrofit.create(FoodApi.class);
        Call<List<Food>> call = foodApi.getAllFood();

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.code() == 200) {
                    ArrayList<String> helpList = new ArrayList<>(faveFoodList);
                    for (int i = 0; i < helpList.size(); i++)
                        helpList.set(i, helpList.get(i).substring(0, helpList.get(i).length() - 2));
                    allFoodList.addAll(response.body().stream().map(Food::getName).collect(Collectors.toList()));
                    allFoodList.removeAll(helpList);

                    spinnerAllFood.setSpinnerListItems(allFoodList);
                    initListeners();
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void confirmDelete(Long mealId, String foodName) {
        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setTitle("Remove a meal")
                .setMessage("Do you want to remove this meal: " + foodName)
                .setPositiveButton("YES", (dialogInterface, i) -> deleteMealRetrofit(mealId)).setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel()).create();
        dialog.show();
    }

    private void countConsumedCalories() {
        for (Meal m : mealsToday) {
            caloriesConsumedDay += m.getFood().getCalories() * m.getQuantity();
            switch (m.getType()) {
                case BREAKFAST:
                    caloriesConsumedBreakfast += m.getFood().getCalories() * m.getQuantity();
                    break;
                case LUNCH:
                    caloriesConsumedLunch += m.getFood().getCalories() * m.getQuantity();
                    break;
                case DINNER:
                    caloriesConsumedDinner += m.getFood().getCalories() * m.getQuantity();
                    break;
            }
        }
    }

    @Override
    public void processDate(int year, int month, int day) {
        selectedDate = LocalDate.of(year, month + 1, day);
        dateView.setText(String.valueOf(selectedDate));
        caloriesConsumedDay = 0;
        caloriesConsumedBreakfast = 0;
        caloriesConsumedLunch = 0;
        caloriesConsumedDinner = 0;
        mealsToday = new ArrayList<>();
        allFoodList = new ArrayList<>();
        mealsRetrofit();
    }
}
