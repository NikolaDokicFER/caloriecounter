package hr.fer.caloriecounter.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@NoArgsConstructor
public class HomeFragment extends Fragment {
    private Bundle bundle;
    private UserDetail user;
    private LocalDate currentDate;
    private List<Meal> mealsToday;
    private TextView caloriesLeft;
    private TextView caloriesRecommended;
    private TextView caloriesConsumed;
    private TextView dateView;
    private TextView breakfastCalories ;
    private TextView lunchCalories ;
    private TextView dinnerCalories ;
    private ImageButton addBreakfastBtn;
    private ImageButton addLunchBtn;
    private ImageButton addDinnerBtn;
    private Button foodBreakfastBtn;
    private Button foodLunchBtn;
    private Button foodDinnerBtn;
    private int caloriesConsumedDay;
    private int caloriesConsumedBreakfast;
    private int caloriesConsumedLunch;
    private int caloriesConsumedDinner;
    private SearchableSpinner spinnerAllFood;
    private SearchableSpinner spinnerConsumedFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        user = (UserDetail) bundle.getSerializable("user");
        currentDate = LocalDate.now();

        caloriesConsumedDay = 0;
        caloriesConsumedBreakfast = 0;
        caloriesConsumedLunch = 0;
        caloriesConsumedDinner = 0;
        mealsToday = new ArrayList<>();

