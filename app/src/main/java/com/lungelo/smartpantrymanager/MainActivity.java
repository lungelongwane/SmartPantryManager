package com.lungelo.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main);
        setupToolbar();
        findViewById(R.id.btnPantry).setOnClickListener(v->startActivity(new Intent(this,PantryActivity.class)));
        findViewById(R.id.btnSuggestions).setOnClickListener(v->startActivity(new Intent(this,SuggestedRecipesActivity.class)));
        findViewById(R.id.btnSettings).setOnClickListener(v->startActivity(new Intent(this,SettingsActivity.class)));
    }
    private void setupToolbar(){MaterialToolbar t=findViewById(R.id.toolbar); setSupportActionBar(t);}
    @Override public boolean onCreateOptionsMenu(Menu menu){getMenuInflater().inflate(R.menu.main_menu,menu);return true;}
    @Override public boolean onOptionsItemSelected(MenuItem item){return Navigation.handle(this,item)||super.onOptionsItemSelected(item);}
}
