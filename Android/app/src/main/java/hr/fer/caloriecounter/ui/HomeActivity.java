package hr.fer.caloriecounter.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import hr.fer.caloriecounter.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();
    }
}
