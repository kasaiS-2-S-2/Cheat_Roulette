package com.kasai.cheatroulette.recyclerView;
/*
recyclerｖiewに表示したい内容を適応（adapt)する場所
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.kasai.cheatroulette.R;
import com.kasai.cheatroulette.activity.mainActivity.MainActivity;
import com.kasai.cheatroulette.common.MyApplication;
import com.kasai.cheatroulette.room.MyRoulette;


public class MyRouletteListAdapter extends ListAdapter<MyRoulette, MyRouletteViewHolder> {

    public MyRouletteListAdapter(@NonNull DiffUtil.ItemCallback<MyRoulette> diffCallback) {
        super(diffCallback);
    }

    @Override
    public MyRouletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
                int myRouletteItemCount = getItemCount();
                if (myRouletteItemCount >= 3) {
                    Context context = MyApplication.getAppContext();
                    SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(context);
                    if (defaultPref.getBoolean(context.getString(R.string.saved_appear_alert_delete_myRoulette_key), true)) {
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
                    } else {
                        //ダイアログを出さない設定の時の処理
                        //ボタンの押された部分のList<word>の項目をList<word>から削除
                        deleteItem(holder.getAdapterPosition());
                        //スワイプされた箇所のデータベースのprimarykeyを取得
                        int primaryKey = holder.getRouletteView().getId();
                        //取得したprimarykeyの所のデータを消去
                        MainActivity.mMyRouletteViewModel.delete(primaryKey);
                    }
                }
            }
        });
    }

    public static class MyRouletteDiff extends DiffUtil.ItemCallback<MyRoulette> {

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
