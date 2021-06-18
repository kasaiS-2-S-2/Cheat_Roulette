package com.kasai.cheatroulette;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class RatingDialogFragment extends DialogFragment {
    boolean isInvokedManually;

    public RatingDialogFragment(boolean isInvokedManually) {
        this.isInvokedManually = isInvokedManually;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder ratingAlertDialogBuilder = new AlertDialog.Builder(getActivity());
        AlertDialog ratingDialog;
        LayoutInflater inflater = getLayoutInflater();
        View ratingDialogLayout = inflater.inflate(R.layout.rating_dialog, null);
        Activity activity = getActivity();

        if (this.isInvokedManually) {
            //メニューから呼ばれた場合の処理
            ratingDialog = ratingAlertDialogBuilder
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(
                                    "https://play.google.com/store/apps/details?id=com.kasai.cheatroulette"));
                            intent.setPackage("com.android.vending");
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // ダイアログがキャンセルされた際の処理
                        }
                    })
                    .create();
        } else {
            //onCreateで呼ばれた場合の処理
            ratingDialog = ratingAlertDialogBuilder
                    .setPositiveButton("評価する", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(
                                    "https://play.google.com/store/apps/details?id=com.kasai.cheatroulette"));
                            intent.setPackage("com.android.vending");
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("今後表示しない", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean(getString(R.string.saved_dialog_never_invoke_key), true);
                            editor.apply();
                        }
                    })
                    .setNegativeButton("あとでする", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // ダイアログがキャンセルされた際の処理
                        }
                    })
                    .create();
        }

        RatingBar ratingBar = ratingDialogLayout.findViewById(R.id.dialog_rating_rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=com.kasai.cheatroulette"));
                intent.setPackage("com.android.vending");
                startActivity(intent);
                ratingDialog.dismiss();
            }
        });

        ratingDialog.setView(ratingDialogLayout);

        return ratingDialog;
    }
}

