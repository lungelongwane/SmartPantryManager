package com.lungelo.smartpantrymanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lungelo.smartpantrymanager.R;
import com.lungelo.smartpantrymanager.models.Recipe;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{
    public interface Listener{void onRecipeClick(Recipe recipe);}
    private final List<Recipe> recipes; private final Listener listener;
    public RecipeAdapter(List<Recipe> recipes,Listener listener){this.recipes=recipes;this.listener=listener;}
    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup p,int v){
        return new ViewHolder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_recipe,p,false));
    }
    @Override public void onBindViewHolder(@NonNull ViewHolder h,int pos){
        Recipe r=recipes.get(pos); h.name.setText(r.getName()); h.itemView.setOnClickListener(v->listener.onRecipeClick(r));
    }
    @Override public int getItemCount(){return recipes.size();}
    static class ViewHolder extends RecyclerView.ViewHolder{TextView name; ViewHolder(View v){super(v);name=v.findViewById(R.id.txtRecipeName);}}
}
