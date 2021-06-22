package com.kasai.cheatroulette;

import java.util.ArrayList;

// セットしてあるルーレットがあれば、そのルーレット情報を保存するためのクラス
public class SavedRouletteOfMainActivity {
    private int splitCount;
    private String rouletteName;
    private ArrayList<Integer> colors;
    private ArrayList<String> itemNames;
    private ArrayList<Integer> itemRatios;
    private ArrayList<Integer> OnOffOfSwitch100;
    private ArrayList<Integer> OnOffOfSwitch0;
    private ArrayList<Float> itemProbabilities;

    // デフォルト値の入ったオブジェクトを返す
    public static SavedRouletteOfMainActivity getDefaultInstance() {

        SavedRouletteOfMainActivity instance = new SavedRouletteOfMainActivity();

        instance.splitCount = 1;

        instance.rouletteName = "";

        ArrayList<Integer> defaultColors = new ArrayList<Integer>();
        defaultColors.add(MyApplication.getAppContext().getResources().getColor(R.color.appPink));
        instance.colors = defaultColors;

        ArrayList<String> defaultItemNames = new ArrayList<String>();
        defaultItemNames.add("");
        instance.itemNames = defaultItemNames;

        ArrayList<Integer> defaultItemRatios = new ArrayList<Integer>();
        defaultItemRatios.add(1);
        instance.itemRatios = defaultItemRatios;

        ArrayList<Integer> defaultOnOffOfSwitch100 = new ArrayList<Integer>();
        defaultOnOffOfSwitch100.add(0);
        instance.OnOffOfSwitch100 = defaultOnOffOfSwitch100;

        ArrayList<Integer> defaultOnOffOfSwitch0 = new ArrayList<Integer>();
        defaultOnOffOfSwitch100.add(0);
        instance.OnOffOfSwitch0 = defaultOnOffOfSwitch0;

        ArrayList<Float> defaultItemProbabilities = new ArrayList<Float>();
        defaultItemProbabilities.add(100f);
        instance.itemProbabilities = defaultItemProbabilities;

        return instance;
    }



    public int getSplitCount() {
        return splitCount;
    }

    public String getRouletteName() {
        return rouletteName;
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

    public ArrayList<Integer> getOnOffOfSwitch100() {
        return OnOffOfSwitch100;
    }

    public ArrayList<Integer> getOnOffOfSwitch0() {
        return OnOffOfSwitch0;
    }

    public ArrayList<Float> getItemProbabilities() {
        return itemProbabilities;
    }


    public void setSavedRouletteContents(int splitCount,
                                         String rouletteNameInfo,
                                         ArrayList<Integer> colorsInfo,
                                         ArrayList<String> itemNamesInfo,
                                         ArrayList<Integer> itemRatiosInfo,
                                         ArrayList<Integer> OnOffOfSwitch100Info,
                                         ArrayList<Integer> OnOffOfSwitch0Info,
                                         ArrayList<Float> itemProbabilitiesInfo) {


        //ルーレットの分割数
        this.splitCount = splitCount;
        //ルーレットの名前
        this.rouletteName = rouletteNameInfo;
        //ルーレットの色のリスト
        this.colors = colorsInfo;
        //ルーレットの文字列のリスト
        this.itemNames = itemNamesInfo;
        //ルーレットの項目比率のリスト
        this.itemRatios = itemRatiosInfo;
        //必中スイッチのONOFF情報
        this.OnOffOfSwitch100 = OnOffOfSwitch100Info;
        //絶対ハズレスイッチのONOFF情報
        this.OnOffOfSwitch0 = OnOffOfSwitch0Info;
        //ルーレットの項目別の当選確率のリスト
        this.itemProbabilities = itemProbabilitiesInfo;
    }
}
