package com.kasai.cheatroulette;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class EditRouletteAdapter extends RouletteItemListAdapter {

    public EditRouletteAdapter(RecyclerView recyclerView, RouletteItemListInfo dataSet) {
        super(recyclerView ,dataSet);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d("ああああああああああああああああああ", "onBindViewHolder");

        //visibleFlagがfalseの時は、switch100, switch0 を非表示にする
        //ここがEditRouletteActivityになっているのが、RouletteItemListAdapterとの違い
        if (!EditRouletteActivity.visibleFlag) {
            viewHolder.getLinearLayout2().setVisibility(View.GONE);
        } else {
            viewHolder.getLinearLayout2().setVisibility(View.VISIBLE);
        }
        ((GradientDrawable)viewHolder.getColorButton().getBackground()).setColor(getRouletteItemDataSet().getColors().get(viewHolder.getAdapterPosition()));
        ((ColorButton) viewHolder.getColorButton()).setButtonColor(getRouletteItemDataSet().getColors().get(viewHolder.getAdapterPosition()));

        //EditText（ルーレット名）監視リスナーに使うpositionを今のadapterPositionに設定
        viewHolder.editTextListenerForItemName.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getItemName().setText(getRouletteItemDataSet().getItemNames().get(viewHolder.getAdapterPosition()));

        //EditText（面積比率）監視リスナーに使うpositionを今のadapterPositionに設定
        viewHolder.editTextListenerForRatio.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getRatio().setText(String.valueOf(getRouletteItemDataSet().getItemRatios().get(viewHolder.getAdapterPosition())));

        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ルーレット項目が２つ未満になるのを防ぐ
                if (getItemCount() > 2) {
                    deleteItem(viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.switch100OnCheckedChangeListener.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getSwitch100().setChecked(getRouletteItemDataSet().getOnOffInfoOfSwitch100().get(viewHolder.getAdapterPosition()));

        viewHolder.switch0OnCheckedChangeListener.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getSwitch0().setChecked(getRouletteItemDataSet().getOnOffInfoOfSwitch0().get(viewHolder.getAdapterPosition()));
    }
}