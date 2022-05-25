package hr.fer.caloriecounter.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.RegisterApi;
import hr.fer.caloriecounter.model.UserDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterCaloriesActivity extends AppCompatActivity {
    private UserDetail user;
    private EditText age;
    private EditText height;
    private EditText weight;
    private RadioGroup groupGender;
    private RadioGroup groupActivity;
    private RadioGroup groupWeight;
    private Button button;
    private boolean updating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_calories);

        user = (UserDetail) getIntent().getSerializableExtra("user");
        updating = getIntent().getBooleanExtra("updating", false);
        getSupportActionBar().hide();

        initUI();
        initListeners();
    }

    private void initUI(){
        age = findViewById(R.id.register_age);
        height = findViewById(R.id.register_height);
        weight = findViewById(R.id.register_weight);
        groupGender = findViewById(R.id.register_radio_gender);
        groupActivity = findViewById(R.id.register_radio_activity);
        groupWeight = findViewById(R.id.register_radio_weight);
        button = findViewById(R.id.register_calories_button);
    }

    private void initListeners(){
        button.setOnClickListener(view ->{
            user.setWeight(Integer.parseInt(weight.getText().toString()));

            int caloriesNeeded = 0;
            caloriesNeeded += Integer.parseInt(height.getText().toString())*6.25;
            caloriesNeeded += Integer.parseInt(weight.getText().toString())*10;
            caloriesNeeded -= Integer.parseInt(age.getText().toString())*5;

            int group1 = groupGender.getCheckedRadioButtonId();
            RadioButton userGender = (RadioButton) findViewById(group1);
            switch (userGender.getText().toString()){
                case "Male": caloriesNeeded += 5; break;
                case "Female": caloriesNeeded -= 161; break;
            }

            int group2 = groupActivity.getCheckedRadioButtonId();
            RadioButton userActivity = (RadioButton) findViewById(group2);
            switch (userActivity.getText().toString()){
                case "Sedentary (no exercise)": caloriesNeeded *= 1.2; break;
                case "Light (1-3 days a week)": caloriesNeeded *= 1.375; break;
                case "Moderate (3-5 days a week)": caloriesNeeded *= 1.55; break;
                case "Active (5-7 days a week)": caloriesNeeded *= 1.725; break;
            }

            int group3 = groupWeight.getCheckedRadioButtonId();
            RadioButton userWeight = (RadioButton) findViewById(group3);
            switch (userWeight.getText().toString()){
                case "+0.25": caloriesNeeded *= 1.1; break;
                case "+0.5": caloriesNeeded *= 1.21; break;
                case "-0.25": caloriesNeeded *= 0.9; break;
                case "-0.5": caloriesNeeded *= 0.79; break;
            }
            user.setCaloriesNeeded(caloriesNeeded);
            if(updating){
                updateRetrofit(user);
            }else{
                registerRetrofit(user);
            }
        });
    }

    private void updateRetrofit(UserDetail user) {
        Retrofit retrofit = NetworkClient.retrofit();

        RegisterApi registerApi = retrofit.create(RegisterApi.class);
        Call<UserDetail> call = registerApi.updateUser(user);

        call.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if(response.code() == 200) {
                    switchToHome(response.body());
                    finish();
                    Toast.makeText(RegisterCaloriesActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterCaloriesActivity.this, "Email address or username already taken", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    private void registerRetrofit(UserDetail user) {
        Retrofit retrofit = NetworkClient.retrofit();

        RegisterApi registerApi = retrofit.create(RegisterApi.class);
        Call<UserDetail> call = registerApi.saveUser(user);

        call.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if(response.code() == 200) {
                    switchToLoginActivity();
                    Toast.makeText(RegisterCaloriesActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterCaloriesActivity.this, "Email address or username already taken", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    private void switchToLoginActivity(){
        Intent switchActivity = new Intent(this, MainActivity.class);
        startActivity(switchActivity);
    }

    private void switchToHome(UserDetail user){
        Intent switchActivity = new Intent(this, HomeActivity.class);
        switchActivity.putExtra("user", user);
        switchActivity.putExtra("update", true);
        startActivity(switchActivity);
    }
}
