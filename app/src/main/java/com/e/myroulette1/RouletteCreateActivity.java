package com.e.myroulette1;

//import android.annotation.SuppressLint;

//import android.annotation.SuppressLint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

//import androidx.annotation.RequiresApi;
/*
今はゴリゴリレイアウト書いてるけど、ホントは,リストviewとか、adapterとか使ったほうが良さそう。
 */

public class RouletteCreateActivity extends AppCompatActivity {

    //public int rouletteCount;
    private CheckBox checkBox;
    private ConstraintLayout rouletteCreateLayout;
    private EditText rouletteName;
    private Toolbar toolbar;
    private Button itemAddButton;
    private Button cheatButton;
    private FloatingActionButton createFinishFab;
    //private EditText editText, editText2;
    //ルーレットの項目リストの情報を保持するもの
    private RouletteItemListInfo rouletteItemListInfo = new RouletteItemListInfo(
            new ArrayList<Integer>(), new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<Boolean>(), new ArrayList<Boolean>());
    private RecyclerView rouletteItemList;
    private RouletteItemListAdapter rouletteItemListAdapter;
    //private RouletteItemListAdapter rouletteItemListAdapter = new RouletteItemListAdapter(rouletteItemList ,rouletteItemListInfo);
    //private RouletteItemListInfo rouletteItemListInfo;

    public static boolean visibleFlag = false;
    private boolean isTutorialState = false;

    private Toast mToast = null;

