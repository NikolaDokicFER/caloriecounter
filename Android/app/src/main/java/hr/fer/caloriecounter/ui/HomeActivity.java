package hr.fer.caloriecounter.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.model.UserDetail;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Bundle bundle = new Bundle();
    HomeFragment homeFragment = new HomeFragment();
    JournalFragment journalFragment = new JournalFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UserDetail user = (UserDetail) getIntent().getSerializableExtra("user");
        bundle.putSerializable("user", user);

        bottomNavigationView = findViewById(R.id.home_navigation);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.icon_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.icon_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, homeFragment).commit();
                homeFragment.setArguments(bundle);
                return true;

            case R.id.icon_calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, journalFragment).commit();
                return true;

            case R.id.icon_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, settingsFragment).commit();
                return true;
        }
        return false;
    }
}
