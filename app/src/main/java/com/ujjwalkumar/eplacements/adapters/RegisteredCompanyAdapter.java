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
import com.ujjwalkumar.eplacements.models.RegisteredCompany;

import java.util.ArrayList;

public class RegisteredCompanyAdapter extends RecyclerView.Adapter<RegisteredCompanyAdapter.RegisteredCompanyViewHolder> {

    Context context;
    ArrayList<RegisteredCompany> al;

    public RegisteredCompanyAdapter(Context context, ArrayList<RegisteredCompany> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public RegisteredCompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_registered_companies, parent, false);
        return new RegisteredCompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisteredCompanyViewHolder holder, int position) {
        RegisteredCompany obj = al.get(position);
        holder.textViewCompanyName.setText(obj.getName());
        holder.textViewJobProfile.setText(obj.getProfile());
        holder.textViewCTC.setText(obj.getCtc());
        holder.textViewTimestamp.setText(obj.getTimestamp());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CompanyDetailsActivity.class);
            intent.putExtra("name", obj.getName());
            intent.putExtra("id", obj.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class RegisteredCompanyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCompanyName, textViewJobProfile, textViewCTC, textViewTimestamp;

        public RegisteredCompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCompanyName = itemView.findViewById(R.id.textViewCompanyName);
            textViewJobProfile = itemView.findViewById(R.id.textViewJobProfile);
            textViewCTC = itemView.findViewById(R.id.textViewCTC);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
