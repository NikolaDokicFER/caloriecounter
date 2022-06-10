package hr.fer.caloriecounter.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.model.UserDetail;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SettingsFragment extends Fragment {
    private View view;
    private Bundle bundle;
    private UserDetail user;
    private TextView caloriesText;
    private TextView weightText;
    private TextView firstNameText;
    private TextView lastNameText;
    private TextView usernameText;
    private TextView emailText;
    private Button progressBtn;
    private Button graphsBtn;
    private Button signOutBtn;
    private Button updateBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        user = (UserDetail) bundle.getSerializable("user");

        initUI();
        initListeners();
    }

    private void initUI() {
        caloriesText = view.findViewById(R.id.settings_calories);
        caloriesText.setText("Calories needed: " + user.getCaloriesNeeded());

        weightText = view.findViewById(R.id.settings_weight);
        weightText.setText("Current weight: " + user.getWeight());

        firstNameText = view.findViewById(R.id.settings_first_name);
        firstNameText.setText("First name:" + user.getFirstName());

        lastNameText = view.findViewById(R.id.settings_last_name);
        lastNameText.setText("Last name: " + user.getLastName());

        usernameText = view.findViewById(R.id.settings_username);
        usernameText.setText("Username: " + user.getUsername());

        emailText = view.findViewById(R.id.settings_email);
        emailText.setText("Email: " + user.getEmail());

        progressBtn = view.findViewById(R.id.progress_button);
        graphsBtn = view.findViewById(R.id.settings_graphs_button);
        updateBtn = view.findViewById(R.id.settings_update);
        signOutBtn = view.findViewById(R.id.settings_sign_out);
    }

    private void initListeners() {
        progressBtn.setOnClickListener(view -> switchToProgress());

        graphsBtn.setOnClickListener(view -> switchToGraphs());

        updateBtn.setOnClickListener(view -> {
            switchToCalories();
            initUI();
            initListeners();
        });

        signOutBtn.setOnClickListener(view -> {
            SharedPreferences.Editor shEditor = SplashScreenActivity.sharedPreferences.edit();
            shEditor.putBoolean("loggedIn", false);
            shEditor.putString("User", "");
            shEditor.apply();
            switchToLogin();
        });
    }

    private void switchToProgress() {
        Intent switchActivity = new Intent(view.getContext(), ProgressActivity.class);
        switchActivity.putExtra("user", user);
        startActivity(switchActivity);
    }

    private void switchToGraphs() {
        Intent switchActivity = new Intent(view.getContext(), GraphsActivity.class);
        switchActivity.putExtra("user", user);
        startActivity(switchActivity);
    }

    private void switchToCalories() {
        Intent switchActivity = new Intent(view.getContext(), RegisterCaloriesActivity.class);
        switchActivity.putExtra("user", user);
        switchActivity.putExtra("updating", true);
        startActivity(switchActivity);
    }

    private void switchToLogin() {
        Intent switchActivity = new Intent(view.getContext(), MainActivity.class);
        startActivity(switchActivity);
    }
}
