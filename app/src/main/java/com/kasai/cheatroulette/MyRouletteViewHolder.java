/*
 * Copyright (C) 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain ic_cheat_roulette_launcher2_foreground copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kasai.cheatroulette;

import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.WINDOW_SERVICE;

class MyRouletteViewHolder extends RecyclerView.ViewHolder {
    private final CardView cardView;
    private final TextView rouletteNameView;
    private final TextView dateView;
    private final LinearLayout dateLayout;
    private final LinearLayout imageButtonLayout;
    private final ImageButton deleteMyRouletteButton;
    private final ImageButton editMyRouletteButton;
    private final RouletteView rouletteView;

    private MyRouletteViewHolder(View itemView) {
        super(itemView);

        this.cardView = itemView.findViewById(R.id.cardView);////////////////////////////////////////
        this.rouletteNameView = itemView.findViewById(R.id.roulette_name);
        this.dateView = itemView.findViewById(R.id.date);
        this.dateLayout = itemView.findViewById(R.id.date_layout);
        this.imageButtonLayout = itemView.findViewById(R.id.image_button_layout);
        this.deleteMyRouletteButton = itemView.findViewById(R.id.delete_myRoulette);
        this.editMyRouletteButton = itemView.findViewById(R.id.edit_myRoulette);
        this.rouletteView = itemView.findViewById(R.id.myRoulette);

        //画面サイズを取得
        WindowManager wm = (WindowManager)MyApplication.getAppContext().getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point displayAre = new Point();
        disp.getSize(displayAre);

        //画面サイズを元にmargin, widthを決定
        ViewGroup.LayoutParams rouletteLayoutParams = this.rouletteView.getLayoutParams();
        ViewGroup.MarginLayoutParams rouletteMarginLayoutParams = (ViewGroup.MarginLayoutParams) rouletteLayoutParams;
        rouletteMarginLayoutParams.setMarginEnd(displayAre.x * 2 / 3);
        this.rouletteView.setLayoutParams(rouletteMarginLayoutParams);
        this.rouletteView.getLayoutParams().width = (displayAre.x)/2;

        this.rouletteNameView.setMaxWidth((displayAre.x * 9) / 20);

        this.imageButtonLayout.getLayoutParams().width = displayAre.x/2;

    }

    //viewHolderとデータをつなぎ合わせる部分
    public void bind(int id,
                     String rouletteName,
                     String date,
                     ArrayList<Integer> colorsInfo,
                     ArrayList<String> itemNamesInfo,
                     ArrayList<Integer> itemRatiosInfo,
                     ArrayList<Integer> OnOffOfSwitch100Info,
                     ArrayList<Integer> OnOffOfSwitch0Info,
                     ArrayList<Float> itemProbabilitiesInfo) {

        if (rouletteName.equals("")) {
            rouletteNameView.setText("未設定");
        } else {
            rouletteNameView.setText(rouletteName);
        }
        dateView.setText(date);
        rouletteView.setId(id);
        rouletteView.setRouletteContents(1, rouletteName, colorsInfo, itemNamesInfo, itemRatiosInfo,
                OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
    }

    //viewHolderを作る部分
    static MyRouletteViewHolder create(ViewGroup parent) {
        //parent = recyclerView(親ビュー）
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_roulette_list_item, parent, false);
        return new MyRouletteViewHolder(view);
    }

    public RouletteView getRouletteView() { return rouletteView; }

    public CardView getCardView() { return cardView; }

    public TextView getRouletteNameView() { return rouletteNameView; }

    public ImageButton getDeleteMyRouletteButton() { return deleteMyRouletteButton; }

    public ImageButton getEditMyRouletteButton() { return editMyRouletteButton; }
}
