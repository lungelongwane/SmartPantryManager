package com.lungelo.smartpantrymanager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {

        super.onCreate(b);

        setContentView(R.layout.activity_settings);

        MaterialToolbar toolbar =
                findViewById(R.id.toolbar);

        toolbar.setTitle("Settings");

        toolbar.setNavigationIcon(
                R.drawable.ic_arrow_back
        );

        toolbar.setNavigationOnClickListener(
                v -> finish()
        );

        setSupportActionBar(toolbar);

        SwitchMaterial expirySwitch =
                findViewById(R.id.switchExpiry);

        android.content.SharedPreferences preferences =
                getSharedPreferences(
                        "settings",
                        MODE_PRIVATE
                );

        expirySwitch.setChecked(
                preferences.getBoolean(
                        "expiry_alerts",
                        true
                )
        );

        expirySwitch.setOnCheckedChangeListener(
                (button, checked) ->
                        preferences
                                .edit()
                                .putBoolean(
                                        "expiry_alerts",
                                        checked
                                )
                                .apply()
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.main_menu,
                menu
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return Navigation.handle(this, item)
                || super.onOptionsItemSelected(item);
    }
}