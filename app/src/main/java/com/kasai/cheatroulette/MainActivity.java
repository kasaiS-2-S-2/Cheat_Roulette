package com.kasai.cheatroulette;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;

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

//import androidx.annotation.RequiresApi;

public class MainActivity extends AppCompatActivity {

    //private RouletteView rouletteView;
    /*
    public com.e.myroulette1.RouletteView rouletteView = (RouletteView) findViewById(R.id.roulette);;
    private PushButton pushButton = (PushButton) findViewById(R.id.pushButton);
    private TextView resultTextView = findViewById(R.id.resultTextView);
    private Button resetButton = findViewById(R.id.reset_button);;
    private Button pulus = findViewById(R.id.pulus);
    private Button sendButton = findViewById(R.id.send_button);
    private Button checkButton = findViewById(R.id.check_button);
    private ConstraintLayout constraintLayout;
    private ArcAnimation animation;
     */

    private RotateAnimation rotate;
    private RouletteView rouletteViewInLayout;
    private PushImageButton rouletteStartButton;
    private TextView resultTextView;
    private Button resetButton;
    //private Button plusButton;
    //private Button minusButton;
    private ImageButton plusButton;
    private ImageButton minusButton;
    private Button createButton;
    private Button checkButton;
    private Button editButton;
    private Button toMyRouletteButton;
    private LinearLayout splitButtonLayout;
    private ConstraintLayout constraintLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBar actionBar;
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private ArcAnimation animation;
    private FABsMenu fabsMenu;
    private TitleFAB rouletteCreateFab;
    private TitleFAB editRouletteFab;
    private TitleFAB myRouletteFab;

    private boolean rouletteExists = false;
    private boolean isTutorialState = false;
    static final int RESULT_ROULETTECREATE = 1;
    static final int RESULT_MYROULETTE = 2;
    static final int RESULT_EDITROULETTE = 3;

    private int layoutWidth;
    private int layoutHeight;
    private static final Random RANDOM = new Random();
    private float degree = 0;
    private float degreeOld = 0;/////////////////////////////////////////////////////////////
    private float sectorDegree = 0;
    private float toDegree = 5400f;
    private long duration = 10000;
    private float interpolatorFactor = 2.3f;
    final String notRouletteExistsMessage = "ルーレットが作成されていません";

    public static MyRouletteViewModel mMyRouletteViewModel;
    public static final MyRouletteListAdapter adapter = new MyRouletteListAdapter(new MyRouletteListAdapter.MyRouletteDiff());

    static Toast mToast= null;
    static boolean CheatFlag = true;

    private SoundPool soundPool;
    private int drumRollStart, drumRollLoop, finishSound;
    private int drumRollLoopStreamID;

    private ReviewManager reviewManager;
    private ReviewInfo reviewInfo = null;

    //private float xc = 0.0f;
    //private float yc = 0.0f;


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("あああああああああああああああ", "onStart");

        //SwitchCompat soundSwitch = findViewById(R.id.sound_switch_in_drawer_layout);
        //SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        //boolean soundStateStart = sharedPref.getBoolean(getString(R.string.saved_sound_state_key), true);
        //Log.d("ああああああああああああSSSSSSSSSSｓ", String.valueOf(navigationView));
        //Log.d("ああああああああああああSSSSSSSSSSｓ", String.valueOf(navigationView.findViewById(R.id.nav_sound_option)));
        //Log.d("ああああああああああああSSSSSSSSSSｓ", String.valueOf(navigationView.findViewById(R.id.sound_switch_in_drawer_layout)));
        /*soundSwitch.setChecked(soundStateStart);
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.saved_sound_state_key), isChecked);
                editor.apply();
            }
        });

         */
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("あああああああああああああああ", "onPostCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("あああああああああああああああ", "onResume");
        //soundPool.stop(drumRollLoopStreamID);

    }

    //@RequiresApi(api = Build.VERSION_CODES.Q)
    public void onWindowFocusChanged(boolean hasFocus) {
        //public void onResume() {
        //super.onRestart();
        //super.onStart();
        //super.onResume();

        super.onWindowFocusChanged(hasFocus);

        Log.d("あああああああああああああああああ", "onWindowFocusChanged");

        //情報保存用の共有環境設定ファイル
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);

        /*
        //サウンドの情報を取得、適用
        boolean startSoundState = sharedPref.getBoolean(getString(R.string.saved_sound_state_key), true);
        SwitchCompat soundSwitch = navigationView.findViewById(R.id.nav_sound_option).findViewById(R.id.sound_switch_in_drawer_layout);
        soundSwitch.setChecked(startSoundState);

        //止まるまでの時間の情報を取得、適用
        RadioGroup timeRadioGroup = navigationView.findViewById(R.id.nav_roulette_time_bar).findViewById(R.id.time_radio_group);
        boolean startTimeShortState = sharedPref.getBoolean(getString(R.string.saved_time_short_state_key), false);
        boolean startTimeNormalState = sharedPref.getBoolean(getString(R.string.saved_time_normal_state_key), true);
        boolean startTimeLongState = sharedPref.getBoolean(getString(R.string.saved_time_long_state_key), false);
        ((RadioButton)timeRadioGroup.findViewById(R.id.time_short)).setChecked(startTimeShortState);
        ((RadioButton)timeRadioGroup.findViewById(R.id.time_normal)).setChecked(startTimeNormalState);
        ((RadioButton)timeRadioGroup.findViewById(R.id.time_long)).setChecked(startTimeLongState);
        // Check which radio button was clicked
        switch(timeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.time_short:
                toDegree = 1440f;
                duration = 5000;
                interpolatorFactor = 2.3f;
                break;
            case R.id.time_normal:
                toDegree = 5400f;
                duration = 10000;
                interpolatorFactor = 2.3f;
                break;
            case R.id.time_long:
                toDegree = 14400f;
                duration = 17000;
                interpolatorFactor = 2.0f;
                break;
            default:
                //例外があった場合、デフォルトで抽選時間 普通の値を設定
                toDegree = 5400f;
                duration = 10000;
                interpolatorFactor = 2.3f;
                break;
        }

        //どのテーマかの情報を取得、適用
        RadioGroup themeRadioGroup = navigationView.findViewById(R.id.nav_theme_bar).findViewById(R.id.change_theme_radio_group);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                //ライトにチェック
                ((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(true);
                ((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                //ダークにチェック
                ((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(false);
                ((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(true);
                break;
        }

        //テーマを保存するかの情報を取得、適用
        boolean startThemeSaveState = sharedPref.getBoolean(getString(R.string.saved_theme_save_state_key), false);
        SwitchCompat themeSaveSwitch = navigationView.findViewById(R.id.nav_theme_save_option).findViewById(R.id.theme_save_switch_in_drawer_layout);
        themeSaveSwitch.setChecked(startThemeSaveState);

        //rouletteView = new RouletteView(getApplicationContext());

        //xc = rouletteView.getWidth() / 2;
        //yc = rouletteView.getHeight() / 2;

        ////////////////////////////////////////////////////////////////
        //constraintLayout.addView(new PointerView(getApplicationContext(), rouletteView.xc, rouletteView.yc));
        //if (constraintLayout.getChildAt(constraintLayout.getChildCount() - 2) instanceof RouletteView) {
        //rouletteViewInLayout = (RouletteView) constraintLayout.getChildAt(constraintLayout.getChildCount() - 2);
        //rouletteViewInLayout = findViewById(R.id.roulette);
        //}
        //setContentView(constraintLayout);

        */


