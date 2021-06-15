package com.kasai.cheatroulette;

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
import java.util.Date;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

//import androidx.annotation.RequiresApi;
/*
今はゴリゴリレイアウト書いてるけど、ホントは,リストviewとか、adapterとか使ったほうが良さそう。
 */

public class EditMyRouletteActivity extends AppCompatActivity {

    //public int rouletteCount;
    private ConstraintLayout editMyRouletteLayout;
    private CheckBox checkBox;
    private EditText rouletteName;
    private Toolbar toolbar;
    private Button itemAddButton;
    private Button cheatButton;
    private FloatingActionButton editMyRouletteFinishFab;
    //private EditText editText, editText2;
    //ルーレットの項目リストの情報を保持するもの
    private RouletteItemListInfo rouletteItemListInfo = new RouletteItemListInfo(
            new ArrayList<Integer>(), new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<Boolean>(), new ArrayList<Boolean>());
    private RecyclerView rouletteItemList;
    private EditMyRouletteAdapter rouletteItemListAdapter;
    //private EditMyRouletteAdapter rouletteItemListAdapter = new EditMyRouletteAdapter(rouletteItemListInfo);
    //private RouletteItemListInfo rouletteItemListInfo;

    public static boolean visibleFlag = false;

    private Toast mToast = null;

    //編集前のルーレット情報
    int rouletteIdFromMyRoulette;
    String rouletteNameFromMyRoulette;
    ArrayList<Integer> colorsFromMyRoulette;
    ArrayList<String> itemNamesFromMyRoulette;
    ArrayList<Integer> itemRatiosFromMyRoulette;
    ArrayList<Integer> Switch100InfoFromMyRoulette;
    ArrayList<Boolean> Switch100InfoFromMyRouletteBoolean;
    ArrayList<Integer> Switch0InfoFromMyRoulette;
    ArrayList<Boolean> Switch0InfoFromMyRouletteBoolean;

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

        MainActivity.mMyRouletteViewModel.getAllMyRoulette().observe(this, myRoulettes -> {
            MainActivity.adapter.submitList(myRoulettes);
        });

        visibleFlag = false;

        toolbar = findViewById(R.id.toolbar_roulette_create);
        toolbar.setTitle(R.string.edit_myRoulette);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editMyRouletteLayout = findViewById(R.id.roulette_create_layout);

        //スクリーンサイズの取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point realSize = new Point();
        disp.getRealSize(realSize);
        int ScreenWidth = realSize.x;
        int ScreenHeight = realSize.y;

        rouletteItemList = findViewById(R.id.roulette_item_list);
        rouletteItemListAdapter = new EditMyRouletteAdapter(rouletteItemList, rouletteItemListInfo);
        rouletteItemList.setAdapter(rouletteItemListAdapter);
        rouletteItemList.setLayoutManager(new LinearLayoutManager(this));

        //スワイプ削除時にアラートダイアログを表示できるやつ
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when ic_cheat_roulette_launcher2_foreground user swipes left or right on ic_cheat_roulette_launcher2_foreground ViewHolder
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


        //recyclerViewの真下に少しの空白を空ける（スクリーンサイズの1/8分空ける）
        rouletteItemList.setPadding(0, 0, 0, ScreenHeight/8);
        //RouletteItemListInfo rouletteItemListInfo;
        //RouletteItemListAdapter rouletteItemListAdapter;

        rouletteName = findViewById(R.id.rouletteName);

        //checkBox.setChecked(false);

        Intent fromMyRouletteIntent = getIntent(); //Mainでedit_Buttonを押した時の遷移

        rouletteIdFromMyRoulette = fromMyRouletteIntent.getIntExtra("rouletteId", 0);
        rouletteNameFromMyRoulette = fromMyRouletteIntent.getStringExtra("rouletteName");
        colorsFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra("colors");
        itemNamesFromMyRoulette = fromMyRouletteIntent.getStringArrayListExtra("itemNames");
        itemRatiosFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra("itemRatios");
        Switch100InfoFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra("OnOffInfoOfSwitch100");
        Switch100InfoFromMyRouletteBoolean = new ArrayList<Boolean>();
        Switch0InfoFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra("OnOffInfoOfSwitch0");
        Switch0InfoFromMyRouletteBoolean = new ArrayList<Boolean>();

        for (int i=0; i<Switch100InfoFromMyRoulette.size(); i++) {

            if (Switch100InfoFromMyRoulette.get(i) == 1) {
                Switch100InfoFromMyRouletteBoolean.add(true);
            } else {
                Switch100InfoFromMyRouletteBoolean.add(false);
            }

            if (Switch0InfoFromMyRoulette.get(i) == 1) {
                Switch0InfoFromMyRouletteBoolean.add(true);
            } else {
                Switch0InfoFromMyRouletteBoolean.add(false);
            }

        }