        mealsRetrofit();
    }


    private void initUI(){
        dateView = getView().findViewById(R.id.home_date);
        dateView.setText(currentDate.toString());

        caloriesLeft = getView().findViewById(R.id.daily_calories);
        caloriesLeft.setText(String.valueOf(user.getCaloriesNeeded() - caloriesConsumedDay));

        caloriesRecommended = getView().findViewById(R.id.kcal_recommended_value);
        caloriesRecommended.setText(String.valueOf(user.getCaloriesNeeded()));

        caloriesConsumed = getView().findViewById(R.id.kcal_consumed_value);
        caloriesConsumed.setText(String.valueOf(caloriesConsumedDay));

        breakfastCalories = getView().findViewById(R.id.home_breakfast_text);
        breakfastCalories.setText("  BREAKFAST\n  consumed calories: " + caloriesConsumedBreakfast);

        lunchCalories = getView().findViewById(R.id.home_lunch_text);
        lunchCalories.setText("  LUNCH\n  consumed calories: " + caloriesConsumedLunch);

        dinnerCalories = getView().findViewById(R.id.home_dinner_text);
        dinnerCalories.setText("  DINNER\n  consumed calories: " + caloriesConsumedDinner);

        addBreakfastBtn = getView().findViewById(R.id.home_breakfast_button);
        addLunchBtn = getView().findViewById(R.id.home_lunch_button);
        addDinnerBtn = getView().findViewById(R.id.home_dinner_button);

        foodBreakfastBtn = getView().findViewById(R.id.home_consumed_breakfast);
        foodLunchBtn = getView().findViewById(R.id.home_consumed_lunch);
        foodDinnerBtn = getView().findViewById(R.id.home_consumed_dinner);

        spinnerAllFood = new SearchableSpinner(getContext());
        spinnerAllFood.setWindowTitle("Select a food to add");

        spinnerConsumedFood = new SearchableSpinner(getContext());
        spinnerConsumedFood.setSearchViewVisibility(SearchableSpinner.SpinnerView.GONE);
        spinnerConsumedFood.setWindowTitle("Select a meal to remove");
    }

    private void initListeners(){
        addBreakfastBtn.setOnClickListener(view -> {
            spinnerAllFood.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                    Intent switchActivity = new Intent(getContext(), FoodDetailActivity.class);
                    switchActivity.putExtra("foodName", selectedString);
                    switchActivity.putExtra("user", user);
                    switchActivity.putExtra("date", String.valueOf(currentDate));
                    switchActivity.putExtra("type", "BREAKFAST");
                    startActivity(switchActivity);
                }
            });
            spinnerAllFood.show();
        });

        addLunchBtn.setOnClickListener(view -> {
            spinnerAllFood.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                    Intent switchActivity = new Intent(getContext(), FoodDetailActivity.class);
                    switchActivity.putExtra("foodName", selectedString);
                    switchActivity.putExtra("user", user);
                    switchActivity.putExtra("date", String.valueOf(currentDate));
                    switchActivity.putExtra("type", "LUNCH");
                    startActivity(switchActivity);
                }
            });
            spinnerAllFood.show();
        });

        addDinnerBtn.setOnClickListener(view -> {
            spinnerAllFood.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                    Intent switchActivity = new Intent(getContext(), FoodDetailActivity.class);
                    switchActivity.putExtra("foodName", selectedString);
                    switchActivity.putExtra("user", user);
                    switchActivity.putExtra("date", String.valueOf(currentDate));
                    switchActivity.putExtra("type", "DINNER");
                    startActivity(switchActivity);
                }
            });
            spinnerAllFood.show();
        });

        foodBreakfastBtn.setOnClickListener(view ->{
            spinnerConsumedFood.setSpinnerListItems(new ArrayList<>(mealsToday.stream().filter(m -> m.getType() == MealType.BREAKFAST).map(m -> m.getFood().getName() + " (" + m.getQuantity() * m.getFood().getCalories() + ")" ).collect(Collectors.toList())));

            spinnerConsumedFood.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public void setOnItemSelectListener(int i, @NotNull String s) {
                    Meal meal = mealsToday.stream().filter(m -> m.getType() == MealType.BREAKFAST).collect(Collectors.toList()).get(i);
                    confirmDelete(meal.getId(), meal.getFood().getName());
                }
            });

            spinnerConsumedFood.show();
        });

        foodLunchBtn.setOnClickListener(view ->{
            spinnerConsumedFood.setSpinnerListItems(new ArrayList<>(mealsToday.stream().filter(m -> m.getType() == MealType.LUNCH).map(m -> m.getFood().getName() + " (" + m.getQuantity() * m.getFood().getCalories() + ")" ).collect(Collectors.toList())));

            spinnerConsumedFood.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public void setOnItemSelectListener(int i, @NotNull String s) {
                    Meal meal = mealsToday.stream().filter(m -> m.getType() == MealType.LUNCH).collect(Collectors.toList()).get(i);
                    confirmDelete(meal.getId(), meal.getFood().getName());
                }
            });

            spinnerConsumedFood.show();
        });

        foodDinnerBtn.setOnClickListener(view ->{
            spinnerConsumedFood.setSpinnerListItems(new ArrayList<>(mealsToday.stream().filter(m -> m.getType() == MealType.DINNER).map(m -> m.getFood().getName() + " (" + m.getQuantity() * m.getFood().getCalories() + ")" ).collect(Collectors.toList())));

            spinnerConsumedFood.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public void setOnItemSelectListener(int i, @NotNull String s) {
                    Meal meal = mealsToday.stream().filter(m -> m.getType() == MealType.DINNER).collect(Collectors.toList()).get(i);
                    confirmDelete(meal.getId(), meal.getFood().getName());
                }
            });

            spinnerConsumedFood.show();
        });
    }

    private void mealsRetrofit(){
        Retrofit retrofit = NetworkClient.retrofit();

        MealApi mealApi = retrofit.create(MealApi.class);
        Call<List<Meal>> call = mealApi.getMeals(user.getId(), currentDate.toString());

        call.enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
               if(response.code() == 200){
                   mealsToday = response.body();
                   countConsumedCalories();
                   initUI();
                   foodRetrofit();
               }else{
                   System.out.println(response.code());
               }
            }

            @Override
            public void onFailure(Call<List<Meal>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void deleteMealRetrofit(Long mealId){
        Retrofit retrofit = NetworkClient.retrofit();

        MealApi mealApi = retrofit.create(MealApi.class);
        Call<Void> call = mealApi.deleteMeal(mealId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(getContext(), "Meal has been removed", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }else{
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void foodRetrofit(){
        Retrofit retrofit = NetworkClient.retrofit();

        FoodApi foodApi = retrofit.create(FoodApi.class);
        Call<List<Food>> call = foodApi.getAllFood();

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if(response.code() == 200){
                    spinnerAllFood.setSpinnerListItems(new ArrayList<>(response.body().stream().map(Food::getName).collect(Collectors.toList())));
                    initListeners();
                }else{
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void confirmDelete(Long mealId, String foodName){
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Remove a meal")
                .setMessage("Do you want to remove this meal: " + foodName)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteMealRetrofit(mealId);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
        dialog.show();
    }

    private void countConsumedCalories(){
        for (Meal m: mealsToday) {
            caloriesConsumedDay += m.getFood().getCalories() * m.getQuantity();
            switch (m.getType()){
                case BREAKFAST: caloriesConsumedBreakfast += m.getFood().getCalories() * m.getQuantity(); break;
                case LUNCH: caloriesConsumedLunch += m.getFood().getCalories() * m.getQuantity(); break;
                case DINNER:caloriesConsumedDinner += m.getFood().getCalories() * m.getQuantity(); break;
            }
        }
    }
}