        //////////////////////////////////////////////////////////////////////////

        //どのテーマかの情報を取得、適用
        //RadioGroup themeRadioGroup = navigationView.findViewById(R.id.nav_theme_bar).findViewById(R.id.change_theme_radio_group);
        RadioGroup themeRadioGroup = ((LinearLayout)navigationView.getMenu().findItem(R.id.nav_theme_bar).getActionView()).findViewById(R.id.change_theme_radio_group);
        RadioButton lightThemeRadioButton = themeRadioGroup.findViewById(R.id.light_theme);
        RadioButton darkThemeRadioButton = themeRadioGroup.findViewById(R.id.dark_theme);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                //ライトにチェック
                //((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(true);
                //((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(false);
                lightThemeRadioButton.setChecked(true);
                darkThemeRadioButton.setChecked(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                //ダークにチェック
                //((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(false);
                //((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(true);
                darkThemeRadioButton.setChecked(true);
                lightThemeRadioButton.setChecked(false);
                break;
        }

        //SharedPreferences sharedPref = EditRouletteActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_main_first_tutorial_done_key), false);
        if (!isFirstTutorialDone) {
            //isTutorialState = true;
            isTutorialState = true;
            tutorial();
            //最初のチュートリアルが終わったら、そのことを保存しておく
            //SharedPreferences sharedPref = EditRouletteActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.saved_main_first_tutorial_done_key), true);
            editor.apply();
            //MaterialShowcaseView.resetSingleUse(this, getString(R.string.roulette_create_first_tutorial_id));//////////////////////////////////////////////////////
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("あああああああああああああああ", "onRestart");

        soundPool.autoResume();

        if (fabsMenu.isExpanded()) {
            fabsMenu.collapseImmediately();
        }

        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            //drawerLayout.closeDrawer(Gravity.RIGHT);
            drawerLayout.closeDrawer(Gravity.RIGHT, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("あああああああああああああああ", "onPause");

        //soundPool.stop(drumRollLoopStreamID);
        soundPool.autoPause();

        //SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (rouletteExists) {
            //boolean redisplayLastUseRoulette = defaultPref.getBoolean(getString(R.string.saved_redisplay_last_use_roulette_key), true);
            //boolean savedRouletteExist = sharedPref.getBoolean(getString(R.string.saved_roulette_exist_key), true);

            //if (redisplayLastUseRoulette) {
            //次回アプリ開始時のためにセットしてあるルーレット情報を保存
            saveRouletteInfoToSharedPrefs(editor);
            //ルーレットがセットされていなかったことを共有環境設定に保存
            //editor.putBoolean(getString(R.string.saved_roulette_exist_key), false);
            //editor.putString(getString(R.string.saved_roulette_key), "");
            //editor.apply();
            //次回アプリ開始時のためにセットしてあるルーレット情報を保存
            //saveRouletteInfoToSharedPrefs(editor);
            //} else if (!savedRouletteExist) {
            //ルーレットがセットされていなかったことを共有環境設定に保存
            //editor.putBoolean(getString(R.string.saved_roulette_exist_key), false);
            //editor.putString(getString(R.string.saved_roulette_key), "");
            //editor.apply();
            //次回アプリ開始時のためにセットしてあるルーレット情報を保存
            //saveRouletteInfoToSharedPrefs(editor);
            //ルーレットがセットされていなかったことを共有環境設定に保存
            //editor.putBoolean(getString(R.string.saved_roulette_exist_key), false);
            //editor.putString(getString(R.string.saved_roulette_key), "");
            //editor.apply();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("あああああああああああああああ", "onStop");
    }

    @Override
    protected void onDestroy() {
        //通知を削除
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        Log.d("あああああああああああああああ", "onDestroy");

        super.onDestroy();


        //} else {
            //ルーレットがセットされていなかったことを共有環境設定に保存
            //editor.putBoolean(getString(R.string.saved_roulette_exist_key), false);
            //editor.putString(getString(R.string.saved_roulette_key), "");
            //editor.apply();
        //}
    }

    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                    Log.d("ああああああLLLLLLLLL", "Mode_NIGHT_YES");
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
            SharedPreferences.Editor editor = sharedPref.edit();
            //手動でのテーマ変更後は、keyをfalse（初期化）にし、保存する
            editor.putBoolean(getString(R.string.saved_theme_changed_manually_key), false);
            editor.apply();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("あああああああああああああああ", "onCreate");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        //final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff());

        // Get a new or existing ViewModel from the ViewModelProvider.
        mMyRouletteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(MyRouletteViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        // ここで、recyclerViewにDBのデータを表示する。無いと何も表示されない（recyclerViewがあり、DBも更新されているが、見た目のみ反映できない

        //MyRouletteActivity myRouletteActivity = new MyRouletteActivity();
        mMyRouletteViewModel.getAllMyRoulette().observe(this, words -> {

            /*
            ラムダ式でなければ、
           mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(List<Word> words) {
                    adapter.submitList(words);
                }
           });
            的な感じ？ 参考（onClickListenerの無名クラスの形での実装コード, https://qiita.com/sano1202/items/64593e8e981e8d6439d3#3-%E3%83%A9%E3%83%A0%E3%83%80%E5%BC%8F%E3%81%AE%E4%BD%BF%E3%81%84%E6%96%B9, 1-2.無名クラス)
            無名クラスは、インターフェースをimplementの記述なしで実装できる！みたいな書き方ができる。ラムダ式はその無名クラスをさらに簡略化して書ける！
            wordsはObserverインターフェースのonChange abstractメソッドの引数
            */

            // Update the cached copy of the words in the adapter.
            //MyRouletteActivity myRouletteActivity = new MyRouletteActivity();
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


        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("debug","sampleId="+sampleId);
                Log.d("debug","status="+status);
            }
        });

        //relativeLayout = findViewById(R.id.re);
        drawerLayout = findViewById(R.id.drawer_layout_main);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();

        //情報保存用の共有環境設定ファイル
        //SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
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


        /*
        //どのテーマかの情報を取得、適用
        RadioGroup themeRadioGroup = ((LinearLayout)navigationView.getMenu().findItem(R.id.nav_theme_bar).getActionView()).findViewById(R.id.change_theme_radio_group);
        RadioButton lightThemeRadioButton = themeRadioGroup.findViewById(R.id.light_theme);
        RadioButton darkThemeRadioButton = themeRadioGroup.findViewById(R.id.dark_theme);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                //ライトにチェック
                //((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(true);
                //((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(false);
                Log.d("ああああああLLLLLLLLL", "Mode_NIGHT_YES1");
                lightThemeRadioButton.setChecked(true);
                darkThemeRadioButton.setChecked(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                //ダークにチェック
                Log.d("ああああああLLLLLLLLL", "Mode_NIGHT_YES2");
                //((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(false);
                //((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(true);
                darkThemeRadioButton.setChecked(true);
                //lightThemeRadioButton.setSelected(false);

                Log.d("ああああああLLLLLLLLL", "Mode_NIGHT_YES3");
                lightThemeRadioButton.setChecked(false);
                break;
        }

         */


        //テーマを保存するかの情報を取得、適用
        boolean startThemeSaveState = sharedPref.getBoolean(getString(R.string.saved_theme_save_state_key), false);
        SwitchCompat themeSaveSwitch = ((LinearLayout)navigationView.getMenu().findItem(R.id.nav_theme_save_option).getActionView()).findViewById(R.id.theme_save_switch_in_drawer_layout);
        themeSaveSwitch.setChecked(startThemeSaveState);

        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, );

