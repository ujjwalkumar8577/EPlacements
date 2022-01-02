package com.ujjwalkumar.eplacements.activities.student;

import static xute.markdeditor.Styles.TextComponentStyle.BLOCKQUOTE;
import static xute.markdeditor.Styles.TextComponentStyle.H1;
import static xute.markdeditor.Styles.TextComponentStyle.H3;
import static xute.markdeditor.components.TextComponentItem.MODE_PLAIN;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityAddExperienceBinding;
import com.ujjwalkumar.eplacements.models.Experience;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xute.markdeditor.EditorControlBar;
import xute.markdeditor.datatype.DraftDataItemModel;
import xute.markdeditor.models.DraftModel;
import xute.markdeditor.utilities.FilePathUtils;

public class EditExperienceActivity extends AppCompatActivity implements EditorControlBar.EditorControlListener {

    private static final int REQUEST_IMAGE_SELECTOR = 930;
    private String action = "";
    private String data = "";
    private ActivityAddExperienceBinding binding;
    private SharedPreferences user;
    private Experience experience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExperienceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);

        action = getIntent().getStringExtra("action");
        data = getIntent().getStringExtra("data");
        Gson gson = new Gson();
        experience = gson.fromJson(data, Experience.class);

        binding.mdEditor.configureEditor(
                "https://api.hapramp.com/api/v2/",
                "",
                true,
                "Start Here...",
                BLOCKQUOTE
        );

        if(action.equals("view")) {
            binding.textView3.setText(experience.getCompany_name());
            binding.controlBar.setVisibility(View.GONE);
            binding.buttonAdd.setVisibility(View.GONE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        else {
            binding.controlBar.setEditorControlListener(this);
            binding.controlBar.setEditor(binding.mdEditor);
        }

        binding.mdEditor.loadDraft(getDraftContent());

        binding.imageViewBack.setOnClickListener(view -> super.onBackPressed());

        binding.buttonAdd.setOnClickListener(view -> {
            DraftModel dm = binding.mdEditor.getDraft();
            experience.setDesc(gson.toJson(dm));
            addExperience();
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private DraftModel getDraftContent() {
        if(action.equals("view") || action.equals("edit")) {
            Gson gson = new Gson();
            return gson.fromJson(experience.getDesc(), DraftModel.class);
        }

        ArrayList<DraftDataItemModel> contentTypes = new ArrayList<>();
        DraftDataItemModel heading = new DraftDataItemModel();
        heading.setItemType(DraftModel.ITEM_TYPE_TEXT);
        heading.setContent(experience.getCompany_name());
        heading.setMode(MODE_PLAIN);
        heading.setStyle(H1);

        DraftDataItemModel sub_heading = new DraftDataItemModel();
        sub_heading.setItemType(DraftModel.ITEM_TYPE_TEXT);
        sub_heading.setContent(experience.getStudent_name());
        sub_heading.setMode(MODE_PLAIN);
        sub_heading.setStyle(H3);

        DraftDataItemModel separator = new DraftDataItemModel();
        separator.setItemType(DraftModel.ITEM_TYPE_HR);

        DraftDataItemModel main_content = new DraftDataItemModel();
        try {
            main_content.setItemType(DraftModel.ITEM_TYPE_TEXT);
            main_content.setContent(experience.getDesc());
            main_content.setMode(MODE_PLAIN);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        contentTypes.add(heading);
        contentTypes.add(sub_heading);
        contentTypes.add(separator);
        contentTypes.add(main_content);
        return new DraftModel(contentTypes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECTOR) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
                String filePath = FilePathUtils.getPath(EditExperienceActivity.this, uri);
                addImage(filePath);
            }
        }
    }

    public void addImage(String filePath) {
        binding.mdEditor.insertImage(filePath, false, "caption");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_SELECTOR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(EditExperienceActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInsertImageClicked() {
        Toast.makeText(EditExperienceActivity.this, "Add image under development", Toast.LENGTH_SHORT).show();
//        openGallery();
    }

    @Override
    public void onInserLinkClicked() {
        LayoutInflater li = LayoutInflater.from(EditExperienceActivity.this);
        View promptsView = li.inflate(R.layout.dialog_add_link, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditExperienceActivity.this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput1 = promptsView.findViewById(R.id.editTextDialogUserInput1);
        final EditText userInput2 = promptsView.findViewById(R.id.editTextDialogUserInput2);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Insert",
                        (dialog, id) -> {
                            String text = userInput1.getText().toString();
                            String url = userInput2.getText().toString();
                            if(text.equals("") || url.equals(""))
                                Toast.makeText(EditExperienceActivity.this, "Empty text or link", Toast.LENGTH_SHORT).show();
                            else
                                binding.mdEditor.addLink(text, url);
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(EditExperienceActivity.this, R.color.gray));
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
    }

    private void addExperience() {
        String url = getString(R.string.base_url) + "student/addExperience";
        JSONObject postData = new JSONObject();
        try {
            postData.put("year", experience.getYear());
            postData.put("reg_no", experience.getReg_no());
            postData.put("student_name", experience.getStudent_name());
            postData.put("company_name", experience.getCompany_name());
            postData.put("job_profile", experience.getJob_profile());
            postData.put("job_location", experience.getJob_location());
            postData.put("desc", experience.getDesc());
            postData.put("rating", experience.getRating());
            postData.put("timestamp", experience.getTimestamp());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            Toast.makeText(EditExperienceActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(EditExperienceActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(EditExperienceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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