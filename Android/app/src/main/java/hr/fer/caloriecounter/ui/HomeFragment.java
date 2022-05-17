package hr.fer.caloriecounter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.MealApi;
import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.UserDetail;
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
    private int caloriesConsumedInt = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        user = (UserDetail) bundle.getSerializable("user");
        currentDate = LocalDate.of(2020, 05, 05);

        mealsRetrofit();
    }

    private void initUI(){
        System.out.println(caloriesConsumedInt);
        dateView = getView().findViewById(R.id.home_date);
        dateView.setText(currentDate.toString());

        caloriesLeft = getView().findViewById(R.id.daily_calories);
        caloriesLeft.setText(String.valueOf(user.getCaloriesNeeded() - caloriesConsumedInt));

        caloriesRecommended = getView().findViewById(R.id.kcal_recommended_value);
        caloriesRecommended.setText(String.valueOf(user.getCaloriesNeeded()));

        caloriesConsumed = getView().findViewById(R.id.kcal_consumed_value);
        caloriesConsumed.setText(String.valueOf(caloriesConsumedInt));
    }

    private void mealsRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MealApi mealApi = retrofit.create(MealApi.class);
        Call<List<Meal>> call = mealApi.getMeals(user.getId(), currentDate.toString());

        call.enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
                mealsToday = response.body();
                countConsumedCalories();
                initUI();
            }

            @Override
            public void onFailure(Call<List<Meal>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void countConsumedCalories(){
        for (Meal m: mealsToday) {
            caloriesConsumedInt += m.getFood().getCalories();
        }
    }
}