        rouletteName.setText(rouletteNameFromMyRoulette);//ルーレット名を設定

        rouletteItemListInfo.setColors(colorsFromMyRoulette);
        rouletteItemListInfo.setItemNames(itemNamesFromMyRoulette);
        rouletteItemListInfo.setItemRatios(itemRatiosFromMyRoulette);
        rouletteItemListInfo.setOnOffInfoOfSwitch100(Switch100InfoFromMyRouletteBoolean);
        rouletteItemListInfo.setOnOffInfoOfSwitch0(Switch0InfoFromMyRouletteBoolean);

        rouletteItemListAdapter.notifyDataSetChanged();


        //************* ボタンの設定 *************/
        itemAddButton = findViewById(R.id.item_add_button);
        // リスナーをボタンに登録, lambda
        itemAddButton.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (rouletteItemListAdapter.getItemCount() >= 300) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(),  R.string.notice_item_is_max, Toast.LENGTH_SHORT);
                    mToast.show();
                } else {
                    //addEditView(layout, scale, margins);
                    //rouletteItemListAdapter.addItem(generateColor(), "", 1, 0, 0);

                    //項目が追加された場合は、キーボードを隠す
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //RouletteItemListAdapterのaddItemをルーレット項目追加に使用する
                    rouletteItemListAdapter.addItem(generateColor(), "", 1, false, false);
                    //新しいルーレット項目が追加された時、recyclerViewを一番下に自動スクロールする
                    rouletteItemList.scrollToPosition(rouletteItemListAdapter.getItemCount() - 1);
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

                    //trueの場合は、イカサマスイッチを隠す
                    for (int i = 0; i < rouletteItemList.getChildCount(); i++) {
                        rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).setVisibility(View.GONE);
                        //((ViewGroup) layout.getChildAt(i)).getChildAt(1).setVisibility(View.GONE);
                        //rouletteItemListAdapter.notifyItemChanged(i);
                    }
                    rouletteItemListAdapter.notifyDataSetChanged();
                    //rouletteItemListAdapter.notifyItemRangeChanged(0, rouletteItemList.getChildCount());

                    visibleFlag = false;
                    //cheatButton.setVisibility(View.INVISIBLE);
                    cheatButton.setText("");
                    cheatButton.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    // falseの場合は、イカサマスイッチを表示
                    for (int i = 0; i < rouletteItemList.getChildCount(); i++) {
                        rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).setVisibility(View.VISIBLE);
                        //((ViewGroup) layout.getChildAt(i)).getChildAt(1).setVisibility(View.VISIBLE);
                        //rouletteItemListAdapter.notifyItemChanged(i);
                    }
                    rouletteItemListAdapter.notifyDataSetChanged();
                    //rouletteItemListAdapter.notifyItemRangeChanged(0, rouletteItemList.getChildCount());

                    visibleFlag = true;
                    //cheatButton.setVisibility(View.VISIBLE);
                    cheatButton.setText(R.string.hide_cheat);
                    cheatButton.setBackgroundColor(Color.RED);
                }
                //イカサマボタンが押された場合、キーボードを隠す
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        //EditText rouletteName = findViewById(R.id.rouletteName);

        editMyRouletteFinishFab = findViewById(R.id.create_finish_fab);
        editMyRouletteFinishFab.setOnClickListener(new View.OnClickListener() {
            // @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                boolean hasProblem = false;
                String problemContent = "内容を変更してください";

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

                //作成ボタンが押された場合、キーボードを隠す
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                for (int i=0; i < rouletteItemCount; i++) {
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

                if (switch0PositiveCount >= 1 && switch100PositiveCount >= 1) {
                    hasProblem = true;
                    problemContent = "必中スイッチと絶対ハズレスイッチがどちらONになっています。\nどちらかをOFFにしてください。";
                }

                if (switch0PositiveCount == rouletteItemCount) {
                    hasProblem = true;
                    problemContent = "全ての絶対ハズレスイッチをONにすることはできません。\nどれかをOFFにしてください。";
                }

                ArrayList<Integer> itemRatioArrayList = rouletteItemDataSet.getItemRatios();
                for (int i=0; i < itemRatioArrayList.size(); i++) {
                    int ratio = itemRatioArrayList.get(i);
                    if (ratio < 1 || ratio > 99) {
                        hasProblem = true;
                        problemContent = "面積比の値が不正です。\n1~99の値を入れてください。";
                        break;
                    }
                }


                if (switch100PositiveCount >= 1) {
                    for (int i = 0; i < rouletteItemCount; i++) {
                        //switch100がONだったら
                        if (rouletteItemDataSet.getOnOffInfoOfSwitch100().get(i)) {
                            //そこの項目に当たる確率を付与する
                            //itemProbabilityArray[i] = 1f / switch100PositiveCount;
                            itemProbabilities.add(1f/switch100PositiveCount);
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
                for (int i=0; i<itemProbabilities.size(); i++) {
                    itemProbabilityArray[i] = itemProbabilities.get(i);
                }

                if (hasProblem) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditMyRouletteActivity.this);
                    builder.setTitle("ルーレット内容が不正です。")
                            .setMessage(problemContent)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                } else {

                    MyRoulette myRoulette = new MyRoulette(rouletteName.getText().toString(), getNowDate(),
                            rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                            rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                    myRoulette.setId(rouletteIdFromMyRoulette);
                    MainActivity.mMyRouletteViewModel.update(myRoulette);
                    //MainActivity.adapter.notifyItemChanged(editItemPosition);
                    //MyRouletteActivity.recyclerView.getAdapter().notifyItemChanged(editItemPosition);


                /*
                Intent toMyRoulette = new Intent(getApplicationContext(), MyRouletteActivity.class);
                startActivity(toMyRoulette);
                finish();

                 */
                    finish();
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

                /*
                // 保存するかどうかをアラートダイアログで確認する場合
                AlertDialog.Builder builder = new AlertDialog.Builder(EditMyRouletteActivity.this);
                builder.setMessage("Myルーレットに保存しますか？")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Word word = new Word(rouletteName.getText().toString(), getNowDate(),
                                        rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                                        rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                                //データベースにinsertされて初めて、primaryKeyがautoGenerateされる
                                MainActivity.mWordViewModel.insert(word);

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

                                RouletteCreateActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

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

//                startActivity(rouletteCreateIntent);

                //finish();

                 */

            }
        });

        SharedPreferences sharedPref = EditMyRouletteActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_edit_myRoulette_first_tutorial_done_key), false);
        if (!isFirstTutorialDone) {
            //isTutorialState = true;
            tutorial();
            //最初のチュートリアルが終わったら、そのことを保存しておく
            //SharedPreferences sharedPref = EditRouletteActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.saved_edit_myRoulette_first_tutorial_done_key), true);
            editor.apply();
            //MaterialShowcaseView.resetSingleUse(this, getString(R.string.roulette_create_first_tutorial_id));//////////////////////////////////////////////////////
        }

    }

    private void tutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.edit_myRoulette_tutorial_id));

        /*
        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });
        */

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.edit_myRoulette_tutorial_id));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(editMyRouletteLayout)
                        .setContentText("ここでは選択したMyルーレットの編集を行うことができます。\n\n基本的な操作は「ルーレット作成」と同じです。詳しい操作は「ルーレット作成」をご覧ください。")
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

        ShowcaseTooltip editFinishToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("ここでルーレットの編集を完了します。<br>完了後は、選択したMyルーレットに編集内容が反映されます。<br><br>以上でこの画面のチュートリアルを終了します。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(editMyRouletteFinishFab)
                        .setToolTip(editFinishToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        .setShapePadding(50)
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
        SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(EditMyRouletteActivity.this);
        if (!(defaultPref.getBoolean(getString(R.string.saved_tutorial_appear_key), true))) {
            menu.setGroupVisible(R.id.roulette_create_toolbar_menu_group, false);
        }
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
                // ボタンをタップした際の処理を記述
                AlertDialog.Builder builder = new AlertDialog.Builder(EditMyRouletteActivity.this);
                builder.setTitle("チュートリアルを開始しますか？")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MaterialShowcaseView.resetSingleUse(EditMyRouletteActivity.this, getString(R.string.edit_myRoulette_tutorial_id));
                                tutorial();
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
    //バックキーを押すと、内容に変更があった場合にアラートダイアログを出す処理
    //うまくいかない
    @Override
    public void onBackPressed() {

        //メインスレッドで行う場合の処理

        //RouletteItemListInfo rouletteItemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();

     */

        /*
        Log.d("あああああああああああああああああああ1", String.valueOf((rouletteName.getText().toString()).equals(rouletteNameFromMain)));
        Log.d("あああああああああああああああああああ2", String.valueOf((rouletteItemDataSet.getColors()).equals(colorsFromMain)));
        Log.d("あああああああああああああああああああ3", String.valueOf((rouletteItemDataSet.getItemNames()).equals(itemNamesFromMain)));
        Log.d("あああああああああああああああああああ3", String.valueOf((rouletteItemDataSet.getItemNames())));
        Log.d("あああああああああああああああああああ3", String.valueOf(itemNamesFromMain));

        Log.d("あああああああああああああああああああ4", String.valueOf((rouletteItemDataSet.getItemRatios()).equals(itemRatiosFromMain)));
        Log.d("あああああああああああああああああああ5", String.valueOf((rouletteItemDataSet.getOnOffInfoOfSwitch100()).equals(Switch100InfoFromMainBoolean)));
        Log.d("あああああああああああああああああああ6", String.valueOf((rouletteItemDataSet.getOnOffInfoOfSwitch0().equals(Switch0InfoFromMainBoolean))));

        Log.d("あああああああああああああああああああ7", String.valueOf(! (rouletteName.getText().toString()).equals(rouletteNameFromMain) &&
                (rouletteItemDataSet.getColors()).equals(colorsFromMain) &&
                (rouletteItemDataSet.getItemNames()).equals(itemNamesFromMain) &&
                (rouletteItemDataSet.getItemRatios()).equals(itemRatiosFromMain) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch100()).equals(Switch100InfoFromMainBoolean) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch0().equals(Switch0InfoFromMainBoolean))));

        Log.d("あああああああああああああああああああ8", String.valueOf(!( (rouletteName.getText().toString()).equals(rouletteNameFromMain) &&
                (rouletteItemDataSet.getColors()).equals(colorsFromMain) &&
                (rouletteItemDataSet.getItemNames()).equals(itemNamesFromMain) &&
                (rouletteItemDataSet.getItemRatios()).equals(itemRatiosFromMain) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch100()).equals(Switch100InfoFromMainBoolean) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch0().equals(Switch0InfoFromMainBoolean)))));

         */


        //Word infoFromMyRoulette = MainActivity.mWordViewModel.getWord(rouletteIdFromMyRoulette);

/*
        ArrayList<Integer> Switch100InfoOfCurrent = new ArrayList<Integer>();
        ArrayList<Integer> Switch0InfoOfCurrent = new ArrayList<Integer>();

        ArrayList<Boolean> Switch100InfoOfCurrentBoolean = rouletteItemDataSet.getOnOffInfoOfSwitch100();
        ArrayList<Boolean> Switch0InfoOfCurrentBoolean = rouletteItemDataSet.getOnOffInfoOfSwitch0();

        for (int i=0; i<rouletteItemDataSet.getOnOffInfoOfSwitch100().size(); i++) {
            if (Switch100InfoOfCurrentBoolean.get(i)) {
                Switch100InfoOfCurrent.add(1);
            } else {
                Switch100InfoOfCurrent.add(0);
            }

            if (Switch0InfoOfCurrentBoolean.get(i)) {
                Switch0InfoOfCurrent.add(1);
            } else {
                Switch0InfoOfCurrent.add(0);
            }
        }

        Log.d("ああああああああああああああ1", String.valueOf((rouletteName.getText().toString()).equals(rouletteNameFromMyRoulette)));
        Log.d("ああああああああああああああ2", String.valueOf((rouletteItemDataSet.getColors()).equals(colorsFromMyRoulette)));
        Log.d("ああああああああああああああ3", String.valueOf((rouletteItemDataSet.getItemNames()).equals(itemNamesFromMyRoulette)));
        Log.d("ああああああああああああああ4", String.valueOf((rouletteItemDataSet.getItemRatios()).equals(itemRatiosFromMyRoulette)));
        Log.d("ああああああああああああああ5", String.valueOf(Switch100InfoOfCurrent.equals(Switch100InfoFromMyRoulette)));
        Log.d("ああああああああああああああ6", String.valueOf(Switch0InfoOfCurrent.equals(Switch0InfoFromMyRoulette)));

        //~FromMainが勝手に更新されるから、全部trueになって絶対if文通らない
        if ( ! ( (rouletteName.getText().toString()).equals(rouletteNameFromMyRoulette) &&
                (rouletteItemDataSet.getColors()).equals(colorsFromMyRoulette) &&
                (rouletteItemDataSet.getItemNames()).equals(itemNamesFromMyRoulette) &&
                (rouletteItemDataSet.getItemRatios()).equals(itemRatiosFromMyRoulette) &&
                Switch100InfoOfCurrent.equals(Switch100InfoFromMyRoulette) &&
                Switch0InfoOfCurrent.equals(Switch0InfoFromMyRoulette) ) ) {


            // 内容更新が合った場合にそれを破棄するかどうかをアラートダイアログで確認
            AlertDialog.Builder builder = new AlertDialog.Builder(EditMyRouletteActivity.this);
            builder.setMessage("編集内容を破棄しますか？")
                    .setPositiveButton("破棄する", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditMyRouletteActivity.this.finish();
                        }
                    })
                    .setNegativeButton("編集を続行する", new DialogInterface.OnClickListener() {
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
        } else {
            EditMyRouletteActivity.this.finish();
        }

 */

/*
        //別スレッドで行う場合の処理
        new Thread(new Runnable() {
            @Override
            public void run() {
                //idに対応する文字列をデータベースから取得する処理
                Word infoFromMyRoulette = MainActivity.mWordViewModel.getWord(rouletteIdFromMyRoulette);
                actionOfInfoMyRoulette(infoFromMyRoulette);
            }
        }).start();

    }

 */

/*
    //別スレッドで行う場合の処理
    private void actionOfInfoMyRoulette (Word infoFromMyRoulette) {
        runOnUiThread(new Runnable() {
            public void run() {
                showUiDialog(infoFromMyRoulette);
            }
        });
    }

 */

/*
    //別スレッドで行った場合の処理
    private void showUiDialog(Word infoFromMyRoulette) {

        RouletteItemListInfo rouletteItemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();

        ArrayList<Integer> Switch100InfoOfCurrent = new ArrayList<Integer>();
        ArrayList<Integer> Switch0InfoOfCurrent = new ArrayList<Integer>();

        ArrayList<Boolean> Switch100InfoOfCurrentBoolean = rouletteItemDataSet.getOnOffInfoOfSwitch100();
        ArrayList<Boolean> Switch0InfoOfCurrentBoolean = rouletteItemDataSet.getOnOffInfoOfSwitch0();

        for (int i=0; i<rouletteItemDataSet.getOnOffInfoOfSwitch100().size(); i++) {
            if (Switch100InfoOfCurrentBoolean.get(i)) {
                Switch100InfoOfCurrent.add(1);
            } else {
                Switch100InfoOfCurrent.add(0);
            }

            if (Switch0InfoOfCurrentBoolean.get(i)) {
                Switch0InfoOfCurrent.add(1);
            } else {
                Switch0InfoOfCurrent.add(0);
            }
        }

        Log.d("あああああああああああああああああああ1", String.valueOf((rouletteName.getText().toString()).equals(infoFromMyRoulette.getWord())));
        Log.d("あああああああああああああああああああ2", String.valueOf((rouletteItemDataSet.getColors()).equals(infoFromMyRoulette.getColorsInfo())));
        Log.d("あああああああああああああああああああ3", String.valueOf((rouletteItemDataSet.getItemNames()).equals(infoFromMyRoulette.getTextStringsInfo())));
        Log.d("あああああああああああああああああああ4", String.valueOf((rouletteItemDataSet.getItemRatios()).equals(infoFromMyRoulette.getItemRatiosInfo())));
        Log.d("あああああああああああああああああああ5", String.valueOf(Switch100InfoOfCurrent.equals(infoFromMyRoulette.getOnOffOfSwitch100Info())));
        Log.d("あああああああああああああああああああ6", String.valueOf(Switch0InfoOfCurrent.equals(infoFromMyRoulette.getOnOffOfSwitch0Info())));


        if ( ! ( (rouletteName.getText().toString()).equals(infoFromMyRoulette.getWord()) &&
                (rouletteItemDataSet.getColors()).equals(infoFromMyRoulette.getColorsInfo()) &&
                (rouletteItemDataSet.getItemNames()).equals(infoFromMyRoulette.getTextStringsInfo()) &&
                (rouletteItemDataSet.getItemRatios()).equals(infoFromMyRoulette.getItemRatiosInfo()) &&
                Switch100InfoOfCurrent.equals(infoFromMyRoulette.getOnOffOfSwitch100Info()) &&
                Switch0InfoOfCurrent.equals(infoFromMyRoulette.getOnOffOfSwitch0Info()) ) ){


            // 内容更新が合った場合にそれを破棄するかどうかをアラートダイアログで確認
            AlertDialog.Builder builder = new AlertDialog.Builder(EditMyRouletteActivity.this);
            builder.setMessage("編集内容を破棄しますか？")
                    .setPositiveButton("破棄する", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditMyRouletteActivity.this.finish();
                        }
                    })
                    .setNegativeButton("編集を続行する", new DialogInterface.OnClickListener() {
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
        } else {
            EditMyRouletteActivity.this.finish();
        }
    }

 */

    public void onClickColorButton(View colorButton) {
        //ColorPickDialogを開始する
        AlertDialog.Builder colorPickAlert = new AlertDialog.Builder(EditMyRouletteActivity.this);

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