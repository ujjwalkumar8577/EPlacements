package com.ujjwalkumar.eplacements.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.models.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    Context context;
    ArrayList<Contact> al;

    public ContactAdapter(Context context, ArrayList<Contact> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_contacts, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact obj = al.get(position);
        holder.textViewName.setText(obj.getName());
        holder.textViewRole.setText(obj.getRole());
        holder.textViewDegreeCourse.setText(obj.getDegreeCourse());
        holder.textViewPhone.setText(obj.getPhone());
        holder.textViewEmail.setText(obj.getEmail());

        Glide.with(context)
                .load(obj.getPhoto())
                .placeholder(R.drawable.passportphoto)
                .centerCrop()
                .into(holder.imageView);

        holder.textViewPhone.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + obj.getPhone()));
            context.startActivity(intent);
        });

        holder.textViewEmail.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, obj.getEmail());
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewRole, textViewDegreeCourse, textViewPhone, textViewEmail;
        ImageView imageView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            textViewDegreeCourse = itemView.findViewById(R.id.textViewDegreeCourse);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
