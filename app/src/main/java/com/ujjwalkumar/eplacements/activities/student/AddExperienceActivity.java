package com.ujjwalkumar.eplacements.activities.student;

import static xute.markdeditor.Styles.TextComponentStyle.BLOCKQUOTE;
import static xute.markdeditor.Styles.TextComponentStyle.H1;
import static xute.markdeditor.Styles.TextComponentStyle.H3;
import static xute.markdeditor.components.TextComponentItem.MODE_PLAIN;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityAddExperienceBinding;

import java.util.ArrayList;

import xute.markdeditor.EditorControlBar;
import xute.markdeditor.datatype.DraftDataItemModel;
import xute.markdeditor.models.DraftModel;
import xute.markdeditor.utilities.FilePathUtils;

public class AddExperienceActivity extends AppCompatActivity implements EditorControlBar.EditorControlListener {

    private static final int REQUEST_IMAGE_SELECTOR = 930;
    private String action = "";
    private String company_name = "";
    private String name = "";
    private ActivityAddExperienceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExperienceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.mdEditor.configureEditor(
                "https://api.hapramp.com/api/v2/",
                "",
                true,
                "Start Here...",
                BLOCKQUOTE
        );

        action = getIntent().getStringExtra("action");
        if(action.equals("add")) {
            company_name = getIntent().getStringExtra("company_name");
            name = getIntent().getStringExtra("name");
            binding.mdEditor.loadDraft(getDraftContent(""));
        }
        else {
            binding.mdEditor.loadDraft(getDraftContent(""));
        }

        binding.controlBar.setEditorControlListener(this);
        binding.controlBar.setEditor(binding.mdEditor);

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.buttonAdd.setOnClickListener(view -> {
            DraftModel dm = binding.mdEditor.getDraft();
            String json = new Gson().toJson(dm);
            Log.d("MarkDEditor", json);
        });
    }

    private DraftModel getDraftContent(String str) {
        if(!str.equals("")) {
            return (new Gson().fromJson(str, DraftModel.class));
        }

        ArrayList<DraftDataItemModel> contentTypes = new ArrayList<>();
        DraftDataItemModel heading = new DraftDataItemModel();
        heading.setItemType(DraftModel.ITEM_TYPE_TEXT);
        heading.setContent(company_name);
        heading.setMode(MODE_PLAIN);
        heading.setStyle(H1);

        DraftDataItemModel sub_heading = new DraftDataItemModel();
        sub_heading.setItemType(DraftModel.ITEM_TYPE_TEXT);
        sub_heading.setContent(name);
        sub_heading.setMode(MODE_PLAIN);
        sub_heading.setStyle(H3);

        DraftDataItemModel separator = new DraftDataItemModel();
        separator.setItemType(DraftModel.ITEM_TYPE_HR);

        DraftDataItemModel main_content = new DraftDataItemModel();
        main_content.setItemType(DraftModel.ITEM_TYPE_TEXT);
        main_content.setContent("");
        main_content.setMode(MODE_PLAIN);

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
                String filePath = FilePathUtils.getPath(AddExperienceActivity.this, uri);
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
                Toast.makeText(AddExperienceActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
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
        openGallery();
    }

    @Override
    public void onInserLinkClicked() {
        LayoutInflater li = LayoutInflater.from(AddExperienceActivity.this);
        View promptsView = li.inflate(R.layout.dialog_add_link, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddExperienceActivity.this);
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
                                Toast.makeText(AddExperienceActivity.this, "Empty text or link", Toast.LENGTH_SHORT).show();
                            else
                                binding.mdEditor.addLink(text, url);
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(AddExperienceActivity.this, R.color.gray));
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
    }
}