        //relativeLayout = findViewById(R.id.relativeLayout_main);


        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("ルーレット");
        setSupportActionBar(toolbar);

        //navigationView.setNavigationItemSelectedListener(this);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawerLayout.addDrawerListener(toggle);
        //toggle.syncState();
        //setSupportActionBar(toolbar);
        //rouletteStartButton = findViewById(R.id.rouletteStartButton);

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
        //Matrix matrix = new Matrix();
        //matrix.setScale(displayAre.x * 2f / 9f, displayAre.x * 2f / 9f);
        //matrix.setScale(3, 3);
        //rouletteStartButton.setImageMatrix(matrix);

        //rouletteViewInLayout = new RouletteView(getApplicationContext());//初めはインスタンスの中身なし;
        //constraintLayout.addView(rouletteViewInLayout);
        //setContentView(constraintLayout);
        setContentView(drawerLayout);
        //setContentView(relativeLayout);


        rouletteStartButton = findViewById(R.id.rouletteStartButton);
        resultTextView = findViewById(R.id.resultTextView);
        //resetButton = findViewById(R.id.reset_button);
        plusButton = findViewById(R.id.plus_button);
        minusButton = findViewById(R.id.minus_button);
        //createButton = findViewById(R.id.send_button);
        //checkButton = findViewById(R.id.check_button);
        //editButton = findViewById(R.id.edit_button);
        //toMyRouletteButton = findViewById(R.id.myRoulette_button);
        //fabsMenu = findViewById(R.id.fabs_menu);

