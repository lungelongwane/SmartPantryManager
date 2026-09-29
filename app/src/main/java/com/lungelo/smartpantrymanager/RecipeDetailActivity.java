package com.lungelo.smartpantrymanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.lungelo.smartpantrymanager.database.DatabaseHelper;
import com.lungelo.smartpantrymanager.models.Recipe;
import com.lungelo.smartpantrymanager.models.RecipeIngredient;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){
        super.onCreate(b);setContentView(R.layout.activity_recipe_detail);
        MaterialToolbar t=findViewById(R.id.toolbar);t.setTitle("Recipe Details");t.setNavigationIcon(R.drawable.ic_arrow_back);t.setNavigationOnClickListener(v->finish());setSupportActionBar(t);
        DatabaseHelper db=new DatabaseHelper(this); int id=getIntent().getIntExtra("recipeId",-1); Recipe r=db.getRecipe(id);
        if(r==null){finish();return;}
        ((android.widget.TextView)findViewById(R.id.txtRecipeName)).setText(r.getName());
        StringBuilder ingredients=new StringBuilder();
        List<RecipeIngredient> list=db.getRecipeIngredients(id);
        for(RecipeIngredient x:list) ingredients.append("• ").append(x.getQuantity()).append(" ").append(x.getUnit()).append(" ").append(x.getName()).append("\n");
        ((android.widget.TextView)findViewById(R.id.txtIngredients)).setText(ingredients.toString());
        ((android.widget.TextView)findViewById(R.id.txtInstructions)).setText(r.getInstructions());
    }
}
