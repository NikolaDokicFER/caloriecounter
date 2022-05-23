package hr.fer.caloriecounter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.FoodApi;
import hr.fer.caloriecounter.api.MealApi;
import hr.fer.caloriecounter.model.Food;
import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.UserDetail;
import hr.fer.caloriecounter.model.enums.MealType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodDetailActivity extends AppCompatActivity {
    private Meal meal;
    private Food food;
    private UserDetail user;
    private String foodName;
    private String date;
    private String type;
    private TextView foodNameText;
    private TextView caloriesText;
    private TextView carbsText;
    private TextView fatText;
    private TextView proteinText;
    private TextView vitaminAText;
    private TextView vitaminCText;
    private TextView iodineText;
    private TextView saltText;
    private TextView calciumText;
    private TextView ironText;
    private EditText quantity;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        foodName = getIntent().getStringExtra("foodName");
        user = (UserDetail) getIntent().getSerializableExtra("user");
        date = getIntent().getStringExtra("date");
        type = getIntent().getStringExtra("type");

        getFoodRetrofit();
    }

    private void initUI() {
        foodNameText = findViewById(R.id.food_food_name);
        foodNameText.setText(food.getName().toUpperCase());

        caloriesText = findViewById(R.id.food_calories);
        caloriesText.setText("Calories: " + food.getCalories());

        carbsText = findViewById(R.id.food_carbs);
        carbsText.setText("Carbs:\n" + food.getCarbohydrates());

        fatText = findViewById(R.id.food_fat);
        fatText.setText("Fat\n" + food.getFat());

        proteinText = findViewById(R.id.food_protein);
        proteinText.setText("Protein\n" + food.getProteins());

        vitaminAText = findViewById(R.id.food_vitamin_a);
        vitaminAText.setText(String.valueOf(food.getVitaminA()));

        vitaminCText = findViewById(R.id.food_vitamin_c);
        vitaminCText.setText(String.valueOf(food.getVitaminA()));

        iodineText = findViewById(R.id.food_iodine);
        iodineText.setText(String.valueOf(food.getIodine()));

        saltText = findViewById(R.id.food_salt);
        saltText.setText(String.valueOf(food.getSalt()));

        calciumText = findViewById(R.id.food_calcium);
        calciumText.setText(String.valueOf(food.getCalcium()));

        ironText = findViewById(R.id.food_iron);
        ironText.setText(String.valueOf(food.getIron()));

        quantity = findViewById(R.id.food_quantity);
        addButton = findViewById(R.id.food_add_btn);
    }

    private void initListeners() {
        addButton.setOnClickListener(view -> {
            meal = new Meal();
            meal.setFood(food);
            meal.setDate(date);
            meal.setUser(user);
            meal.setQuantity(Float.parseFloat(quantity.getText().toString()) * 0.01f);
            switch (type){
                case "BREAKFAST": meal.setType(MealType.BREAKFAST); break;
                case "LUNCH": meal.setType(MealType.LUNCH); break;
                case "DINNER": meal.setType(MealType.DINNER); break;
            }
            addMealRetrofit();
        });
    }

    private void getFoodRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodApi foodApi = retrofit.create(FoodApi.class);
        Call<Food> call = foodApi.getFood(foodName);

        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.code() == 200) {
                    food = response.body();
                    if (food.getVitaminA() == null) food.setVitaminA(0.0f);
                    if (food.getVitaminC() == null) food.setVitaminC(0.0f);
                    if (food.getIodine() == null) food.setIodine(0.0f);
                    if (food.getSalt() == null) food.setSalt(0.0f);
                    if (food.getCalcium() == null) food.setCalcium(0.0f);
                    if (food.getIron() == null) food.setIron(0.0f);
                    initUI();
                    initListeners();
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void addMealRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MealApi mealApi = retrofit.create(MealApi.class);
        Call<Meal> call = mealApi.saveMeal(meal);

        call.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if(response.code() == 200){
                   switchToHomeActivity();
                }else{
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void switchToHomeActivity(){
        Intent switchActivity = new Intent(this, HomeActivity.class);
        switchActivity.putExtra("user", user);
        startActivity(switchActivity);
    }
}
