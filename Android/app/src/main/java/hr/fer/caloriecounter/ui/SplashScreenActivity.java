package hr.fer.caloriecounter.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.model.UserDetail;

public class SplashScreenActivity extends AppCompatActivity {
    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loggedIn) {
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("User", "");
                    switchToHome(gson.fromJson(json, UserDetail.class));
                } else {
                    switchToLogin();
                }
            }
        }, 1000);

        getSupportActionBar().hide();
    }

    private void switchToHome(UserDetail user) {
        Intent switchActivity = new Intent(this, HomeActivity.class);
        switchActivity.putExtra("user", user);
        startActivity(switchActivity);
    }

    private void switchToLogin() {
        Intent switchActivity = new Intent(this, MainActivity.class);
        startActivity(switchActivity);
    }
}
