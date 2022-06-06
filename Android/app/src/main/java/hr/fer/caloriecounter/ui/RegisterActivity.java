package hr.fer.caloriecounter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.model.UserDetail;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailText;
    private EditText usernameText;
    private EditText password1Text;
    private EditText password2Text;
    private Button registerButton;
    private UserDetail user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        initUI();
        initListeners();
    }

    private void initUI() {
        firstNameText = findViewById(R.id.register_first_name);
        lastNameText = findViewById(R.id.register_last_name);
        emailText = findViewById(R.id.register_email);
        usernameText = findViewById(R.id.register_username);
        password1Text = findViewById(R.id.register_password1);
        password2Text = findViewById(R.id.register_password2);
        registerButton = findViewById(R.id.register_button);
    }

    private void initListeners() {
        registerButton.setOnClickListener(view -> {
            if (validateUserName(usernameText.getText().toString()) && validateEmail(emailText.getText().toString()) && validatePassword(password1Text.getText().toString(), password2Text.getText().toString())) {
                user = new UserDetail();
                user.setFirstName(firstNameText.getText().toString());
                user.setLastName(lastNameText.getText().toString());
                user.setEmail(emailText.getText().toString());
                user.setUsername(usernameText.getText().toString());
                user.setPassword(password1Text.getText().toString());

                switchToCaloriesActivity(user);
            }
        });
    }

    private boolean validateUserName(String userName) {
        if (userName.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (userName.contains(" ")) {
            Toast.makeText(RegisterActivity.this, "Username: Remove space between characters", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            Toast.makeText(RegisterActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validatePassword(String password, String password2) {
        Pattern pattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])");
        Matcher matcher = pattern.matcher(password);
        if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.contains(" ")) {
            Toast.makeText(RegisterActivity.this, "Password: Remove space between characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!matcher.find()) {
            Toast.makeText(RegisterActivity.this, "Password needs to contain at least 1 lowercase letter, uppercase letter and number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 8) {
            Toast.makeText(RegisterActivity.this, "Password needs to be at least 8 characters long", Toast.LENGTH_LONG).show();
            return false;
        } else if (!password.equals(password2)) {
            Toast.makeText(RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void switchToCaloriesActivity(UserDetail user) {
        Intent switchActivity = new Intent(this, RegisterCaloriesActivity.class);
        switchActivity.putExtra("user", user);
        switchActivity.putExtra("updating", false);
        startActivity(switchActivity);
    }
}
