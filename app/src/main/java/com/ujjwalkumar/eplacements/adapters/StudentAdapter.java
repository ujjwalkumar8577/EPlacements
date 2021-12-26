package com.ujjwalkumar.eplacements.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.activities.common.CompleteProfileActivity;
import com.ujjwalkumar.eplacements.models.Student;
import com.ujjwalkumar.eplacements.models.StudentProfile;
import com.ujjwalkumar.eplacements.utilities.EPlacementsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    Context context;
    ArrayList<Student> al;
    String token;

    public StudentAdapter(Context context, ArrayList<Student> al, String token) {
        this.context = context;
        this.al = al;
        this.token = token;
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

        holder.itemView.setOnClickListener(view -> getDetail(obj.getRegNo()));
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDegreeCourse, textViewRegNo, textViewStatus;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDegreeCourse = itemView.findViewById(R.id.textViewDegreeCourse);
            textViewRegNo = itemView.findViewById(R.id.textViewRegNo);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }

    private void getDetail(String reg_no) {
        EPlacementsUtil.showToast(context, "Loading student profile ...", R.drawable.outline_error_white_48dp);
        String url = context.getString(R.string.base_url) + "admin/getDetail";
        JSONObject postData = new JSONObject();
        try {
            postData.put("reg_no", reg_no);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            Gson gson = new Gson();
                            StudentProfile userObj = gson.fromJson(response.getJSONObject("student").toString(), StudentProfile.class);

                            Intent intent = new Intent(context, CompleteProfileActivity.class);
                            intent.putExtra("userObj", gson.toJson(userObj));
                            context.startActivity(intent);
                            ((Activity)context).finish();
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
