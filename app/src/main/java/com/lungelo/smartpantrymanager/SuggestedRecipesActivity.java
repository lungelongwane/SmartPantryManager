package com.lungelo.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.lungelo.smartpantrymanager.adapters.RecipeAdapter;
import com.lungelo.smartpantrymanager.database.DatabaseHelper;
import com.lungelo.smartpantrymanager.models.Recipe;
import com.lungelo.smartpantrymanager.utils.RecipeMatcher;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private DatabaseHelper db; private android.widget.TextView empty;
    @Override protected void onCreate(Bundle b){
        super.onCreate(b);setContentView(R.layout.activity_suggested_recipes);
        MaterialToolbar t=findViewById(R.id.toolbar);t.setTitle("Suggested Recipes");setSupportActionBar(t);
        db=new DatabaseHelper(this);empty=findViewById(R.id.txtNoRecipes);
        ((RecyclerView)findViewById(R.id.recyclerRecipes)).setLayoutManager(new LinearLayoutManager(this));
    }
    @Override protected void onResume(){super.onResume();load();}
    private void load(){
        List<Recipe> matches=new RecipeMatcher(db).getStrictMatches(db.getAllRecipes(),db.getAllIngredients());
        empty.setVisibility(matches.isEmpty()?android.view.View.VISIBLE:android.view.View.GONE);
        ((RecyclerView)findViewById(R.id.recyclerRecipes)).setAdapter(new RecipeAdapter(matches,r->{Intent i=new Intent(this,RecipeDetailActivity.class);i.putExtra("recipeId",r.getId());startActivity(i);}));
    }
    @Override public boolean onCreateOptionsMenu(Menu m){getMenuInflater().inflate(R.menu.main_menu,m);return true;}
    @Override public boolean onOptionsItemSelected(MenuItem i){return Navigation.handle(this,i)||super.onOptionsItemSelected(i);}
}
