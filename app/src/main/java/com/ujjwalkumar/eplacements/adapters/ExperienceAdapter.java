package com.ujjwalkumar.eplacements.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.activities.student.AddExperienceActivity;
import com.ujjwalkumar.eplacements.models.Experience;

import java.util.ArrayList;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder> {

    Context context;
    ArrayList<Experience> al;

    public ExperienceAdapter(Context context, ArrayList<Experience> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_experiences, parent, false);
        return new ExperienceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder holder, int position) {
        Experience obj = al.get(position);
        holder.textViewTitle.setText(obj.getCompany_name());
        holder.textViewContent.setText(obj.getStudent_name());
        holder.textViewTimestamp.setText("Added on " + obj.getTimeString());

        holder.layout.setOnClickListener(view -> {
            Gson gson = new Gson();
            Intent intent = new Intent(context, AddExperienceActivity.class);
            intent.putExtra("action", "view");
            intent.putExtra("data", gson.toJson(obj));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class ExperienceViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        TextView textViewTitle, textViewContent, textViewTimestamp;

        public ExperienceViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
