package com.kasai.cheatroulette;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "myRoulette_table")
public class MyRoulette {

    @PrimaryKey(autoGenerate = true)
    private int id;

    //ルーレット名
    @NonNull
    @ColumnInfo(name = "rouletteName")
    private String rouletteName;
    //作成日
    @NonNull
    @ColumnInfo(name = "date")
    private String date;
    //ルーレットの色情報
    @NonNull
    @ColumnInfo(name = "colorsInfo")
    private ArrayList<Integer> colorsInfo;
    //ルーレットの項目名
    @NonNull
    @ColumnInfo(name = "itemNamesInfo")
    private ArrayList<String> itemNamesInfo;
    //項目の面積比
    @NonNull
    @ColumnInfo(name = "itemRatiosInfo")
    private ArrayList<Integer> itemRatiosInfo;
    //必中スイッチのONOFF情報
    @NonNull
    @ColumnInfo(name = "OnOffOfSwitch100Info")
    private ArrayList<Integer> OnOffOfSwitch100Info;
    //絶対ハズレスイッチのONOFF情報
    @NonNull
    @ColumnInfo(name = "OnOffOfSwitch0Info")
    private ArrayList<Integer> OnOffOfSwitch0Info;
    //各項目の当たる確率
    @NonNull
    @ColumnInfo(name = "itemProbabilitiesInfo")
    private ArrayList<Float> itemProbabilitiesInfo;


    //コンストラクタで貰う引数名は、対応するカラムの名前と同じにしなければならない？
    public MyRoulette(@NonNull String rouletteName,
                      @NonNull String date,
                      @NonNull ArrayList<Integer> colorsInfo,
                      @NonNull ArrayList<String> itemNamesInfo,
                      @NonNull ArrayList<Integer> itemRatiosInfo,
                      @NonNull ArrayList<Integer> OnOffOfSwitch100Info,
                      @NonNull ArrayList<Integer> OnOffOfSwitch0Info,
                      @NonNull ArrayList<Float> itemProbabilitiesInfo) {

        this.rouletteName = rouletteName;

        //ルーレットの作成日時
        this.date = date;
        //ルーレットの色のリスト
        this.colorsInfo = colorsInfo;
        //ルーレットの文字列のリスト
        this.itemNamesInfo = itemNamesInfo;
        //ルーレットの項目比率のリスト
        this.itemRatiosInfo = itemRatiosInfo;
        //必中スイッチのONOFF情報
        this.OnOffOfSwitch100Info = OnOffOfSwitch100Info;
        //絶対ハズレスイッチのONOFF情報
        this.OnOffOfSwitch0Info = OnOffOfSwitch0Info;
        //ルーレットの項目別の当選確率のリスト
        this.itemProbabilitiesInfo = itemProbabilitiesInfo;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public int getId() { return this.id; }

    @NonNull
    public String getRouletteName() {
        return this.rouletteName;
    }

    @NonNull
    public String getDate() { return this.date; }

    @NonNull
    public ArrayList<Integer> getColorsInfo() { return this.colorsInfo; }

    @NonNull
    public ArrayList<String> getItemNamesInfo() { return this.itemNamesInfo; }

    @NonNull
    public ArrayList<Integer> getItemRatiosInfo() { return this.itemRatiosInfo; }

    @NonNull
    public ArrayList<Integer> getOnOffOfSwitch100Info() { return this.OnOffOfSwitch100Info; }

    @NonNull
    public ArrayList<Integer> getOnOffOfSwitch0Info() { return this.OnOffOfSwitch0Info; }

    @NonNull
    public ArrayList<Float> getItemProbabilitiesInfo() { return this.itemProbabilitiesInfo; }

}
