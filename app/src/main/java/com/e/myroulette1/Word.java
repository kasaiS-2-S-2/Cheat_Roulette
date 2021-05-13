package com.e.myroulette1;

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

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int id;

    //ルーレット名
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    /*
    @NonNull
    @ColumnInfo(name = "words")
    private ArrayList<String> mWords;

     */
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "colorsInfo")
    private ArrayList<Integer> colorsInfo;

    @NonNull
    @ColumnInfo(name = "textStringsInfo")
    private ArrayList<String> textStringsInfo;

    @NonNull
    @ColumnInfo(name = "itemRatiosInfo")
    private ArrayList<Integer> itemRatiosInfo;

    @NonNull
    @ColumnInfo(name = "OnOffOfSwitch100Info")
    private ArrayList<Integer> OnOffOfSwitch100Info;

    @NonNull
    @ColumnInfo(name = "OnOffOfSwitch0Info")
    private ArrayList<Integer> OnOffOfSwitch0Info;

    @NonNull
    @ColumnInfo(name = "itemProbabilitiesInfo")
    private ArrayList<Float> itemProbabilitiesInfo;


//コンストラクタで貰う引数名は、対応するカラムの名前と同じにしなければならないっぽい
    public Word(@NonNull String mWord,
                @NonNull String date,
                @NonNull ArrayList<Integer> colorsInfo,
                @NonNull ArrayList<String> textStringsInfo,
                @NonNull ArrayList<Integer> itemRatiosInfo,
                @NonNull ArrayList<Integer> OnOffOfSwitch100Info,
                @NonNull ArrayList<Integer> OnOffOfSwitch0Info,
                @NonNull ArrayList<Float> itemProbabilitiesInfo) {

        this.mWord = mWord;
        //this.mWords = mWords;

        //ルーレットの作成日時
        this.date = date;
        //ルーレットの色のリスト
        this.colorsInfo = colorsInfo;
        //ルーレットの文字列のリスト
        this.textStringsInfo = textStringsInfo;
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

    //各カラムのgetterは必ず存在しなければならない
    //getterの名前もなにか制約があるっぽいが、そのルールがわからん

    @NonNull
    public int getId() { return this.id; }

    @NonNull
    public String getWord() {
        return this.mWord;
    }

    /*
    @NonNull
    public ArrayList<String> getWords() {
        return this.mWords;
    }

     */

    @NonNull
    public String getDate() { return this.date; }

    @NonNull
    public ArrayList<Integer> getColorsInfo() { return this.colorsInfo; }

    @NonNull
    public ArrayList<String> getTextStringsInfo() { return this.textStringsInfo; }

    @NonNull
    public ArrayList<Integer> getItemRatiosInfo() { return this.itemRatiosInfo; }

    @NonNull
    public ArrayList<Integer> getOnOffOfSwitch100Info() { return this.OnOffOfSwitch100Info; }

    @NonNull
    public ArrayList<Integer> getOnOffOfSwitch0Info() { return this.OnOffOfSwitch0Info; }

    @NonNull
    public ArrayList<Float> getItemProbabilitiesInfo() { return this.itemProbabilitiesInfo; }

}
