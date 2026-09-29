package com.lungelo.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.lungelo.smartpantrymanager.adapters.PantryAdapter;
import com.lungelo.smartpantrymanager.database.DatabaseHelper;
import com.lungelo.smartpantrymanager.models.Ingredient;
import java.util.List;

public class PantryActivity extends AppCompatActivity implements PantryAdapter.Listener {
    private DatabaseHelper db; private PantryAdapter adapter; private List<Ingredient> items; private android.widget.TextView empty;
    @Override protected void onCreate(Bundle b){
        super.onCreate(b); setContentView(R.layout.activity_pantry);
        MaterialToolbar t=findViewById(R.id.toolbar); t.setTitle("My Pantry"); setSupportActionBar(t);
        db=new DatabaseHelper(this); empty=findViewById(R.id.txtEmpty);
        RecyclerView rv=findViewById(R.id.recyclerPantry); rv.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.btnAdd).setOnClickListener(v->startActivity(new Intent(this,AddEditIngredientActivity.class)));
        findViewById(R.id.btnRecipes).setOnClickListener(v->startActivity(new Intent(this,SuggestedRecipesActivity.class)));
    }
    @Override protected void onResume(){super.onResume();load();}
    private void load(){items=db.getAllIngredients(); adapter=new PantryAdapter(items,this); ((RecyclerView)findViewById(R.id.recyclerPantry)).setAdapter(adapter); empty.setVisibility(items.isEmpty()?android.view.View.VISIBLE:android.view.View.GONE);}
    @Override public void onEdit(Ingredient i){Intent x=new Intent(this,AddEditIngredientActivity.class);x.putExtra("id",i.getId());startActivity(x);}
    @Override public void onDelete(Ingredient i){
        new AlertDialog.Builder(this).setTitle("Delete ingredient").setMessage("Remove "+i.getName()+" from your pantry?")
            .setNegativeButton("Cancel",null).setPositiveButton("Delete",(d,w)->{db.deleteIngredient(i.getId());load();}).show();
    }
    @Override public boolean onCreateOptionsMenu(Menu m){getMenuInflater().inflate(R.menu.main_menu,m);return true;}
    @Override public boolean onOptionsItemSelected(MenuItem i){return Navigation.handle(this,i)||super.onOptionsItemSelected(i);}
}
