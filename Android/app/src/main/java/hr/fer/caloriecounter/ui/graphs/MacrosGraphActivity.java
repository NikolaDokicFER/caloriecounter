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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MacrosGraphActivity extends AppCompatActivity {
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
        text.setText("How you consume your macronutrients");

        pieChart = findViewById(R.id.macros_pie_chart);

        int proteins = 0;
        int fat = 0;
        int carbs = 0;
        for (Meal m : meals) {
            proteins += m.getFood().getProteins() * m.getQuantity();
            fat += m.getFood().getFat() * m.getQuantity();
            carbs += m.getFood().getCarbohydrates() * m.getQuantity();
        }

        ArrayList<PieEntry> visitors = new ArrayList<>();
        visitors.add(new PieEntry((float) proteins / (proteins + fat + carbs), "Protein"));
        visitors.add(new PieEntry((float) fat / (proteins + fat + carbs), "Fat"));
        visitors.add(new PieEntry((float) carbs / (proteins + fat + carbs), "Carbs"));

        PieDataSet pieDataSet = new PieDataSet(visitors, "Macros");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(20f);
        pieData.setValueTextColor(Color.BLACK);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Macros");
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
