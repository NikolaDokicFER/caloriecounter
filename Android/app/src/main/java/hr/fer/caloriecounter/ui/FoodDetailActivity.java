package hr.fer.caloriecounter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.FoodApi;
import hr.fer.caloriecounter.api.MealApi;
import hr.fer.caloriecounter.model.Food;
import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.UserDetail;
import hr.fer.caloriecounter.model.enums.MealType;
import hr.fer.caloriecounter.sqlite.CalorieCounterFoodDbHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    private TextView cholesterolText;
    private TextView saltText;
    private TextView calciumText;
    private TextView ironText;
    private EditText quantity;
    private Button addButton;
    private ImageButton favoriteButton;
    private boolean favorite = false;
    private CalorieCounterFoodDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        foodName = getIntent().getStringExtra("foodName");
        user = (UserDetail) getIntent().getSerializableExtra("user");
        date = getIntent().getStringExtra("date");
        type = getIntent().getStringExtra("type");

        dbHelper = new CalorieCounterFoodDbHelper(this);
        Cursor cursor = dbHelper.fetchEntry(foodName);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            favorite = true;
            food = new Food();
            food.setId(Long.parseLong(cursor.getString(0)));
            food.setName(cursor.getString(1));
            food.setCalories(Integer.parseInt(cursor.getString(2)));
            food.setFat(Float.parseFloat(cursor.getString(3)));
            food.setProteins(Float.parseFloat(cursor.getString(4)));
            food.setCarbohydrates(Float.parseFloat(cursor.getString(5)));
            food.setVitaminA(Float.parseFloat(cursor.getString(6)));
            food.setVitaminC(Float.parseFloat(cursor.getString(7)));
            food.setCholesterol(Float.parseFloat(cursor.getString(8)));
            food.setSalt(Float.parseFloat(cursor.getString(9)));
            food.setCalcium(Float.parseFloat(cursor.getString(10)));
            food.setIron(Float.parseFloat(cursor.getString(11)));

            cursor.close();

            initUI();
            initListeners();
        } else {
            getFoodRetrofit();
        }
    }

    @Override
    public void onBackPressed() {
        switchToHomeActivity();
    }

    private void initUI() {
        foodNameText = findViewById(R.id.food_food_name);
        foodNameText.setText(food.getName().toUpperCase());

        caloriesText = findViewById(R.id.food_calories);
        caloriesText.setText("Calories: " + food.getCalories());

        carbsText = findViewById(R.id.food_carbs);
        carbsText.setText("Carbs:\n  " + food.getCarbohydrates());

        fatText = findViewById(R.id.food_fat);
        fatText.setText("Fat\n" + food.getFat());

        proteinText = findViewById(R.id.food_protein);
        proteinText.setText("Protein\n   " + food.getProteins());

        vitaminAText = findViewById(R.id.food_vitamin_a);
        vitaminAText.setText(String.valueOf(food.getVitaminA()));

        vitaminCText = findViewById(R.id.food_vitamin_c);
        vitaminCText.setText(String.valueOf(food.getVitaminA()));

        cholesterolText = findViewById(R.id.food_cholesterol);
        cholesterolText.setText(String.valueOf(food.getCholesterol()));

        saltText = findViewById(R.id.food_salt);
        saltText.setText(String.valueOf(food.getSalt()));

        calciumText = findViewById(R.id.food_calcium);
        calciumText.setText(String.valueOf(food.getCalcium()));

        ironText = findViewById(R.id.food_iron);
        ironText.setText(String.valueOf(food.getIron()));

        quantity = findViewById(R.id.food_quantity);
        addButton = findViewById(R.id.food_add_btn);
        favoriteButton = findViewById(R.id.food_favorite_button);
        if (favorite) favoriteButton.setImageResource(R.drawable.ic_favorite_full);
        else favoriteButton.setImageResource(R.drawable.ic_favorite_empty);
    }

    private void initListeners() {
        favoriteButton.setOnClickListener(view -> {
            if (favorite) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_empty);
                favorite = false;
                dbHelper.deleteEntry(String.valueOf(food.getId()));
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite_full);
                favorite = true;
                dbHelper.insert(food);
            }
        });

        addButton.setOnClickListener(view -> {
            if (!quantity.getText().toString().equals("") && TextUtils.isDigitsOnly(quantity.getText())) {
                meal = new Meal();
                meal.setFood(food);
                meal.setDate(date);
                meal.setUser(user);
                meal.setQuantity(Float.parseFloat(quantity.getText().toString()) * 0.01f);
                switch (type) {
                    case "BREAKFAST":
                        meal.setType(MealType.BREAKFAST);
                        break;
                    case "LUNCH":
                        meal.setType(MealType.LUNCH);
                        break;
                    case "DINNER":
                        meal.setType(MealType.DINNER);
                        break;
                }
                addMealRetrofit();
            } else {
                Toast.makeText(this, "Please enter qunatity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFoodRetrofit() {
        Retrofit retrofit = NetworkClient.retrofit();

        FoodApi foodApi = retrofit.create(FoodApi.class);
        Call<Food> call = foodApi.getFood(foodName);

        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.code() == 200) {
                    food = response.body();
                    if (food.getVitaminA() == null) food.setVitaminA(0.0f);
                    if (food.getVitaminC() == null) food.setVitaminC(0.0f);
                    if (food.getCholesterol() == null) food.setCholesterol(0.0f);
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
        Retrofit retrofit = NetworkClient.retrofit();

        MealApi mealApi = retrofit.create(MealApi.class);
        Call<Meal> call = mealApi.saveMeal(meal);

        call.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if (response.code() == 200) {
                    switchToHomeActivity();
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void switchToHomeActivity() {
        Intent switchActivity = new Intent(this, HomeActivity.class);
        switchActivity.putExtra("user", user);
        startActivity(switchActivity);
    }
}