    //private AdView mAdView;
    //private int rouletteCount = 0;


/*
    //ルーレットの色のリスト
    private static ArrayList<Integer> colors = new ArrayList<Integer>();
    //ルーレットの文字列のリスト
    private static ArrayList<String> textString = new ArrayList<String>();
    //ルーレットの項目比率のリスト
    private static ArrayList<String> itemRatio = new ArrayList<String>();


 */
    @SuppressLint({"WrongConstant", "SetTextI18n"})
    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulettecreate);

        //広告を付ける
        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        visibleFlag = false;

        toolbar = findViewById(R.id.toolbar_roulette_create);
        toolbar.setTitle(R.string.roulette_create);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rouletteCreateLayout = findViewById(R.id.roulette_create_layout);

        rouletteItemList = findViewById(R.id.roulette_item_list);
        rouletteItemListAdapter = new RouletteItemListAdapter(rouletteItemList, rouletteItemListInfo);
        rouletteItemList.setAdapter(rouletteItemListAdapter);
        rouletteItemList.setLayoutManager(new LinearLayoutManager(this));

        //スクリーンサイズの取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point realSize = new Point();
        disp.getRealSize(realSize);
        int ScreenWidth = realSize.x;
        int ScreenHeight = realSize.y;

        //recyclerViewの真下に少しの空白を空ける（スクリーンサイズの1/8分空ける）
        rouletteItemList.setPadding(0, 0, 0, ScreenHeight/8);

        //スワイプ削除時にアラートダイアログを表示できるやつ
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();

                if (!(rouletteItemListAdapter.getItemCount() <= 2)) {
                    // Here is where you'll implement swipe to delete
                    rouletteItemListAdapter.getRouletteItemDataSet().getColors().remove(position);
                    rouletteItemListAdapter.getRouletteItemDataSet().getItemNames().remove(position);
                    rouletteItemListAdapter.getRouletteItemDataSet().getItemRatios().remove(position);
                    rouletteItemListAdapter.getRouletteItemDataSet().getOnOffInfoOfSwitch100().remove(position);
                    rouletteItemListAdapter.getRouletteItemDataSet().getOnOffInfoOfSwitch0().remove(position);

                    rouletteItemListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                    //削除しただけではデータがリバインドされないので、以下のメソッドでリバインドさせる
                    rouletteItemListAdapter.notifyItemRangeChanged(position, rouletteItemListAdapter.getItemCount() - position);
                } else {
                    rouletteItemListAdapter.notifyItemChanged(position);
                }
            }
        }).attachToRecyclerView(rouletteItemList);

        //RouletteItemListInfo rouletteItemListInfo;
        //RouletteItemListAdapter rouletteItemListAdapter;

        rouletteName = findViewById(R.id.rouletteName);

        //checkBox.setChecked(false);

        /*
        Intent fromMainIntent = getIntent(); //Mainでedit_Buttonを押した時の遷移
        //セットしてあるルーレットを編集する場合
        if (fromMainIntent.getAction() != null && fromMainIntent.getAction().equals("EDIT_ROULETTE")) {
            String rouletteNameFromMain = fromMainIntent.getStringExtra("editInfoOfRouletteName");

            ArrayList<Integer> colorsFromMain = fromMainIntent.getIntegerArrayListExtra("editInfoOfColors");
            ArrayList<String> textStringsFromMain = fromMainIntent.getStringArrayListExtra("editInfoOfTextStrings");
            ArrayList<Integer> itemRatioFromMain = fromMainIntent.getIntegerArrayListExtra("editInfoOfItemRatio");
            ArrayList<Integer> Switch100InfoFromMain = fromMainIntent.getIntegerArrayListExtra("editInfoOfSwitch100");
            ArrayList<Boolean> Switch100InfoFromMainBoolean = new ArrayList<Boolean>();
            ArrayList<Integer> Switch0InfoFromMain = fromMainIntent.getIntegerArrayListExtra("editInfoOfSwitch0");
            ArrayList<Boolean> Switch0InfoFromMainBoolean = new ArrayList<Boolean>();


            for (int i=0; i<Switch100InfoFromMain.size(); i++) {
                if (Switch100InfoFromMain.get(i) == 1) {
                    Switch100InfoFromMainBoolean.add(true);
                } else {
                    Switch100InfoFromMainBoolean.add(false);
                }

                if (Switch0InfoFromMain.get(i) == 1) {
                    Switch0InfoFromMainBoolean.add(true);
                } else {
                    Switch0InfoFromMainBoolean.add(false);
                }
            }

            rouletteName.setText(rouletteNameFromMain);//ルーレット名の項目に設定

            rouletteItemListInfo.setColors(colorsFromMain);
            rouletteItemListInfo.setItemNames(textStringsFromMain);
            rouletteItemListInfo.setItemRatios(itemRatioFromMain);
            rouletteItemListInfo.setOnOffInfoOfSwitch100(Switch100InfoFromMainBoolean);
            rouletteItemListInfo.setOnOffInfoOfSwitch0(Switch0InfoFromMainBoolean);

            rouletteItemListAdapter.notifyDataSetChanged();

            //ルーレットを一から作る場合
        } else {


            //addEditView(layout, scale, margins);
            //予め2項目は作っておく
            ArrayList<Integer> colors = new ArrayList<Integer>(Arrays.asList(generateColor(), generateColor()));
            ArrayList<String> itemNames = new ArrayList<String>(Arrays.asList("", ""));
            ArrayList<Integer> itemRatios = new ArrayList<Integer>(Arrays.asList(1, 1));
            ArrayList<Boolean> OnOffInfoOfSwitch100Boolean = new ArrayList<Boolean>(Arrays.asList(false, false));
            ArrayList<Boolean> OnOffInfoOfSwitch0Boolean = new ArrayList<Boolean>(Arrays.asList(false, false));

         */

            rouletteItemListInfo.setColors(new ArrayList<Integer>(Arrays.asList(generateColor(), generateColor())));
            rouletteItemListInfo.setItemNames(new ArrayList<String>(Arrays.asList("", "")));
            rouletteItemListInfo.setItemRatios(new ArrayList<Integer>(Arrays.asList(1, 1)));
            rouletteItemListInfo.setOnOffInfoOfSwitch100(new ArrayList<Boolean>(Arrays.asList(false, false)));
            rouletteItemListInfo.setOnOffInfoOfSwitch0(new ArrayList<Boolean>(Arrays.asList(false, false)));

            rouletteItemListAdapter.notifyDataSetChanged();
        //}

        
        //************* ボタンの設定 *************/
        itemAddButton = findViewById(R.id.item_add_button);
        // リスナーをボタンに登録, lambda
        itemAddButton.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                if (rouletteItemListAdapter.getItemCount() >= 300) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), R.string.notice_item_is_max, Toast.LENGTH_SHORT);
                    mToast.show();
                } else {
                    //addEditView(layout, scale, margins);
                    //rouletteItemListAdapter.addItem(generateColor(), "", 1, 0, 0);

                    //項目が追加された場合は、キーボードを隠す
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        //RouletteItemListAdapter.ViewHolder viewHolder =(RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForLayoutPosition(rouletteItemListAdapter.getItemCount()-1);
                        //viewHolder.getItemName().requestFocus();
                    }
                    //RouletteItemListAdapterのaddItemをルーレット項目追加に使用する
                    rouletteItemListAdapter.addItem(generateColor(), "", 1, false, false);
                    //新しいルーレット項目が追加された時、recyclerViewを一番下に自動スクロールする
                    rouletteItemList.scrollToPosition(rouletteItemListAdapter.getItemCount() - 1);
                    //Log.d("ああああああああああああああああああJJJJJ", String.valueOf(rouletteItemList.getChildCount()));
                    //rouletteItemList.getChildAt(rouletteItemList.getChildCount() - 1).findViewById(R.id.itemName).requestFocus();

                /*
                hueLevel++;
                if (hueLevel >= 12) { hueLevel = 0; }

                 */
                }
            }
        });


        cheatButton = findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (visibleFlag) {
                    visibleFlag = false;
                    //trueの場合は、イカサマスイッチを隠す
                    /*
                    for (int i = 0; i < rouletteItemList.getChildCount(); i++) {
                        rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).setVisibility(View.GONE);
                        //((ViewGroup) layout.getChildAt(i)).getChildAt(1).setVisibility(View.GONE);
                        //rouletteItemListAdapter.notifyItemChanged(i);
                    }

                     */
                    rouletteItemListAdapter.notifyDataSetChanged();
                    //rouletteItemListAdapter.notifyItemRangeChanged(0, rouletteItemListAdapter.getItemCount() - 1);

                    //visibleFlag = false;
                    //cheatButton.setVisibility(View.INVISIBLE);
                    cheatButton.setText("");
                    cheatButton.setBackgroundColor(Color.TRANSPARENT);

                } else {
                    visibleFlag = true;
                    // falseの場合は、イカサマスイッチを表示
                    /*
                    for (int i = 0; i < rouletteItemList.getChildCount(); i++) {
                        rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).setVisibility(View.VISIBLE);
                        //((ViewGroup) layout.getChildAt(i)).getChildAt(1).setVisibility(View.VISIBLE);
                        //rouletteItemListAdapter.notifyItemChanged(i);
                    }

                     */
                    rouletteItemListAdapter.notifyDataSetChanged();
                    //rouletteItemListAdapter.notifyItemRangeChanged(0, rouletteItemList.getChildCount());
                    //rouletteItemListAdapter.notifyItemRangeChanged(0, rouletteItemListAdapter.getItemCount() - 1);

                    //visibleFlag = true;
                    //cheatButton.setVisibility(View.VISIBLE);
                    cheatButton.setText(R.string.hide_cheat);
                    cheatButton.setBackgroundColor(Color.RED);

                    //SharedPreferences sharedPref = RouletteCreateActivity.this.getPreferences(Context.MODE_PRIVATE);
                    //boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_roulette_create_first_tutorial_done_key), false);
                    if (isTutorialState) {
                        //チュートリアルのつづきを表示
                        continuationOfTutorial();
                        //MaterialShowcaseView.resetSingleUse(RouletteCreateActivity.this, getString(R.string.roulette_create_continuation_first_tutorial_id));//////////////////////////////////////////////////////

                        //チュートリアルが終わったら、チュートリアル状態を解除する
                        //isTutorialState = false;
                    }
                }

                //イカサマボタンが押された場合、キーボードを隠す
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });


        createFinishFab = findViewById(R.id.create_finish_fab);
        createFinishFab.setOnClickListener(new View.OnClickListener() {
            // @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                int switch100PositiveCount = 0;
                int switch0PositiveCount = 0;
                RouletteItemListInfo rouletteItemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                int rouletteItemCount = rouletteItemDataSet.getColors().size();
                //ルーレットの項目別の当選確率のリスト
                ArrayList<Float> itemProbabilities = new ArrayList<Float>();
                //必中スイッチのONOFF情報（データベースに保存するためboolean から Integerに変換）
                ArrayList<Integer> OnOffOfSwitch100 = new ArrayList<Integer>();
                //絶対ハズレスイッチのONOFF情報（データベースに保存するためboolean から Integerに変換）
                ArrayList<Integer> OnOffOfSwitch0 = new ArrayList<Integer>();

                for (int i = 0; i < rouletteItemCount; i++) {
                    //switch100, switch0 それぞれのONOFF情報を追加
                    if (rouletteItemDataSet.getOnOffInfoOfSwitch100().get(i)) {
                        switch100PositiveCount++;
                        OnOffOfSwitch100.add(1);// 1 = ON
                        OnOffOfSwitch0.add(0);// 0 = OFF
                    } else if (rouletteItemDataSet.getOnOffInfoOfSwitch0().get(i)) {
                        switch0PositiveCount++;
                        OnOffOfSwitch0.add(1);// 1 = ON
                        OnOffOfSwitch100.add(0);// 0 = OFF
                    } else {
                        OnOffOfSwitch100.add(0); // 0 = OFF
                        OnOffOfSwitch0.add(0); // 0 = OFF
                    }
                }


                if (switch100PositiveCount >= 1) {
                    for (int i = 0; i < rouletteItemCount; i++) {
                        //switch100がONだったら
                        if (rouletteItemDataSet.getOnOffInfoOfSwitch100().get(i)) {
                            //そこの項目に当たる確率を付与する
                            //itemProbabilityArray[i] = 1f / switch100PositiveCount;
                            itemProbabilities.add(1f / switch100PositiveCount);
                        } else {
                            //ONになっていない項目には０％を付与
                            //itemProbabilityArray[i] = 0f;
                            itemProbabilities.add(0f);
                        }
                    }
                }

                if (switch0PositiveCount >= 1) {
                    //switch100, switch0 のONOFF情報を元に、項目別当選確率を決定、追加する
                    for (int i = 0; i < rouletteItemCount; i++) {
                        //switch0がONだったら
                        if (rouletteItemDataSet.getOnOffInfoOfSwitch0().get(i)) {
                            //そこの項目に０％を付与する
                            //itemProbabilityArray[i] = 0f;
                            itemProbabilities.add(0f);
                        } else {
                            //ONになっていない項目は均等に確率を付与する
                            //itemProbabilityArray[i] = 1f / (rouletteItemCount - switch0PositiveCount);
                            itemProbabilities.add(1f / (rouletteItemCount - switch0PositiveCount));
                        }
                    }
                }

                //ArrayList<Float>はintentで送れないので、送信可能な配列の形に変換
                float itemProbabilityArray[] = new float[itemProbabilities.size()];
                for (int i = 0; i < itemProbabilities.size(); i++) {
                    itemProbabilityArray[i] = itemProbabilities.get(i);
                }

                //保存するかどうかをチェックボックスで確認する場合
                /*
                if (checkBox.isChecked()) {
                    Word word = new Word(rouletteName.getText().toString(), getNowDate(),
                            rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                            rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                    //データベースにinsertされて初めて、primaryKeyがautoGenerateされる
                    MainActivity.mWordViewModel.insert(word);

                    //Log.d("うううううううううううううう", String.valueOf(mainActivity.mWordViewModel.getAllWords().getValue().get(mainActivity.mWordViewModel.getAllWords().getValue().size() - 1).getId()));
                }

                Intent fromRouletteCreateIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？
                fromRouletteCreateIntent.putExtra("rouletteName", rouletteName.getText().toString());
                fromRouletteCreateIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                fromRouletteCreateIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                fromRouletteCreateIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                //rouletteCreateIntent.putExtra("itemProbabilitySize", itemProbability.size());
                fromRouletteCreateIntent.putExtra("itemProbability", itemProbabilityArray);

                setResult(RESULT_OK, fromRouletteCreateIntent);

                finish();

                 */

                Intent fromRouletteCreateIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？

                if (!isTutorialState) {
                    SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(RouletteCreateActivity.this);
                    if (!(defaultPref.getBoolean(getString(R.string.saved_auto_save_created_roulette_key), false))) {
                        // 保存するかどうかをアラートダイアログで確認する場合
                        AlertDialog.Builder builder = new AlertDialog.Builder(RouletteCreateActivity.this);
                        builder.setMessage("Myルーレットに保存しますか？")
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        MyRoulette myRoulette = new MyRoulette(rouletteName.getText().toString(), getNowDate(),
                                                rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                                                rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                                        //データベースにinsertされて初めて、primaryKeyがautoGenerateされる


                                        MainActivity.mMyRouletteViewModel.insert(myRoulette);

                                        //Intent fromRouletteCreateIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？
                                        fromRouletteCreateIntent.putExtra("isTutorialState", isTutorialState);
                                        fromRouletteCreateIntent.putExtra("rouletteName", rouletteName.getText().toString());
                                        fromRouletteCreateIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                                        fromRouletteCreateIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                                        fromRouletteCreateIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                                        fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                                        fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                                        //rouletteCreateIntent.putExtra("itemProbabilitySize", itemProbability.size());
                                        fromRouletteCreateIntent.putExtra("itemProbability", itemProbabilityArray);

                                        setResult(RESULT_OK, fromRouletteCreateIntent);

                                        RouletteCreateActivity.this.finish();
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //Intent fromRouletteCreateIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？
                                        fromRouletteCreateIntent.putExtra("isTutorialState", isTutorialState);
                                        fromRouletteCreateIntent.putExtra("rouletteName", rouletteName.getText().toString());
                                        fromRouletteCreateIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                                        fromRouletteCreateIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                                        fromRouletteCreateIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                                        fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                                        fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                                        //rouletteCreateIntent.putExtra("itemProbabilitySize", itemProbability.size());
                                        fromRouletteCreateIntent.putExtra("itemProbability", itemProbabilityArray);

                                        setResult(RESULT_OK, fromRouletteCreateIntent);

                                        RouletteCreateActivity.this.finish();
                                    }
                                })
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // ダイアログがキャンセルされた際の処理
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        //確認しない場合（自動保存）
                        MyRoulette myRoulette = new MyRoulette(rouletteName.getText().toString(), getNowDate(),
                                rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                                rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                        //データベースにinsertされて初めて、primaryKeyがautoGenerateされる

                        MainActivity.mMyRouletteViewModel.insert(myRoulette);

                        //Intent fromRouletteCreateIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？
                        fromRouletteCreateIntent.putExtra("isTutorialState", isTutorialState);
                        fromRouletteCreateIntent.putExtra("rouletteName", rouletteName.getText().toString());
                        fromRouletteCreateIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                        fromRouletteCreateIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                        fromRouletteCreateIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                        fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                        fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                        //rouletteCreateIntent.putExtra("itemProbabilitySize", itemProbability.size());
                        fromRouletteCreateIntent.putExtra("itemProbability", itemProbabilityArray);

                        setResult(RESULT_OK, fromRouletteCreateIntent);

                        RouletteCreateActivity.this.finish();
                    }

//                startActivity(rouletteCreateIntent);

                    //finish();

                } else {
                    //Intent fromRouletteCreateIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？
                    fromRouletteCreateIntent.putExtra("isTutorialContinue", isTutorialState);
                    fromRouletteCreateIntent.putExtra("rouletteName", rouletteName.getText().toString());
                    fromRouletteCreateIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                    fromRouletteCreateIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                    fromRouletteCreateIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                    fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                    fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                    //rouletteCreateIntent.putExtra("itemProbabilitySize", itemProbability.size());
                    fromRouletteCreateIntent.putExtra("itemProbability", itemProbabilityArray);

                    setResult(RESULT_OK, fromRouletteCreateIntent);

                    isTutorialState = false;
                    /*
                    //最初のチュートリアルが終わったら、そのことを保存しておく
                    SharedPreferences sharedPref = RouletteCreateActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.saved_roulette_create_first_tutorial_done_key), true);
                    editor.apply();

                     */

                    RouletteCreateActivity.this.finish();
                }

            }
        });


        /*
        SharedPreferences sharedPref = RouletteCreateActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_roulette_create_first_tutorial_done_key), false);
        if (!isFirstTutorialDone) {
            isTutorialState = true;
            tutorial();
            //MaterialShowcaseView.resetSingleUse(this, getString(R.string.roulette_create_first_tutorial_id));//////////////////////////////////////////////////////
        }

         */
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        //public void onResume() {
        //super.onRestart();
        //super.onStart();
        //super.onResume();

        super.onWindowFocusChanged(hasFocus);

        Log.d("あああああああVVVVVVVVVV", "onWindowFocusChanged");

        SharedPreferences sharedPref = RouletteCreateActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_roulette_create_first_tutorial_done_key), false);
        if (!isFirstTutorialDone) {
            isTutorialState = true;
            //SharedPreferences sharedPref = RouletteCreateActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.saved_roulette_create_first_tutorial_done_key), true);
            editor.apply();

            tutorial();
            //MaterialShowcaseView.resetSingleUse(this, getString(R.string.roulette_create_first_tutorial_id));//////////////////////////////////////////////////////
        }
    }

    private void tutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.roulette_create_tutorial_id));

        /*
        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });
        */

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.roulette_create_tutorial_id));

        /*
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(rouletteCreateLayout)
                        .setContentText("ここではルーレットの作成を行うことができます。")
                        //.setToolTip(itemNameToolTip)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .withoutShape()
                        .build()
        );

         */

        ShowcaseTooltip rouletteNameToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("ここで、作成するルーレットに名前を付けることができます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(rouletteName)
                        //.setContentText("ここでルーレットの項目を１つずつ追加できます。")
                        .setToolTip(rouletteNameToolTip)
                        .setDismissOnTouch(true)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .withRectangleShape()
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .build()
        );

        ShowcaseTooltip colorButtonToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("タップすると、ルーレット項目の色を自由に選択できます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(((RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForLayoutPosition(1)).getColorButton())
                        //.setTarget(rouletteItemList.getChildAt(1).findViewById(R.id.colorButton))
                        //.setContentText("ここでルーレットの項目を１つずつ追加できます。")
                        .setToolTip(colorButtonToolTip)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .withRectangleShape()
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .build()
        );

        ShowcaseTooltip itemNameToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("ここでルーレット項目名を設定できます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(((RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForLayoutPosition(1)).getItemName())
                        //.setTarget(rouletteItemList.getChildAt(1).findViewById(R.id.itemName))
                        //.setContentText("ここでルーレットの項目を１つずつ追加できます。")
                        .setToolTip(itemNameToolTip)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .withRectangleShape()
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .build()
        );

        ShowcaseTooltip ratioToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("ルーレット項目の面積比を、０〜９９の数値で設定できます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(((RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForLayoutPosition(1)).getRatio())
                        //.setTarget(rouletteItemList.getChildAt(1).findViewById(R.id.ratio))
                        //.setContentText("ここでルーレットの項目を１つずつ追加できます。")
                        .setToolTip(ratioToolTip)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .withRectangleShape()
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .build()
        );

        ShowcaseTooltip deleteToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("タップするとルーレット項目を削除できます。スワイプでも削除が可能です。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(((RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForLayoutPosition(1)).getDeleteButton())
                        //.setTarget(rouletteItemList.getChildAt(1).findViewById(R.id.deleteButton))
                        //.setContentText("ここでルーレットの項目を１つずつ追加できます。")
                        .setToolTip(deleteToolTip)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .withRectangleShape()
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .build()
        );

        ShowcaseTooltip itemAddToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("ここでルーレットの項目を１つずつ追加できます。<br><br>タップして追加してみましょう。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(itemAddButton)
                        //.setContentText("ここでルーレットの項目を１つずつ追加できます。")
                        .setToolTip(itemAddToolTip)
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .withRectangleShape()
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .build()
        );

        ShowcaseTooltip cheatAppearToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("イカサマをするには、この隠しボタンを押す必要があります。<br><br>タップしてみましょう。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(cheatButton)
                        .setToolTip(cheatAppearToolTip)
                        //.setContentText("イカサマをするには、この隠しボタンを押す必要があります。\n\nタップしてみましょう。")
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withRectangleShape()
                        .build()
        );

        /*
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(((RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForAdapterPosition(1)).getLinearLayout2())
                        .setContentText("This is button three")
                        .withRectangleShape()
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(cheatButton)
                        .setContentText("This is button two")
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(createFinishFab)
                        .setContentText("This is button two")
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .build()
        );

         */

        Log.d("あああああああああああああ", "firstTutorial()");
        sequence.start();
    }

    private void continuationOfTutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100); // half second between each showcase view
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.roulette_create_continuation_tutorial_id));

        /*
        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });
        */

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.roulette_create_continuation_tutorial_id));

        ShowcaseTooltip cheatSwitchToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("イカサマメニューが出現しました。<br><br>必中スイッチ：この項目が<b>絶対に当たります</b>。<br><br>絶対ハズレスイッチ：この項目が<b>絶対に当たらなくなります</b>。<br><br>ここの設定をしなかった場合は通常の抽選を行います。<br><br>どちらかのスイッチをONにしてみましょう。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        //.setTarget(view)
                        //.setTarget(rouletteItemList.getChildAt(1).findViewById(R.id.LinearLayout2))
                        .setTarget(((RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForLayoutPosition(1)).getLinearLayout2())
                        //.setTarget(rouletteItemList.getChildAt(0).findViewById(R.id.LinearLayout2))
                        .setToolTip(cheatSwitchToolTip)
                        //.setContentText("イカサマメニューが出現しました。\n\n必中スイッチ：必ずこの項目が当たります。\n\n絶対ハズレスイッチ：絶対にこの項目が当たらなくなります。\n\nどちらかのスイッチをONにしてみましょう。")
                        .withRectangleShape()
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .build()
        );

        ShowcaseTooltip cheatHideToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("もう一度押すと、イカサマメニューを隠すことができます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(cheatButton)
                        .setToolTip(cheatHideToolTip)
                        //.setContentText("もう一度押すと、イカサマメニューを隠すことができます。")
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withRectangleShape()
                        .build()
        );

        ShowcaseTooltip createFinishToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("ここでルーレットの作成を完了します。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(createFinishFab)
                        .setToolTip(createFinishToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        .setShapePadding(50)
                        .build()
        );

        sequence.start();
    }

    //背景タプでフォーカスを外し、キーボードを隠す処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup rouletteCreateLayout = findViewById(R.id.roulette_create_layout);
        InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(rouletteCreateLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //背景にフォーカスを移す
        rouletteCreateLayout.requestFocus();

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_roulette_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //キーボードがあれば隠す
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        switch (item.getItemId()) {
            case R.id.page_explain:
                // ボタンをタップした際の処理を記述
                return true;
            case R.id.tutorial:

                AlertDialog.Builder builder = new AlertDialog.Builder(RouletteCreateActivity.this);
                builder.setTitle("チュートリアルを開始しますか？");
                builder.setMessage("開始すると現在の編集内容は破棄されます。")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //int listItemCount = rouletteItemListAdapter.getItemCount();

                                /*
                                rouletteItemListAdapter.getRouletteItemDataSet().getColors().removeAll(rouletteItemListAdapter.getRouletteItemDataSet().getColors());
                                rouletteItemListAdapter.getRouletteItemDataSet().getItemNames().removeAll(rouletteItemListAdapter.getRouletteItemDataSet().getItemNames());
                                rouletteItemListAdapter.getRouletteItemDataSet().getItemRatios().removeAll(rouletteItemListAdapter.getRouletteItemDataSet().getItemRatios());
                                rouletteItemListAdapter.getRouletteItemDataSet().getOnOffInfoOfSwitch100()
                                        .removeAll(rouletteItemListAdapter.getRouletteItemDataSet().getOnOffInfoOfSwitch100());
                                rouletteItemListAdapter.getRouletteItemDataSet().getOnOffInfoOfSwitch0()
                                        .removeAll(rouletteItemListAdapter.getRouletteItemDataSet().getOnOffInfoOfSwitch0());

                                 */

                                //rouletteItemListInfo.getColors().removeAll(rouletteItemListInfo.getColors());
                                //rouletteItemListInfo.getItemNames().removeAll(rouletteItemListInfo.getItemNames());
                                //rouletteItemListInfo.getItemRatios().removeAll(rouletteItemListInfo.getItemRatios());
                                //rouletteItemListInfo.getOnOffInfoOfSwitch100().removeAll(rouletteItemListInfo.getOnOffInfoOfSwitch100());
                                //rouletteItemListInfo.getOnOffInfoOfSwitch0().removeAll(rouletteItemListInfo.getOnOffInfoOfSwitch0());

                                rouletteItemListInfo.setColors(new ArrayList<Integer>(Arrays.asList(generateColor(), generateColor())));
                                rouletteItemListInfo.setItemNames(new ArrayList<String>(Arrays.asList("", "")));
                                rouletteItemListInfo.setItemRatios(new ArrayList<Integer>(Arrays.asList(1, 1)));
                                rouletteItemListInfo.setOnOffInfoOfSwitch100(new ArrayList<Boolean>(Arrays.asList(false, false)));
                                rouletteItemListInfo.setOnOffInfoOfSwitch0(new ArrayList<Boolean>(Arrays.asList(false, false)));

                                rouletteItemListAdapter.notifyDataSetChanged();

                                //rouletteItemListAdapter.notifyDataSetChanged();

                                //rouletteItemListAdapter.addItem(generateColor(), "", 1, false, false);
                                //rouletteItemListAdapter.addItem(generateColor(), "", 1, false, false);

                                if (visibleFlag) {
                                    visibleFlag = false;

                                    rouletteItemListAdapter.notifyDataSetChanged();
                                    //rouletteItemListAdapter.notifyItemRangeChanged(0, rouletteItemList.getChildCount());

                                    //visibleFlag = false;
                                    //cheatButton.setVisibility(View.INVISIBLE);
                                    cheatButton.setText("");
                                    cheatButton.setBackgroundColor(Color.TRANSPARENT);
                                }

                                MaterialShowcaseView.resetSingleUse(RouletteCreateActivity.this, getString(R.string.roulette_create_tutorial_id));
                                MaterialShowcaseView.resetSingleUse(RouletteCreateActivity.this, getString(R.string.roulette_create_continuation_tutorial_id));
                                MaterialShowcaseView.resetSingleUse(RouletteCreateActivity.this, getString(R.string.roulette_create_start_tutorial_id));

                                isTutorialState = true;

                                //チュートリアルを始める
                                ShowcaseConfig config = new ShowcaseConfig();
                                config.setDelay(100);
                                config.setRenderOverNavigationBar(true);

                                MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(RouletteCreateActivity.this, getString(R.string.roulette_create_start_tutorial_id));

                                sequence.setConfig(config);

                                sequence.singleUse(getString(R.string.roulette_create_start_tutorial_id));

                                sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
                                    @Override
                                    public void onDismiss(MaterialShowcaseView itemView, int position) {
                                        tutorial();
                                    }
                                });

                                //sequence.singleUse(getString(R.string.roulette_create_tutorial_id));

                                sequence.addSequenceItem(
                                        new MaterialShowcaseView.Builder(RouletteCreateActivity.this)
                                                .setTarget(rouletteCreateLayout)
                                                .setContentText("ここではルーレットの作成を行うことができます。")
                                                .setContentTextColor(getResources().getColor(R.color.showcase_text_color))
                                                .setGravity(16)
                                                .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                                                //.setToolTip(itemNameToolTip)
                                                //.setTargetTouchable(true)
                                                //.setDismissOnTargetTouch(true)
                                                .setDismissOnTouch(true)
                                                .withoutShape()
                                                .build()
                                );

                                sequence.start();

                                //tutorial();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                // ダイアログがキャンセルされた際の処理
                            }
                        })
                        .create()
                        .show();

                //MaterialShowcaseView.resetSingleUse(this, getString(R.string.roulette_create_tutorial_id));
                ///MaterialShowcaseView.resetSingleUse(this, getString(R.string.roulette_create_continuation_tutorial_id));
                //isTutorialState = true;
                //チュートリアルを始める
                //tutorial();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //ツールバーの戻るボタンを押した時の処理
    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return super.onSupportNavigateUp();
    }

    /*
    public void onClickColorButton(View colorButton) {
        ColorPickerDialogBuilder
                .with(RouletteCreateActivity.this)
                .setTitle("Choose color")
                .initialColor(((ColorButton)colorButton).getButtonColor())
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorChangedListener(new OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int selectedColor) {
                        // Handle on color change
                        //Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        //色情報の設定
                        ((ColorButton)colorButton).setButtonColor(selectedColor);
                        //色の変更
                        ((GradientDrawable)colorButton.getBackground()).setColor(selectedColor);
                        //色の変更があった部分のadapterPositionを取得
                        int position = rouletteItemList.getLayoutManager().getPosition((View)colorButton.getParent().getParent().getParent());
                        //色の変更を適応
                        ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().setColor(position, selectedColor);
                        //colorButton.setBackgroundColor(picker.getColor());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .showAlphaSlider(false)
                .showColorEdit(false)
                .showColorPreview(true)
                .build()
                .show();
    }

     */

    public void onClickColorButton(View colorButton) {
        //ColorPickDialogを開始する
        AlertDialog.Builder colorPickAlert = new AlertDialog.Builder(RouletteCreateActivity.this);

        colorPickAlert.setTitle("色の選択");

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.color_pick_dialog, null);
        //アラートダイアログの中にある色ボタン（選択色確認のview）
        ColorButton dialogColorButton = dialoglayout.findViewById(R.id.color_preview);
        ((GradientDrawable) dialogColorButton.getBackground()).setColor(((ColorButton)colorButton).getButtonColor());
        colorPickAlert.setView(dialoglayout);

        ColorPickerView colorPickerView = (ColorPickerView) dialoglayout.findViewById(R.id.color_picker_view);
        colorPickerView.setInitialColor(((ColorButton)colorButton).getButtonColor(), false);
        colorPickerView.setShowBorder(true);
        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                ((GradientDrawable) dialogColorButton.getBackground()).setColor(selectedColor);
            }
        });

        colorPickAlert
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //色情報の設定
                        ((ColorButton)colorButton).setButtonColor(colorPickerView.getSelectedColor());
                        //色の変更
                        ((GradientDrawable)colorButton.getBackground()).setColor(colorPickerView.getSelectedColor());
                        //色の変更があった部分のadapterPositionを取得
                        int position = rouletteItemList.getLayoutManager().getPosition((View)colorButton.getParent().getParent().getParent());
                        //色の変更を適応
                        ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().setColor(position, colorPickerView.getSelectedColor());
                        //colorButton.setBackgroundColor(picker.getColor());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // ダイアログがキャンセルされた際の処理
                    }
                })
                .create()
                .show();
    }

    public void onSwitch100Clicked(View view ) {

        SwitchCompat clickedSwitch = (SwitchCompat)view;

        if (clickedSwitch.isChecked()) {
            // The toggle is enabled
            //int itemCount = rouletteItemList.getChildCount();
            int itemCount = ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().getOnOffInfoOfSwitch100().size();
            for (int i = 0; i < itemCount; i++) {
                //ViewGroup layout2 = rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2);
                //SwitchCompat switch0 = rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).findViewById(R.id.switch0);
                //((SwitchCompat) layout2.findViewById(R.id.switch0)).setChecked(false);
                //switch0.setChecked(false);
                //((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().setOnOffInfoOfSwitch0Partially(i, false);
                RouletteItemListInfo itemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                itemDataSet.setOnOffInfoOfSwitch0Partially(i, false);
            }
            rouletteItemListAdapter.notifyDataSetChanged();
        } else {
            // The toggle is disabled
        }
    }


    public void onSwitch0Clicked(View view) {

        SwitchCompat clickedSwitch = (SwitchCompat) view;

        if (clickedSwitch.isChecked()) {
            // The toggle is enabled
            Boolean containSwitch0False = false;
            //int itemCount = rouletteItemList.getChildCount();
            int itemCount = ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().getOnOffInfoOfSwitch100().size();

            for (int i = 0; i < itemCount; i++) {
                //ViewGroup layout2 = rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2);
                //((SwitchCompat) layout2.findViewById(R.id.switch100)).setChecked(false);
                RouletteItemListInfo itemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                itemDataSet.setOnOffInfoOfSwitch100Partially(i, false);
                //他のswitch0が全てONになっているか確認　→　ひとつでもOFFがあったらflag = 1
                if (!itemDataSet.getOnOffInfoOfSwitch0().get(i)) {
                    containSwitch0False = true;
                }
            }
            //flag = 0 →　全てのswitch0がONになっているため、今ONになったswitch0をOFFにする（switch0を全てONにすることを禁止している）
            if (!containSwitch0False) {
                clickedSwitch.setChecked(false);
            }
            rouletteItemListAdapter.notifyDataSetChanged();
        } else {
            // The toggle is disabled
        }
    }

    public int generateColor() {
        int red = ((int) (Math.random() * 255));
        int green = ((int) (Math.random() * 255));
        int blue = ((int) (Math.random() * 255));
        return Color.rgb(red, green, blue);
    }

    public static String getNowDate(){
        @SuppressLint("SimpleDateFormat") final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    //public static int getRouletteCount() { return rouletteCount; }
}