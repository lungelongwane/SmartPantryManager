package com.lungelo.smartpantrymanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.lungelo.smartpantrymanager.database.DatabaseHelper;
import com.lungelo.smartpantrymanager.models.Ingredient;

public class AddEditIngredientActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private int editId = -1;

    private TextInputEditText name;
    private TextInputEditText quantity;
    private TextInputEditText unit;
    private TextInputEditText expiry;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setContentView(R.layout.activity_add_edit_ingredient);

        // Toolbar
        MaterialToolbar t = findViewById(R.id.toolbar);
        t.setTitle("Ingredient");
        t.setNavigationIcon(R.drawable.ic_arrow_back);
        t.setNavigationOnClickListener(v -> finish());
        setSupportActionBar(t);

        // Database
        db = new DatabaseHelper(this);

        // Input fields
        name = findViewById(R.id.edtName);
        quantity = findViewById(R.id.edtQuantity);
        unit = findViewById(R.id.edtUnit);
        expiry = findViewById(R.id.edtExpiry);

        // Check whether we are editing an existing ingredient
        editId = getIntent().getIntExtra("id", -1);

        if (editId != -1) {
            loadIngredient();
        }

        // Save button
        findViewById(R.id.btnSave).setOnClickListener(v -> save());
    }

    private void loadIngredient() {

        for (Ingredient i : db.getAllIngredients()) {

            if (i.getId() == editId) {

                name.setText(i.getName());
                quantity.setText(String.valueOf(i.getQuantity()));
                unit.setText(i.getUnit());
                expiry.setText(i.getExpiryDate());

                break;
            }
        }

        android.widget.TextView title =
                findViewById(R.id.txtTitle);

        if (title != null) {
            title.setText("Edit Ingredient");
        }
    }

    private void save() {

        String n = value(name);
        String q = value(quantity);
        String u = value(unit);
        String e = value(expiry);

        // Validate ingredient name
        if (TextUtils.isEmpty(n)) {
            name.setError("Ingredient name is required");
            name.requestFocus();
            return;
        }

        // Validate quantity
        if (TextUtils.isEmpty(q)) {
            quantity.setError("Quantity is required");
            quantity.requestFocus();
            return;
        }

        // Validate unit
        if (TextUtils.isEmpty(u)) {
            unit.setError("Unit is required");
            unit.requestFocus();
            return;
        }

        // Convert quantity to a number
        double qty;

        try {

            qty = Double.parseDouble(q);

            if (qty <= 0) {
                throw new NumberFormatException();
            }

        } catch (Exception ex) {

            quantity.setError("Enter a quantity greater than 0");
            quantity.requestFocus();
            return;
        }

        // Create ingredient object
        Ingredient i = new Ingredient(
                editId,
                n,
                qty,
                u,
                e
        );

        // Add or update
        if (editId == -1) {

            db.addIngredient(i);

            Toast.makeText(
                    this,
                    "Ingredient added",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            db.updateIngredient(i);

            Toast.makeText(
                    this,
                    "Ingredient updated",
                    Toast.LENGTH_SHORT
            ).show();
        }

        // Return to My Pantry
        finish();
    }

    private String value(TextInputEditText e) {

        if (e.getText() == null) {
            return "";
        }

        return e.getText()
                .toString()
                .trim();
    }
}