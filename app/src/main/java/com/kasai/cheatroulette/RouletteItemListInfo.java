package com.kasai.cheatroulette;

import java.util.ArrayList;

//recyclerViewで使うルーレット項目情報のクラス
public class RouletteItemListInfo {
    //ルーレットの色のリスト
    private ArrayList<Integer> colors = new ArrayList<Integer>();
    //ルーレットの文字列のリスト
    private ArrayList<String> itemNames = new ArrayList<String>();
    //ルーレットの項目比率のリスト
    private ArrayList<Integer> itemRatios = new ArrayList<Integer>();
    //必中スイッチのONOFF情報
    private ArrayList<Boolean> OnOffInfoOfSwitch100 = new ArrayList<Boolean>();
    //絶対ハズレスイッチのONOFF情報
    private ArrayList<Boolean> OnOffInfoOfSwitch0 = new ArrayList<Boolean>();

    public RouletteItemListInfo(ArrayList<Integer> colors,
                                ArrayList<String> itemNames,
                                ArrayList<Integer> itemRatios,
                                ArrayList<Boolean> OnOffInfoOfSwitch100,
                                ArrayList<Boolean> OnOffInfoOfSwitch0) {

        this.colors = colors;
        this.itemNames = itemNames;
        this.itemRatios = itemRatios;
        this.OnOffInfoOfSwitch100 = OnOffInfoOfSwitch100;
        this.OnOffInfoOfSwitch0 = OnOffInfoOfSwitch0;
    }

    public ArrayList<Integer> getColors() {
        return colors;
    }

    public ArrayList<String> getItemNames() {
        return itemNames;
    }

    public ArrayList<Integer> getItemRatios() {
        return itemRatios;
    }

    public ArrayList<Boolean> getOnOffInfoOfSwitch100() {
        return OnOffInfoOfSwitch100;
    }

    public ArrayList<Boolean> getOnOffInfoOfSwitch0() {
        return OnOffInfoOfSwitch0;
    }


    public void setColors(ArrayList<Integer> colors) {
        this.colors = colors;
    }

    public void setColor(int index, int color) {
        this.colors.set(index, color);
    }

    public void setItemNames(ArrayList<String> itemNames) {
        this.itemNames = itemNames;
    }

    public void setItemName(int index, CharSequence itemName) {
        this.itemNames.set(index, itemName.toString());
    }

    public void setItemRatios(ArrayList<Integer> itemRatios) {
        this.itemRatios = itemRatios;
    }

    public void setItemRatio(int index, Integer ratio) {
        this.itemRatios.set(index, ratio);
    }

    public void setOnOffInfoOfSwitch100(ArrayList<Boolean> onOffInfoOfSwitch100) {
        this.OnOffInfoOfSwitch100 = onOffInfoOfSwitch100;
    }

    public void setOnOffInfoOfSwitch100Partially(int index, Boolean OnOffInfo) {
        this.OnOffInfoOfSwitch100.set(index, OnOffInfo);
    }

    public void setOnOffInfoOfSwitch0(ArrayList<Boolean> onOffInfoOfSwitch0) {
        this.OnOffInfoOfSwitch0 = onOffInfoOfSwitch0;
    }

    public void setOnOffInfoOfSwitch0Partially(int index, Boolean OnOffInfo) {
        this.OnOffInfoOfSwitch0.set(index, OnOffInfo);
    }
}
