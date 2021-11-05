package com.ujjwalkumar.eplacements.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

//        if(holder.textViewContent.getLineCount()>=2 && obj.getContent().charAt(holder.textViewContent.getLayout().getLineEnd(1))=='.')
//            holder.textViewMore.setVisibility(View.VISIBLE);
//        else
//            holder.textViewMore.setVisibility(View.GONE);

        holder.textViewMore.setOnClickListener(view -> {
            holder.textViewContent.setMaxLines(1000);
            holder.textViewMore.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewContent, textViewMore, textViewTimestamp;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewMore = itemView.findViewById(R.id.textViewMore);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
