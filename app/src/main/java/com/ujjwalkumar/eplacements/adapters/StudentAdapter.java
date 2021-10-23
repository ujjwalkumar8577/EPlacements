package com.ujjwalkumar.eplacements.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.activities.CompleteProfileActivity;
import com.ujjwalkumar.eplacements.models.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    Context context;
    ArrayList<Student> al;

    public StudentAdapter(Context context, ArrayList<Student> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_students, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student obj = al.get(position);
        holder.textViewName.setText(obj.getName());
        holder.textViewDegreeCourse.setText(obj.getDegreeCourse());
        holder.textViewRegNo.setText(obj.getRegNo());
        String status = obj.getStatus().substring(0, 1).toUpperCase() + obj.getStatus().substring(1);
        holder.textViewStatus.setText(status);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CompleteProfileActivity.class);
            intent.putExtra("name", obj.getName());
            intent.putExtra("regno", obj.getRegNo());
            intent.putExtra("status", obj.getStatus());
            intent.putExtra("flag", "admin");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDegreeCourse, textViewRegNo, textViewStatus;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDegreeCourse = itemView.findViewById(R.id.textViewDegreeCourse);
            textViewRegNo = itemView.findViewById(R.id.textViewRegNo);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
