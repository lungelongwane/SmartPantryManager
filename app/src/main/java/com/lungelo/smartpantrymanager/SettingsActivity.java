package com.lungelo.smartpantrymanager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){
        super.onCreate(b);setContentView(R.layout.activity_settings);
        MaterialToolbar t=findViewById(R.id.toolbar);t.setTitle("Settings");setSupportActionBar(t);
        SwitchMaterial s=findViewById(R.id.switchExpiry);
        android.content.SharedPreferences p=getSharedPreferences("settings",MODE_PRIVATE);
        s.setChecked(p.getBoolean("expiry_alerts",true));
        s.setOnCheckedChangeListener((button,checked)->p.edit().putBoolean("expiry_alerts",checked).apply());
    }
    @Override public boolean onCreateOptionsMenu(Menu m){getMenuInflater().inflate(R.menu.main_menu,m);return true;}
    @Override public boolean onOptionsItemSelected(MenuItem i){return Navigation.handle(this,i)||super.onOptionsItemSelected(i);}
}
