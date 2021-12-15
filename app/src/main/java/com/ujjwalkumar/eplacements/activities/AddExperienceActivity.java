package com.ujjwalkumar.eplacements.activities;

import static xute.markdeditor.Styles.TextComponentStyle.BLOCKQUOTE;
import static xute.markdeditor.Styles.TextComponentStyle.H1;
import static xute.markdeditor.Styles.TextComponentStyle.H3;
import static xute.markdeditor.components.TextComponentItem.MODE_PLAIN;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.databinding.ActivityAddExperienceBinding;

import java.util.ArrayList;

import xute.markdeditor.EditorControlBar;
import xute.markdeditor.datatype.DraftDataItemModel;
import xute.markdeditor.models.DraftModel;
import xute.markdeditor.utilities.FilePathUtils;

public class AddExperienceActivity extends AppCompatActivity implements EditorControlBar.EditorControlListener {

    private static final int REQUEST_IMAGE_SELECTOR = 930;
    private ActivityAddExperienceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExperienceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.buttonAdd.setOnClickListener(view -> {
            DraftModel dm = binding.mdEditor.getDraft();
            String json = new Gson().toJson(dm);
            Log.d("MarkDEditor", json);
        });

        binding.mdEditor.configureEditor(
                "https://api.hapramp.com/api/v2/",
                "",
                true,
                "Start Here...",
                BLOCKQUOTE
        );
        binding.mdEditor.loadDraft(getDraftContent(""));
        binding.controlBar.setEditorControlListener(this);
        binding.controlBar.setEditor(binding.mdEditor);
    }

    private DraftModel getDraftContent(String str) {
        if(!str.equals("")) {
            return (new Gson().fromJson(str, DraftModel.class));
        }

        ArrayList<DraftDataItemModel> contentTypes = new ArrayList<>();
        DraftDataItemModel heading = new DraftDataItemModel();
        heading.setItemType(DraftModel.ITEM_TYPE_TEXT);
        heading.setContent("Google Inc");
        heading.setMode(MODE_PLAIN);
        heading.setStyle(H1);

        DraftDataItemModel sub_heading = new DraftDataItemModel();
        sub_heading.setItemType(DraftModel.ITEM_TYPE_TEXT);
        sub_heading.setContent("Aman Gupta");
        sub_heading.setMode(MODE_PLAIN);
        sub_heading.setStyle(H3);

        contentTypes.add(heading);
        contentTypes.add(sub_heading);
        return new DraftModel(contentTypes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECTOR) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
                String filePath = FilePathUtils.getPath(this, uri);
                addImage(filePath);
            }
        }
    }

    public void addImage(String filePath) {
        binding.mdEditor.insertImage(filePath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_IMAGE_SELECTOR:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                    //Toast.makeText()"Permission not granted to access images.");
                }
                break;
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
        binding.mdEditor.addLink("Click Here", "http://www.hapramp.com");
    }
}