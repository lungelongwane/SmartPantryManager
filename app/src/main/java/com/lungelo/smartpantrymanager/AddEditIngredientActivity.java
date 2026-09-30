package com.lungelo.smartpantrymanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.lungelo.smartpantrymanager.database.DatabaseHelper;
import com.lungelo.smartpantrymanager.models.Ingredient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        MaterialToolbar t = findViewById(R.id.toolbar);

        t.setTitle("Ingredient");

        t.setNavigationIcon(R.drawable.ic_arrow_back);

        t.setNavigationOnClickListener(
                v -> finish()
        );

        setSupportActionBar(t);

        db = new DatabaseHelper(this);

        name = findViewById(R.id.edtName);
        quantity = findViewById(R.id.edtQuantity);
        unit = findViewById(R.id.edtUnit);
        expiry = findViewById(R.id.edtExpiry);

        editId =
                getIntent().getIntExtra(
                        "id",
                        -1
                );

        if (editId != -1) {
            loadIngredient();
        }

        findViewById(R.id.btnSave)
                .setOnClickListener(v -> save());
    }

    private void loadIngredient() {

        for (Ingredient i :
                db.getAllIngredients()) {

            if (i.getId() == editId) {

                name.setText(i.getName());

                quantity.setText(
                        String.valueOf(
                                i.getQuantity()
                        )
                );

                unit.setText(i.getUnit());

                expiry.setText(
                        i.getExpiryDate()
                );

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

        if (TextUtils.isEmpty(n)) {

            name.setError(
                    "Ingredient name is required"
            );

            name.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(q)) {

            quantity.setError(
                    "Quantity is required"
            );

            quantity.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(u)) {

            unit.setError(
                    "Unit is required"
            );

            unit.requestFocus();

            return;
        }

        double qty;

        try {

            qty = Double.parseDouble(q);

            if (qty <= 0) {
                throw new NumberFormatException();
            }

        } catch (Exception ex) {

            quantity.setError(
                    "Enter a quantity greater than 0"
            );

            quantity.requestFocus();

            return;
        }

        // Validate expiry date when provided
        if (!TextUtils.isEmpty(e)
                && !isValidDate(e)) {

            expiry.setError(
                    "Use date format YYYY-MM-DD"
            );

            expiry.requestFocus();

            return;
        }

        Ingredient i =
                new Ingredient(
                        editId,
                        n,
                        qty,
                        u,
                        e
                );

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

        finish();
    }

    private boolean isValidDate(String date) {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        format.setLenient(false);

        try {

            Date parsed =
                    format.parse(date);

            return parsed != null
                    && date.equals(
                    format.format(parsed)
            );

        } catch (ParseException e) {

            return false;
        }
    }

    private String value(
            TextInputEditText e) {

        if (e.getText() == null) {
            return "";
        }

        return e.getText()
                .toString()
                .trim();
    }
}

