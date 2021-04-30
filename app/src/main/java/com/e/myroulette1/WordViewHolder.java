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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class WordViewHolder extends RecyclerView.ViewHolder {
    //private final TextView wordItemView;
    //private final TextView wordItemView2;
    private final TextView wordItemView;
    private final TextView dateItemView;
    private final Button deleteMyRouletteButton;
    private final Button editMyRouletteButton;
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
        this.wordItemView = itemView.findViewById(R.id.title);
        this.dateItemView = itemView.findViewById(R.id.date);
        this.deleteMyRouletteButton = itemView.findViewById(R.id.delete_myRoulette);
        this.editMyRouletteButton = itemView.findViewById(R.id.edit_myRoulette);
        this.rouletteView = itemView.findViewById(R.id.myRoulette);
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
        wordItemView.setText(word);
        dateItemView.setText(date);
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

    public Button getDeleteMyRouletteButton() { return deleteMyRouletteButton; }

    public Button getEditMyRouletteButton() { return editMyRouletteButton; }
}
