package com.ujjwalkumar.eplacements.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityMyAccountBinding;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAccountActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;
    private String encodedImage, encodedResume;
    private ActivityMyAccountBinding binding;
    private SharedPreferences user;
    private Object userObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
        }

        encodedImage = "";
        encodedResume = "";
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
            
        });

        binding.imageViewEdit.setOnClickListener(view -> {
            Intent selectIntent = new Intent();
            selectIntent.setType("image/*");
            selectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(selectIntent, "Select image"), PICK_IMAGE_REQUEST);
        });

        binding.buttonViewResume.setOnClickListener(view -> {

        });

        binding.buttonUpdateResume.setOnClickListener(view -> {
            Intent selectIntent = new Intent();
            selectIntent.setType("application/pdf");
            selectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(selectIntent, "Select image"), PICK_IMAGE_REQUEST);
        });

        binding.buttonDetails.setOnClickListener(view -> {
            if(userObj!=null) {
                Gson gson = new Gson();
                String str = gson.toJson(userObj);
                Intent intent = new Intent(MyAccountActivity.this, CompleteProfileActivity.class);
                intent.putExtra("userObj", str);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10 && grantResults[0]==1)
            Toast.makeText(MyAccountActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MyAccountActivity.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            Uri filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                binding.imageViewPhoto.setImageBitmap(bitmap);

                // Encode bitmap to String
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showInformation() {
        binding.textViewName.setText(user.getString("name", ""));
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            userObj = response.getJSONObject("user");
                            JSONArray photo = response.getJSONObject("user").getJSONObject("photo").getJSONObject("data").getJSONArray("data");
                            byte[] imageBytes = new byte[photo.length()];
                            for(int i=0; i<photo.length(); i++) {
                                imageBytes[i] = (byte)photo.getInt(i);
                            }
                            Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            binding.imageViewPhoto.setImageBitmap(bmp);
                        }
                        else
                            Toast.makeText(MyAccountActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MyAccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}