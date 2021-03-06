package com.kasai.cheatroulette.activity.mainActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.kasai.cheatroulette.R;
import com.kasai.cheatroulette.activity.EditMyRouletteActivity;
import com.kasai.cheatroulette.activity.detailSettingActivity.DetailSettingsActivity;
import com.kasai.cheatroulette.activity.EditRouletteActivity;
import com.kasai.cheatroulette.activity.MyRouletteActivity;
import com.kasai.cheatroulette.activity.RouletteCreateActivity;
import com.kasai.cheatroulette.dialogFragment.RatingDialogFragment;
import com.kasai.cheatroulette.recyclerView.MyRouletteListAdapter;
import com.kasai.cheatroulette.room.MyRouletteViewModel;
import com.kasai.cheatroulette.ui.PushImageButton;
import com.kasai.cheatroulette.ui.RouletteView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.listener.DismissListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

public class MainActivity extends AppCompatActivity {

    private RotateAnimation rotate;
    private RouletteView rouletteViewInLayout;
    private PushImageButton rouletteStartButton;
    private TextView resultTextView;
    private ImageButton plusButton;
    private TextView splitItemCountTextView;
    private ImageButton minusButton;
    private LinearLayout splitButtonLayout;
    private ConstraintLayout constraintLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FABsMenu fabsMenu;
    private TitleFAB rouletteCreateFab;
    private TitleFAB editRouletteFab;
    private TitleFAB myRouletteFab;

    private boolean rouletteExists = false;
    private boolean ratingDialogAlreadyInvoked = false;
    static final int RESULT_ROULETTECREATE = 1;
    static final int RESULT_MYROULETTE = 2;
    static final int RESULT_EDITROULETTE = 3;

    public static final String INTENT_VAL_NAME_TO_MAIN_ISTUTORIALCONTINUE = "isTutorialContinue";
    public static final String INTENT_VAL_NAME_TO_MAIN_ROULETTE_NAME = "rouletteName";
    public static final String INTENT_VAL_NAME_TO_MAIN_COLORS = "colors";
    public static final String INTENT_VAL_NAME_TO_MAIN_TEXT_STRINGS = "textStrings";
    public static final String INTENT_VAL_NAME_TO_MAIN_ITEM_RATIOS = "itemRatios";
    public static final String INTENT_VAL_NAME_TO_MAIN_SWITCH0_INFO = "OnOffInfoOfSwitch0";
    public static final String INTENT_VAL_NAME_TO_MAIN_SWITCH100_INFO = "OnOffInfoOfSwitch100";
    public static final String INTENT_VAL_NAME_TO_MAIN_ITEM_PROBABILITIES = "itemProbability";

    private static final Random RANDOM = new Random();
    private float degree = 0;
    private float sectorDegree = 0;
    private float toDegree = 5400f;
    private long duration = 10000;
    private float interpolatorFactor = 2.3f;

    public static MyRouletteViewModel mMyRouletteViewModel;
    public static final MyRouletteListAdapter adapter = new MyRouletteListAdapter(new MyRouletteListAdapter.MyRouletteDiff());

    static Toast mToast= null;
    public static boolean CheatFlag = true;

