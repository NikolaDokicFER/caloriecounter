package hr.fer.caloriecounter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.LoginApi;
import hr.fer.caloriecounter.model.UserDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initUI();
        initListeners();
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }

    private void initUI(){
        usernameText = findViewById(R.id.login_username);
        passwordText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button_login);
        registerButton = findViewById(R.id.login_button_register);
    }

    private void initListeners() {
        registerButton.setOnClickListener(view -> switchToRegister());

        loginButton.setOnClickListener(view -> {
            if(validateUsername(usernameText.getText().toString()) && validatePassword(passwordText.getText().toString()))
                loginRetrofit(usernameText.getText().toString(), passwordText.getText().toString());
        });
    }

    private void loginRetrofit(String username, String password) {
        Retrofit retrofit = NetworkClient.retrofit();

        LoginApi loginApi = retrofit.create(LoginApi.class);
        Call<UserDetail> call = loginApi.getUser(username, password);

        call.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if(response.code() == 200) {
                    SharedPreferences.Editor shEditor = SplashScreenActivity.sharedPreferences.edit();
                    shEditor.putBoolean("loggedIn", true);
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    shEditor.putString("User", json);
                    shEditor.apply();
                    switchToHome(response.body());
                }else{
                    System.out.println(response.code());
                    Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    private boolean validateUsername(String username){
        if(username.isEmpty()) {
            Toast.makeText(MainActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if(username.contains(" ")){
            Toast.makeText(MainActivity.this, "Username: Remove space between characters", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    private void switchToRegister(){
        Intent switchActivity = new Intent(this, RegisterActivity.class);
        startActivity(switchActivity);
    }

    private void switchToHome(UserDetail user){
        Intent switchActivity = new Intent(this, HomeActivity.class);
        switchActivity.putExtra("user", user);
        startActivity(switchActivity);
    }
}