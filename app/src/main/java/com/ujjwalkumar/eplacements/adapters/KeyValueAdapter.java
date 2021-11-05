package com.ujjwalkumar.eplacements.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        if(obj.first.contains("Link")) {
            holder.textViewValue.setTextColor(context.getResources().getColor(R.color.blue));
            holder.textViewValue.setOnClickListener(view -> {
                String url = obj.second;
                if (url.startsWith("https://") || url.startsWith("http://")) {
                    Uri uri = Uri.parse(url);
                    context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }else
                    Toast.makeText(context, "Invalid Url", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class KeyValueViewHolder extends RecyclerView.ViewHolder {

        TextView textViewKey, textViewValue;

        public KeyValueViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewKey = itemView.findViewById(R.id.textViewKey);
            textViewValue = itemView.findViewById(R.id.textViewValue);
        }
    }
}
