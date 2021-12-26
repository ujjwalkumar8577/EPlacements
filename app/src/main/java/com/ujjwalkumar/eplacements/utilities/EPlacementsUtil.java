package com.ujjwalkumar.eplacements.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.ujjwalkumar.eplacements.R;

import java.io.ByteArrayOutputStream;

public class EPlacementsUtil {

    public static void checkInternetConnection(Context context) {
        if(!isNetworkConnected(context))
            showNoInternetDialog(context);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static void showNoInternetDialog(Context context) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_no_internet, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        final TextView textViewSettings = promptsView.findViewById(R.id.textViewSettings);
        final TextView textViewCancel = promptsView.findViewById(R.id.textViewCancel);

        textViewSettings.setOnClickListener(view -> {
            alertDialog.dismiss();
            context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        });

        textViewCancel.setOnClickListener(view -> alertDialog.dismiss());
    }

    public static void showToast(Context context, String message, int resID) {
        View layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
        ImageView imageViewToast = layout.findViewById(R.id.imageViewToast);
        imageViewToast.setImageResource(resID);
        TextView text = layout.findViewById(R.id.textViewToast);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.BOTTOM, 50, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public Bitmap base64ToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}