        rouletteViewInLayout = findViewById(R.id.roulette);
        //ルーレットの状態保存があったかどうかの情報を共有環境設定から取得
        boolean savedRouletteExist = sharedPref.getBoolean(getString(R.string.saved_roulette_exist_key), false);
        //SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //boolean redisplayLastUseRoulette = defaultPref.getBoolean(getString(R.string.saved_redisplay_last_use_roulette_key), true);
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
            }// else {
                // 何も保存されてない 初期時点 この時はデフォルト値を入れて上げる
                //savedRouletteOfMainActivity = SavedRouletteOfMainActivity.getDefaultInstance();
            //}
            //rouletteViewInLayout.invalidate();

            //SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            //if (!(defaultPref.getBoolean(getString(R.string.saved_redisplay_last_use_roulette_key), true))) {
                //SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putBoolean(getString(R.string.saved_roulette_exist_key), false);
                //editor.apply();
            //}

            //背景色を初期化する
            constraintLayout.setBackgroundColor(Color.parseColor(getResources().getString(R.color.appPrimaryColor)));

            if (TextUtils.isEmpty(rouletteViewInLayout.getRouletteName())) {
                toolbar.setTitle("ルーレット");
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

                    case R.id.nav_roulette_create:
                        Intent toRouletteCreateIntent = new Intent(getApplicationContext(), RouletteCreateActivity.class);
                        //Intent intent = new Intent(getApplication(), SubActivity.class);
                        startActivityForResult(toRouletteCreateIntent, RESULT_ROULETTECREATE);
                        break;
                    case R.id.nav_edit_roulette:
                        if (rouletteExists) {
                            Intent rouletteEditIntent = new Intent(MainActivity.this, EditRouletteActivity.class);
                            //Intent intent = new Intent(getApplication(), SubActivity.class);
                            rouletteEditIntent.putExtra("editInfoOfRouletteName", rouletteViewInLayout.getRouletteName());
                            rouletteEditIntent.putIntegerArrayListExtra("editInfoOfColors", rouletteViewInLayout.getColors());
                            rouletteEditIntent.putStringArrayListExtra("editInfoOfTextStrings", rouletteViewInLayout.getItemNames());
                            rouletteEditIntent.putIntegerArrayListExtra("editInfoOfItemRatio", rouletteViewInLayout.getItemRatios());
                            rouletteEditIntent.putIntegerArrayListExtra("editInfoOfSwitch100", rouletteViewInLayout.getOnOffInfoOfSwitch100());
                            rouletteEditIntent.putIntegerArrayListExtra("editInfoOfSwitch0", rouletteViewInLayout.getOnOffInfoOfSwitch0());

                            startActivityForResult(rouletteEditIntent, RESULT_EDITROULETTE);
                        } else {
                            if (mToast != null) mToast.cancel();
                            mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                            mToast.show();
                        }
                        break;
                    case R.id.nav_myRoulette:
                        Intent toMyRouletteIntent = new Intent(getApplicationContext(), MyRouletteActivity.class);
                        startActivityForResult(toMyRouletteIntent, RESULT_MYROULETTE);
                        //menu.collapseImmediately();
                        //startActivity(toMyRouletteIntent);
                        break;
                    case R.id.nav_review_app:
                        getReviewInfo();
                        startReviewFlow();
                        break;
                }
                return true;
            }
        });


        fabsMenu = findViewById(R.id.fabs_menu);
        Log.d("ああああああああああああああああああああ", String.valueOf(fabsMenu.isExpanded()));
        fabsMenu.setMenuUpdateListener(new FABsMenuListener() {
            // You don't need to override all methods. Just the ones you want.

            @Override
            public void onMenuClicked(FABsMenu fabsMenu) {
                Log.d("あああああああああああああああああ", "onMenuClicked");
                super.onMenuClicked(fabsMenu);
            }

            @Override
            public void onMenuCollapsed(FABsMenu fabsMenu) {
                Log.d("あああああああああああああああああ", "onMenuCollapsed");
                super.onMenuCollapsed(fabsMenu);
                rouletteCreateFab.setEnabled(false);
                editRouletteFab.setEnabled(false);
                myRouletteFab.setEnabled(false);
            }

            @Override
            public void onMenuExpanded(FABsMenu fabsMenu) {
                Log.d("あああああああああああああああああ", "onMenuExpanded");
                super.onMenuExpanded(fabsMenu);
                rouletteCreateFab.setEnabled(true);
                editRouletteFab.setEnabled(true);
                myRouletteFab.setEnabled(true);
            }
        });


        rouletteCreateFab = findViewById(R.id.fab_roulette_create);
        rouletteCreateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ああああああああああああああああ", "rouletteCreateFab");
                Intent toRouletteCreateIntent = new Intent(getApplicationContext(), RouletteCreateActivity.class);
                //Intent intent = new Intent(getApplication(), SubActivity.class);
                startActivityForResult(toRouletteCreateIntent, RESULT_ROULETTECREATE);
            }
        });
        //rouletteCreateFab.setEnabled(false);

        editRouletteFab = findViewById(R.id.fab_edit_roulette);
        editRouletteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rouletteExists) {
                    Log.d("ああああああああああああああああ", "editRouletteFab");
                    Intent rouletteEditIntent = new Intent(MainActivity.this, EditRouletteActivity.class);
                    //Intent intent = new Intent(getApplication(), SubActivity.class);
                    rouletteEditIntent.putExtra("editInfoOfRouletteName", rouletteViewInLayout.getRouletteName());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfColors", rouletteViewInLayout.getColors());
                    rouletteEditIntent.putStringArrayListExtra("editInfoOfTextStrings", rouletteViewInLayout.getItemNames());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfItemRatio", rouletteViewInLayout.getItemRatios());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfSwitch100", rouletteViewInLayout.getOnOffInfoOfSwitch100());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfSwitch0", rouletteViewInLayout.getOnOffInfoOfSwitch0());

                    startActivityForResult(rouletteEditIntent, RESULT_EDITROULETTE);
                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });
        //editButton.setEnabled(false);

        myRouletteFab = findViewById(R.id.fab_myRoulette);
        myRouletteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ああああああああああああああああ", "myRouletteFab");
                Intent toMyRouletteIntent = new Intent(getApplicationContext(), MyRouletteActivity.class);
                startActivityForResult(toMyRouletteIntent, RESULT_MYROULETTE);
                //menu.collapseImmediately();
                //startActivity(toMyRouletteIntent);
            }
        });
        //myRouletteFab.setEnabled(false);

        //rouletteStartButton = findViewById(R.id.rouletteStartButton);
        rouletteStartButton.setOnClickListener(new View.OnClickListener() {
            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(View view) {
                if (rouletteExists) {
                    //RouletteView roulette = (RouletteView) rouletteViewInLayout;
                    //degree = 0;
                    //float degree = 0;
                    //degreeOld = degree % 360;///////////////////////////////////////////////////////////////////
                    sectorDegree = 360f / (rouletteViewInLayout.sumOfItemRatio * rouletteViewInLayout.getSplitCount());

                    if (rouletteViewInLayout.getItemProbabilities().isEmpty() || !CheatFlag) {
                        //普通の抽選
                        Log.d("aaaaaaaaaaaaaaaaaa", "通常");
                        degree = 360f - (RANDOM.nextFloat() * 360f);
                        ///////////////////////////////////////////////////////////////////
                        //sectorDegree = 360f / (rouletteView.sumOfItemRatio * rouletteView.splitCount);
                    /*
                    RotateAnimation rotate = new RotateAnimation(0, degree + 1080, xc, yc);
                    rotate.setDuration(3600);       // アニメーションにかける時間(ミリ秒)
                    rotate.setFillAfter(true);          // アニメーション表示後の状態を保持
                    rotate.setInterpolator(new DecelerateInterpolator()); //勢い良く回り、だんだんゆっくりになって止まるように
                    rotate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // we empty the result text view when the animation start
                            resultTextView.setText("");
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // we display the correct sector pointed by the triangle at the end of the rotate animation
                            resultTextView.setText(getSector(360 - (degree % 360)));
                            //resultTv.setText(getSector(degree));
                            //Log.d("getsector", String.valueOf(360 - (degree % 720)));
                            Log.d("getsector", String.valueOf(degree));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                    rouletteView.startAnimation(rotate);    // アニメーション開始
                    */

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

                    //degreeOld = degree;

                    /*
                    animation = new ArcAnimation(rouletteViewInLayout);
                    animation.setDuration(3600);
                    animation.setInterpolator(new DecelerateInterpolator()); //勢い良く回り、だんだんゆっくりになって止まるように
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // we empty the result text view when the animation start
                            resultTextView.setText("");
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // we display the correct sector pointed by the triangle at the end of the rotate animation
                            //resultTextView.setText(getSector(360f - (degree % 360)));
                            resultTextView.setText(getSector(degree, rouletteViewInLayout));
                            //resultTv.setText(getSector(degree));
                            //Log.d("getsector", String.valueOf(360 - (degree % 720)));
                            Log.d("getsector", String.valueOf(degree));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    rouletteViewInLayout.startAnimation(animation);

                     */

                    //RotateAnimation rotate = new RotateAnimation(0, (360f - degree) + 1800f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);


                    //RotateAnimation rotate = new RotateAnimation(360f - degreeOld, (360f - degree) + 360f - degreeOld, rouletteViewInLayout.xc, rouletteViewInLayout.yc);

                    //情報保存用の共有環境設定ファイル
                    SharedPreferences pref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                    //止まるまでの時間の情報を取得、適用
                    //RadioGroup timeRadioGroup = navigationView.findViewById(R.id.nav_roulette_time_bar).findViewById(R.id.time_radio_group);
                    boolean startTimeShortState = pref.getBoolean(getString(R.string.saved_time_short_state_key), false);
                    boolean startTimeNormalState = pref.getBoolean(getString(R.string.saved_time_normal_state_key), true);
                    boolean startTimeLongState = pref.getBoolean(getString(R.string.saved_time_long_state_key), false);

                    if (startTimeNormalState) {
                        toDegree = 5400f;
                        duration = 10000;
                        interpolatorFactor = 2.3f;
                    } else if (startTimeShortState) {
                        toDegree = 1440f;
                        duration = 5000;
                        interpolatorFactor = 2.3f;
                    } else if (startTimeLongState) {
                        toDegree = 14400f;
                        duration = 17000;
                        interpolatorFactor = 2.0f;
                    } else {
                        //例外があった場合、デフォルトで抽選時間 普通の値を設定
                        toDegree = 5400f;
                        duration = 10000;
                        interpolatorFactor = 2.3f;
                    }

                    boolean soundState = pref.getBoolean(getString(R.string.saved_sound_state_key), true);

                    rotate = new RotateAnimation(0, (360f - degree) + toDegree, rouletteViewInLayout.xc, rouletteViewInLayout.yc);
                    rotate.setDuration(duration);       // アニメーションにかける時間(ミリ秒)
                    rotate.setFillAfter(true);          // アニメーション表示後の状態を保持
                    rotate.setInterpolator(new DecelerateInterpolator(interpolatorFactor)); //勢い良く回り、だんだんゆっくりになって止まるように


                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /*
                    ObjectAnimator rotate = ObjectAnimator.ofFloat(rouletteViewInLayout, View.ROTATION, 0f, (360f - degree) + 1800f);
                    rotate.setDuration(3000);
                    rotate.setInterpolator(new DecelerateInterpolator()); //勢い良く回り、だんだんゆっくりになって止まるように
                    rotate.start();
                    */
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



                    rotate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            //背景色、resultTextViewをそれぞれ初期化する
                            //constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            changeBackgroundColorWithAnimation(Color.parseColor(getResources().getString(R.color.appPrimaryColor)));

                            resultTextView.setText("");

                            //抽選中は各種ボタンを無効化する
                            rouletteStartButton.setEnabled(false);
                            //createButton.setEnabled(false);
                            //toMyRouletteButton.setEnabled(false);
                            //editButton.setEnabled(false);
                            plusButton.setEnabled(false);
                            minusButton.setEnabled(false);
                            fabsMenu.setEnabled(false);
                            //rouletteCreateFab.setEnabled(false);
                            //editRouletteFab.setEnabled(false);
                            //myRouletteFab.setEnabled(false);
                            toolbar.findViewById(R.id.menuButton).setEnabled(false);
                            toolbar.findViewById(R.id.tutorialMenuButton).setEnabled(false);
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                            //SwitchCompat soundSwitch = navigationView.findViewById(R.id.nav_sound_option).findViewById(R.id.sound_switch_in_drawer_layout);
                            //情報保存用の共有環境設定ファイル
                            //SharedPreferences pref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                            //boolean soundState = pref.getBoolean(getString(R.string.saved_sound_state_key), true);

                            if (soundState) {
                                soundPool.play(drumRollStart, 1.0f, 1.0f, 1, 0, 1);
                                drumRollLoopStreamID = soundPool.play(drumRollLoop, 1.0f, 1.0f, 1, 5, 1);
                            }
                            // we empty the result text view when the animation start
                            resultTextView.setText("");

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            //SwitchCompat soundSwitch = navigationView.findViewById(R.id.nav_sound_option).findViewById(R.id.sound_switch_in_drawer_layout);
                            //情報保存用の共有環境設定ファイル
                            //SharedPreferences pref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                            //boolean soundState = pref.getBoolean(getString(R.string.saved_sound_state_key), true);

                            if (soundState) {
                                soundPool.stop(drumRollLoopStreamID);
                                soundPool.play(finishSound, 1.0f, 1.0f, 1, 0, 1);
                            }
                            // we display the correct sector pointed by the triangle at the end of the rotate animation
                            //resultTextView.setText(getSector(360f - (degree % 360)));
                            //ルーレットが止まった位置の項目名と色をそれぞれ設定する
                            int resultBackgroundColor = rouletteViewInLayout.getColors().get(getSector(degree, rouletteViewInLayout));
                            constraintLayout.setBackgroundColor(resultBackgroundColor);
                            //changeBackgroundColorWithAnimation(rouletteViewInLayout.getColors().get(getSector(degree, rouletteViewInLayout)));
                            String resultText = rouletteViewInLayout.getItemNames().get(getSector(degree, rouletteViewInLayout));
                            if (rouletteViewInLayout.isColorDark(resultBackgroundColor)) {
                                resultTextView.setTextColor(Color.WHITE);
                            } else {
                                resultTextView.setTextColor(Color.BLACK);
                            }
                            resultTextView.setText(resultText);
                            //resultTv.setText(getSector(degree));
                            //Log.d("getsector", String.valueOf(360 - (degree % 720)));

                            //抽選終了後に各種ボタンを有効化する
                            rouletteStartButton.setEnabled(true);
                            //createButton.setEnabled(true);
                            //toMyRouletteButton.setEnabled(true);
                            //editButton.setEnabled(true);
                            plusButton.setEnabled(true);
                            minusButton.setEnabled(true);
                            fabsMenu.setEnabled(true);
                            //rouletteCreateFab.setEnabled(true);
                            //editRouletteFab.setEnabled(true);
                            //myRouletteFab.setEnabled(true);
                            toolbar.findViewById(R.id.menuButton).setEnabled(true);
                            toolbar.findViewById(R.id.tutorialMenuButton).setEnabled(true);
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            //degreeOld = degree;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    rouletteViewInLayout.startAnimation(rotate);    // アニメーション開始


                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
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
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                    mToast.show();
                } else if (rouletteViewInLayout.getColors().size() * (rouletteViewInLayout.getSplitCount() + 1) > 300){
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), R.string.notify_cannot_split, Toast.LENGTH_SHORT);
                    mToast.show();
                } else {
                    //背景色、resultTextViewをそれぞれ初期化する
                    //constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    changeBackgroundColorWithAnimation(Color.parseColor(getResources().getString(R.color.appPrimaryColor)));
                    resultTextView.setText("");

                    //RouletteView roulette = (RouletteView) rouletteViewInLayout;
                    //(rouletteViewInLayout.splitCount)++;
                    rouletteViewInLayout.setSplitCount(rouletteViewInLayout.getSplitCount() + 1);
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
                    //constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    //RouletteView roulette = (RouletteView) rouletteViewInLayout;
                    if (rouletteViewInLayout.getSplitCount() >= 2) {
                        //(rouletteViewInLayout.getSplitCount())--;
                        rouletteViewInLayout.setSplitCount(rouletteViewInLayout.getSplitCount() - 1);
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
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

/*
        int roulette = getResources().getIdentifier("roulette","id","MainActivity");
        RouletteView rouletteview = new RouletteView(this, (AttributeSet) this);
        rouletteView = (com.e.myroulette1.RouletteView) findViewById(R.id.roulette);
        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.addView(rouletteview, new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        ));


 */
        //setContentView(new TouchView(this));//タッチして座標取得に必要

/*
        ObjectAnimator animation = ObjectAnimator.ofFloat(rouletteView, "rotation", 500f);
        animation.setDuration(1000);
        animation.start();
 */


        //init();

/*
        animation = new ArcAnimation(rouletteView);
        animation.setDuration(1000);
        rouletteView.startAnimation(animation);
*/

    }

    private void tutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.main_tutorial_id));

        /*
        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });
        */

        sequence.setConfig(config);

        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView itemView, int position) {
                isTutorialState = false;
            }
        });

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                isTutorialState = true;
            }
        });

        sequence.singleUse(getString(R.string.main_tutorial_id));


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(constraintLayout)
                        .setContentText("チュートリアルを開始します。")
                        .setContentTextColor(getResources().getColor(R.color.showcase_text_color))
                        .setGravity(16)
                        //.renderOverNavigationBar()
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        //.setToolTip(itemNameToolTip)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .withoutShape()
                        .build()
        );

        ShowcaseTooltip drawerMenuToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("タップするとメニューが開き、各種設定を行うことができます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(toolbar.findViewById(R.id.menuButton))
                        //.setTarget(constraintLayout)
                        //.setContentText("タップするとメニューが開き、各種設定を行うことができます。")
                        .setToolTip(drawerMenuToolTip)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withCircleShape()
                        //.withoutShape()
                        //.setGravity(16)
                        .setShapePadding(30)
                        .build()

                        /*
                        .setTarget(constraintLayout)
                        .setContentText("チュートリアルを開始します。")
                        .setContentTextColor(getResources().getColor(R.color.showcase_text_color))
                        .setGravity(16)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        //.setToolTip(itemNameToolTip)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .withoutShape()
                        .build()

                         */
        );

        ShowcaseTooltip fabsMenuToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("タップするとボタンメニューが出現します。<br><br> タップしてみましょう。");

        FABsMenu faBsMenu = findViewById(R.id.fabs_menu);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(faBsMenu.getMenuButton())
                        .setToolTip(fabsMenuToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        //.setDismissOnTouch(true)
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
                .text("押すとルーレット作成画面に移動します。移動先ではルーレットの作成、保存をすることができます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(faBsMenu.findViewById(R.id.fab_roulette_create))
                        .setToolTip(fabRouletteCreateToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .withCircleShape()
                        .setShapePadding(30)
                        .build()
        );

        ShowcaseTooltip fabEditRouletteToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("押すとルーレット編集画面に移動します。現在セットしてあるルーレットの編集を行うことができます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(faBsMenu.findViewById(R.id.fab_edit_roulette))
                        .setToolTip(fabEditRouletteToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .withCircleShape()
                        .setShapePadding(30)
                        .build()
        );

        ShowcaseTooltip fabMyRouletteToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("押すと保存済みのルーレットを見ることができます。また、保存済みルーレットを編集することもできます。<br><br>以上でこの画面のチュートリアルを終了します。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(faBsMenu.findViewById(R.id.fab_myRoulette))
                        .setToolTip(fabMyRouletteToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .withCircleShape()
                        .setShapePadding(30)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuButton:

                /*
                //情報保存用の共有環境設定ファイル
                SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                //サウンドの情報を取得、適用
                boolean startSoundState = sharedPref.getBoolean(getString(R.string.saved_sound_state_key), true);
                SwitchCompat soundSwitch = navigationView.findViewById(R.id.nav_sound_option).findViewById(R.id.sound_switch_in_drawer_layout);
                soundSwitch.setChecked(startSoundState);

                //止まるまでの時間の情報を取得、適用
                RadioGroup timeRadioGroup = navigationView.findViewById(R.id.nav_roulette_time_bar).findViewById(R.id.time_radio_group);
                boolean startTimeShortState = sharedPref.getBoolean(getString(R.string.saved_time_short_state_key), false);
                boolean startTimeNormalState = sharedPref.getBoolean(getString(R.string.saved_time_normal_state_key), true);
                boolean startTimeLongState = sharedPref.getBoolean(getString(R.string.saved_time_long_state_key), false);
                ((RadioButton)timeRadioGroup.findViewById(R.id.time_short)).setChecked(startTimeShortState);
                ((RadioButton)timeRadioGroup.findViewById(R.id.time_normal)).setChecked(startTimeNormalState);
                ((RadioButton)timeRadioGroup.findViewById(R.id.time_long)).setChecked(startTimeLongState);

                //どのテーマかの情報を取得、適用
                RadioGroup themeRadioGroup = navigationView.findViewById(R.id.nav_theme_bar).findViewById(R.id.change_theme_radio_group);
                int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        // Night mode is not active, we're using the light theme
                        //ライトにチェック
                        ((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(true);
                        ((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(false);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        // Night mode is active, we're using dark theme
                        //ダークにチェック
                        ((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(false);
                        ((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(true);
                        break;
                }
                //テーマを保存するかの情報を取得、適用
                boolean startThemeSaveState = sharedPref.getBoolean(getString(R.string.saved_theme_save_state_key), false);
                SwitchCompat themeSaveSwitch = navigationView.findViewById(R.id.nav_theme_save_option).findViewById(R.id.theme_save_switch_in_drawer_layout);
                themeSaveSwitch.setChecked(startThemeSaveState);

                 */
                // ボタンをタップした際の処理を記述
                drawerLayout.openDrawer(Gravity.RIGHT);

                return true;

            case R.id.tutorial:
                MaterialShowcaseView.resetSingleUse(MainActivity.this, getString(R.string.main_tutorial_id));
                isTutorialState = true;
                tutorial();

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
                    //rouletteView.splitCount = 1;
                    boolean isTutorialContinue = intent.getBooleanExtra("isTutorialContinue", false);
                    String rouletteNameInfo = intent.getStringExtra("rouletteName");
                    ArrayList<Integer> colorsInfo = intent.getIntegerArrayListExtra("colors");
                    ArrayList<String> textStringsInfo = intent.getStringArrayListExtra("textStrings");
                    ArrayList<Integer> itemRatiosInfo = intent.getIntegerArrayListExtra("itemRatios");
                    ArrayList<Integer> OnOffOfSwitch100Info = intent.getIntegerArrayListExtra("OnOffInfoOfSwitch100");
                    ArrayList<Integer> OnOffOfSwitch0Info = intent.getIntegerArrayListExtra("OnOffInfoOfSwitch0");
                    //rouletteView.itemProbabilities.clear();
                    ArrayList<Float> itemProbabilitiesInfo = new ArrayList<Float>();
                    //int itemProbabilitySize = intent.getIntExtra("itemProbabilitySize", 0);
                    float itemProbabilityArray[] = intent.getFloatArrayExtra("itemProbability");
                    for (int i = 0; i < itemProbabilityArray.length; i++) {
                        itemProbabilitiesInfo.add(itemProbabilityArray[i]);
                    }
                    if (!itemProbabilitiesInfo.isEmpty()) {
                        //イカサマ設定のルーレットセット時は、イカサマモードONで初期化
                        CheatFlag = true;
                    }
                    //constraintLayout.removeView(constraintLayout.getChildAt(constraintLayout.getChildCount() - 1));
                    //constraintLayout.removeView(constraintLayout.getChildAt(constraintLayout.getChildCount() - 1));
                    //constraintLayout.addView(rouletteView);
                    //constraintLayout.addView(new PointerView(getApplicationContext()));

                    rouletteViewInLayout.setRouletteContents(1,rouletteNameInfo, colorsInfo, textStringsInfo, itemRatiosInfo, OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
                    //setContentView(relativeLayout);
                    rouletteViewInLayout.invalidate();
                    //setContentView(drawerLayout);
                    //rouletteViewInLayout.setRotation(- (360f - degreeOld));

                    //背景色、resultTextViewをそれぞれ初期化する
                    constraintLayout.setBackgroundColor(Color.parseColor(getResources().getString(R.color.appPrimaryColor)));
                    resultTextView.setText("");

                    if (rotate != null) {
                        //ルーレットを変更した場合は,角度を初期値に戻す
                        rotate.cancel();
                    }

                    if (TextUtils.isEmpty(rouletteNameInfo)) {
                        toolbar.setTitle("ルーレット");
                    } else {
                        toolbar.setTitle(rouletteNameInfo);
                    }

                    //通知を削除
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(1);

                    rouletteExists = true;

                    if (isTutorialContinue) {
                        ////int pointerPosX = constraintLayout.getLayoutParams().width / 2;
                        //int pointerPosY = (constraintLayout.getLayoutParams().height / 2) - pointerPosX - (pointerPosX/8);

                        int pointerPosX = constraintLayout.getWidth() / 2;
                        int pointerPosY = (constraintLayout.getHeight()/ 2) - pointerPosX - (pointerPosX/8) + ((3 * toolbar.getHeight()) / 2);
                        //Log.d("あああああああああああああああああXY", pointerPosX+" " + " "+pointerPosY);


                        Log.d("あああああああああああああああああXY", pointerPosX+" " + " "+pointerPosY);

                       //FancyShowCaseView fancyShowCaseView = new FancyShowCaseView(this);
                        /*
                       FancyShowCaseView.Builder builder = new FancyShowCaseView.Builder(this);
                       builder.focusCircleAtPosition(pointerPosX, pointerPosY, constraintLayout.getWidth() / 12)
                               .title("この部分をタップすることで、イカサマ設定のON、OFFを切り替えることができます。イカサマがバレそうになった時に使ってみましょう。\n\nイカサマ設定が切り替わるとそのことを端末に通知します。\n\n以上でチュートリアルを終了します。")
                               .titleSize(18, 1)
                               .build();

                         */

                        /*sequence.addSequenceItem(
                                new MaterialShowcaseView.Builder(this)
                                        .setTarget(rouletteName)
                                        //.setContentText("ここでルーレットの項目を１つずつ追加できます。")
                                        .setToolTip(rouletteNameToolTip)
                                        .setDismissOnTouch(true)
                                        //.setTargetTouchable(true)
                                        //.setDismissOnTargetTouch(true)
                                        .withRectangleShape()
                                        .build()
                        );

                         */


                        new FancyShowCaseView.Builder(this)
                                .focusCircleAtPosition(pointerPosX, pointerPosY, constraintLayout.getWidth() / 12)
                                .dismissListener(new DismissListener() {
                                    @Override
                                    public void onDismiss(@org.jetbrains.annotations.Nullable String s) {

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
                                                .text("＋：押すとルーレットを分割します。<br><br>−：押すと分割前に戻します。");

                                        sequence.addSequenceItem(
                                                new MaterialShowcaseView.Builder(MainActivity.this)
                                                        .setTarget(splitButtonLayout)
                                                        //.setContentText("ここでルーレットの項目を１つずつ追加できます。")
                                                        .setToolTip(splitLayoutToolTip)
                                                        .setDismissOnTouch(true)
                                                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                                                        //.setTargetTouchable(true)
                                                        //.setDismissOnTargetTouch(true)
                                                        .withRectangleShape()
                                                        .build()
                                        );

                                        ShowcaseTooltip rouletteStartToolTip = ShowcaseTooltip.build(MainActivity.this)
                                                .corner(30)
                                                .textColor(getResources().getColor(R.color.tooltip_text_color))
                                                .color(getResources().getColor(R.color.appPrimaryColor))
                                                .text("タップするとセットしてあるルーレットが回転し始めます。回転はしばらくすると勝手に止まります。<br><br>以上でチュートリアルを終了します。");

                                        sequence.addSequenceItem(
                                                new MaterialShowcaseView.Builder(MainActivity.this)
                                                        .setTarget(rouletteStartButton)
                                                        //.setContentText("ここでルーレットの項目を１つずつ追加できます。")
                                                        .setToolTip(rouletteStartToolTip)
                                                        .setDismissOnTouch(true)
                                                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                                                        //.setTargetTouchable(true)
                                                        //.setDismissOnTargetTouch(true)
                                                        .withCircleShape()
                                                        .setShapePadding(30)
                                                        .build()
                                        );

                                        sequence.start();
                                    }

                                    @Override
                                    public void onSkipped(@org.jetbrains.annotations.Nullable String s) {

                                    }

                                })
                                .title("この部分をタップすることで、イカサマ設定のON、OFFを切り替えることができます。イカサマがバレそうになった時に使ってみましょう。\n\nイカサマ設定が切り替わるとそのことを端末に通知します。")
                                .titleGravity(16)
                                .titleSize(18, 1)
                                .build()
                                .show();
                    }
                }
                break;
        }

    }

    /*
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_roulette_create:
                Intent toRouletteCreateIntent = new Intent(getApplicationContext(), RouletteCreateActivity.class);
                //Intent intent = new Intent(getApplication(), SubActivity.class);
                startActivityForResult(toRouletteCreateIntent, RESULT_ROULETTECREATE);
                break;
            case R.id.nav_edit_roulette:
                if (rouletteExists) {
                    Intent rouletteEditIntent = new Intent(MainActivity.this, EditRouletteActivity.class);
                    //Intent intent = new Intent(getApplication(), SubActivity.class);
                    rouletteEditIntent.putExtra("editInfoOfRouletteName", rouletteViewInLayout.getRouletteName());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfColors", rouletteViewInLayout.getColors());
                    rouletteEditIntent.putStringArrayListExtra("editInfoOfTextStrings", rouletteViewInLayout.getTextStrings());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfItemRatio", rouletteViewInLayout.getItemRatios());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfSwitch100", rouletteViewInLayout.getOnOffInfoOfSwitch100());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfSwitch0", rouletteViewInLayout.getOnOffInfoOfSwitch0());

                    startActivityForResult(rouletteEditIntent, RESULT_EDITROULETTE);
                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                    mToast.show();
                }
                break;
            case R.id.nav_myRoulette:
                Intent toMyRouletteIntent = new Intent(getApplicationContext(), MyRouletteActivity.class);
                startActivityForResult(toMyRouletteIntent, RESULT_MYROULETTE);
                //menu.collapseImmediately();
                //startActivity(toMyRouletteIntent);
                break;
        }

        return true;
    }

     */

    //ReviewInfo オブジェクトをリクエストする
    private void getReviewInfo() {
        reviewManager = ReviewManagerFactory.create(getApplicationContext());
        Task<ReviewInfo> manager = reviewManager.requestReviewFlow();
        manager.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
            } else {
                if (mToast != null) mToast.cancel();
                mToast = Toast.makeText(getApplicationContext(), "アプリのレビューを開始できませんでした。", Toast.LENGTH_LONG);
                mToast.show();
                //Toast.makeText(getApplicationContext(), "アプリのレビューを開始できませんでした。", Toast.LENGTH_LONG).show();
            }
        });
    }

    //アプリ内レビューフローを開始する
    private void startReviewFlow() {
        if (reviewInfo != null) {
            Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), "レビューが完了しました。ありがとうございました。", Toast.LENGTH_LONG);
                    mToast.show();
                    //Toast.makeText(getApplicationContext(), "レビューが完了しました。ありがとうございました。", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            if (mToast != null) mToast.cancel();
            mToast = Toast.makeText(getApplicationContext(), "アプリのレビューに失敗しました。", Toast.LENGTH_LONG);
            mToast.show();
            //Toast.makeText(getApplicationContext(), "アプリのレビューに失敗しました。", Toast.LENGTH_LONG).show();
        }
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
                    duration = 5000;
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
                    duration = 10000;
                    interpolatorFactor = 2.3f;

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
                    duration = 17000;
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
                duration = 10000;
                interpolatorFactor = 2.3f;

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

                    //セットされているルーレットがあった場合、アクティビティ復元時にそのルーレットを復元するために、情報を保存する
                    if (rouletteExists) {
                        //saveRouletteInfoToSharedPrefs(editor);
                    }

                    //ライトテーマにする
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

                    //セットされているルーレットがあった場合、アクティビティ復元時にそのルーレットを復元するために、情報を保存する
                    if (rouletteExists) {
                        //saveRouletteInfoToSharedPrefs(editor);
                    }

                    //ダークテーマにする
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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

