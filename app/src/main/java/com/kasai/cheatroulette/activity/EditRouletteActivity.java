package com.kasai.cheatroulette.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
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
import com.kasai.cheatroulette.recyclerView.EditRouletteAdapter;
import com.kasai.cheatroulette.recyclerView.RouletteItemListAdapter;
import com.kasai.cheatroulette.recyclerView.RouletteItemListInfo;
import com.kasai.cheatroulette.room.MyRoulette;
import com.kasai.cheatroulette.ui.ColorButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

public class EditRouletteActivity extends AppCompatActivity {

    private ConstraintLayout editRouletteLayout;
    private EditText rouletteName;
    private Toolbar toolbar;
    private Button itemAddButton;
    private Button previewButton;
    private Button cheatButton;
    private Button editFinishButton;
    //???????????????????????????????????????????????????????????????
    private RouletteItemListInfo rouletteItemListInfo = new RouletteItemListInfo(
            new ArrayList<Integer>(), new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<Boolean>(), new ArrayList<Boolean>());
    private RecyclerView rouletteItemList;
    private EditRouletteAdapter rouletteItemListAdapter;

    public static boolean visibleFlag = false;

    private Toast mToast = null;

    public static final String INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ROULETTE_NAME = "editInfoOfRouletteName";
    public static final String INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_COLORS = "editInfoOfColors";
    public static final String INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ITEM_NAMES = "editInfoOfTextStrings";
    public static final String INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ITEM_RATIOS = "editInfoOfItemRatio";
    public static final String INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_SWITCH100_INFO = "editInfoOfSwitch100";
    public static final String INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_SWITCH0_INFO = "editInfoOfSwitch0";


