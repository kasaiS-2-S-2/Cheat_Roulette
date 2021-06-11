package com.e.myroulette1;

import java.util.ArrayList;

// セットしてあるルーレットがあれば、保存し、情報の取り出しも行うクラス
public class SavedRouletteOfMainActivity {
    private int splitCount;
    private String rouletteName;
    private ArrayList<Integer> colors;
    private ArrayList<String> itemNames;
    private ArrayList<Integer> itemRatios;
    private ArrayList<Integer> OnOffOfSwitch100;
    private ArrayList<Integer> OnOffOfSwitch0;
    private ArrayList<Float> itemProbabilities;

    /*
    // 保存情報取得メソッド
    public static SavedRouletteOfMainActivity getInstance(Context context) {
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String savedRouletteString = prefs.getString(context.getResources().getString(R.string.saved_roulette_key), "");

        SavedRouletteOfMainActivity instance;
        // 保存したオブジェクトを取得
        if( !TextUtils.isEmpty(savedRouletteString)) {
            instance = gson.fromJson(savedRouletteString, SavedRouletteOfMainActivity.class);
        }else {
            // 何も保存されてない 初期時点 この時はデフォルト値を入れて上げる
            instance = getDefaultInstance();
        }
        return instance;
    }

     */

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

    /*
    // 状態保存メソッド
    public void saveRoulette(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        // 現在のインスタンスの状態を保存
        prefs.edit().putString(USER_SETTING_PREF_KEY, gson.toJson(this)).apply();
    }

     */
}
