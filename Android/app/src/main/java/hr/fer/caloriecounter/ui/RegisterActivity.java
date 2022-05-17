package hr.fer.caloriecounter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.RegisterApi;
import hr.fer.caloriecounter.model.UserDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailText;
    private EditText usernameText;
    private EditText password1Text;
    private EditText password2Text;
    private Button registerButton;
    private UserDetail user;
    private boolean caloriesScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        initUI();
        initListeners();
    }

    private void initUI(){
        firstNameText = findViewById(R.id.register_first_name);
        lastNameText = findViewById(R.id.register_last_name);
        emailText = findViewById(R.id.register_email);
        usernameText = findViewById(R.id.register_username);
        password1Text = findViewById(R.id.register_password1);
        password2Text = findViewById(R.id.register_password2);
        registerButton = findViewById(R.id.register_button);
    }

    private void initListeners(){
        registerButton.setOnClickListener(view -> {
            if(validateUserName(usernameText.getText().toString()) && validateEmail(emailText.getText().toString()) && validatePassword(password1Text.getText().toString(), password2Text.getText().toString())){
                user = new UserDetail();
                user.setFirstName(firstNameText.getText().toString());
                user.setLastName(lastNameText.getText().toString());
                user.setEmail(emailText.getText().toString());
                user.setUsername(usernameText.getText().toString());
                user.setPassword(password1Text.getText().toString());

                calculateUserCalories();
            }
        });
    }

    private void registerRetrofit(UserDetail user) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterApi registerApi = retrofit.create(RegisterApi.class);
        Call<UserDetail> call = registerApi.saveUser(user);

        call.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if(response.code() == 200) {
                    switchToLoginActivity();
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "Email address or username already taken", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    private boolean validateUserName(String userName){
        if(userName.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if(userName.contains(" ")){
            Toast.makeText(RegisterActivity.this, "Username: Remove space between characters", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    private boolean validateEmail(String email){
        if(email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }else{
            Toast.makeText(RegisterActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validatePassword(String password, String password2){
        Pattern pattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])");
        Matcher matcher = pattern.matcher(password);
        if(password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if(password.contains(" ")){
            Toast.makeText(RegisterActivity.this, "Password: Remove space between characters", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!matcher.find()){
            Toast.makeText(RegisterActivity.this, "Password needs to contain at least 1 lowercase letter, uppercase letter and number", Toast.LENGTH_SHORT).show();
            return false;
        }else if(password.length() < 8){
            Toast.makeText(RegisterActivity.this, "Password needs to be at least 8 characters long", Toast.LENGTH_LONG).show();
            return false;
        }else if(!password.equals(password2)){
            Toast.makeText(RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    private void calculateUserCalories(){
        caloriesScreen = true;
        setContentView(R.layout.activity_register_calories);
        EditText age = findViewById(R.id.register_age);
        EditText height = findViewById(R.id.register_height);
        EditText weight = findViewById(R.id.register_weight);
        RadioGroup groupGender = findViewById(R.id.register_radio_gender);
        RadioGroup groupActivity = findViewById(R.id.register_radio_activity);
        RadioGroup groupWeight = findViewById(R.id.register_radio_weight);
        Button button = findViewById(R.id.register_calories_button);

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
            registerRetrofit(user);
        });
    }

    @Override
    public void onBackPressed() {
        if(caloriesScreen){
            setContentView(R.layout.activity_register);
            caloriesScreen = false;
        }else{
            super.onBackPressed();
        }
    }

    private void switchToLoginActivity(){
        Intent switchActivity = new Intent(this, MainActivity.class);
        startActivity(switchActivity);
    }
}
