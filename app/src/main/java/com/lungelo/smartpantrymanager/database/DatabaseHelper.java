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
    private static final String DB_NAME="smart_pantry.db";
    private static final int DB_VERSION=1;
    public DatabaseHelper(Context context){super(context,DB_NAME,null,DB_VERSION);}

    @Override public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL,expiry_date TEXT)");
        db.execSQL("CREATE TABLE recipes(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,instructions TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT,recipe_id INTEGER NOT NULL,ingredient_name TEXT NOT NULL,required_quantity REAL NOT NULL,unit TEXT NOT NULL,FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
        seedRecipes(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        onCreate(db);
    }

    public long addIngredient(Ingredient i){
        ContentValues v=new ContentValues();
        v.put("name",i.getName()); v.put("quantity",i.getQuantity()); v.put("unit",i.getUnit()); v.put("expiry_date",i.getExpiryDate());
        return getWritableDatabase().insert("ingredients",null,v);
    }

    public List<Ingredient> getAllIngredients(){
        List<Ingredient> list=new ArrayList<>();
        Cursor c=getReadableDatabase().rawQuery("SELECT * FROM ingredients ORDER BY name COLLATE NOCASE",null);
        while(c.moveToNext()) list.add(new Ingredient(c.getInt(0),c.getString(1),c.getDouble(2),c.getString(3),c.getString(4)));
        c.close(); return list;
    }

    public int updateIngredient(Ingredient i){
        ContentValues v=new ContentValues();
        v.put("name",i.getName()); v.put("quantity",i.getQuantity()); v.put("unit",i.getUnit()); v.put("expiry_date",i.getExpiryDate());
        return getWritableDatabase().update("ingredients",v,"id=?",new String[]{String.valueOf(i.getId())});
    }

    public int deleteIngredient(int id){
        return getWritableDatabase().delete("ingredients","id=?",new String[]{String.valueOf(id)});
    }

    public List<Recipe> getAllRecipes(){
        List<Recipe> list=new ArrayList<>();
        Cursor c=getReadableDatabase().rawQuery("SELECT id,name,instructions FROM recipes ORDER BY name COLLATE NOCASE",null);
        while(c.moveToNext()) list.add(new Recipe(c.getInt(0),c.getString(1),c.getString(2)));
        c.close(); return list;
    }

    public Recipe getRecipe(int id){
        Cursor c=getReadableDatabase().rawQuery("SELECT id,name,instructions FROM recipes WHERE id=?",new String[]{String.valueOf(id)});
        Recipe r=null;
        if(c.moveToFirst()) r=new Recipe(c.getInt(0),c.getString(1),c.getString(2));
        c.close(); return r;
    }

    public List<RecipeIngredient> getRecipeIngredients(int recipeId){
        List<RecipeIngredient> list=new ArrayList<>();
        Cursor c=getReadableDatabase().rawQuery("SELECT ingredient_name,required_quantity,unit FROM recipe_ingredients WHERE recipe_id=?",new String[]{String.valueOf(recipeId)});
        while(c.moveToNext()) list.add(new RecipeIngredient(c.getString(0),c.getDouble(1),c.getString(2)));
        c.close(); return list;
    }

    private void seedRecipes(SQLiteDatabase db){
        addRecipe(db,"Chicken Fried Rice",
                "Cook the rice. Stir-fry onion, carrot and chicken. Add rice and peas, season, then cook until hot.",
                new String[][]{{"chicken","250","g"},{"rice","2","cup"},{"onion","1","piece"},{"carrot","1","piece"},{"peas","100","g"}});
        addRecipe(db,"Tomato Pasta",
                "Cook pasta. Sauté onion and garlic, add tomatoes and simmer. Toss with pasta and season.",
                new String[][]{{"pasta","200","g"},{"tomato","3","piece"},{"onion","1","piece"},{"garlic","2","clove"},{"olive oil","15","ml"}});
        addRecipe(db,"Vegetable Omelette",
                "Beat eggs. Sauté vegetables, pour in eggs and cook gently until set.",
                new String[][]{{"egg","3","piece"},{"onion","1","piece"},{"tomato","1","piece"},{"spinach","50","g"}});
        addRecipe(db,"Chicken Sandwich",
                "Cook or use leftover chicken. Fill bread with chicken, lettuce and tomato, then serve.",
                new String[][]{{"bread","2","slice"},{"chicken","150","g"},{"lettuce","30","g"},{"tomato","1","piece"}});
        addRecipe(db,"Tuna Pasta",
                "Cook pasta. Mix tuna, sweetcorn and mayonnaise, then combine with warm pasta.",
                new String[][]{{"pasta","200","g"},{"tuna","1","can"},{"sweetcorn","100","g"},{"mayonnaise","30","ml"}});
        addRecipe(db,"Potato Egg Hash",
                "Dice potatoes and fry until tender. Add onion and cooked egg, season and serve.",
                new String[][]{{"potato","3","piece"},{"egg","2","piece"},{"onion","1","piece"},{"oil","15","ml"}});
        addRecipe(db,"Chicken Curry",
                "Brown chicken, sauté onion and garlic, add curry spices and tomatoes, then simmer until cooked.",
                new String[][]{{"chicken","300","g"},{"onion","1","piece"},{"garlic","2","clove"},{"tomato","2","piece"},{"curry powder","10","g"}});
        addRecipe(db,"Bean Wrap",
                "Warm beans and corn. Fill the tortilla with beans, corn, tomato and lettuce.",
                new String[][]{{"tortilla","2","piece"},{"beans","200","g"},{"sweetcorn","100","g"},{"tomato","1","piece"},{"lettuce","30","g"}});
        addRecipe(db,"Cheese Toastie",
                "Place cheese between bread slices and toast in a pan until golden and melted.",
                new String[][]{{"bread","2","slice"},{"cheese","60","g"},{"butter","10","g"}});
        addRecipe(db,"Vegetable Rice",
                "Stir-fry onion, carrot and peas. Add cooked rice and soy sauce, then heat through.",
                new String[][]{{"rice","2","cup"},{"onion","1","piece"},{"carrot","1","piece"},{"peas","100","g"},{"soy sauce","15","ml"}});
        addRecipe(db,"Greek Salad",
                "Chop vegetables and cheese. Toss with olive oil and serve chilled.",
                new String[][]{{"cucumber","1","piece"},{"tomato","2","piece"},{"onion","0.5","piece"},{"feta","80","g"},{"olive oil","15","ml"}});
        addRecipe(db,"Garlic Butter Pasta",
                "Cook pasta. Melt butter with garlic, add pasta and toss. Finish with cheese if available.",
                new String[][]{{"pasta","200","g"},{"garlic","2","clove"},{"butter","30","g"},{"cheese","40","g"}});
        addRecipe(db,"Banana Pancakes",
                "Mash banana, mix with egg and flour, then cook small pancakes in a lightly oiled pan.",
                new String[][]{{"banana","2","piece"},{"egg","2","piece"},{"flour","100","g"},{"milk","100","ml"},{"oil","10","ml"}});
        addRecipe(db,"Chicken Salad",
                "Slice cooked chicken and toss with lettuce, cucumber and tomato. Dress and serve.",
                new String[][]{{"chicken","200","g"},{"lettuce","50","g"},{"cucumber","1","piece"},{"tomato","1","piece"},{"olive oil","10","ml"}});
        addRecipe(db,"Lentil Soup",
                "Sauté onion and carrot. Add lentils, tomato and water or stock. Simmer until tender.",
                new String[][]{{"lentils","200","g"},{"onion","1","piece"},{"carrot","1","piece"},{"tomato","2","piece"}});
        addRecipe(db,"Egg Fried Rice",
                "Scramble eggs, add cooked rice and peas, then stir-fry with soy sauce.",
                new String[][]{{"egg","2","piece"},{"rice","2","cup"},{"peas","100","g"},{"soy sauce","15","ml"}});
        addRecipe(db,"Tomato Egg Scramble",
                "Cook chopped tomato and onion, add beaten eggs and scramble until set.",
                new String[][]{{"egg","3","piece"},{"tomato","2","piece"},{"onion","1","piece"},{"oil","10","ml"}});
        addRecipe(db,"Cheesy Potato Bake",
                "Boil potatoes until nearly tender. Layer with cheese and milk, then bake until golden.",
                new String[][]{{"potato","4","piece"},{"cheese","100","g"},{"milk","150","ml"},{"butter","15","g"}});
    }

    private void addRecipe(SQLiteDatabase db,String name,String instructions,String[][] ingredients){
        ContentValues r=new ContentValues();
        r.put("name",name); r.put("instructions",instructions);
        long id=db.insert("recipes",null,r);
        for(String[] x:ingredients){
            ContentValues v=new ContentValues();
            v.put("recipe_id",id); v.put("ingredient_name",x[0]); v.put("required_quantity",Double.parseDouble(x[1])); v.put("unit",x[2]);
            db.insert("recipe_ingredients",null,v);
        }
    }
}
