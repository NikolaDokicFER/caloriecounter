package hr.fer.caloriecounter.ui.graphs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.MealApi;
import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.UserDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MostConsumedGraphActivity extends AppCompatActivity {
    private UserDetail user;
    private List<Meal> meals;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_consumed_food);

        user = (UserDetail) getIntent().getSerializableExtra("user");

        getSupportActionBar().hide();

        mealsRetrofit();
    }

    private void initUI() {
        barChart = findViewById(R.id.consumed_bar_chart);

        Map<String, Long> countMap = meals.stream().collect(Collectors.groupingBy(m -> m.getFood().getName(), Collectors.counting()));
        Map<String, Long> sortedMap = countMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        ArrayList<BarEntry> visitors = new ArrayList<>();
        ArrayList<String> labelNames = new ArrayList<>();

        int maxVisitors = 5;
        if (sortedMap.size() < 5) maxVisitors = sortedMap.size();

        int i = 0;
        for (Map.Entry<String, Long> entry : sortedMap.entrySet()) {
            if (i == maxVisitors) break;
            visitors.add(new BarEntry(i, entry.getValue()));
            labelNames.add(entry.getKey());
            i++;
        }

        BarDataSet barDataSet = new BarDataSet(visitors, "");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(22f);


        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(labelNames.size());

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);

        barChart.animateY(2000);
        barChart.invalidate();
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
