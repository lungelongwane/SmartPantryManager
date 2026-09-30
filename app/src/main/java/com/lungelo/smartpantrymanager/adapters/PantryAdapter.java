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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    public interface Listener {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }

    private final List<Ingredient> items;
    private final Listener listener;

    public PantryAdapter(
            List<Ingredient> items,
            Listener listener) {

        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        return new ViewHolder(
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_ingredient,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Ingredient ingredient =
                items.get(position);

        holder.name.setText(
                ingredient.getName()
        );

        holder.quantity.setText(
                "Quantity: "
                        + format(
                        ingredient.getQuantity()
                )
                        + " "
                        + ingredient.getUnit()
        );

        String expiry =
                ingredient.getExpiryDate();

        if (expiry == null
                || expiry.trim().isEmpty()) {

            holder.expiry.setText(
                    "Expiry: Not provided"
            );

        } else {

            holder.expiry.setText(
                    "Expiry: "
                            + formatExpiryDate(expiry)
            );
        }

        holder.edit.setOnClickListener(
                v -> listener.onEdit(ingredient)
        );

        holder.delete.setOnClickListener(
                v -> listener.onDelete(ingredient)
        );
    }

    private String format(double number) {

        if (number == Math.rint(number)) {

            return String.valueOf(
                    (int) number
            );
        }

        return String.valueOf(number);
    }

    private String formatExpiryDate(String date) {

        SimpleDateFormat inputFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        inputFormat.setLenient(false);

        SimpleDateFormat outputFormat =
                new SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                );

        try {

            Date parsed =
                    inputFormat.parse(date);

            if (parsed != null) {

                return outputFormat.format(
                        parsed
                );
            }

        } catch (ParseException ignored) {
            // Keep the original value if parsing fails.
        }

        return date;
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView name;
        TextView quantity;
        TextView expiry;

        MaterialButton edit;
        MaterialButton delete;

        ViewHolder(View view) {

            super(view);

            name =
                    view.findViewById(
                            R.id.txtName
                    );

            quantity =
                    view.findViewById(
                            R.id.txtQuantity
                    );

            expiry =
                    view.findViewById(
                            R.id.txtExpiry
                    );

            edit =
                    view.findViewById(
                            R.id.btnEdit
                    );

            delete =
                    view.findViewById(
                            R.id.btnDelete
                    );
        }
    }
}

