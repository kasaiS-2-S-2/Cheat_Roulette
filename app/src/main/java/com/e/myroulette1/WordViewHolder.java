/*
 * Copyright (C) 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.e.myroulette1;

import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.WINDOW_SERVICE;

class WordViewHolder extends RecyclerView.ViewHolder {
    //private final TextView wordItemView;
    //private final TextView wordItemView2;
    private final CardView cardView;/////////////////////////////////////
    private final TextView titleView;
    private final TextView dateView;
    private final LinearLayout imageButtonLayout;
    private final ImageButton deleteMyRouletteButton;
    private final ImageButton editMyRouletteButton;
    private final RouletteView rouletteView;

    private WordViewHolder(View itemView) {
        super(itemView);
        //wordItemView = itemView.findViewById(R.id.title);
        //wordItemView2 = itemView.findViewById(R.id.sub);
        /*
        Log.d("あああああああああああああああああああ", "WordViewHolder");
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ああああああああああああああああ","いいいいいいいいいいいいいいいいいい" );
            }
        });

         */

        this.cardView = itemView.findViewById(R.id.cardView);////////////////////////////////////////
        this.titleView = itemView.findViewById(R.id.title);
        this.dateView = itemView.findViewById(R.id.date);
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
        ViewGroup.LayoutParams layoutParams = this.rouletteView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        marginLayoutParams.setMarginStart(displayAre.x * 2 / 3);
        this.rouletteView.setLayoutParams(marginLayoutParams);
        this.rouletteView.getLayoutParams().width = (displayAre.x) / 2;

        this.titleView.setMaxWidth(displayAre.x/2);

        this.imageButtonLayout.getLayoutParams().width = displayAre.x / 2;

    }

    //viewHolderとデータをつなぎ合わせる部分
    public void bind(int id,
                     String word,
                     String date,
                     ArrayList<Integer> colorsInfo,
                     ArrayList<String> textStringsInfo,
                     ArrayList<Integer> itemRatiosInfo,
                     ArrayList<Integer> OnOffOfSwitch100Info,
                     ArrayList<Integer> OnOffOfSwitch0Info,
                     ArrayList<Float> itemProbabilitiesInfo) {

        //wordItemView.setText(words.get(0));
        //wordItemView2.setText(words.get(1));
        titleView.setText(word);
        dateView.setText(date);
        rouletteView.setId(id);
        rouletteView.setRouletteContents(word, colorsInfo, textStringsInfo, itemRatiosInfo,
                OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
    }

    //viewHolderを作る部分
    static WordViewHolder create(ViewGroup parent) {
        //parent = recyclerView(親ビュー）
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(view);
    }

    public RouletteView getRouletteView() { return rouletteView; }

    public ImageButton getDeleteMyRouletteButton() { return deleteMyRouletteButton; }

    public ImageButton getEditMyRouletteButton() { return editMyRouletteButton; }
}
