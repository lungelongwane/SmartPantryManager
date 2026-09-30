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

public class PantryActivity extends AppCompatActivity
        implements PantryAdapter.Listener {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private List<Ingredient> items;

    private android.widget.TextView empty;
    private android.view.View emptyState;

    @Override
    protected void onCreate(Bundle b) {

        super.onCreate(b);

        setContentView(R.layout.activity_pantry);

        MaterialToolbar toolbar =
                findViewById(R.id.toolbar);

        toolbar.setTitle("Smart Pantry Manager");

        setSupportActionBar(toolbar);

        db = new DatabaseHelper(this);

        empty = findViewById(R.id.txtEmpty);
        emptyState = findViewById(R.id.emptyState);

        RecyclerView recycler =
                findViewById(R.id.recyclerPantry);

        recycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        findViewById(R.id.btnAdd)
                .setOnClickListener(
                        v -> openAddIngredient()
                );

        findViewById(R.id.btnEmptyAdd)
                .setOnClickListener(
                        v -> openAddIngredient()
                );

        findViewById(R.id.btnRecipes)
                .setOnClickListener(
                        v -> startActivity(
                                new Intent(
                                        this,
                                        SuggestedRecipesActivity.class
                                )
                        )
                );
    }

    @Override
    protected void onResume() {

        super.onResume();

        load();
    }

    private void openAddIngredient() {

        startActivity(
                new Intent(
                        this,
                        AddEditIngredientActivity.class
                )
        );
    }

    private void load() {

        items = db.getAllIngredients();

        adapter =
                new PantryAdapter(
                        items,
                        this
                );

        RecyclerView recycler =
                findViewById(R.id.recyclerPantry);

        recycler.setAdapter(adapter);

        boolean isEmpty = items.isEmpty();

        emptyState.setVisibility(
                isEmpty
                        ? android.view.View.VISIBLE
                        : android.view.View.GONE
        );

        recycler.setVisibility(
                isEmpty
                        ? android.view.View.GONE
                        : android.view.View.VISIBLE
        );

        empty.setVisibility(
                isEmpty
                        ? android.view.View.VISIBLE
                        : android.view.View.GONE
        );
    }

    @Override
    public void onEdit(Ingredient ingredient) {

        Intent intent =
                new Intent(
                        this,
                        AddEditIngredientActivity.class
                );

        intent.putExtra(
                "id",
                ingredient.getId()
        );

        startActivity(intent);
    }

    @Override
    public void onDelete(Ingredient ingredient) {

        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage(
                        "Remove "
                                + ingredient.getName()
                                + " from your pantry?"
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            db.deleteIngredient(
                                    ingredient.getId()
                            );

                            load();
                        }
                )
                .show();
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