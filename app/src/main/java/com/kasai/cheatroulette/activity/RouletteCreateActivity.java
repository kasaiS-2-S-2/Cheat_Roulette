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
import com.kasai.cheatroulette.common.NowDate;
import com.kasai.cheatroulette.dialogFragment.ColorPickDialogFragment;
import com.kasai.cheatroulette.dialogFragment.RoulettePreviewDialogFragment;
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
    private Button previewButton;
    private Button cheatButton;
    private Button createFinishButton;
    //???????????????????????????????????????????????????????????????
    private RouletteItemListInfo rouletteItemListInfo = new RouletteItemListInfo(
            new ArrayList<Integer>(), new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<Boolean>(), new ArrayList<Boolean>());
    private RecyclerView rouletteItemList;
    private RouletteItemListAdapter rouletteItemListAdapter;

    public static final String TAG_ROULETTE_PREVIEW_DIALOG_FRAGMENT = "roulettePreviewDialog";

    public static boolean visibleFlag = false;
    private boolean isTutorialState = false;

    private Toast mToast = null;

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulettecreate);

        //??????????????????
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

        //?????????????????????????????????
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point realSize = new Point();
        disp.getRealSize(realSize);
        int ScreenWidth = realSize.x;
        int ScreenHeight = realSize.y;

        //recyclerView?????????????????????????????????????????????????????????????????????1/8???????????????
        rouletteItemList.setPadding(0, 0, 0, ScreenHeight/8);

        //???????????????????????????????????????????????????????????????????????????
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

                    //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
        // ?????????????????????????????????, lambda
        itemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rouletteItemListAdapter.getItemCount() >= 300) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), R.string.notice_item_is_max, Toast.LENGTH_SHORT);
                    mToast.show();
                } else {
                    //????????????????????????????????????????????????????????????
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //RouletteItemListAdapter???addItem?????????????????????????????????????????????
                    rouletteItemListAdapter.addItem(generateColor(), "", 1, false, false);
                    //??????????????????????????????????????????????????????recyclerView??????????????????????????????????????????
                    rouletteItemList.scrollToPosition(rouletteItemListAdapter.getItemCount() - 1);
                }
            }
        });

        previewButton = findViewById(R.id.roulette_preview_button);
        previewButton.setOnClickListener(view -> {
            RouletteItemListInfo rouletteItemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
            // ?????????????????????????????????????????????????????????????????????
            RoulettePreviewDialogFragment roulettePreviewDialogFragment = new RoulettePreviewDialogFragment(
                    rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(), rouletteItemDataSet.getItemRatios());
            roulettePreviewDialogFragment.show(getSupportFragmentManager(), TAG_ROULETTE_PREVIEW_DIALOG_FRAGMENT);
        });


        cheatButton = findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (visibleFlag) {
                    visibleFlag = false;
                    //true????????????????????????????????????????????????
                    rouletteItemListAdapter.notifyDataSetChanged();
                    cheatButton.setText("");
                    cheatButton.setBackgroundColor(Color.TRANSPARENT);

                } else {
                    visibleFlag = true;

                    rouletteItemListAdapter.notifyDataSetChanged();

                    cheatButton.setText(R.string.hide_cheat);
                    cheatButton.setBackgroundColor(Color.RED);

                    if (isTutorialState) {
                        //??????????????????????????????????????????
                        continuationOfTutorial();
                    }
                }

                //?????????????????????????????????????????????????????????????????????
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        createFinishButton = findViewById(R.id.create_finish_button);
        createFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasProblem = false;
                String problemContent = getString(R.string.alert_content_msg_roulette_has_problem);

                int switch100PositiveCount = 0;
                int switch0PositiveCount = 0;
                RouletteItemListInfo rouletteItemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                int rouletteItemCount = rouletteItemListAdapter.getItemCount();
                //??????????????????????????????????????????????????????
                ArrayList<Float> itemProbabilities = new ArrayList<Float>();
                //?????????????????????ONOFF????????????????????????????????????????????????boolean ?????? Integer????????????
                ArrayList<Integer> OnOffOfSwitch100 = new ArrayList<Integer>();
                //??????????????????????????????ONOFF????????????????????????????????????????????????boolean ?????? Integer????????????
                ArrayList<Integer> OnOffOfSwitch0 = new ArrayList<Integer>();

                //???????????????????????????????????????????????????????????????
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                for (int i = 0; i < rouletteItemCount; i++) {
                    //switch100, switch0 ???????????????ONOFF???????????????
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
                    problemContent = getString(R.string.alert_content_msg_both_switch_are_true);
                }

                if (switch0PositiveCount == rouletteItemCount) {
                    hasProblem = true;
                    problemContent = getString(R.string.alert_content_msg_all_switch0_are_true);
                }

                ArrayList<Integer> itemRatioArrayList = rouletteItemDataSet.getItemRatios();
                for (int i=0; i < itemRatioArrayList.size(); i++) {
                    int ratio = itemRatioArrayList.get(i);
                    if (ratio < 1 || ratio > 99) {
                        hasProblem = true;
                        problemContent = getString(R.string.alert_content_msg_ratio_has_problem);
                        break;
                    }
                }

                if (switch100PositiveCount >= 1) {
                    for (int i = 0; i < rouletteItemCount; i++) {
                        //switch100???ON????????????
                        if (rouletteItemDataSet.getOnOffInfoOfSwitch100().get(i)) {
                            //????????????????????????????????????????????????
                            itemProbabilities.add(1f / switch100PositiveCount);
                        } else {
                            //ON????????????????????????????????????????????????
                            itemProbabilities.add(0f);
                        }
                    }
                }

                if (switch0PositiveCount >= 1) {
                    //switch100, switch0 ???ONOFF???????????????????????????????????????????????????????????????
                    for (int i = 0; i < rouletteItemCount; i++) {
                        //switch0???ON????????????
                        if (rouletteItemDataSet.getOnOffInfoOfSwitch0().get(i)) {
                            //???????????????????????????????????????
                            itemProbabilities.add(0f);
                        } else {
                            //ON????????????????????????????????????????????????????????????
                            itemProbabilities.add(1f / (rouletteItemCount - switch0PositiveCount));
                        }
                    }
                }

                //ArrayList<Float>???intent????????????????????????????????????????????????????????????
                float itemProbabilityArray[] = new float[itemProbabilities.size()];
                for (int i = 0; i < itemProbabilities.size(); i++) {
                    itemProbabilityArray[i] = itemProbabilities.get(i);
                }

                Intent fromRouletteCreateIntent = new Intent();//????????????????????????????????????????????????????????????????????????????????????

                if (hasProblem) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RouletteCreateActivity.this);
                    builder.setTitle(getString(R.string.alert_content_msg_roulette_has_problem))
                            .setMessage(problemContent)
                            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    // ??????????????????????????????????????????????????????
                                }
                            })
                            .create()
                            .show();
                } else {

                    if (!isTutorialState) {
                        SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(RouletteCreateActivity.this);
                        if (!(defaultPref.getBoolean(getString(R.string.saved_auto_save_created_roulette_key), false))) {
                            // ???????????????????????????????????????????????????????????????????????????
                            AlertDialog.Builder builder = new AlertDialog.Builder(RouletteCreateActivity.this);
                            builder.setMessage(getString(R.string.confirm_msg_save_to_my_roulette))
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            int myRouletteItemCount = MainActivity.mMyRouletteViewModel.getAllMyRoulette().getValue().size();
                                            if (myRouletteItemCount < 50) {
                                                //????????????????????????????????????
                                                MyRoulette myRoulette = new MyRoulette(rouletteName.getText().toString(), NowDate.getNowDate(),
                                                        rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                                                        rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                                                //?????????????????????insert?????????????????????primaryKey???autoGenerate?????????
                                                MainActivity.mMyRouletteViewModel.insert(myRoulette);
                                            } else {
                                                Toast.makeText(RouletteCreateActivity.this, getString(R.string.notificatoin_myRoulettes_are_max), Toast.LENGTH_SHORT).show();
                                            }

                                            fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ISTUTORIALCONTINUE, isTutorialState);
                                            fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ROULETTE_NAME, rouletteName.getText().toString());
                                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_COLORS, rouletteItemDataSet.getColors());
                                            fromRouletteCreateIntent.putStringArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_TEXT_STRINGS, rouletteItemDataSet.getItemNames());
                                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_RATIOS, rouletteItemDataSet.getItemRatios());
                                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH100_INFO, OnOffOfSwitch100);
                                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH0_INFO, OnOffOfSwitch0);
                                            fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_PROBABILITIES, itemProbabilityArray);

                                            setResult(RESULT_OK, fromRouletteCreateIntent);

                                            RouletteCreateActivity.this.finish();
                                        }
                                    })
                                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ISTUTORIALCONTINUE, isTutorialState);
                                            fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ROULETTE_NAME, rouletteName.getText().toString());
                                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_COLORS, rouletteItemDataSet.getColors());
                                            fromRouletteCreateIntent.putStringArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_TEXT_STRINGS, rouletteItemDataSet.getItemNames());
                                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_RATIOS, rouletteItemDataSet.getItemRatios());
                                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH100_INFO, OnOffOfSwitch100);
                                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH0_INFO, OnOffOfSwitch0);
                                            fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_PROBABILITIES, itemProbabilityArray);

                                            setResult(RESULT_OK, fromRouletteCreateIntent);

                                            RouletteCreateActivity.this.finish();
                                        }
                                    })
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            // ??????????????????????????????????????????????????????
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            //???????????????????????????????????????

                            int myRouletteItemCount = MainActivity.mMyRouletteViewModel.getAllMyRoulette().getValue().size();
                            if (myRouletteItemCount < 50) {
                                //????????????????????????????????????
                                MyRoulette myRoulette = new MyRoulette(rouletteName.getText().toString(), NowDate.getNowDate(),
                                        rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                                        rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                                //?????????????????????insert?????????????????????primaryKey???autoGenerate?????????
                                MainActivity.mMyRouletteViewModel.insert(myRoulette);
                            } else {
                                Toast.makeText(RouletteCreateActivity.this, getString(R.string.notificatoin_myRoulettes_are_max), Toast.LENGTH_SHORT).show();
                            }

                            fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ISTUTORIALCONTINUE, isTutorialState);
                            fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ROULETTE_NAME, rouletteName.getText().toString());
                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_COLORS, rouletteItemDataSet.getColors());
                            fromRouletteCreateIntent.putStringArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_TEXT_STRINGS, rouletteItemDataSet.getItemNames());
                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_RATIOS, rouletteItemDataSet.getItemRatios());
                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH100_INFO, OnOffOfSwitch100);
                            fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH0_INFO, OnOffOfSwitch0);
                            fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_PROBABILITIES, itemProbabilityArray);

                            setResult(RESULT_OK, fromRouletteCreateIntent);

                            RouletteCreateActivity.this.finish();
                        }

                    } else {
                        fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ISTUTORIALCONTINUE, isTutorialState);
                        fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ROULETTE_NAME, rouletteName.getText().toString());
                        fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_COLORS, rouletteItemDataSet.getColors());
                        fromRouletteCreateIntent.putStringArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_TEXT_STRINGS, rouletteItemDataSet.getItemNames());
                        fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_RATIOS, rouletteItemDataSet.getItemRatios());
                        fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH100_INFO, OnOffOfSwitch100);
                        fromRouletteCreateIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH0_INFO, OnOffOfSwitch0);
                        fromRouletteCreateIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_PROBABILITIES, itemProbabilityArray);

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

        Log.d("RouletteCreateActivity", "onWindowFocusChanged");

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
                .text(getString(R.string.tutorial_msg_add_roulette_item_button));

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
                .text(getString(R.string.tutorial_msg_cheat_button));

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
                .text(getString(R.string.tutorial_msg_roulette_name));

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
                .text(getString(R.string.tutorial_msg_color_picker));

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
                .text(getString(R.string.tutorial_msg_roulette_item_name));

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
                .text(getString(R.string.tutorial_msg_item_ratio));

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
                .text(getString(R.string.tutorial_msg_delete_item_button));

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
                .text(getString(R.string.tutorial_msg_add_roulette_item_button));

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
                .text(getString(R.string.tutorial_msg_cheat_button));

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
                .text(getString(R.string.tutorial_msg_role_of_cheat_switches));

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
                .text(getString(R.string.tutorial_msg_hide_cheat_switches));

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
                .text(getString(R.string.tutorial_msg_finish_roulette_create_tutorial));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(createFinishButton)
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

    //????????????????????????????????????????????????????????????????????????
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup rouletteCreateLayout = findViewById(R.id.roulette_create_layout);
        InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //????????????????????????
        inputMethodManager.hideSoftInputFromWindow(rouletteCreateLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //?????????????????????????????????
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
        //?????????????????????????????????
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        switch (item.getItemId()) {
            case R.id.tutorial:

                AlertDialog.Builder builder = new AlertDialog.Builder(RouletteCreateActivity.this);
                builder.setTitle(getString(R.string.tutorial_msg_start_tutorial));
                builder.setMessage(R.string.tutorial_msg_alert_of_start_roulette_create_tutorial)
                        .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
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

                                //?????????????????????????????????
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
                                                .setContentText(getString(R.string.tutorial_msg_role_of_roulette_create))
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
                        .setNegativeButton(R.string.alert_dialog_negative_choice, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                // ??????????????????????????????????????????????????????
                            }
                        })
                        .create()
                        .show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //?????????????????????????????????????????????????????????
    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return super.onSupportNavigateUp();
    }

    public void onClickColorButton(View colorButton) {
        //ColorPickDialog???????????????
        ColorPickDialogFragment colorPickDialogFragment = new ColorPickDialogFragment((ColorButton)colorButton, rouletteItemList);
        colorPickDialogFragment.show(getSupportFragmentManager(), getString(R.string.tag_color_pick_dialog_fragment));
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
                //??????switch0?????????ON???????????????????????????????????????????????????OFF???????????????flag = 1
                if (!itemDataSet.getOnOffInfoOfSwitch0().get(i)) {
                    containSwitch0False = true;
                }
            }
            //flag = 0 ???????????????switch0???ON??????????????????????????????ON????????????switch0???OFF????????????switch0?????????ON???????????????????????????????????????
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
}