    private SoundPool soundPool;
    private int drumRollStart, drumRollLoop, finishSound;
    private int drumRollLoopStreamID;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("MainActivity", "onPostCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("MainActivity", "onWindowFocusChanged");

        //情報保存用の共有環境設定ファイル
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean dialogNeverInvoke = sharedPref.getBoolean(getString(R.string.saved_dialog_never_invoke_key), false);

        if (!dialogNeverInvoke && !ratingDialogAlreadyInvoked) {
            int countOfAppOpened = sharedPref.getInt(getString(R.string.saved_count_of_app_open_key), 0);
            if (countOfAppOpened >= 5) {
                startRating(false);
                SharedPreferences.Editor editor = sharedPref.edit();
                //評価画面がを出したら、アプリ開始回数を0に戻す
                editor.putInt(getString(R.string.saved_count_of_app_open_key), 0);
                //評価画面がすでに出たことを記録する
                ratingDialogAlreadyInvoked = true;

                editor.apply();
            }
        }

        RadioGroup themeRadioGroup = ((LinearLayout)navigationView.getMenu().findItem(R.id.nav_theme_bar).getActionView()).findViewById(R.id.change_theme_radio_group);
        RadioButton lightThemeRadioButton = themeRadioGroup.findViewById(R.id.light_theme);
        RadioButton darkThemeRadioButton = themeRadioGroup.findViewById(R.id.dark_theme);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                //ライトにチェック
                lightThemeRadioButton.setChecked(true);
                darkThemeRadioButton.setChecked(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                //ダークにチェック
                darkThemeRadioButton.setChecked(true);
                lightThemeRadioButton.setChecked(false);
                break;
        }

        boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_main_first_tutorial_done_key), false);
        if (!isFirstTutorialDone) {
            firstTutorial();
            //最初のチュートリアルが終わったら、そのことを保存しておく
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.saved_main_first_tutorial_done_key), true);
            editor.apply();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity", "onRestart");

        soundPool.autoResume();

        if (fabsMenu.isExpanded()) {
            fabsMenu.collapseImmediately();
        }

        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");

        soundPool.autoPause();

        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (rouletteExists) {
            //次回アプリ開始時のためにセットしてあるルーレット情報を保存
            saveRouletteInfoToSharedPrefs(editor);
        }
    }

    @Override
    protected void onDestroy() {
        //通知を削除
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        Log.d("MainActivity", "onDestroy");

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //情報保存用の共有環境設定ファイル
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        //テーマが手動で変更されたかの情報を取得
        boolean themeChangedManually = sharedPref.getBoolean(getString(R.string.saved_theme_changed_manually_key), false);
        //テーマを保存するかの情報を取得
        boolean themeSaveState = sharedPref.getBoolean(getString(R.string.saved_theme_save_state_key), false);

        if (!themeChangedManually) {
            if (themeSaveState) {
                //どのテーマかの情報を取得、適用
                boolean startThemeState = sharedPref.getBoolean(getString(R.string.saved_theme_state_key), false);
                if (startThemeState) {
                    //ダークテーマを適用
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    //ライトテーマを適用
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            } else {
                //システムに応じてテーマを適用
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        } else {
            int countOfAppOpened = sharedPref.getInt(getString(R.string.saved_count_of_app_open_key), 0);
            //手動のテーマ変更による再起動は、アプリを開始したとみなさない
            countOfAppOpened = countOfAppOpened - 1;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.saved_count_of_app_open_key), countOfAppOpened);
            //手動でのテーマ変更後は、keyをfalse（初期化）にし、保存する
            editor.putBoolean(getString(R.string.saved_theme_changed_manually_key), false);
            editor.apply();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate");

        MobileAds.initialize(this);

        mMyRouletteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(MyRouletteViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        // ここで、recyclerViewにDBのデータを表示する。無いと何も表示されない（recyclerViewがあり、DBも更新されているが、見た目のみ反映できない

        mMyRouletteViewModel.getAllMyRoulette().observe(this, words -> {
            adapter.submitList(words);
        });

        AudioAttributes audioAttributes = null;

        audioAttributes = new AudioAttributes.Builder()
                // USAGE_MEDIA
                // USAGE_GAME
                .setUsage(AudioAttributes.USAGE_GAME)
                // CONTENT_TYPE_MUSIC
                // CONTENT_TYPE_SPEECH, etc.
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();



        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(3)
                .build();

        // 予め使うサウンドをロードしておく
        drumRollStart = soundPool.load(this, R.raw.drumrool_start, 1);
        drumRollLoop = soundPool.load(this, R.raw.drumrool_roop, 1);
        finishSound = soundPool.load(this, R.raw.finishsound, 1);

        drawerLayout = findViewById(R.id.drawer_layout_main);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();

        //情報保存用の共有環境設定ファイル
        //サウンドの情報を取得、適用
        boolean startSoundState = sharedPref.getBoolean(getString(R.string.saved_sound_state_key), true);
        SwitchCompat soundSwitch = ((LinearLayout)navigationView.getMenu().findItem(R.id.nav_sound_option).getActionView()).findViewById(R.id.sound_switch_in_drawer_layout);
        soundSwitch.setChecked(startSoundState);

        //止まるまでの時間の情報を取得、適用
        RadioGroup timeRadioGroup = ((LinearLayout)navigationView.getMenu().findItem(R.id.nav_roulette_time_bar).getActionView()).findViewById(R.id.time_radio_group);
        boolean startTimeShortState = sharedPref.getBoolean(getString(R.string.saved_time_short_state_key), false);
        boolean startTimeNormalState = sharedPref.getBoolean(getString(R.string.saved_time_normal_state_key), true);
        boolean startTimeLongState = sharedPref.getBoolean(getString(R.string.saved_time_long_state_key), false);
        ((RadioButton)timeRadioGroup.findViewById(R.id.time_short)).setChecked(startTimeShortState);
        ((RadioButton)timeRadioGroup.findViewById(R.id.time_normal)).setChecked(startTimeNormalState);
        ((RadioButton)timeRadioGroup.findViewById(R.id.time_long)).setChecked(startTimeLongState);

        //高速抽選モード
        boolean highSpeedModeState = sharedPref.getBoolean(getString(R.string.saved_high_speed_mode_key), false);
        SwitchCompat highSpeedModeSwitch = ((LinearLayout)navigationView.getMenu().findItem(R.id.nav_high_speed_mode).getActionView()).findViewById(R.id.high_speed_mode_switch_in_drawer_layout);
        highSpeedModeSwitch.setChecked(highSpeedModeState);

        //テーマを保存するかの情報を取得、適用
        boolean startThemeSaveState = sharedPref.getBoolean(getString(R.string.saved_theme_save_state_key), false);
        SwitchCompat themeSaveSwitch = ((LinearLayout)navigationView.getMenu().findItem(R.id.nav_theme_save_option).getActionView()).findViewById(R.id.theme_save_switch_in_drawer_layout);
        themeSaveSwitch.setChecked(startThemeSaveState);

        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.toolbar_title_when_roulette_name_undefined);
        setSupportActionBar(toolbar);

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point displayAre = new Point();
        disp.getSize(displayAre);

        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.getViewById(R.id.rouletteStartButton).getLayoutParams().width = (int)(displayAre.x / 3.55f);
        constraintLayout.getViewById(R.id.rouletteStartButton).getLayoutParams().height = (int)(displayAre.x / 3.55f);

        splitButtonLayout = findViewById(R.id.split_button_layout);
        ViewGroup.LayoutParams layoutParams = splitButtonLayout.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        marginLayoutParams.setMargins(0, (int)(displayAre.x / 2.9f), 0, 20);
        splitButtonLayout.setLayoutParams(marginLayoutParams);
        setContentView(drawerLayout);

        rouletteStartButton = findViewById(R.id.rouletteStartButton);
        resultTextView = findViewById(R.id.resultTextView);

        plusButton = findViewById(R.id.plus_button);
        splitItemCountTextView = findViewById(R.id.split_item_count_text_view);
        minusButton = findViewById(R.id.minus_button);

        rouletteViewInLayout = findViewById(R.id.roulette);
        //ルーレットの状態保存があったかどうかの情報を共有環境設定から取得
        boolean savedRouletteExist = sharedPref.getBoolean(getString(R.string.saved_roulette_exist_key), false);
        if (savedRouletteExist) {
            SavedRouletteOfMainActivity savedRouletteOfMainActivity;
            Gson gson = new Gson();
            String savedRouletteString = sharedPref.getString(getString(R.string.saved_roulette_key), "");
            // 保存したオブジェクトを取得
            if (!TextUtils.isEmpty(savedRouletteString)) {
                // 保存した文字列から、保存されているルーレット情報を取得する
                savedRouletteOfMainActivity = gson.fromJson(savedRouletteString, SavedRouletteOfMainActivity.class);
                //得た情報を元にルーレットをセットする
                rouletteViewInLayout.setRouletteContents(
                        savedRouletteOfMainActivity.getSplitCount(),
                        savedRouletteOfMainActivity.getRouletteName(),
                        savedRouletteOfMainActivity.getColors(),
                        savedRouletteOfMainActivity.getItemNames(),
                        savedRouletteOfMainActivity.getItemRatios(),
                        savedRouletteOfMainActivity.getOnOffOfSwitch100(),
                        savedRouletteOfMainActivity.getOnOffOfSwitch0(),
                        savedRouletteOfMainActivity.getItemProbabilities()
                );
                setSplitItemCount();
            }
            //背景色を初期化する
            constraintLayout.setBackgroundColor(Color.parseColor(getResources().getString(R.color.appPrimaryColor)));

            if (TextUtils.isEmpty(rouletteViewInLayout.getRouletteName())) {
                toolbar.setTitle(R.string.toolbar_title_when_roulette_name_undefined);
            } else {
                toolbar.setTitle(rouletteViewInLayout.getRouletteName());
            }

            if (!(rouletteViewInLayout.getItemProbabilities().isEmpty())) {
                //保存されているチートフラッグを適用
                CheatFlag = sharedPref.getBoolean(getString(R.string.saved_cheat_flag_state_key), true);
            }
            rouletteExists = true;
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_detail_settings:
                        startActivity(new Intent(MainActivity.this, DetailSettingsActivity.class));
                        break;

/*
                    case R.id.nav_roulette_create:
                        Intent toRouletteCreateIntent = new Intent(getApplicationContext(), RouletteCreateActivity.class);
                        startActivityForResult(toRouletteCreateIntent, RESULT_ROULETTECREATE);
                        break;
                    case R.id.nav_edit_roulette:
                        if (rouletteExists) {
                            Intent rouletteEditIntent = new Intent(MainActivity.this, EditRouletteActivity.class);
                            rouletteEditIntent.putExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ROULETTE_NAME, rouletteViewInLayout.getRouletteName());
                            rouletteEditIntent.putIntegerArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_COLORS, rouletteViewInLayout.getColors());
                            rouletteEditIntent.putStringArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ITEM_NAMES, rouletteViewInLayout.getItemNames());
                            rouletteEditIntent.putIntegerArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ITEM_RATIOS, rouletteViewInLayout.getItemRatios());
                            rouletteEditIntent.putIntegerArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_SWITCH100_INFO, rouletteViewInLayout.getOnOffInfoOfSwitch100());
                            rouletteEditIntent.putIntegerArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_SWITCH0_INFO, rouletteViewInLayout.getOnOffInfoOfSwitch0());

                            startActivityForResult(rouletteEditIntent, RESULT_EDITROULETTE);
                        } else {
                            if (mToast != null) mToast.cancel();
                            mToast = Toast.makeText(getApplicationContext(), getString(R.string.msg_not_roulette_exists), Toast.LENGTH_SHORT);
                            mToast.show();
                        }
                        break;
                    case R.id.nav_myRoulette:
                        Intent toMyRouletteIntent = new Intent(getApplicationContext(), MyRouletteActivity.class);
                        startActivityForResult(toMyRouletteIntent, RESULT_MYROULETTE);
                        break;

 */
                    case R.id.nav_tutorial:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(R.string.confirm_msg_start_tutorial)
                                .setPositiveButton(R.string.alert_dialog_positive_choice, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        drawerLayout.closeDrawer(Gravity.RIGHT, false);
                                        MaterialShowcaseView.resetSingleUse(MainActivity.this, getString(R.string.main_tutorial_id));
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
                                        // ダイアログがキャンセルされた際の処理
                                    }
                                })
                                .create()
                                .show();
                        break;
                    case R.id.nav_review_app:
                        startRating(true);
                        break;
                    case R.id.nav_licenses:
                        Intent toLicensesIntent = new Intent(getApplicationContext(), OssLicensesMenuActivity.class);
                        startActivity(toLicensesIntent);
                        break;
                }
                return true;
            }
        });


        fabsMenu = findViewById(R.id.fabs_menu);
        fabsMenu.setMenuUpdateListener(new FABsMenuListener() {
            @Override
            public void onMenuClicked(FABsMenu fabsMenu) {
                super.onMenuClicked(fabsMenu);
            }

            @Override
            public void onMenuCollapsed(FABsMenu fabsMenu) {
                super.onMenuCollapsed(fabsMenu);
                fabsMenu.setMenuButtonIcon(R.drawable.ic_baseline_format_list_bulleted_24);
                rouletteCreateFab.setEnabled(false);
                editRouletteFab.setEnabled(false);
                myRouletteFab.setEnabled(false);
            }

            @Override
            public void onMenuExpanded(FABsMenu fabsMenu) {
                super.onMenuExpanded(fabsMenu);
                fabsMenu.setMenuButtonIcon(R.drawable.ic_baseline_close_24);
                rouletteCreateFab.setEnabled(true);
                editRouletteFab.setEnabled(true);
                myRouletteFab.setEnabled(true);
            }
        });


        rouletteCreateFab = findViewById(R.id.fab_roulette_create);
        rouletteCreateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRouletteCreateIntent = new Intent(getApplicationContext(), RouletteCreateActivity.class);
                startActivityForResult(toRouletteCreateIntent, RESULT_ROULETTECREATE);
            }
        });

        editRouletteFab = findViewById(R.id.fab_edit_roulette);
        editRouletteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rouletteExists) {
                    Intent rouletteEditIntent = new Intent(MainActivity.this, EditRouletteActivity.class);
                    rouletteEditIntent.putExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ROULETTE_NAME, rouletteViewInLayout.getRouletteName());
                    rouletteEditIntent.putIntegerArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_COLORS, rouletteViewInLayout.getColors());
                    rouletteEditIntent.putStringArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ITEM_NAMES, rouletteViewInLayout.getItemNames());
                    rouletteEditIntent.putIntegerArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_ITEM_RATIOS, rouletteViewInLayout.getItemRatios());
                    rouletteEditIntent.putIntegerArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_SWITCH100_INFO, rouletteViewInLayout.getOnOffInfoOfSwitch100());
                    rouletteEditIntent.putIntegerArrayListExtra(EditRouletteActivity.INTENT_VAL_NAME_TO_EDIT_ROULETTE_FROM_MAIN_SWITCH0_INFO, rouletteViewInLayout.getOnOffInfoOfSwitch0());

                    startActivityForResult(rouletteEditIntent, RESULT_EDITROULETTE);
                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), getString(R.string.msg_not_roulette_exists), Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

        myRouletteFab = findViewById(R.id.fab_myRoulette);
        myRouletteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMyRouletteIntent = new Intent(getApplicationContext(), MyRouletteActivity.class);
                startActivityForResult(toMyRouletteIntent, RESULT_MYROULETTE);
            }
        });

        rouletteStartButton.setOnClickListener(new View.OnClickListener() {
            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(View view) {
                if (rouletteExists) {

                    sectorDegree = 360f / (rouletteViewInLayout.sumOfItemRatio * rouletteViewInLayout.getSplitCount());

                    if (rouletteViewInLayout.getItemProbabilities().isEmpty() || !CheatFlag) {
                        //普通の抽選
                        Log.d("aaaaaaaaaaaaaaaaaa", "通常");
                        degree = 360f - (RANDOM.nextFloat() * 360f);
                    } else { //イカサマ抽選
                        Log.d("aaaaaaaaaaaaaaaaaa", "イカサマ");
                        //degree = 0;
                        float randomNum = RANDOM.nextFloat();//0.0 ~ 0.9999999
                        //0なら絶対に当たらないので、ありえない数値を代入
                        if (randomNum == 0) { randomNum = 42F; }
                        float sumOfProbability = rouletteViewInLayout.getItemProbabilities().get(0);
                        float sumOfDegree = sectorDegree * rouletteViewInLayout.getItemRatios().get(0);

                        if (randomNum <= sumOfProbability) {
                            //0度 ~ 最初の項目の度数未満の乱数生成（回転）
                            degree = RANDOM.nextFloat() * sumOfDegree;
                        } else {
                            for (int i = 1; i < rouletteViewInLayout.getColors().size(); i++) {
                                if (sumOfProbability < randomNum &&
                                        randomNum <= (sumOfProbability + rouletteViewInLayout.getItemProbabilities().get(i))) {
                                    //前回までの項目の後ろの境界 ~ 今回の項目の度数未満の乱数生成（回転）
                                    degree = sumOfDegree +
                                            RANDOM.nextFloat() * (sectorDegree * rouletteViewInLayout.getItemRatios().get(i));

                                    break;
                                }
                                sumOfProbability += rouletteViewInLayout.getItemProbabilities().get(i);
                                sumOfDegree += sectorDegree * rouletteViewInLayout.getItemRatios().get(i);
                            }
                        }
                    }

                    //詳細設定の共有環境設定ファイル
                    SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    //情報保存用の共有環境設定ファイル
                    SharedPreferences pref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                    //止まるまでの時間の情報を取得、適用
                    boolean highSpeedModeState = pref.getBoolean(getString(R.string.saved_high_speed_mode_key), false);
                    boolean startTimeShortState = pref.getBoolean(getString(R.string.saved_time_short_state_key), false);
                    boolean startTimeNormalState = pref.getBoolean(getString(R.string.saved_time_normal_state_key), true);
                    boolean startTimeLongState = pref.getBoolean(getString(R.string.saved_time_long_state_key), false);

                    if (highSpeedModeState) {
                        toDegree = 2160;
                        duration = 400;
                        interpolatorFactor = 1f;
                    } else {
                        if (startTimeNormalState) {
                            toDegree = 5400f;
                            duration = 8000;
                            interpolatorFactor = 2.4f;
                        } else if (startTimeShortState) {
                            toDegree = 1440f;
                            duration = 4000;
                            interpolatorFactor = 2.3f;
                        } else if (startTimeLongState) {
                            toDegree = 14400f;
                            duration = 15000;
                            interpolatorFactor = 2.0f;
                        } else {
                            //例外があった場合、デフォルトで抽選時間 普通の値を設定
                            toDegree = 5400f;
                            duration = 8000;
                            interpolatorFactor = 2.4f;
                        }
                    }

                    boolean soundState = pref.getBoolean(getString(R.string.saved_sound_state_key), true);

                    rotate = new RotateAnimation(0, (360f - degree) + toDegree, rouletteViewInLayout.xc, rouletteViewInLayout.yc);
                    rotate.setDuration(duration);       // アニメーションにかける時間(ミリ秒)
                    rotate.setFillAfter(true);          // アニメーション表示後の状態を保持
                    rotate.setInterpolator(new DecelerateInterpolator(interpolatorFactor)); //勢い良く回り、だんだんゆっくりになって止まるように

                    rotate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            //背景色、resultTextViewをそれぞれ初期化する
                            changeBackgroundColorWithAnimation(Color.parseColor(getResources().getString(R.color.appPrimaryColor)));

                            resultTextView.setText("");

                            //抽選中は各種ボタンを無効化する
                            rouletteStartButton.setEnabled(false);
                            plusButton.setEnabled(false);
                            minusButton.setEnabled(false);
                            fabsMenu.setEnabled(false);
                            toolbar.findViewById(R.id.menuButton).setEnabled(false);
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                            if (soundState) {
                                soundPool.play(drumRollStart, 1.0f, 1.0f, 1, 0, 1);
                                drumRollLoopStreamID = soundPool.play(drumRollLoop, 1.0f, 1.0f, 1, 5, 1);
                            }
                            resultTextView.setText("");
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (soundState) {
                                soundPool.stop(drumRollLoopStreamID);
                                soundPool.play(finishSound, 1.0f, 1.0f, 1, 0, 1);
                            }
                            //ルーレットが止まった位置の項目名と色をそれぞれ設定する
                            int resultBackgroundColor = rouletteViewInLayout.getColors().get(getSector(degree, rouletteViewInLayout));
                            constraintLayout.setBackgroundColor(resultBackgroundColor);
                            String resultText = rouletteViewInLayout.getItemNames().get(getSector(degree, rouletteViewInLayout));
                            if (rouletteViewInLayout.isColorDark(resultBackgroundColor)) {
                                resultTextView.setTextColor(Color.WHITE);
                            } else {
                                resultTextView.setTextColor(Color.BLACK);
                            }
                            resultTextView.setText(resultText);
                            //抽選終了後に各種ボタンを有効化する
                            rouletteStartButton.setEnabled(true);
                            plusButton.setEnabled(true);
                            minusButton.setEnabled(true);
                            fabsMenu.setEnabled(true);
                            toolbar.findViewById(R.id.menuButton).setEnabled(true);
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    rouletteViewInLayout.startAnimation(rotate);    // アニメーション開始


                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), getString(R.string.msg_not_roulette_exists), Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag = 0;
                if (!rouletteExists) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), getString(R.string.msg_not_roulette_exists), Toast.LENGTH_SHORT);
                    mToast.show();
                } else if (rouletteViewInLayout.getColors().size() * (rouletteViewInLayout.getSplitCount() + 1) > 300){
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), R.string.notify_cannot_split, Toast.LENGTH_SHORT);
                    mToast.show();
                } else {
                    //背景色、resultTextViewをそれぞれ初期化する
                    changeBackgroundColorWithAnimation(Color.parseColor(getResources().getString(R.color.appPrimaryColor)));
                    resultTextView.setText("");
                    rouletteViewInLayout.setSplitCount(rouletteViewInLayout.getSplitCount() + 1);
                    setSplitItemCount();
                    rouletteViewInLayout.invalidate();

                    if (rotate != null) {
                        //ルーレットを変更した場合は,角度を初期値に戻す
                        rotate.cancel();
                    }
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rouletteExists) {
                    //背景色、resultTextViewをそれぞれ初期化する
                    if (rouletteViewInLayout.getSplitCount() >= 2) {
                        rouletteViewInLayout.setSplitCount(rouletteViewInLayout.getSplitCount() - 1);
                        setSplitItemCount();
                        rouletteViewInLayout.invalidate();

                        if (rotate != null) {
                            changeBackgroundColorWithAnimation(Color.parseColor(getResources().getString(R.color.appPrimaryColor)));
                            resultTextView.setText("");
                            //ルーレットを変更した場合は,角度を初期値に戻す
                            rotate.cancel();
                        }

                    }
                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), getString(R.string.msg_not_roulette_exists), Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

        boolean dialogNeverInvoke = sharedPref.getBoolean(getString(R.string.saved_dialog_never_invoke_key), false);
        if (!dialogNeverInvoke) {
            int countOfAppOpened = sharedPref.getInt(getString(R.string.saved_count_of_app_open_key), 0);

            //アプリが開かれた回数を増やして記録
            countOfAppOpened = countOfAppOpened + 1;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.saved_count_of_app_open_key), countOfAppOpened);

            editor.apply();
        }
    }

    private void setSplitItemCount() {
        int splitItemCount = rouletteViewInLayout.getSplitCount() * rouletteViewInLayout.getItemCount();
        splitItemCountTextView.setText(String.valueOf(splitItemCount));
    }

    private void firstTutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.main_tutorial_id));

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.main_first_tutorial_id));


        ShowcaseTooltip drawerMenuToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text(getString(R.string.tutorial_msg_main_menu_button));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(toolbar.findViewById(R.id.menuButton))
                        .setToolTip(drawerMenuToolTip)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        .setShapePadding(30)
                        .build()
        );

        ShowcaseTooltip fabsMenuToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text(getString(R.string.tutorial_msg_main_activity_fab));

        FABsMenu faBsMenu = findViewById(R.id.fabs_menu);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(faBsMenu.getMenuButton())
                        .setToolTip(fabsMenuToolTip)
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        .build()
        );


        sequence.start();
    }

    private void tutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.main_tutorial_id));

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.main_tutorial_id));


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(constraintLayout)
                        .setContentText(getString(R.string.tutorial_msg_start_tutorial))
                        .setContentTextColor(getResources().getColor(R.color.showcase_text_color))
                        .setGravity(16)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .setDismissOnTouch(true)
                        .withoutShape()
                        .build()
        );

        ShowcaseTooltip drawerMenuToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text(getString(R.string.tutorial_msg_main_menu_button));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(toolbar.findViewById(R.id.menuButton))
                        .setToolTip(drawerMenuToolTip)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        .setShapePadding(30)
                        .build()
        );

        ShowcaseTooltip fabsMenuToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text(getString(R.string.tutorial_msg_main_activity_fab));

        FABsMenu faBsMenu = findViewById(R.id.fabs_menu);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(faBsMenu.getMenuButton())
                        .setToolTip(fabsMenuToolTip)
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        .build()
        );

        ShowcaseTooltip fabRouletteCreateToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text(getString(R.string.tutorial_msg_to_roulette_create_fab));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(faBsMenu.findViewById(R.id.fab_roulette_create))
                        .setToolTip(fabRouletteCreateToolTip)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        .setShapePadding(30)
                        .build()
        );

        ShowcaseTooltip fabEditRouletteToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text(getString(R.string.tutorial_msg_to_edit_roulette));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(faBsMenu.findViewById(R.id.fab_edit_roulette))
                        .setToolTip(fabEditRouletteToolTip)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        .setShapePadding(30)
                        .build()
        );

        ShowcaseTooltip fabMyRouletteToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text(getString(R.string.tutorial_msg_to_my_roulette));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(faBsMenu.findViewById(R.id.fab_myRoulette))
                        .setToolTip(fabMyRouletteToolTip)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        .setShapePadding(30)
                        .build()
        );
        sequence.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuButton:
                // ボタンをタップした際の処理を記述
                drawerLayout.openDrawer(Gravity.RIGHT);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        RouletteView rouletteView = findViewById(R.id.roulette);
        switch (requestCode) {
            case RESULT_ROULETTECREATE:
            case RESULT_MYROULETTE:
            case RESULT_EDITROULETTE:
                if (RESULT_OK == resultCode && intent != null) {
                    boolean isTutorialContinue = intent.getBooleanExtra(INTENT_VAL_NAME_TO_MAIN_ISTUTORIALCONTINUE, false);
                    String rouletteNameInfo = intent.getStringExtra(INTENT_VAL_NAME_TO_MAIN_ROULETTE_NAME);
                    ArrayList<Integer> colorsInfo = intent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_MAIN_COLORS);
                    ArrayList<String> textStringsInfo = intent.getStringArrayListExtra(INTENT_VAL_NAME_TO_MAIN_TEXT_STRINGS);
                    ArrayList<Integer> itemRatiosInfo = intent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_MAIN_ITEM_RATIOS);
                    ArrayList<Integer> OnOffOfSwitch100Info = intent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_MAIN_SWITCH100_INFO);
                    ArrayList<Integer> OnOffOfSwitch0Info = intent.getIntegerArrayListExtra(INTENT_VAL_NAME_TO_MAIN_SWITCH0_INFO);
                    ArrayList<Float> itemProbabilitiesInfo = new ArrayList<Float>();
                    float itemProbabilityArray[] = intent.getFloatArrayExtra(INTENT_VAL_NAME_TO_MAIN_ITEM_PROBABILITIES);
                    for (int i = 0; i < itemProbabilityArray.length; i++) {
                        itemProbabilitiesInfo.add(itemProbabilityArray[i]);
                    }
                    if (!itemProbabilitiesInfo.isEmpty()) {
                        //イカサマ設定のルーレットセット時は、イカサマモードONで初期化
                        CheatFlag = true;
                    }

                    rouletteViewInLayout.setRouletteContents(1,rouletteNameInfo, colorsInfo, textStringsInfo, itemRatiosInfo, OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
                    rouletteViewInLayout.invalidate();

                    //背景色、resultTextViewをそれぞれ初期化する
                    constraintLayout.setBackgroundColor(Color.parseColor(getResources().getString(R.color.appPrimaryColor)));
                    resultTextView.setText("");

                    if (rotate != null) {
                        //ルーレットを変更した場合は,角度を初期値に戻す
                        rotate.cancel();
                    }

                    if (TextUtils.isEmpty(rouletteNameInfo)) {
                        toolbar.setTitle(R.string.toolbar_title_when_roulette_name_undefined);
                    } else {
                        toolbar.setTitle(rouletteNameInfo);
                    }

                    //通知を削除
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(1);

                    rouletteExists = true;

                    if (isTutorialContinue) {
                        int pointerPosX = constraintLayout.getWidth() / 2;
                        int pointerPosY = (constraintLayout.getHeight()/ 2) - pointerPosX - (pointerPosX/8) + ((3 * toolbar.getHeight()) / 2);

                        new FancyShowCaseView.Builder(this)
                                .focusCircleAtPosition(pointerPosX, pointerPosY, constraintLayout.getWidth() / 12)
                                .dismissListener(new DismissListener() {
                                    @Override
                                    public void onDismiss(@Nullable String s) {

                                        MaterialShowcaseView.resetSingleUse(MainActivity.this, getString(R.string.roulette_create_latter_tutorial_id));

                                        ShowcaseConfig config = new ShowcaseConfig();
                                        config.setDelay(100);
                                        config.setRenderOverNavigationBar(true);

                                        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(MainActivity.this, getString(R.string.roulette_create_latter_tutorial_id));

                                        sequence.setConfig(config);

                                        sequence.singleUse(getString(R.string.roulette_create_latter_tutorial_id));

                                        ShowcaseTooltip splitLayoutToolTip = ShowcaseTooltip.build(MainActivity.this)
                                                .corner(30)
                                                .textColor(getResources().getColor(R.color.tooltip_text_color))
                                                .color(getResources().getColor(R.color.appPrimaryColor))
                                                .text(getString(R.string.tutorial_msg_split_button));

                                        sequence.addSequenceItem(
                                                new MaterialShowcaseView.Builder(MainActivity.this)
                                                        .setTarget(splitButtonLayout)
                                                        .setToolTip(splitLayoutToolTip)
                                                        .setDismissOnTouch(true)
                                                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                                                        .withRectangleShape()
                                                        .build()
                                        );

                                        ShowcaseTooltip rouletteStartToolTip = ShowcaseTooltip.build(MainActivity.this)
                                                .corner(30)
                                                .textColor(getResources().getColor(R.color.tooltip_text_color))
                                                .color(getResources().getColor(R.color.appPrimaryColor))
                                                .text(getString(R.string.tutorial_msg_roulette_start_button));

                                        sequence.addSequenceItem(
                                                new MaterialShowcaseView.Builder(MainActivity.this)
                                                        .setTarget(rouletteStartButton)
                                                        .setToolTip(rouletteStartToolTip)
                                                        .setDismissOnTouch(true)
                                                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                                                        .withCircleShape()
                                                        .setShapePadding(30)
                                                        .build()
                                        );

                                        sequence.start();
                                    }

                                    @Override
                                    public void onSkipped(@Nullable String s) {

                                    }

                                })
                                .title(getString(R.string.tutorial_msg_switch_cheating_area))
                                .titleGravity(16)
                                .titleSize(18, 1)
                                .build()
                                .show();
                    }
                }
                break;
        }
    }

    private void startRating(boolean isInvokedManually) {
        //評価画面を表示する
        RatingDialogFragment ratingDialogFragment = new RatingDialogFragment(isInvokedManually);
        ratingDialogFragment.show(getSupportFragmentManager(), getString(R.string.tag_rating_dialog_fragment));
    }

    public void onSoundSwitchClicked(View view) {
        //変更があった場合、その内容を保存する
        SwitchCompat soundSwitch = (SwitchCompat)view;
        boolean isChecked = soundSwitch.isChecked();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_sound_state_key), isChecked);
        editor.apply();
    }

    public void onTimeRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.time_short:
                if (checked) {
                    toDegree = 1440f;
                    duration = 4000;
                    interpolatorFactor = 2.3f;

                    //チェックがついたら、その情報を保存する
                    SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.saved_time_short_state_key), true);
                    editor.putBoolean(getString(R.string.saved_time_normal_state_key), false);
                    editor.putBoolean(getString(R.string.saved_time_long_state_key), false);
                    editor.apply();
                }
                break;

            case R.id.time_normal:
                if (checked) {
                    toDegree = 5400f;
                    duration = 8000;
                    interpolatorFactor = 2.4f;

                    //チェックがついたら、その情報を保存する
                    SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.saved_time_short_state_key), false);
                    editor.putBoolean(getString(R.string.saved_time_normal_state_key), true);
                    editor.putBoolean(getString(R.string.saved_time_long_state_key), false);
                    editor.apply();
                }
                break;

            case R.id.time_long:
                if (checked) {
                    toDegree = 14400f;
                    duration = 15000;
                    interpolatorFactor = 2.0f;

                    //チェックがついたら、その情報を保存する
                    SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.saved_time_short_state_key), false);
                    editor.putBoolean(getString(R.string.saved_time_normal_state_key), false);
                    editor.putBoolean(getString(R.string.saved_time_long_state_key), true);
                    editor.apply();
                }
                break;

            default:
                //例外が合った場合、デフォルトで抽選時間 普通の値を設定
                toDegree = 5400f;
                duration = 8000;
                interpolatorFactor = 2.4f;

                //チェックがついたら、その情報を保存する
                SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.saved_time_short_state_key), true);
                editor.putBoolean(getString(R.string.saved_time_normal_state_key), false);
                editor.putBoolean(getString(R.string.saved_time_long_state_key), false);
                editor.apply();

                break;
        }
    }

    public void onThemeRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.light_theme:
                if (checked) {
                    //チェックがついたら、その情報を保存する
                    SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    //false = ライトテーマ
                    editor.putBoolean(getString(R.string.saved_theme_state_key), false);
                    editor.putBoolean(getString(R.string.saved_theme_changed_manually_key), true);

                    editor.apply();

                    //ライトテーマにする
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;

            case R.id.dark_theme:
                if (checked) {
                    //チェックがついたら、その情報を保存する
                    SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    //true = ダークテーマ
                    editor.putBoolean(getString(R.string.saved_theme_state_key), true);
                    editor.putBoolean(getString(R.string.saved_theme_changed_manually_key), true);

                    editor.apply();

                    //ダークテーマにする
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                break;

            default:
                //システムに応じてテーマを適用
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    private void saveRouletteInfoToSharedPrefs(SharedPreferences.Editor editor) {
        SavedRouletteOfMainActivity savedRouletteOfMainActivity = new SavedRouletteOfMainActivity();
        savedRouletteOfMainActivity.setSavedRouletteContents(
                rouletteViewInLayout.getSplitCount(),
                rouletteViewInLayout.getRouletteName(),
                rouletteViewInLayout.getColors(),
                rouletteViewInLayout.getItemNames(),
                rouletteViewInLayout.getItemRatios(),
                rouletteViewInLayout.getOnOffInfoOfSwitch100(),
                rouletteViewInLayout.getOnOffInfoOfSwitch0(),
                rouletteViewInLayout.getItemProbabilities()
        );

        Gson gson = new Gson();
        //セットされているルーレットを共有環境設定に保存
        String savedRouletteString = gson.toJson(savedRouletteOfMainActivity);
        editor.putString(getString(R.string.saved_roulette_key), savedRouletteString);
        //ルーレットがセットされていたことを共有環境設定に保存
        editor.putBoolean(getString(R.string.saved_roulette_exist_key), true);

        if (!(rouletteViewInLayout.getItemProbabilities().isEmpty())) {
            //現在のチートフラッグを保存
            editor.putBoolean(getString(R.string.saved_cheat_flag_state_key), CheatFlag);
        }
        editor.apply();
    }

    public void onThemeSaveSwitchClicked(View view) {
        //変更があった場合、その内容を保存する
        SwitchCompat themeSaveSwitch = (SwitchCompat)view;
        boolean isChecked = themeSaveSwitch.isChecked();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_theme_save_state_key), isChecked);
        editor.apply();
    }

    public void onHighSpeedModeSwitchClicked(View view) {
        //変更があった場合、その内容を保存する
        SwitchCompat highSpeedModeSwitch = (SwitchCompat)view;
        boolean isChecked = highSpeedModeSwitch.isChecked();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_high_speed_mode_key), isChecked);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else if (fabsMenu.isExpanded()) {
            fabsMenu.collapse();
        } else {
            super.onBackPressed();
        }
    }

    private Integer getSector(float degrees, RouletteView roulette) {
        int i = 0;
        Integer position = null;
        float start = 0;
        float end = 0;

        if (degrees >= 360) {
            degrees = 0;
        }

        do {
            // start and end of each sector on the wheel
            start = end;
            end = start + (sectorDegree * roulette.getItemRatios().get(i % roulette.getColors().size()));//%の計算量でかいから注意

            if (degrees >= start && degrees < end) {
                //ルーレットの止まった項目のインデックスを決定
                position = i % roulette.getColors().size();
            }
            i++;

        } while (position == null && i < roulette.getColors().size() * roulette.getSplitCount());

        return position;
    }

    private void changeBackgroundColorWithAnimation(int toColor) {

        int colorFrom = ((ColorDrawable)constraintLayout.getBackground()).getColor();
        int colorTo = toColor;

        //背景色に変化がないなら何もしない
        if (!(colorFrom == colorTo)) {
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(200);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
                }

            });
            colorAnimation.start();
        }

    }
}