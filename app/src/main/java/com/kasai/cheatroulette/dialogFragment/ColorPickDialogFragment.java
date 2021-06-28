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
import com.kasai.cheatroulette.R;
import com.kasai.cheatroulette.recyclerView.RouletteItemListAdapter;
import com.kasai.cheatroulette.ui.ColorButton;

public class ColorPickDialogFragment extends DialogFragment {
    ColorButton colorButton;
    RecyclerView rouletteItemList;

    public ColorPickDialogFragment(ColorButton colorButton, RecyclerView rouletteItemList) {
        this.colorButton = colorButton;
        this.rouletteItemList = rouletteItemList;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //ColorPickDialogを開始する
        AlertDialog.Builder colorPickAlert = new AlertDialog.Builder(getActivity());

        colorPickAlert.setTitle("色の選択");

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.color_pick_dialog, null);
        //アラートダイアログの中にある色ボタン（選択色確認のview）
        ColorButton dialogColorButton = dialoglayout.findViewById(R.id.color_preview);
        ((GradientDrawable) dialogColorButton.getBackground()).setColor(((ColorButton)colorButton).getButtonColor());
        colorPickAlert.setView(dialoglayout);

        ColorPickerView colorPickerView = (ColorPickerView) dialoglayout.findViewById(R.id.color_picker_view);
        colorPickerView.setInitialColor(((ColorButton)colorButton).getButtonColor(), false);
        colorPickerView.setShowBorder(true);

        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                ((GradientDrawable) dialogColorButton.getBackground()).setColor(selectedColor);
            }
        });

        colorPickAlert
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //色情報の設定
                        ((ColorButton)colorButton).setButtonColor(colorPickerView.getSelectedColor());
                        //色の変更
                        ((GradientDrawable)colorButton.getBackground()).setColor(colorPickerView.getSelectedColor());
                        //色の変更があった部分のadapterPositionを取得
                        int position = rouletteItemList.getLayoutManager().getPosition((View)colorButton.getParent().getParent().getParent());
                        //色の変更を適応
                        ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().setColor(position, colorPickerView.getSelectedColor());
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
                });
        // Create the AlertDialog object and return it
        return colorPickAlert.create();
    }
}
