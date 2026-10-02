package com.lungelo.smartpantrymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lungelo.smartpantrymanager.models.Ingredient;
import com.lungelo.smartpantrymanager.models.Recipe;
import com.lungelo.smartpantrymanager.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public DatabaseHelper(Context context) {

        super(
                context,
                DB_NAME,
                null,
                DB_VERSION
        );
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {

        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "quantity REAL NOT NULL," +
                        "unit TEXT NOT NULL," +
                        "expiry_date TEXT" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "instructions TEXT NOT NULL" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "recipe_id INTEGER NOT NULL," +
                        "ingredient_name TEXT NOT NULL," +
                        "required_quantity REAL NOT NULL," +
                        "unit TEXT NOT NULL," +
                        "FOREIGN KEY(recipe_id) REFERENCES " +
                        TABLE_RECIPES +
                        "(id) ON DELETE CASCADE" +
                        ")"
        );

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        // Preserve existing pantry data during future upgrades.
        // Add explicit migrations here when the schema changes.
        if (oldVersion < 1) {
            onCreate(db);
        }
    }

    public long addIngredient(Ingredient i) {

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                i.getName()
        );

        values.put(
                "quantity",
                i.getQuantity()
        );

        values.put(
                "unit",
                i.getUnit()
        );

        values.put(
                "expiry_date",
                i.getExpiryDate()
        );

        return getWritableDatabase().insert(
                TABLE_INGREDIENTS,
                null,
                values
        );
    }

    public List<Ingredient> getAllIngredients() {

        List<Ingredient> list =
                new ArrayList<>();

        Cursor cursor =
                getReadableDatabase().rawQuery(
                        "SELECT * FROM "
                                + TABLE_INGREDIENTS
                                + " ORDER BY name COLLATE NOCASE",
                        null
                );

        while (cursor.moveToNext()) {

            list.add(
                    new Ingredient(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getDouble(2),
                            cursor.getString(3),
                            cursor.getString(4)
                    )
            );
        }

        cursor.close();

        return list;
    }

    public int updateIngredient(Ingredient i) {

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                i.getName()
        );

        values.put(
                "quantity",
                i.getQuantity()
        );

        values.put(
                "unit",
                i.getUnit()
        );

        values.put(
                "expiry_date",
                i.getExpiryDate()
        );

        return getWritableDatabase().update(
                TABLE_INGREDIENTS,
                values,
                "id=?",
                new String[]{
                        String.valueOf(i.getId())
                }
        );
    }

    public int deleteIngredient(int id) {

        return getWritableDatabase().delete(
                TABLE_INGREDIENTS,
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }

    public List<Recipe> getAllRecipes() {

        List<Recipe> list =
                new ArrayList<>();

        Cursor cursor =
                getReadableDatabase().rawQuery(
                        "SELECT id,name,instructions FROM "
                                + TABLE_RECIPES
                                + " ORDER BY name COLLATE NOCASE",
                        null
                );

        while (cursor.moveToNext()) {

            list.add(
                    new Recipe(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2)
                    )
            );
        }

        cursor.close();

        return list;
    }

    public Recipe getRecipe(int id) {

        Cursor cursor =
                getReadableDatabase().rawQuery(
                        "SELECT id,name,instructions FROM "
                                + TABLE_RECIPES
                                + " WHERE id=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        Recipe recipe = null;

        if (cursor.moveToFirst()) {

            recipe =
                    new Recipe(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2)
                    );
        }

        cursor.close();

        return recipe;
    }

    public List<RecipeIngredient> getRecipeIngredients(
            int recipeId) {

        List<RecipeIngredient> list =
                new ArrayList<>();

        Cursor cursor =
                getReadableDatabase().rawQuery(
                        "SELECT ingredient_name," +
                                "required_quantity,unit " +
                                "FROM " +
                                TABLE_RECIPE_INGREDIENTS +
                                " WHERE recipe_id=?",
                        new String[]{
                                String.valueOf(recipeId)
                        }
                );

        while (cursor.moveToNext()) {

            list.add(
                    new RecipeIngredient(
                            cursor.getString(0),
                            cursor.getDouble(1),
                            cursor.getString(2)
                    )
            );
        }

        cursor.close();

        return list;
    }

    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(
                db,
                "Chicken Fried Rice",
                "Cook the rice. Stir-fry onion, carrot and chicken. Add rice and peas, season, then cook until hot.",
                new String[][]{
                        {"chicken", "250", "g"},
                        {"rice", "2", "cup"},
                        {"onion", "1", "piece"},
                        {"carrot", "1", "piece"},
                        {"peas", "100", "g"}
                }
        );

        addRecipe(
                db,
                "Tomato Pasta",
                "Cook pasta. Sauté onion and garlic, add tomatoes and simmer. Toss with pasta and season.",
                new String[][]{
                        {"pasta", "200", "g"},
                        {"tomato", "3", "piece"},
                        {"onion", "1", "piece"},
                        {"garlic", "2", "clove"},
                        {"olive oil", "15", "ml"}
                }
        );

        addRecipe(
                db,
                "Vegetable Omelette",
                "Beat eggs. Sauté vegetables, pour in eggs and cook gently until set.",
                new String[][]{
                        {"egg", "3", "piece"},
                        {"onion", "1", "piece"},
                        {"tomato", "1", "piece"},
                        {"spinach", "50", "g"}
                }
        );

        addRecipe(
                db,
                "Chicken Sandwich",
                "Cook or use leftover chicken. Fill bread with chicken, lettuce and tomato, then serve.",
                new String[][]{
                        {"bread", "2", "slice"},
                        {"chicken", "150", "g"},
                        {"lettuce", "30", "g"},
                        {"tomato", "1", "piece"}
                }
        );

        addRecipe(
                db,
                "Tuna Pasta",
                "Cook pasta. Mix tuna, sweetcorn and mayonnaise, then combine with warm pasta.",
                new String[][]{
                        {"pasta", "200", "g"},
                        {"tuna", "1", "can"},
                        {"sweetcorn", "100", "g"},
                        {"mayonnaise", "30", "ml"}
                }
        );

        addRecipe(
                db,
                "Potato Egg Hash",
                "Dice potatoes and fry until tender. Add onion and cooked egg, season and serve.",
                new String[][]{
                        {"potato", "3", "piece"},
                        {"egg", "2", "piece"},
                        {"onion", "1", "piece"},
                        {"oil", "15", "ml"}
                }
        );

        addRecipe(
                db,
                "Chicken Curry",
                "Brown chicken, sauté onion and garlic, add curry spices and tomatoes, then simmer until cooked.",
                new String[][]{
                        {"chicken", "300", "g"},
                        {"onion", "1", "piece"},
                        {"garlic", "2", "clove"},
                        {"tomato", "2", "piece"},
                        {"curry powder", "10", "g"}
                }
        );

        addRecipe(
                db,
                "Bean Wrap",
                "Warm beans and corn. Fill the tortilla with beans, corn, tomato and lettuce.",
                new String[][]{
                        {"tortilla", "2", "piece"},
                        {"beans", "200", "g"},
                        {"sweetcorn", "100", "g"},
                        {"tomato", "1", "piece"},
                        {"lettuce", "30", "g"}
                }
        );

        addRecipe(
                db,
                "Cheese Toastie",
                "Place cheese between bread slices and toast in a pan until golden and melted.",
                new String[][]{
                        {"bread", "2", "slice"},
                        {"cheese", "60", "g"},
                        {"butter", "10", "g"}
                }
        );

        addRecipe(
                db,
                "Vegetable Rice",
                "Stir-fry onion, carrot and peas. Add cooked rice and soy sauce, then heat through.",
                new String[][]{
                        {"rice", "2", "cup"},
                        {"onion", "1", "piece"},
                        {"carrot", "1", "piece"},
                        {"peas", "100", "g"},
                        {"soy sauce", "15", "ml"}
                }
        );

        addRecipe(
                db,
                "Greek Salad",
                "Chop vegetables and cheese. Toss with olive oil and serve chilled.",
                new String[][]{
                        {"cucumber", "1", "piece"},
                        {"tomato", "2", "piece"},
                        {"onion", "0.5", "piece"},
                        {"feta", "80", "g"},
                        {"olive oil", "15", "ml"}
                }
        );

        addRecipe(
                db,
                "Garlic Butter Pasta",
                "Cook pasta. Melt butter with garlic, add pasta and toss. Finish with cheese if available.",
                new String[][]{
                        {"pasta", "200", "g"},
                        {"garlic", "2", "clove"},
                        {"butter", "30", "g"},
                        {"cheese", "40", "g"}
                }
        );

        addRecipe(
                db,
                "Banana Pancakes",
                "Mash banana, mix with egg and flour, then cook small pancakes in a lightly oiled pan.",
                new String[][]{
                        {"banana", "2", "piece"},
                        {"egg", "2", "piece"},
                        {"flour", "100", "g"},
                        {"milk", "100", "ml"},
                        {"oil", "10", "ml"}
                }
        );

        addRecipe(
                db,
                "Chicken Salad",
                "Slice cooked chicken and toss with lettuce, cucumber and tomato. Dress and serve.",
                new String[][]{
                        {"chicken", "200", "g"},
                        {"lettuce", "50", "g"},
                        {"cucumber", "1", "piece"},
                        {"tomato", "1", "piece"},
                        {"olive oil", "10", "ml"}
                }
        );

        addRecipe(
                db,
                "Lentil Soup",
                "Sauté onion and carrot. Add lentils, tomato and water or stock. Simmer until tender.",
                new String[][]{
                        {"lentils", "200", "g"},
                        {"onion", "1", "piece"},
                        {"carrot", "1", "piece"},
                        {"tomato", "2", "piece"}
                }
        );

        addRecipe(
                db,
                "Egg Fried Rice",
                "Scramble eggs, add cooked rice and peas, then stir-fry with soy sauce.",
                new String[][]{
                        {"egg", "2", "piece"},
                        {"rice", "2", "cup"},
                        {"peas", "100", "g"},
                        {"soy sauce", "15", "ml"}
                }
        );

        addRecipe(
                db,
                "Tomato Egg Scramble",
                "Cook chopped tomato and onion, add beaten eggs and scramble until set.",
                new String[][]{
                        {"egg", "3", "piece"},
                        {"tomato", "2", "piece"},
                        {"onion", "1", "piece"},
                        {"oil", "10", "ml"}
                }
        );

        addRecipe(
                db,
                "Cheesy Potato Bake",
                "Boil potatoes until nearly tender. Layer with cheese and milk, then bake until golden.",
                new String[][]{
                        {"potato", "4", "piece"},
                        {"cheese", "100", "g"},
                        {"milk", "150", "ml"},
                        {"butter", "15", "g"}
                }
        );
    }

    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String instructions,
            String[][] ingredients) {

        ContentValues recipeValues =
                new ContentValues();

        recipeValues.put(
                "name",
                name
        );

        recipeValues.put(
                "instructions",
                instructions
        );

        long recipeId =
                db.insert(
                        TABLE_RECIPES,
                        null,
                        recipeValues
                );

        for (String[] ingredient : ingredients) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    "recipe_id",
                    recipeId
            );

            ingredientValues.put(
                    "ingredient_name",
                    ingredient[0]
            );

            ingredientValues.put(
                    "required_quantity",
                    Double.parseDouble(
                            ingredient[1]
                    )
            );

            ingredientValues.put(
                    "unit",
                    ingredient[2]
            );

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    ingredientValues
            );
        }
    }
}

