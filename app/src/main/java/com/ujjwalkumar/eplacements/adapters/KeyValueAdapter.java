package com.ujjwalkumar.eplacements.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ujjwalkumar.eplacements.R;

import java.util.ArrayList;

public class KeyValueAdapter extends RecyclerView.Adapter<KeyValueAdapter.KeyValueViewHolder> {

    Context context;
    ArrayList<Pair<String, String>> al;

    public KeyValueAdapter(Context context, ArrayList<Pair<String, String>> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public KeyValueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_key_value, parent, false);
        return new KeyValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyValueViewHolder holder, int position) {
        Pair<String, String> obj = al.get(position);
        holder.textViewKey.setText(obj.first);
        holder.textViewValue.setText(obj.second);
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class KeyValueViewHolder extends RecyclerView.ViewHolder {

        TextView textViewKey, textViewValue;

        public KeyValueViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewKey = itemView.findViewById(R.id.textViewKey);
            textViewValue = itemView.findViewById(R.id.textViewValue);
        }
    }
}
