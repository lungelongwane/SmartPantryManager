package com.lungelo.smartpantrymanager.utils;

import com.lungelo.smartpantrymanager.database.DatabaseHelper;
import com.lungelo.smartpantrymanager.models.Ingredient;
import com.lungelo.smartpantrymanager.models.Recipe;
import com.lungelo.smartpantrymanager.models.RecipeIngredient;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecipeMatcher {
    private final DatabaseHelper db;
    public RecipeMatcher(DatabaseHelper db){this.db=db;}

    public List<Recipe> getStrictMatches(List<Recipe> recipes,List<Ingredient> pantry){
        java.util.ArrayList<Recipe> matches=new java.util.ArrayList<>();
        for(Recipe recipe:recipes){
            boolean qualifies=true;
            for(RecipeIngredient required:db.getRecipeIngredients(recipe.getId())){
                double available=0;
                for(Ingredient item:pantry){
                    if(sameIngredient(item.getName(),required.getName())){
                        double converted=convertToBase(item.getQuantity(),item.getUnit(),required.getUnit());
                        if(converted>=0) available+=converted;
                    }
                }
                double needed=convertToBase(required.getQuantity(),required.getUnit(),required.getUnit());
                if(available < needed){ qualifies=false; break; }
            }
            if(qualifies) matches.add(recipe);
        }
        return matches;
    }

    private boolean sameIngredient(String a,String b){
        return normalize(a).equals(normalize(b));
    }

    public static String normalize(String value){
        String s=value.toLowerCase(Locale.ROOT).trim();
        if(s.endsWith("ies") && s.length()>3) s=s.substring(0,s.length()-3)+"y";
        else if(s.endsWith("oes") && s.length()>3) s=s.substring(0,s.length()-2);
        else if(s.endsWith("s") && !s.endsWith("ss") && s.length()>2) s=s.substring(0,s.length()-1);
        return s;
    }

    private double convertToBase(double amount,String from,String target){
        String f=from.toLowerCase(Locale.ROOT).trim();
        String t=target.toLowerCase(Locale.ROOT).trim();
        if(f.equals(t)) return amount;
        if(isWeight(f)&&isWeight(t)) return grams(amount,f);
        if(isVolume(f)&&isVolume(t)) return millilitres(amount,f);
        if(isPieceUnit(f)&&isPieceUnit(t)) return amount;
        return -1;
    }

    private boolean isWeight(String u){return u.equals("g")||u.equals("kg")||u.equals("gram")||u.equals("grams")||u.equals("kilogram")||u.equals("kilograms");}
    private boolean isVolume(String u){return u.equals("ml")||u.equals("l")||u.equals("litre")||u.equals("litres")||u.equals("cup")||u.equals("cups");}
    private boolean isPieceUnit(String u){return u.equals("piece")||u.equals("pieces")||u.equals("pc")||u.equals("pcs")||u.equals("slice")||u.equals("slices")||u.equals("clove")||u.equals("cloves")||u.equals("can")||u.equals("cans");}
    private double grams(double amount,String unit){
        return unit.equals("kg")||unit.equals("kilogram")||unit.equals("kilograms") ? amount*1000 : amount;
    }
    private double millilitres(double amount,String unit){
        if(unit.equals("l")||unit.equals("litre")||unit.equals("litres")) return amount*1000;
        if(unit.equals("cup")||unit.equals("cups")) return amount*240;
        return amount;
    }
}
