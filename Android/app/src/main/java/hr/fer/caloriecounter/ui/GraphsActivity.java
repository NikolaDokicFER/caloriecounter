package hr.fer.caloriecounter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import hr.fer.caloriecounter.R;

import hr.fer.caloriecounter.model.UserDetail;
import hr.fer.caloriecounter.ui.graphs.MacrosGraphActivity;
import hr.fer.caloriecounter.ui.graphs.MealTypeGraphActivity;
import hr.fer.caloriecounter.ui.graphs.MostConsumedGraphActivity;
import hr.fer.caloriecounter.ui.graphs.WeightGraphActivity;

public class GraphsActivity extends AppCompatActivity {
    private UserDetail user;
    private Button graphButton1;
    private Button graphButton2;
    private Button graphButton3;
    private Button graphButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        user = (UserDetail) getIntent().getSerializableExtra("user");

        getSupportActionBar().hide();

        initListeners();
    }

    private void initListeners() {
        graphButton1 = findViewById(R.id.graphs_btn1);
        graphButton2 = findViewById(R.id.graphs_btn2);
        graphButton3 = findViewById(R.id.graphs_btn3);
        graphButton4 = findViewById(R.id.graphs_btn4);

        graphButton1.setOnClickListener(view -> {
            Intent switchActivity = new Intent(this, MostConsumedGraphActivity.class);
            switchActivity.putExtra("user", user);
            startActivity(switchActivity);
        });

        graphButton2.setOnClickListener(view -> {
            Intent switchActivity = new Intent(this, MacrosGraphActivity.class);
            switchActivity.putExtra("user", user);
            startActivity(switchActivity);
        });

        graphButton3.setOnClickListener(view -> {
            Intent switchActivity = new Intent(this, MealTypeGraphActivity.class);
            switchActivity.putExtra("user", user);
            startActivity(switchActivity);
        });

        graphButton4.setOnClickListener(view -> {
            Intent switchActivity = new Intent(this, WeightGraphActivity.class);
            switchActivity.putExtra("user", user);
            startActivity(switchActivity);
        });
    }
}
