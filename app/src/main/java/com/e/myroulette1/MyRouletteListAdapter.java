package com.e.myroulette1;

/*
recyclerｖiewに表示したい内容を適応（adapt)する場所
 */

/*
 * Copyright (C) 2017 Google Inc.
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;


public class MyRouletteListAdapter extends ListAdapter<MyRoulette, MyRouletteViewHolder> {

    private Context context;

    public MyRouletteListAdapter(@NonNull DiffUtil.ItemCallback<MyRoulette> diffCallback) {
        super(diffCallback);
    }

    @Override
    public MyRouletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //parent = recyclerView
        return MyRouletteViewHolder.create(parent);
    }

    public void deleteItem(int index) {
        //削除項目をList<word>から削除
        MainActivity.mMyRouletteViewModel.getAllMyRoulette().getValue().remove(index);
        notifyItemRemoved(index);
        //削除しただけではデータがリバインドされないので、以下のメソッドでリバインドさせる
        notifyItemRangeChanged(index, getItemCount() - index);
    }

    @Override
    public void onBindViewHolder(MyRouletteViewHolder holder, int position) {
        MyRoulette current = getItem(position);
        holder.bind(current.getId(), current.getRouletteName(), current.getDate(), current.getColorsInfo(), current.getItemNamesInfo(),
                current.getItemRatiosInfo(), current.getOnOffOfSwitch100Info(), current.getOnOffOfSwitch0Info(), current.getItemProbabilitiesInfo());
        holder.getDeleteMyRouletteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setMessage("削除してもよろしいですか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //ボタンの押された部分のList<word>の項目をList<word>から削除
                                deleteItem(holder.getAdapterPosition());
                                //スワイプされた箇所のデータベースのprimarykeyを取得
                                int primaryKey = holder.getRouletteView().getId();
                                //取得したprimarykeyの所のデータを消去
                                MainActivity.mMyRouletteViewModel.delete(primaryKey);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog,
                                // so we will refresh the adapter to prevent hiding the item from UI
                            }
                        })
                        .create()
                        .show();
            }

/*
                //ダイアログを出さない設定の時の処理

                //スワイプされたList<word>の項目をList<word>から削除
                deleteItem(holder.getLayoutPosition());
                //スワイプされた箇所のデータベースのprimarykeyを取得
                int primaryKey = holder.getRouletteView().getId();
                //取得したprimarykeyの所のデータを消去
                MainActivity.mWordViewModel.delete(primaryKey);

 */

            }
        );
    }


    static class MyRouletteDiff extends DiffUtil.ItemCallback<MyRoulette> {

        @Override
        public boolean areItemsTheSame(@NonNull MyRoulette oldItem, @NonNull MyRoulette newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull MyRoulette oldItem, @NonNull MyRoulette newItem) {
            return oldItem == newItem;
        }
    }

}
