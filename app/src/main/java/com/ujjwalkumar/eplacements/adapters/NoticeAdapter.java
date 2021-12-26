package com.ujjwalkumar.eplacements.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.models.Notice;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    Context context;
    ArrayList<Notice> al;

    public NoticeAdapter(Context context, ArrayList<Notice> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_notices, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice obj = al.get(position);
        holder.textViewTitle.setText(obj.getTitle());
        holder.textViewContent.setText(obj.getContent());
        holder.textViewTimestamp.setText(obj.getTimeString());

        holder.layout.setOnClickListener(view -> holder.textViewContent.setMaxLines(1000));
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        TextView textViewTitle, textViewContent, textViewTimestamp;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
