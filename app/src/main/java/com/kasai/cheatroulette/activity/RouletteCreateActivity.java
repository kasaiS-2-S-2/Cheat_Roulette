package com.kasai.cheatroulette.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kasai.cheatroulette.R;
import com.kasai.cheatroulette.activity.mainActivity.MainActivity;
import com.kasai.cheatroulette.common.MyApplication;
import com.kasai.cheatroulette.dialogFragment.ColorPickDialogFragment;
import com.kasai.cheatroulette.recyclerView.RouletteItemListAdapter;
import com.kasai.cheatroulette.recyclerView.RouletteItemListInfo;
import com.kasai.cheatroulette.room.MyRoulette;
import com.kasai.cheatroulette.ui.ColorButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

public class RouletteCreateActivity extends AppCompatActivity {

    private ConstraintLayout rouletteCreateLayout;
    private EditText rouletteName;
    private Toolbar toolbar;
    private Button itemAddButton;
    private Button cheatButton;
    private FloatingActionButton createFinishFab;
    //ルーレットの項目リストの情報を保持するもの
    private RouletteItemListInfo rouletteItemListInfo = new RouletteItemListInfo(
            new ArrayList<Integer>(), new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<Boolean>(), new ArrayList<Boolean>());
    private RecyclerView rouletteItemList;
    private RouletteItemListAdapter rouletteItemListAdapter;

    public static boolean visibleFlag = false;
    private boolean isTutorialState = false;

    private Toast mToast = null;

    @SuppressLint({"WrongConstant", "SetTextI18n"})
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

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();

