package hr.fer.caloriecounter.ui.graphs;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.MealApi;
import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.UserDetail;
import hr.fer.caloriecounter.model.enums.MealType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MealTypeGraphActivity extends AppCompatActivity {
    private UserDetail user;
    private List<Meal> meals;
    private PieChart pieChart;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_macros);

        user = (UserDetail) getIntent().getSerializableExtra("user");

        getSupportActionBar().hide();

        mealsRetrofit();
    }

    private void initUI() {
        text = findViewById(R.id.graph1_label);
        text.setText("When do you consume your calories");

        pieChart = findViewById(R.id.macros_pie_chart);

        int breakfast = 0;
        int lunch = 0;
        int dinner = 0;
        for (Meal m : meals) {
            if (m.getType() == MealType.BREAKFAST)
                breakfast += m.getFood().getCalories() * m.getQuantity();
            else if (m.getType() == MealType.LUNCH)
                lunch += m.getFood().getCalories() * m.getQuantity();
            else if (m.getType() == MealType.DINNER)
                dinner += m.getFood().getCalories() * m.getQuantity();
        }

        ArrayList<PieEntry> visitors = new ArrayList<>();
        visitors.add(new PieEntry((float) breakfast / (breakfast + lunch + dinner), "Breakfast"));
        visitors.add(new PieEntry((float) lunch / (breakfast + lunch + dinner), "Lunch"));
        visitors.add(new PieEntry((float) dinner / (breakfast + lunch + dinner), "Dinner"));

        PieDataSet pieDataSet = new PieDataSet(visitors, "Calories");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(20f);
        pieData.setValueTextColor(Color.BLACK);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Calories");
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(18f);

        pieChart.animate();
        pieChart.invalidate();
    }

    private void mealsRetrofit() {
        Retrofit retrofit = NetworkClient.retrofit();

        MealApi mealApi = retrofit.create(MealApi.class);
        Call<List<Meal>> call = mealApi.getMealsByUser(user.getId());

        call.enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
                if (response.code() == 200) {
                    meals = response.body();
                    initUI();
                }
            }

            @Override
            public void onFailure(Call<List<Meal>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
