package hr.fer.caloriecounter.ui.graphs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.ProgressApi;
import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.model.UserDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WeightGraphActivity extends AppCompatActivity {
    private UserDetail user;
    private List<Progress> progressList;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_weight);

        user = (UserDetail) getIntent().getSerializableExtra("user");

        getSupportActionBar().hide();

        progressRetrofit();
    }

    private void initUI() {
        lineChart = findViewById(R.id.macros_line_chart);

        ArrayList<Entry> visitors = new ArrayList<>();
        ArrayList<String> labelNames = new ArrayList<>();

        int i = 0;

        Collections.sort(this.progressList);
        Collections.reverse(this.progressList);
        for (Progress p : progressList) {
            visitors.add(new Entry(i, p.getWeight()));
            labelNames.add(p.getDate().substring(5));
            i++;
        }

        LineDataSet lineDataSet = new LineDataSet(visitors, "Weight");
        lineDataSet.setColor(Color.rgb(79, 227, 122));
        lineDataSet.setValueTextSize(12f);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);

        LineData lineData = new LineData(iLineDataSets);

        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(labelNames.size());

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        lineChart.animate();
        lineChart.invalidate();
    }

    private void progressRetrofit() {
        Retrofit retrofit = NetworkClient.retrofit();

        ProgressApi progressApiApi = retrofit.create(ProgressApi.class);
        Call<List<Progress>> call = progressApiApi.getProgress(user.getId());

        call.enqueue(new Callback<List<Progress>>() {
            @Override
            public void onResponse(Call<List<Progress>> call, Response<List<Progress>> response) {
                if (response.code() == 200) {
                    progressList = response.body();
                    initUI();
                }
            }

            @Override
            public void onFailure(Call<List<Progress>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
