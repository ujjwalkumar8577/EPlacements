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
import com.ujjwalkumar.eplacements.activities.CompanyDetailsActivity;
import com.ujjwalkumar.eplacements.models.UpcomingCompany;

import java.util.ArrayList;

public class UpcomingCompanyAdapter extends RecyclerView.Adapter<UpcomingCompanyAdapter.UpcomingCompanyViewHolder> {

    Context context;
    ArrayList<UpcomingCompany> al;

    public UpcomingCompanyAdapter(Context context, ArrayList<UpcomingCompany> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public UpcomingCompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_upcoming_companies, parent, false);
        return new UpcomingCompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingCompanyViewHolder holder, int position) {
        UpcomingCompany obj = al.get(position);
        holder.textViewCompanyName.setText(obj.getName());
        holder.textViewJobProfile.setText(obj.getProfile());
        holder.textViewCTC.setText(obj.getCtc());
        holder.textViewDeadline.setText(obj.getDeadline());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CompanyDetailsActivity.class);
                intent.putExtra("name", obj.getName());
                intent.putExtra("id", obj.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class UpcomingCompanyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCompanyName, textViewJobProfile, textViewCTC, textViewDeadline;

        public UpcomingCompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCompanyName = itemView.findViewById(R.id.textViewCompanyName);
            textViewJobProfile = itemView.findViewById(R.id.textViewJobProfile);
            textViewCTC = itemView.findViewById(R.id.textViewCTC);
            textViewDeadline = itemView.findViewById(R.id.textViewDeadline);
        }
    }
}