/*
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                //ライトにチェック
                //((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(true);
                //((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(false);
                //false = ライトテーマ
                //applyDayNightToMain(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                //ダークにチェック
                //((RadioButton)themeRadioGroup.findViewById(R.id.light_theme)).setChecked(false);
                //((RadioButton)themeRadioGroup.findViewById(R.id.dark_theme)).setChecked(true);
                //true = ダークテーマ
                //applyDayNightToMain(true);
                break;
        }
    }

 */

    /*
    private void applyDayNightToMain(boolean dayNightFlag) {
        navigationView.setBackgroundColor(Color.WHITE);
    }
    */

    @Override
    public void onBackPressed() {

        //if (!isTutorialState) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else if (fabsMenu.isExpanded()) {
                fabsMenu.collapse();
            } else {
                super.onBackPressed();
            }
        //}
    }

        /*
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // something to do
                break;
            case MotionEvent.ACTION_UP:
                // something to do
                break;
            case MotionEvent.ACTION_MOVE:
                // something to do
                break;
            case MotionEvent.ACTION_CANCEL:
                // something to do
                break;
        }

         */

    private Integer getSector(float degrees, RouletteView roulette) {
        int i = 0;
        //String text = null;
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
                // degrees is in [start;end[
                // so text is equals to sectors[i];

                //text = roulette.getTextStrings().get(i % roulette.getColors().size());//get(i)をget(iのcolorsサイズで割った剰余)に変更　→　スプリットしたときに存在しないtextStringを参照しないように対策 →　%の計算量でかいから注意
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


    /*
   //このクラスは//タッチして座標取得に必要
    public class TouchView extends View {

        //X、Y座標用の変数を準備
        private float x_zahyou;
        private float y_zahyou;
        private com.e.myroulette1.RouletteView rouletteView1;

        public TouchView(Context context) {
            super(context);

            //X、Y座標の初期化
            x_zahyou = 0;
            y_zahyou = 0;
        }

        //描画するところ
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //文字の表示スタイルを設定
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextSize(50);
            paint.setStrokeWidth(30);

            //画面に文字列などを表示する
            canvas.drawText("タッチしてください",50,100, paint);
            canvas.drawText("X座標:" + x_zahyou + "　Y座標:" + y_zahyou,50,160, paint);
            canvas.drawText("width座標:" + this.getWidth() + "　height座標:" + this.getHeight() + " left:" + this.getLeft() +
                    "right:" + this.getRight() + "top:" + this.getTop() + "bottom:" + this.getBottom()
                    ,50,260, paint);
            //canvas.drawText("roulette x座標:" + rouletteView1.getWidth() / 2 + "　roulette y座標:" + rouletteView1.getHeight() / 2,50,360, paint);
            canvas.drawPoint(x_zahyou, y_zahyou, paint);
        }

        //画面（View）が操作されると呼び出される
        @Override
        public boolean onTouchEvent(MotionEvent event) {

            //画面（View）が押されたのかを判定
            if(event.getAction() == MotionEvent.ACTION_DOWN) {

                x_zahyou = event.getX();    //X座標を変数にセット
                y_zahyou = event.getY();    //Y座標を変数にセット
            }

            //画面の更新（onDrawの呼び出し）
            invalidate();

            return true;
        }
    }

     */
}

