package com.lungelo.smartpantrymanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.lungelo.smartpantrymanager.R;
import com.lungelo.smartpantrymanager.models.Ingredient;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {
    public interface Listener { void onEdit(Ingredient ingredient); void onDelete(Ingredient ingredient); }
    private final List<Ingredient> items;
    private final Listener listener;
    public PantryAdapter(List<Ingredient> items,Listener listener){this.items=items;this.listener=listener;}

    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient,parent,false));
    }
    @Override public void onBindViewHolder(@NonNull ViewHolder h,int position){
        Ingredient i=items.get(position);
        h.name.setText(i.getName());
        h.quantity.setText("Quantity: "+format(i.getQuantity())+" "+i.getUnit());
        h.expiry.setText(i.getExpiryDate()==null||i.getExpiryDate().trim().isEmpty()?"Expiry: Not provided":"Expiry: "+i.getExpiryDate());
        h.edit.setOnClickListener(v->listener.onEdit(i));
        h.delete.setOnClickListener(v->listener.onDelete(i));
    }
    private String format(double n){return n==Math.rint(n)?String.valueOf((int)n):String.valueOf(n);}
    @Override public int getItemCount(){return items.size();}
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,quantity,expiry; MaterialButton edit,delete;
        ViewHolder(View v){super(v);name=v.findViewById(R.id.txtName);quantity=v.findViewById(R.id.txtQuantity);expiry=v.findViewById(R.id.txtExpiry);edit=v.findViewById(R.id.btnEdit);delete=v.findViewById(R.id.btnDelete);}
    }
}
