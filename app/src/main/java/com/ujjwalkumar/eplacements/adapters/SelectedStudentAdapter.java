package com.ujjwalkumar.eplacements.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.utilities.Triplet;

import java.util.ArrayList;

public class SelectedStudentAdapter extends RecyclerView.Adapter<SelectedStudentAdapter.SelectedStudentViewHolder> {

    Context context;
    ArrayList<Triplet> al;

    public SelectedStudentAdapter(Context context, ArrayList<Triplet> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public SelectedStudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_selected_students, parent, false);
        return new SelectedStudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedStudentViewHolder holder, int position) {
        Triplet obj = al.get(position);
        holder.textViewName.setText(obj.first);
        holder.textViewRegNo.setText(obj.second);

        holder.imageViewRemove.setOnClickListener(view -> {
            al.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class SelectedStudentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewRegNo;
        ImageView imageViewRemove;

        public SelectedStudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRegNo = itemView.findViewById(R.id.textViewRegNo);
            imageViewRemove = itemView.findViewById(R.id.imageViewRemove);
        }
    }
}
