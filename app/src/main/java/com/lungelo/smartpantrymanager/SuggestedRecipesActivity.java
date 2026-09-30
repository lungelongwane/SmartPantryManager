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

    private DatabaseHelper db;
    private android.widget.TextView empty;
    private android.view.View emptyRecipeState;

    @Override
    protected void onCreate(Bundle b) {

        super.onCreate(b);

        setContentView(
                R.layout.activity_suggested_recipes
        );

        MaterialToolbar toolbar =
                findViewById(R.id.toolbar);

        toolbar.setTitle("Suggested Recipes");

        toolbar.setNavigationIcon(
                R.drawable.ic_arrow_back
        );

        toolbar.setNavigationOnClickListener(
                v -> finish()
        );

        setSupportActionBar(toolbar);

        db = new DatabaseHelper(this);

        empty =
                findViewById(R.id.txtNoRecipes);

        emptyRecipeState =
                findViewById(R.id.emptyRecipeState);

        RecyclerView recycler =
                findViewById(R.id.recyclerRecipes);

        recycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        findViewById(R.id.btnAddIngredients)
                .setOnClickListener(
                        v -> {

                            Intent intent =
                                    new Intent(
                                            this,
                                            AddEditIngredientActivity.class
                                    );

                            startActivity(intent);
                        }
                );
    }

    @Override
    protected void onResume() {

        super.onResume();

        load();
    }

    private void load() {

        List<Recipe> allRecipes =
                db.getAllRecipes();

        List<com.lungelo.smartpantrymanager.models.Ingredient> pantry =
                db.getAllIngredients();

        List<Recipe> matches =
                new RecipeMatcher(db)
                        .getStrictMatches(
                                allRecipes,
                                pantry
                        );

        boolean noMatches =
                matches.isEmpty();

        emptyRecipeState.setVisibility(
                noMatches
                        ? android.view.View.VISIBLE
                        : android.view.View.GONE
        );

        RecyclerView recycler =
                findViewById(R.id.recyclerRecipes);

        recycler.setVisibility(
                noMatches
                        ? android.view.View.GONE
                        : android.view.View.VISIBLE
        );

        empty.setVisibility(
                noMatches
                        ? android.view.View.VISIBLE
                        : android.view.View.GONE
        );

        recycler.setAdapter(
                new RecipeAdapter(
                        matches,
                        recipe -> {

                            Intent intent =
                                    new Intent(
                                            this,
                                            RecipeDetailActivity.class
                                    );

                            intent.putExtra(
                                    "recipeId",
                                    recipe.getId()
                            );

                            startActivity(intent);
                        }
                )
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
    public boolean onOptionsItemSelected(
            MenuItem item) {

        return Navigation.handle(
                this,
                item
        ) || super.onOptionsItemSelected(item);
    }
}

