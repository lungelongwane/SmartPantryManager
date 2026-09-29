package com.lungelo.smartpantrymanager;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

public final class Navigation {
    private Navigation(){}
    public static boolean handle(Activity a,MenuItem item){
        Intent i=null;
        if(item.getItemId()==R.id.menu_pantry)i=new Intent(a,PantryActivity.class);
        else if(item.getItemId()==R.id.menu_suggestions)i=new Intent(a,SuggestedRecipesActivity.class);
        else if(item.getItemId()==R.id.menu_settings)i=new Intent(a,SettingsActivity.class);
        if(i!=null){a.startActivity(i);return true;} return false;
    }
}