    //?????????????????????????????????
    String rouletteNameFromMain;
    ArrayList<Integer> colorsFromMain;
    ArrayList<String>  itemNamesFromMain;
    ArrayList<Integer> itemRatiosFromMain;
    ArrayList<Integer> Switch100InfoFromMain;
    ArrayList<Boolean> Switch100InfoFromMainBoolean = new ArrayList<Boolean>();
    ArrayList<Integer> Switch0InfoFromMain;
    ArrayList<Boolean> Switch0InfoFromMainBoolean = new ArrayList<Boolean>();

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulettecreate);
        visibleFlag = false;

        //??????????????????
        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar = findViewById(R.id.toolbar_roulette_create);
        toolbar.setTitle(R.string.edit_roulette);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editRouletteLayout = findViewById(R.id.roulette_create_layout);

        rouletteItemList = findViewById(R.id.roulette_item_list);
        rouletteItemListAdapter = new EditRouletteAdapter(rouletteItemList,rouletteItemListInfo);
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

        Intent fromMainIntent = getIntent();
        //?????????????????????????????????????????????????????????
        rouletteNameFromMain = fromMainIntent.getStringExtra(INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ROULETTE_NAME);
        colorsFromMain = fromMainIntent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_COLORS);
        itemNamesFromMain = fromMainIntent.getStringArrayListExtra(INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ITEM_NAMES);
        itemRatiosFromMain = fromMainIntent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ITEM_RATIOS);
        Switch100InfoFromMain = fromMainIntent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_SWITCH100_INFO);
        Switch0InfoFromMain = fromMainIntent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_SWITCH0_INFO);

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

        rouletteName.setText(rouletteNameFromMain);

        rouletteItemListInfo.setColors(colorsFromMain);
        rouletteItemListInfo.setItemNames(itemNamesFromMain);
        rouletteItemListInfo.setItemRatios(itemRatiosFromMain);
        rouletteItemListInfo.setOnOffInfoOfSwitch100(Switch100InfoFromMainBoolean);
        rouletteItemListInfo.setOnOffInfoOfSwitch0(Switch0InfoFromMainBoolean);

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
            roulettePreviewDialogFragment.show(getSupportFragmentManager(), RouletteCreateActivity.TAG_ROULETTE_PREVIEW_DIALOG_FRAGMENT);
        });

        cheatButton = findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visibleFlag) {
                    //true????????????????????????????????????????????????
                    for (int i = 0; i < rouletteItemList.getChildCount(); i++) {
                        rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).setVisibility(View.GONE);
                    }
                    rouletteItemListAdapter.notifyDataSetChanged();

                    visibleFlag = false;
                    cheatButton.setText("");
                    cheatButton.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    // false????????????????????????????????????????????????
                    for (int i = 0; i < rouletteItemList.getChildCount(); i++) {
                        rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).setVisibility(View.VISIBLE);
                    }
                    rouletteItemListAdapter.notifyDataSetChanged();

                    visibleFlag = true;
                    cheatButton.setText(R.string.hide_cheat);
                    cheatButton.setBackgroundColor(Color.RED);
                }
                //?????????????????????????????????????????????????????????????????????
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        editFinishButton = findViewById(R.id.create_finish_button);
        editFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasProblem = false;
                String problemContent = getString(R.string.alert_content_msg_roulette_has_problem);

                int switch100PositiveCount = 0;
                int switch0PositiveCount = 0;
                RouletteItemListInfo rouletteItemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                int rouletteItemCount = rouletteItemDataSet.getColors().size();
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

                for (int i=0; i < rouletteItemCount; i++) {
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
                            itemProbabilities.add(1f/switch100PositiveCount);
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
                for (int i=0; i<itemProbabilities.size(); i++) {
                    itemProbabilityArray[i] = itemProbabilities.get(i);
                }

                if (hasProblem) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditRouletteActivity.this);
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
                    // ???????????????????????????????????????????????????????????????????????????
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditRouletteActivity.this);
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
                                        Toast.makeText(EditRouletteActivity.this, getString(R.string.notificatoin_myRoulettes_are_max), Toast.LENGTH_SHORT).show();
                                    }

                                    Intent fromEditRouletteIntent = new Intent();
                                    fromEditRouletteIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ROULETTE_NAME, rouletteName.getText().toString());
                                    fromEditRouletteIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_COLORS, rouletteItemDataSet.getColors());
                                    fromEditRouletteIntent.putStringArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_TEXT_STRINGS, rouletteItemDataSet.getItemNames());
                                    fromEditRouletteIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_RATIOS, rouletteItemDataSet.getItemRatios());
                                    fromEditRouletteIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH100_INFO, OnOffOfSwitch100);
                                    fromEditRouletteIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH0_INFO, OnOffOfSwitch0);
                                    fromEditRouletteIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_PROBABILITIES, itemProbabilityArray);

                                    setResult(RESULT_OK, fromEditRouletteIntent);

                                    EditRouletteActivity.this.finish();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent fromEditRouletteIntent = new Intent();
                                    fromEditRouletteIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ROULETTE_NAME, rouletteName.getText().toString());
                                    fromEditRouletteIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_COLORS, rouletteItemDataSet.getColors());
                                    fromEditRouletteIntent.putStringArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_TEXT_STRINGS, rouletteItemDataSet.getItemNames());
                                    fromEditRouletteIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_RATIOS, rouletteItemDataSet.getItemRatios());
                                    fromEditRouletteIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH100_INFO, OnOffOfSwitch100);
                                    fromEditRouletteIntent.putIntegerArrayListExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_SWITCH0_INFO, OnOffOfSwitch0);
                                    fromEditRouletteIntent.putExtra(MainActivity.INTENT_VAL_NAME_TO_MAIN_ITEM_PROBABILITIES, itemProbabilityArray);

                                    setResult(RESULT_OK, fromEditRouletteIntent);

                                    EditRouletteActivity.this.finish();
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
                }
            }
        });
    }

    private void tutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.edit_roulette_tutorial_id));

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.edit_roulette_tutorial_id));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(editRouletteLayout)
                        .setContentText(getString(R.string.tutorial_msg_role_of_edit_roulette))
                        .setContentTextColor(getResources().getColor(R.color.showcase_text_color))
                        .setGravity(16)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .setDismissOnTouch(true)
                        .withoutShape()
                        .build()
        );

        ShowcaseTooltip editFinishToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text(getString(R.string.utorial_msg_edit_roulette_finish_create_fab));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(editFinishButton)
                        .setToolTip(editFinishToolTip)
                        .setDismissOnTouch(true)
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
        SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(EditRouletteActivity.this);
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
                // ????????????????????????????????????????????????
                AlertDialog.Builder builder = new AlertDialog.Builder(EditRouletteActivity.this);
                builder.setTitle(getString(R.string.confirm_msg_start_tutorial))
                        .setPositiveButton(R.string.alert_dialog_positive_choice, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MaterialShowcaseView.resetSingleUse(EditRouletteActivity.this, getString(R.string.edit_roulette_tutorial_id));
                                tutorial();
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
            // The toggle is enabled
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