package hr.fer.caloriecounter.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.model.UserDetail;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private Bundle bundle = new Bundle();
    private HomeFragment homeFragment = new HomeFragment();
    private JournalFragment journalFragment = new JournalFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private boolean updated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        UserDetail user = (UserDetail) getIntent().getSerializableExtra("user");
        updated = getIntent().getBooleanExtra("update", false);
        bundle.putSerializable("user", user);

        getSupportActionBar().hide();
        bottomNavigationView = findViewById(R.id.home_navigation);

        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestStoragePermission();

        bottomNavigationView.setOnItemSelectedListener(this);
        if (updated) bottomNavigationView.setSelectedItemId(R.id.icon_user);
        else bottomNavigationView.setSelectedItemId(R.id.icon_home);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            finishAffinity();
        } else {
            getSupportFragmentManager().popBackStack();
        }
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
                journalFragment.setArguments(bundle);
                return true;

            case R.id.icon_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, settingsFragment).commit();
                settingsFragment.setArguments(bundle);
                return true;
        }
        return false;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }
}
