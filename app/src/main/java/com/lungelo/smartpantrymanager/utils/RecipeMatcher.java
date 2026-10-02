package com.lungelo.smartpantrymanager.utils;

import android.util.Log;

import com.lungelo.smartpantrymanager.database.DatabaseHelper;
import com.lungelo.smartpantrymanager.models.Ingredient;
import com.lungelo.smartpantrymanager.models.Recipe;
import com.lungelo.smartpantrymanager.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeMatcher {

    private static final String TAG = "RECIPE_MATCHER";

    private final DatabaseHelper db;

    public RecipeMatcher(DatabaseHelper db) {
        this.db = db;
    }

    /**
     * Returns only recipes where the pantry contains every required
     * ingredient in a sufficient quantity.
     *
     * Quantities are compared using a common base unit for compatible
     * units (grams for weight and millilitres for volume).
     */
    public List<Recipe> getStrictMatches(
            List<Recipe> recipes,
            List<Ingredient> pantry) {

        List<Recipe> matches = new ArrayList<>();

        for (Recipe recipe : recipes) {

            boolean qualifies = true;

            List<RecipeIngredient> requiredIngredients =
                    db.getRecipeIngredients(recipe.getId());

            Log.d(TAG, "Checking recipe: " + recipe.getName());

            for (RecipeIngredient required : requiredIngredients) {

                double available = 0;

                String requiredName = normalize(required.getName());

                Log.d(
                        TAG,
                        "Requires: "
                                + required.getName()
                                + " | "
                                + required.getQuantity()
                                + " "
                                + required.getUnit()
                );

                for (Ingredient pantryItem : pantry) {

                    String pantryName = normalize(pantryItem.getName());

                    if (!pantryName.equals(requiredName)) {
                        continue;
                    }

                    double converted = convertToBase(
                            pantryItem.getQuantity(),
                            pantryItem.getUnit(),
                            required.getUnit()
                    );

                    if (converted >= 0) {
                        available += converted;
                    }

                    Log.d(
                            TAG,
                            "Matched pantry item: "
                                    + pantryItem.getName()
                                    + " | "
                                    + pantryItem.getQuantity()
                                    + " "
                                    + pantryItem.getUnit()
                                    + " | converted = "
                                    + converted
                    );
                }

                // Convert both values to the same base unit.
                double needed = convertToBase(
                        required.getQuantity(),
                        required.getUnit(),
                        required.getUnit()
                );

                Log.d(
                        TAG,
                        "Available: "
                                + available
                                + " | Needed: "
                                + needed
                );

                if (needed < 0 || available < needed) {

                    Log.d(
                            TAG,
                            "FAILED: " + required.getName()
                    );

                    qualifies = false;
                    break;
                }
            }

            if (qualifies) {
                Log.d(TAG, "MATCH FOUND: " + recipe.getName());
                matches.add(recipe);
            }
        }

        return matches;
    }

    /**
     * Normalizes ingredient names so simple singular/plural differences
     * do not prevent matching.
     */
    public static String normalize(String value) {

        if (value == null) {
            return "";
        }

        String s = value.toLowerCase(Locale.ROOT).trim();

        if (s.endsWith("ies") && s.length() > 3) {
            s = s.substring(0, s.length() - 3) + "y";
        } else if (s.endsWith("oes") && s.length() > 3) {
            s = s.substring(0, s.length() - 2);
        } else if (
                s.endsWith("s")
                        && !s.endsWith("ss")
                        && s.length() > 2
        ) {
            s = s.substring(0, s.length() - 1);
        }

        return s;
    }

    /**
     * Converts compatible units to a common base unit.
     *
     * Weight is converted to grams, volume to millilitres, and
     * like-for-like count units are kept as counts.
     *
     * Returns -1 when the units cannot safely be compared.
     */
    private double convertToBase(
            double amount,
            String from,
            String target) {

        String f = canonicalUnit(from);
        String t = canonicalUnit(target);

        if (f.isEmpty() || t.isEmpty()) {
            return -1;
        }

        // Weight: base unit = gram.
        if (isWeight(f) && isWeight(t)) {
            return convertWeight(amount, f);
        }

        // Volume: base unit = millilitre.
        if (isVolume(f) && isVolume(t)) {
            return convertVolume(amount, f);
        }

        // Count units are only interchangeable with the same kind
        // of count. A clove is not a piece, and a slice is not a can.
        if (f.equals(t) && isCountUnit(f)) {
            return amount;
        }

        return -1;
    }

    private String canonicalUnit(String unit) {

        if (unit == null) {
            return "";
        }

        String value = unit.toLowerCase(Locale.ROOT).trim();

        switch (value) {
            case "gram":
            case "grams":
            case "g":
                return "g";

            case "kilogram":
            case "kilograms":
            case "kg":
                return "kg";

            case "millilitre":
            case "millilitres":
            case "milliliter":
            case "milliliters":
            case "ml":
                return "ml";

            case "litre":
            case "litres":
            case "liter":
            case "liters":
            case "l":
                return "l";

            case "cup":
            case "cups":
                return "cup";

            case "piece":
            case "pieces":
            case "pc":
            case "pcs":
                return "piece";

            case "slice":
            case "slices":
                return "slice";

            case "clove":
            case "cloves":
                return "clove";

            case "can":
            case "cans":
                return "can";

            default:
                return value;
        }
    }

    private boolean isWeight(String unit) {
        return unit.equals("g") || unit.equals("kg");
    }

    private boolean isVolume(String unit) {
        return unit.equals("ml")
                || unit.equals("l")
                || unit.equals("cup");
    }

    private boolean isCountUnit(String unit) {
        return unit.equals("piece")
                || unit.equals("slice")
                || unit.equals("clove")
                || unit.equals("can");
    }

    /**
     * Converts weight to grams.
     */
    private double convertWeight(double amount, String unit) {

        if (unit.equals("kg")) {
            return amount * 1000.0;
        }

        return amount;
    }

    /**
     * Converts volume to millilitres.
     *
     * One cup is treated as 240 ml.
     */
    private double convertVolume(double amount, String unit) {

        if (unit.equals("l")) {
            return amount * 1000.0;
        }

        if (unit.equals("cup")) {
            return amount * 240.0;
        }

        return amount;
    }
}
