package com.ujjwalkumar.eplacements.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.models.Grievance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GrievanceAdapter extends RecyclerView.Adapter<GrievanceAdapter.GrievanceViewHolder> {

    Context context;
    String token;
    ArrayList<Grievance> al;

    public GrievanceAdapter(Context context, String token, ArrayList<Grievance> al) {
        this.context = context;
        this.token = token;
        this.al = al;
    }

    @NonNull
    @Override
    public GrievanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_grievances, parent, false);
        return new GrievanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrievanceViewHolder holder, int position) {
        Grievance obj = al.get(position);
        holder.textViewName.setText(obj.getName());
        holder.textViewEmail.setText(obj.getEmail());
        holder.textViewRegNo.setText(obj.getReg_no());
        holder.textViewMessage.setText(obj.getMessage());
        holder.textViewTimestamp.setText(obj.getTimeString());

        if(obj.getStatus().equals("resolved"))
            holder.textViewResolve.setVisibility(View.GONE);
        else
            holder.textViewResolve.setVisibility(View.VISIBLE);

        holder.layout.setOnClickListener(view -> holder.textViewMessage.setMaxLines(1000));

        holder.textViewEmail.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + obj.getEmail()));
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);
        });

        holder.textViewResolve.setOnClickListener(view -> showInformation(holder.textViewResolve, obj.getId()));
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class GrievanceViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        TextView textViewName, textViewEmail, textViewRegNo, textViewMessage, textViewTimestamp, textViewResolve;

        public GrievanceViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewRegNo = itemView.findViewById(R.id.textViewRegNo);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            textViewResolve = itemView.findViewById(R.id.textViewResolve);
        }
    }

    private void showInformation(TextView textView, String id) {
        textView.setText("Updating Status");
        String url = context.getString(R.string.base_url) + "admin/resolveGrievance";
        JSONObject postData = new JSONObject();
        try {
            postData.put("id", id);
            postData.put("status", "resolved");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            textView.setText("Resolved");
                            Toast.makeText(context, "Marked as resolved", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", token);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
}
