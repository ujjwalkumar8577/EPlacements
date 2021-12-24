package com.ujjwalkumar.eplacements.activities.admin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.activities.student.LoginActivity;
import com.ujjwalkumar.eplacements.databinding.ActivityAdminMyAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AdminMyAccountActivity extends AppCompatActivity {

    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final int PICK_IMAGE_REQUEST = 11;
    private String photoURL;
    private ActivityAdminMyAccountBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMyAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
        }

        photoURL = "";
        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.buttonLogout.setOnClickListener(view -> {
            user.edit().clear().apply();
            Intent in = new Intent();
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(in);
            finishAffinity();
        });

        binding.textViewChangePassword.setOnClickListener(view -> {
            LayoutInflater li = LayoutInflater.from(AdminMyAccountActivity.this);
            View promptsView = li.inflate(R.layout.dialog_change_password, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminMyAccountActivity.this);
            alertDialogBuilder.setView(promptsView);

            final EditText userInput1 = promptsView.findViewById(R.id.editTextDialogUserInput1);
            final EditText userInput2 = promptsView.findViewById(R.id.editTextDialogUserInput2);

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Change",
                            (dialog, id) -> {
                                String currentPassword = userInput1.getText().toString();
                                String newPassword = userInput2.getText().toString();
                                if(currentPassword.equals("") || newPassword.equals(""))
                                    Toast.makeText(AdminMyAccountActivity.this, "Fields required", Toast.LENGTH_SHORT).show();
                                else
                                    changePassword(currentPassword, newPassword);
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            alertDialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(AdminMyAccountActivity.this, R.color.gray));
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        });

        binding.imageViewEdit.setOnClickListener(view -> {
            Intent selectIntent = new Intent();
            selectIntent.setType("image/*");
            selectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(selectIntent, "Select image"), PICK_IMAGE_REQUEST);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10 && grantResults[0]==1)
            Toast.makeText(AdminMyAccountActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(AdminMyAccountActivity.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                binding.imageViewPhoto.setImageBitmap(bitmap);
                uploadFile(filePath, "photo", user.getString("id", "tmp"+ new Random().nextInt(10000)));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showInformation() {
        binding.textViewName.setText(user.getString("name", ""));
        binding.textViewEmail.setText(user.getString("id", ""));
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            photoURL = response.getJSONObject("user").getString("photoURL");
                            Glide.with(AdminMyAccountActivity.this)
                                    .load(photoURL)
                                    .placeholder(R.drawable.passportphoto)
                                    .centerCrop()
                                    .into(binding.imageViewPhoto);
                        }
                        else
                            Toast.makeText(AdminMyAccountActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminMyAccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void changePassword(String currentPassword, String newPassword) {
        String url = getString(R.string.base_url) + "admin/changePassword";
        JSONObject postData = new JSONObject();
        try {
            postData.put("current_password", currentPassword);
            postData.put("new_password", newPassword);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            String token = response.getString("token");
                            user.edit().putString("token", token).apply();
                            Toast.makeText(AdminMyAccountActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(AdminMyAccountActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminMyAccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void uploadFile(Uri filePath, String folderName, String fileName) {
        if (filePath != null) {
            // showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading file ...");
            progressDialog.show();

            // uploading file and adding listeners on upload or failure of image
            StorageReference reference = firebaseStorage.getReference("admins").child(folderName).child(fileName);
            reference.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(AdminMyAccountActivity.this, "Upload success", Toast.LENGTH_SHORT).show();
                    })

                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(AdminMyAccountActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    })

                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    })

                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String downloadURL = uri.toString();
                                updatePhoto(downloadURL);
                            });
                        }
                    });
        }
    }

    private void updatePhoto(String downloadURL) {
        String url = getString(R.string.base_url) + "admin/updateAdmin";
        JSONObject postData = new JSONObject();
        try {
            postData.put("photoURL", downloadURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            Toast.makeText(AdminMyAccountActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            photoURL = downloadURL;
                        }
                        else
                            Toast.makeText(AdminMyAccountActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminMyAccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}