                if (!(rouletteItemListAdapter.getItemCount() <= 2)) {
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

        rouletteName = findViewById(R.id.rouletteName);

        rouletteItemListInfo.setColors(new ArrayList<Integer>(Arrays.asList(generateColor(), generateColor())));
        rouletteItemListInfo.setItemNames(new ArrayList<String>(Arrays.asList("", "")));
        rouletteItemListInfo.setItemRatios(new ArrayList<Integer>(Arrays.asList(1, 1)));
        rouletteItemListInfo.setOnOffInfoOfSwitch100(new ArrayList<Boolean>(Arrays.asList(false, false)));
        rouletteItemListInfo.setOnOffInfoOfSwitch0(new ArrayList<Boolean>(Arrays.asList(false, false)));

        rouletteItemListAdapter.notifyDataSetChanged();

        itemAddButton = findViewById(R.id.item_add_button);
        // リスナーをボタンに登録, lambda
        itemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rouletteItemListAdapter.getItemCount() >= 300) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), R.string.notice_item_is_max, Toast.LENGTH_SHORT);
                    mToast.show();
                } else {
                    //項目が追加された場合は、キーボードを隠す
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //RouletteItemListAdapterのaddItemをルーレット項目追加に使用する
                    rouletteItemListAdapter.addItem(generateColor(), "", 1, false, false);
                    //新しいルーレット項目が追加された時、recyclerViewを一番下に自動スクロールする
                    rouletteItemList.scrollToPosition(rouletteItemListAdapter.getItemCount() - 1);
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
                    rouletteItemListAdapter.notifyDataSetChanged();
                    cheatButton.setText("");
                    cheatButton.setBackgroundColor(Color.TRANSPARENT);

                } else {
                    visibleFlag = true;

                    rouletteItemListAdapter.notifyDataSetChanged();

                    cheatButton.setText(R.string.hide_cheat);
                    cheatButton.setBackgroundColor(Color.RED);

                    if (isTutorialState) {
                        //チュートリアルのつづきを表示
                        continuationOfTutorial();
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
            @Override
            public void onClick(View v) {
                boolean hasProblem = false;
                String problemContent = "内容を変更してください";

                int switch100PositiveCount = 0;
                int switch0PositiveCount = 0;
                RouletteItemListInfo rouletteItemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                int rouletteItemCount = rouletteItemListAdapter.getItemCount();
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
                            itemProbabilities.add(1f / switch100PositiveCount);
                        } else {
                            //ONになっていない項目には０％を付与
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
                            itemProbabilities.add(0f);
                        } else {
                            //ONになっていない項目は均等に確率を付与する
                            itemProbabilities.add(1f / (rouletteItemCount - switch0PositiveCount));
                        }
                    }
                }

                //ArrayList<Float>はintentで送れないので、送信可能な配列の形に変換
                float itemProbabilityArray[] = new float[itemProbabilities.size()];
                for (int i = 0; i < itemProbabilities.size(); i++) {
                    itemProbabilityArray[i] = itemProbabilities.get(i);
                }

                Intent fromRouletteCreateIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？

                if (hasProblem) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RouletteCreateActivity.this);
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

                    if (!isTutorialState) {
                        SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(RouletteCreateActivity.this);
                        if (!(defaultPref.getBoolean(getString(R.string.saved_auto_save_created_roulette_key), false))) {
                            // 保存するかどうかをアラートダイアログで確認する場合
                            AlertDialog.Builder builder = new AlertDialog.Builder(RouletteCreateActivity.this);
                            builder.setMessage("Myルーレットに保存しますか？")
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            int myRouletteItemCount = MainActivity.mMyRouletteViewModel.getAllMyRoulette().getValue().size();
                                            if (myRouletteItemCount < 50) {
                                                //保存個数上限未満なら保存
                                                MyRoulette myRoulette = new MyRoulette(rouletteName.getText().toString(), getNowDate(),
                                                        rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                                                        rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                                                //データベースにinsertされて初めて、primaryKeyがautoGenerateされる
                                                MainActivity.mMyRouletteViewModel.insert(myRoulette);
                                            } else {
                                                Toast.makeText(RouletteCreateActivity.this, getString(R.string.notificatoin_myRoulettes_are_max), Toast.LENGTH_SHORT).show();
                                            }

                                            fromRouletteCreateIntent.putExtra("isTutorialState", isTutorialState);
                                            fromRouletteCreateIntent.putExtra("rouletteName", rouletteName.getText().toString());
                                            fromRouletteCreateIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                                            fromRouletteCreateIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                                            fromRouletteCreateIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                                            fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                                            fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                                            fromRouletteCreateIntent.putExtra("itemProbability", itemProbabilityArray);

                                            setResult(RESULT_OK, fromRouletteCreateIntent);

                                            RouletteCreateActivity.this.finish();
                                        }
                                    })
                                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
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

                            int myRouletteItemCount = MainActivity.mMyRouletteViewModel.getAllMyRoulette().getValue().size();
                            if (myRouletteItemCount < 50) {
                                //保存個数上限未満なら保存
                                MyRoulette myRoulette = new MyRoulette(rouletteName.getText().toString(), getNowDate(),
                                        rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                                        rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                                //データベースにinsertされて初めて、primaryKeyがautoGenerateされる
                                MainActivity.mMyRouletteViewModel.insert(myRoulette);
                            } else {
                                Toast.makeText(RouletteCreateActivity.this, getString(R.string.notificatoin_myRoulettes_are_max), Toast.LENGTH_SHORT).show();
                            }

                            fromRouletteCreateIntent.putExtra("isTutorialState", isTutorialState);
                            fromRouletteCreateIntent.putExtra("rouletteName", rouletteName.getText().toString());
                            fromRouletteCreateIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                            fromRouletteCreateIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                            fromRouletteCreateIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                            fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                            fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                            fromRouletteCreateIntent.putExtra("itemProbability", itemProbabilityArray);

                            setResult(RESULT_OK, fromRouletteCreateIntent);

                            RouletteCreateActivity.this.finish();
                        }

                    } else {
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

                        RouletteCreateActivity.this.finish();
                    }

                }

            }
        });
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        Log.d("あああああああVVVVVVVVVV", "onWindowFocusChanged");

        SharedPreferences sharedPref = RouletteCreateActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_roulette_create_first_tutorial_done_key), false);
        if (!isFirstTutorialDone) {
            isTutorialState = true;

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.saved_roulette_create_first_tutorial_done_key), true);
            editor.apply();

            MaterialShowcaseView.resetSingleUse(RouletteCreateActivity.this, getString(R.string.roulette_create_first_tutorial_id));
            MaterialShowcaseView.resetSingleUse(RouletteCreateActivity.this, getString(R.string.roulette_create_continuation_tutorial_id));

            firstTutorial();
        }
    }

    private void firstTutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.roulette_create_first_tutorial_id));

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.roulette_create_first_tutorial_id));

        ShowcaseTooltip itemAddToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("ここでルーレットの項目を１つずつ追加できます。<br><br>タップして追加してみましょう。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(itemAddButton)
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
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withRectangleShape()
                        .build()
        );
        sequence.start();
    }

    private void tutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.roulette_create_tutorial_id));

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.roulette_create_tutorial_id));

        ShowcaseTooltip rouletteNameToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("ここで、作成するルーレットに名前を付けることができます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(rouletteName)
                        .setToolTip(rouletteNameToolTip)
                        .setDismissOnTouch(true)
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
                        .setToolTip(colorButtonToolTip)
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
                        .setToolTip(itemNameToolTip)
                        .setDismissOnTouch(true)
                        .withRectangleShape()
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .build()
        );

        ShowcaseTooltip ratioToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("ルーレット項目の面積比を、1~99の数値で設定できます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(((RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForLayoutPosition(1)).getRatio())
                        .setToolTip(ratioToolTip)
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
                        .setToolTip(deleteToolTip)
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
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withRectangleShape()
                        .build()
        );
        sequence.start();
    }

    private void continuationOfTutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.roulette_create_continuation_tutorial_id));

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.roulette_create_continuation_tutorial_id));

        ShowcaseTooltip cheatSwitchToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("イカサマメニューが出現しました。<br><br>必中スイッチ：この項目が<b>絶対に当たります</b>。<br><br>絶対ハズレスイッチ：この項目が<b>絶対に当たらなくなります</b>。<br><br>ここの設定をしなかった場合は通常の抽選を行います。<br><br>どちらかのスイッチをONにしてみましょう。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(((RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForLayoutPosition(1)).getLinearLayout2())
                        .setToolTip(cheatSwitchToolTip)
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
                .text("ここでルーレットの作成を完了します。<br><br>チュートリアルは右上のアイコンからいつでも見ることができます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(createFinishFab)
                        .setToolTip(createFinishToolTip)
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
        SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(RouletteCreateActivity.this);
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
            case R.id.tutorial:

                AlertDialog.Builder builder = new AlertDialog.Builder(RouletteCreateActivity.this);
                builder.setTitle("チュートリアルを開始しますか？");
                builder.setMessage("開始すると現在の編集内容は破棄されます。")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                rouletteItemListInfo.setColors(new ArrayList<Integer>(Arrays.asList(generateColor(), generateColor())));
                                rouletteItemListInfo.setItemNames(new ArrayList<String>(Arrays.asList("", "")));
                                rouletteItemListInfo.setItemRatios(new ArrayList<Integer>(Arrays.asList(1, 1)));
                                rouletteItemListInfo.setOnOffInfoOfSwitch100(new ArrayList<Boolean>(Arrays.asList(false, false)));
                                rouletteItemListInfo.setOnOffInfoOfSwitch0(new ArrayList<Boolean>(Arrays.asList(false, false)));

                                rouletteItemListAdapter.notifyDataSetChanged();

                                if (visibleFlag) {
                                    visibleFlag = false;

                                    rouletteItemListAdapter.notifyDataSetChanged();

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

                                sequence.addSequenceItem(
                                        new MaterialShowcaseView.Builder(RouletteCreateActivity.this)
                                                .setTarget(rouletteCreateLayout)
                                                .setContentText("ここではルーレットの作成を行うことができます。")
                                                .setContentTextColor(getResources().getColor(R.color.showcase_text_color))
                                                .setGravity(16)
                                                .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                                                .setDismissOnTouch(true)
                                                .withoutShape()
                                                .build()
                                );

                                sequence.start();
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

    public void onClickColorButton(View colorButton) {
        //ColorPickDialogを開始する
        ColorPickDialogFragment colorPickDialogFragment = new ColorPickDialogFragment((ColorButton)colorButton, rouletteItemList);
        colorPickDialogFragment.show(getSupportFragmentManager(), "colorPickDialog");
    }

    public void onSwitch100Clicked(View view ) {

        SwitchCompat clickedSwitch = (SwitchCompat)view;

        if (clickedSwitch.isChecked()) {
            int itemCount = ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().getOnOffInfoOfSwitch100().size();
            for (int i = 0; i < itemCount; i++) {
                RouletteItemListInfo itemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                itemDataSet.setOnOffInfoOfSwitch0Partially(i, false);
            }
            rouletteItemListAdapter.notifyDataSetChanged();
        }
    }


    public void onSwitch0Clicked(View view) {

        SwitchCompat clickedSwitch = (SwitchCompat) view;

        if (clickedSwitch.isChecked()) {
            boolean containSwitch0False = false;
            int itemCount = ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().getOnOffInfoOfSwitch100().size();

            for (int i = 0; i < itemCount; i++) {
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
}