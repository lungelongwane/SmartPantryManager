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
     * Returns only recipes where the pantry contains
     * every required ingredient in the required quantity.
     */
    public List<Recipe> getStrictMatches(
            List<Recipe> recipes,
            List<Ingredient> pantry) {

        List<Recipe> matches = new ArrayList<>();

        for (Recipe recipe : recipes) {

            boolean qualifies = true;

            List<RecipeIngredient> requiredIngredients =
                    db.getRecipeIngredients(recipe.getId());

            Log.d(
                    TAG,
                    "Checking recipe: " + recipe.getName()
            );

            for (RecipeIngredient required : requiredIngredients) {

                double available = 0;

                String requiredName =
                        normalize(required.getName());

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

                    String pantryName =
                            normalize(pantryItem.getName());

                    if (pantryName.equals(requiredName)) {

                        double converted =
                                convertToBase(
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
                }

                double needed =
                        convertToBase(
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

                if (available < needed) {

                    Log.d(
                            TAG,
                            "FAILED: "
                                    + required.getName()
                    );

                    qualifies = false;
                    break;
                }
            }

            if (qualifies) {

                Log.d(
                        TAG,
                        "MATCH FOUND: "
                                + recipe.getName()
                );

                matches.add(recipe);
            }
        }

        return matches;
    }

    /**
     * Normalizes ingredient names so that simple
     * singular/plural differences do not prevent matching.
     */
    public static String normalize(String value) {

        if (value == null) {
            return "";
        }

        String s =
                value
                        .toLowerCase(Locale.ROOT)
                        .trim();

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
     * Returns -1 when the units cannot be compared.
     */
    private double convertToBase(
            double amount,
            String from,
            String target) {

        String f =
                from == null
                        ? ""
                        : from.toLowerCase(Locale.ROOT).trim();

        String t =
                target == null
                        ? ""
                        : target.toLowerCase(Locale.ROOT).trim();

        // Same unit
        if (f.equals(t)) {
            return amount;
        }

        // Weight conversion
        if (isWeight(f) && isWeight(t)) {
            return convertWeight(amount, f);
        }

        // Volume conversion
        if (isVolume(f) && isVolume(t)) {
            return convertVolume(amount, f);
        }

        // Piece-based units
        if (isPieceUnit(f) && isPieceUnit(t)) {
            return amount;
        }

        // Units cannot be safely compared
        return -1;
    }

    private boolean isWeight(String unit) {

        return unit.equals("g")
                || unit.equals("gram")
                || unit.equals("grams")
                || unit.equals("kg")
                || unit.equals("kilogram")
                || unit.equals("kilograms");
    }

    private boolean isVolume(String unit) {

        return unit.equals("ml")
                || unit.equals("millilitre")
                || unit.equals("millilitres")
                || unit.equals("l")
                || unit.equals("litre")
                || unit.equals("litres")
                || unit.equals("cup")
                || unit.equals("cups");
    }

    private boolean isPieceUnit(String unit) {

        return unit.equals("piece")
                || unit.equals("pieces")
                || unit.equals("pc")
                || unit.equals("pcs")
                || unit.equals("slice")
                || unit.equals("slices")
                || unit.equals("clove")
                || unit.equals("cloves")
                || unit.equals("can")
                || unit.equals("cans");
    }

    /**
     * Converts weight to grams.
     */
    private double convertWeight(
            double amount,
            String unit) {

        if (
                unit.equals("kg")
                        || unit.equals("kilogram")
                        || unit.equals("kilograms")
        ) {
            return amount * 1000.0;
        }

        return amount;
    }

    /**
     * Converts volume to millilitres.
     *
     * One cup is treated as 240 ml.
     */
    private double convertVolume(
            double amount,
            String unit) {

        if (
                unit.equals("l")
                        || unit.equals("litre")
                        || unit.equals("litres")
        ) {
            return amount * 1000.0;
        }

        if (
                unit.equals("cup")
                        || unit.equals("cups")
        ) {
            return amount * 240.0;
        }

        return amount;
    }
}