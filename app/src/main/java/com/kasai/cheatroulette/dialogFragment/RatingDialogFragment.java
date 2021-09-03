package com.kasai.cheatroulette.dialogFragment;

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

import com.kasai.cheatroulette.R;

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
                    .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(
                                    getString(R.string.url_this_app_google_play_page)));
                            intent.setPackage(getString(R.string.package_com_android_vending));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.alert_dialog_negative_choice, new DialogInterface.OnClickListener() {
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
                    .setPositiveButton(getString(R.string.rating_alert_dialog_positive_choice), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(
                                    getString(R.string.url_this_app_google_play_page)));
                            intent.setPackage(getString(R.string.package_com_android_vending));
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton(R.string.rating_alert_dialog_neutral_choice, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean(getString(R.string.saved_dialog_never_invoke_key), true);
                            editor.apply();
                        }
                    })
                    .setNegativeButton(R.string.rating_alert_dialog_negative_choice, new DialogInterface.OnClickListener() {
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
                        getString(R.string.url_this_app_google_play_page)));
                intent.setPackage(getString(R.string.package_com_android_vending));
                startActivity(intent);
                ratingDialog.dismiss();
            }
        });

        ratingDialog.setView(ratingDialogLayout);

        return ratingDialog;
    }
}

