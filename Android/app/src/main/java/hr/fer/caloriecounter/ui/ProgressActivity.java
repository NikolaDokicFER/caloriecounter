package hr.fer.caloriecounter.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.model.UserDetail;

public class ProgressActivity extends AppCompatActivity {
    private UserDetail user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        user = (UserDetail) getIntent().getSerializableExtra("user");

        getSupportActionBar().hide();
    }
}
