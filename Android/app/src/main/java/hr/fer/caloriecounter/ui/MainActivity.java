package hr.fer.caloriecounter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hr.fer.caloriecounter.R;

public class MainActivity extends AppCompatActivity {
    public static final String BASEURL = "http://10.0.2.2:8080/";
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
                System.out.println("DA");
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
}