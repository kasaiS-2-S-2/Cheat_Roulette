package com.e.myroulette1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

public class ColorPickFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //ColorPickDialogを開始する
        AlertDialog.Builder colorPickAlert = new AlertDialog.Builder(getActivity(), R.style.ColorPickDialogStyle);
        colorPickAlert.setTitle("choose color");
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.color_pick_dialog, null);
        colorPickAlert.setView(dialoglayout);
        ColorPicker picker = (ColorPicker) dialoglayout.findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);
/*
                //変更前の色
                int buttonColor;
                buttonColor = ((GradientDrawable) button.getBackground()).getColor().getDefaultColor();
                //変更前の色をpickerの初期色に設定
                picker.setColor(buttonColor);
 */
        SVBar svBar = (SVBar) dialoglayout.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) dialoglayout.findViewById(R.id.opacitybar);
        colorPickAlert.
                setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //((GradientDrawable)colorButton.getBackground()).setColor(picker.getColor());

                        //colorButton.setBackgroundColor(picker.getColor());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
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
                .create()
                .show();

        //final AlertDialog colorDialog = colorPickAlert.show();

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener()
        {
            @Override
            public void onColorChanged(int color)
            {
                //text.setBackgroundColor(color);
            }
        });
        return new Dialog(getContext());
    }
}
