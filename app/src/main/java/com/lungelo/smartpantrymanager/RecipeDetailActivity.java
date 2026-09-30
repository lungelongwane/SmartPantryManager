package com.lungelo.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.lungelo.smartpantrymanager.database.DatabaseHelper;
import com.lungelo.smartpantrymanager.models.Recipe;
import com.lungelo.smartpantrymanager.models.RecipeIngredient;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setContentView(R.layout.activity_recipe_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Recipe Details");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(
                v -> finish()
        );

        setSupportActionBar(toolbar);

        DatabaseHelper db =
                new DatabaseHelper(this);

        int id =
                getIntent().getIntExtra(
                        "recipeId",
                        -1
                );

        Recipe recipe =
                db.getRecipe(id);

        if (recipe == null) {
            finish();
            return;
        }

        TextView recipeName =
                findViewById(R.id.txtRecipeName);

        TextView ingredients =
                findViewById(R.id.txtIngredients);

        TextView instructions =
                findViewById(R.id.txtInstructions);

        recipeName.setText(recipe.getName());

        StringBuilder ingredientText =
                new StringBuilder();

        List<RecipeIngredient> requiredIngredients =
                db.getRecipeIngredients(id);

        for (RecipeIngredient ingredient :
                requiredIngredients) {

            ingredientText
                    .append("• ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getName())
                    .append("\n");
        }

        ingredients.setText(
                ingredientText.toString()
        );

        instructions.setText(
                recipe.getInstructions()
        );
    }
}

