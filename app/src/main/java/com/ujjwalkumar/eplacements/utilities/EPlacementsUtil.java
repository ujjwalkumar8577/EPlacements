package com.ujjwalkumar.eplacements.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ujjwalkumar.eplacements.R;

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

        textViewCancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
    }
}
