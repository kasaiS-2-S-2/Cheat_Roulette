package com.e.myroulette1;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;

public class EditRouletteAdapter extends RouletteItemListAdapter {

    public EditRouletteAdapter(RouletteItemListInfo dataSet) {
        super(dataSet);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
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
        /*
        viewHolder.getSwitch100().setOnCheckedChangeListener(null);
        viewHolder.getSwitch100().setChecked(rouletteItemDataSet.getOnOffInfoOfSwitch100().get(position));
        viewHolder.getSwitch100().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rouletteItemDataSet.setOnOffInfoOfSwitch100Partially(position, isChecked);
            }
        });

         */

        viewHolder.switch0OnCheckedChangeListener.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getSwitch0().setChecked(getRouletteItemDataSet().getOnOffInfoOfSwitch0().get(viewHolder.getAdapterPosition()));
        /*
        viewHolder.getSwitch0().setOnCheckedChangeListener(null);
        viewHolder.getSwitch0().setChecked(rouletteItemDataSet.getOnOffInfoOfSwitch0().get(position));

        viewHolder.getSwitch0().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rouletteItemDataSet.setOnOffInfoOfSwitch0Partially(position, isChecked);
            }
        });

         */
    }
}