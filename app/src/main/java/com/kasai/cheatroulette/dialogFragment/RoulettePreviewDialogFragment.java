package com.kasai.cheatroulette.dialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.kasai.cheatroulette.ui.RouletteView;
import com.kasai.cheatroulette.R;

import java.util.ArrayList;

public class RoulettePreviewDialogFragment extends DialogFragment {
    ArrayList<Integer> colorsInfo;
    ArrayList<String> itemNamesInfo;
    ArrayList<Integer> itemRatiosInfo;

    public RoulettePreviewDialogFragment(
            ArrayList<Integer> colorsInfo, ArrayList<String> itemNamesInfo, ArrayList<Integer> itemRatiosInfo) {
        this.colorsInfo = colorsInfo;
        this.itemNamesInfo = itemNamesInfo;
        this.itemRatiosInfo = itemRatiosInfo;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder roulettePreviewAlertDialog = new AlertDialog.Builder(getActivity());

        roulettePreviewAlertDialog.setTitle(R.string.tilte_of_roulette_preview_dialog);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.roulette_preview_dialog, null);
        RouletteView roulettePreview = dialoglayout.findViewById(R.id.roulette_preview);
        roulettePreview.makeRoulettePreview(colorsInfo, itemNamesInfo, itemRatiosInfo);
        roulettePreviewAlertDialog.setView(dialoglayout);

        roulettePreviewAlertDialog
                .setPositiveButton(R.string.alert_dialog_ok, null);
        // Create the AlertDialog object and return it
        return roulettePreviewAlertDialog.create();
    }
}
