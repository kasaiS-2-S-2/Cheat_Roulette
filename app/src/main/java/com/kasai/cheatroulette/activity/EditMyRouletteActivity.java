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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kasai.cheatroulette.R;
import com.kasai.cheatroulette.activity.mainActivity.MainActivity;
import com.kasai.cheatroulette.common.MyApplication;
import com.kasai.cheatroulette.common.NowDate;
import com.kasai.cheatroulette.dialogFragment.ColorPickDialogFragment;
import com.kasai.cheatroulette.dialogFragment.RoulettePreviewDialogFragment;
import com.kasai.cheatroulette.recyclerView.EditMyRouletteAdapter;
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

public class EditMyRouletteActivity extends AppCompatActivity {

    private ConstraintLayout editMyRouletteLayout;
    private CheckBox checkBox;
    private EditText rouletteName;
    private Toolbar toolbar;
    private Button itemAddButton;
    private Button previewButton;
    private Button cheatButton;
    private Button editMyRouletteFinishButton;
    //???????????????????????????????????????????????????????????????
    private RouletteItemListInfo rouletteItemListInfo = new RouletteItemListInfo(
            new ArrayList<Integer>(), new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<Boolean>(), new ArrayList<Boolean>());
    private RecyclerView rouletteItemList;
    private EditMyRouletteAdapter rouletteItemListAdapter;

    public static boolean visibleFlag = false;

    private Toast mToast = null;

    public static final String INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_ROULETTE_ID = "rouletteId";
    public static final String INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_ROULETTE_NAME = "rouletteName";
    public static final String INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_COLORS = "colors";
    public static final String INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_ITEM_NAMES = "itemNames";
    public static final String INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_ITEM_RATIOS = "itemRatios";
    public static final String INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_SWITCH100_INFO = "OnOffInfoOfSwitch100";
    public static final String INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_SWITCH0_INFO = "OnOffInfoOfSwitch0";

    //?????????????????????????????????
    int rouletteIdFromMyRoulette;
    String rouletteNameFromMyRoulette;
    ArrayList<Integer> colorsFromMyRoulette;
    ArrayList<String> itemNamesFromMyRoulette;
    ArrayList<Integer> itemRatiosFromMyRoulette;
    ArrayList<Integer> Switch100InfoFromMyRoulette;
    ArrayList<Boolean> Switch100InfoFromMyRouletteBoolean;
    ArrayList<Integer> Switch0InfoFromMyRoulette;
    ArrayList<Boolean> Switch0InfoFromMyRouletteBoolean;

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

        MainActivity.mMyRouletteViewModel.getAllMyRoulette().observe(this, myRoulettes -> {
            MainActivity.adapter.submitList(myRoulettes);
        });

        visibleFlag = false;

        toolbar = findViewById(R.id.toolbar_roulette_create);
        toolbar.setTitle(R.string.edit_myRoulette);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editMyRouletteLayout = findViewById(R.id.roulette_create_layout);

        //?????????????????????????????????
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


        //recyclerView?????????????????????????????????????????????????????????????????????1/8???????????????
        rouletteItemList.setPadding(0, 0, 0, ScreenHeight/8);

        rouletteName = findViewById(R.id.rouletteName);

        Intent fromMyRouletteIntent = getIntent();

        rouletteIdFromMyRoulette = fromMyRouletteIntent.getIntExtra(INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_ROULETTE_ID, 0);
        rouletteNameFromMyRoulette = fromMyRouletteIntent.getStringExtra(INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_ROULETTE_NAME);
        colorsFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_COLORS);
        itemNamesFromMyRoulette = fromMyRouletteIntent.getStringArrayListExtra(INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_ITEM_NAMES);
        itemRatiosFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_ITEM_RATIOS);
        Switch100InfoFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_SWITCH100_INFO);
        Switch100InfoFromMyRouletteBoolean = new ArrayList<Boolean>();
        Switch0InfoFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_EDIT_MY_ROULETTE_SWITCH0_INFO);
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

        rouletteName.setText(rouletteNameFromMyRoulette);

        rouletteItemListInfo.setColors(colorsFromMyRoulette);
        rouletteItemListInfo.setItemNames(itemNamesFromMyRoulette);
        rouletteItemListInfo.setItemRatios(itemRatiosFromMyRoulette);
        rouletteItemListInfo.setOnOffInfoOfSwitch100(Switch100InfoFromMyRouletteBoolean);
        rouletteItemListInfo.setOnOffInfoOfSwitch0(Switch0InfoFromMyRouletteBoolean);

        rouletteItemListAdapter.notifyDataSetChanged();

        itemAddButton = findViewById(R.id.item_add_button);
        itemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rouletteItemListAdapter.getItemCount() >= 300) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(),  R.string.notice_item_is_max, Toast.LENGTH_SHORT);
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


        editMyRouletteFinishButton = findViewById(R.id.create_finish_button);
        editMyRouletteFinishButton.setOnClickListener(new View.OnClickListener() {
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditMyRouletteActivity.this);
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
                    MyRoulette myRoulette = new MyRoulette(rouletteName.getText().toString(), NowDate.getNowDate(),
                            rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                            rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                    myRoulette.setId(rouletteIdFromMyRoulette);
                    MainActivity.mMyRouletteViewModel.update(myRoulette);

                    finish();
                }
            }
        });

    }

    private void tutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.edit_myRoulette_tutorial_id));

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.edit_myRoulette_tutorial_id));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(editMyRouletteLayout)
                        .setContentText(getString(R.string.tutorial_msg_role_of_edit_my_roulette))
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
                .text(getString(R.string.tutorial_msg_edit_my_roulette_finish_create_fab));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(editMyRouletteFinishButton)
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
        SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(EditMyRouletteActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditMyRouletteActivity.this);
                builder.setTitle(getString(R.string.tutorial_msg_start_tutorial))
                        .setPositiveButton(getString(R.string.alert_dialog_positive_choice), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MaterialShowcaseView.resetSingleUse(EditMyRouletteActivity.this, getString(R.string.edit_myRoulette_tutorial_id));
                                tutorial();
                            }
                        })
                        .setNegativeButton(getString(R.string.alert_dialog_negative_choice), new DialogInterface.OnClickListener() {
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
            Boolean containSwitch0False = false;
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