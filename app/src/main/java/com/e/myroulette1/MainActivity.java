package com.e.myroulette1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Random;

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

    RouletteView rouletteViewInLayout;//onWindowFocusChange用の変数
    private PushImageButton rouletteStartButton;
    private TextView resultTextView;
    private Button resetButton;
    private Button plusButton;
    private Button minusButton;
    private Button createButton;
    private Button checkButton;
    private Button editButton;
    private Button toMyRouletteButton;
    private ConstraintLayout constraintLayout;
    //private DrawerLayout drawerLayout;
    private ArcAnimation animation;

    private boolean rouletteExists = false;
    static final int RESULT_ROULETTECREATEACTIVITY = 1;
    static final int RESULT_MYROULETTE = 2;

    private static final Random RANDOM = new Random();
    private float degree = 0;
    public static float degreeOld = 0;/////////////////////////////////////////////////////////////
    private float sectorDegree = 0;
    final String notRouletteExistsMessage = "ルーレットが作成されていません";

    public static WordViewModel mWordViewModel;
    public static final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff());

    static Toast mToast= null;
    static boolean CheatFlag = true;

    private SoundPool soundPool;
    private int drumRollStart, drumRollLoop, finishSound;
    private int drumRollLoopStreamID;

    //private float xc = 0.0f;
    //private float yc = 0.0f;


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("あああああああああああああああ", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("あああああああああああああああ", "onResume");

        //soundPool.stop(drumRollLoopStreamID);
        soundPool.autoResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("あああああああああああああああ", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("あああああああああああああああ", "onPause");

        //soundPool.stop(drumRollLoopStreamID);
        soundPool.autoPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("あああああああああああああああ", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("あああああああああああああああ", "onDestroy");
    }

    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("あああああああああああああああ", "onCreate");
        //final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff());

        // Get a new or existing ViewModel from the ViewModelProvider.
        mWordViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(WordViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        // ここで、recyclerViewにDBのデータを表示する。無いと何も表示されない（recyclerViewがあり、DBも更新されているが、見た目のみ反映できない

        //MyRouletteActivity myRouletteActivity = new MyRouletteActivity();
        mWordViewModel.getAllWords().observe(this, words -> {

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

        //drawerLayout = findViewById(R.id.drawer_layout);
        constraintLayout = findViewById(R.id.constraintLayout);
        //rouletteStartButton = findViewById(R.id.rouletteStartButton);

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point displayAre = new Point();
        disp.getSize(displayAre);


        constraintLayout.getViewById(R.id.rouletteStartButton).getLayoutParams().width = displayAre.x / 3;
        constraintLayout.getViewById(R.id.rouletteStartButton).getLayoutParams().height = displayAre.x / 3;
        //Matrix matrix = new Matrix();
        //matrix.setScale(displayAre.x * 2f / 9f, displayAre.x * 2f / 9f);
        //matrix.setScale(3, 3);
        //rouletteStartButton.setImageMatrix(matrix);

        //rouletteViewInLayout = new RouletteView(getApplicationContext());//初めはインスタンスの中身なし;
        //constraintLayout.addView(rouletteViewInLayout);
        setContentView(constraintLayout);
        //setContentView(drawerLayout);

        rouletteStartButton = findViewById(R.id.rouletteStartButton);
        resultTextView = findViewById(R.id.resultTextView);
        resetButton = findViewById(R.id.reset_button);
        plusButton = findViewById(R.id.plus_button);
        minusButton = findViewById(R.id.minus_button);
        createButton = findViewById(R.id.send_button);
        checkButton = findViewById(R.id.check_button);
        editButton = findViewById(R.id.edit_button);
        toMyRouletteButton = findViewById(R.id.myRoulette_button);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRouletteCreateIntent = new Intent(getApplicationContext(), RouletteCreateActivity.class);
                //Intent intent = new Intent(getApplication(), SubActivity.class);
                startActivityForResult(toRouletteCreateIntent, RESULT_ROULETTECREATEACTIVITY);
            }
        });

        toMyRouletteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMyRouletteIntent = new Intent(getApplicationContext(), MyRouletteActivity.class);
                startActivityForResult(toMyRouletteIntent, RESULT_MYROULETTE);
                //startActivity(toMyRouletteIntent);
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

    //@RequiresApi(api = Build.VERSION_CODES.Q)
    public void onWindowFocusChanged(boolean hasFocus) {
        //public void onResume() {
        //super.onRestart();
        //super.onStart();
        //super.onResume();

        super.onWindowFocusChanged(hasFocus);

        //rouletteView = new RouletteView(getApplicationContext());

        //xc = rouletteView.getWidth() / 2;
        //yc = rouletteView.getHeight() / 2;

        ////////////////////////////////////////////////////////////////
        //constraintLayout.addView(new PointerView(getApplicationContext(), rouletteView.xc, rouletteView.yc));
        //if (constraintLayout.getChildAt(constraintLayout.getChildCount() - 2) instanceof RouletteView) {
        //rouletteViewInLayout = (RouletteView) constraintLayout.getChildAt(constraintLayout.getChildCount() - 2);
        rouletteViewInLayout = findViewById(R.id.roulette);
        //}
        //setContentView(constraintLayout);

        /*
        //Myルーレットのルーレットをセットする場合の処理
        Intent fromMainIntent = getIntent();
        if (fromMainIntent.getAction() != null && fromMainIntent.getAction().equals("SET_MYROULETTE")) {
            //RouletteView rouletteView = findViewById(R.id.roulette);
            //rouletteView.splitCount = 1;
            String rouletteNameInfo = fromMainIntent.getStringExtra("rouletteName");
            ArrayList<Integer> colorsInfo = fromMainIntent.getIntegerArrayListExtra("colors");
            ArrayList<String> textStringsInfo = fromMainIntent.getStringArrayListExtra("textStrings");
            ArrayList<Integer> itemRatiosInfo = fromMainIntent.getIntegerArrayListExtra("itemRatios");
            ArrayList<Integer> OnOffOfSwitch100Info = fromMainIntent.getIntegerArrayListExtra("OnOffInfoOfSwitch100");
            ArrayList<Integer> OnOffOfSwitch0Info = fromMainIntent.getIntegerArrayListExtra("OnOffInfoOfSwitch0");
            //rouletteView.itemProbabilities.clear();
            ArrayList<Float> itemProbabilitiesInfo = new ArrayList<Float>();
            //int itemProbabilitySize = intent.getIntExtra("itemProbabilitySize", 0);
            float itemProbabilityArray[] = fromMainIntent.getFloatArrayExtra("itemProbability");
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

            rouletteViewInLayout.setRouletteContents(rouletteNameInfo, colorsInfo, textStringsInfo, itemRatiosInfo, OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
            setContentView(constraintLayout);
            rouletteExists = true;
        }

         */

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rouletteExists) {
                    Intent rouletteEditIntent = new Intent(MainActivity.this, EditRouletteActivity.class);
                    //Intent intent = new Intent(getApplication(), SubActivity.class);
                    rouletteEditIntent.putExtra("editInfoOfRouletteName", rouletteViewInLayout.getRouletteName());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfColors", rouletteViewInLayout.getColors());
                    rouletteEditIntent.putStringArrayListExtra("editInfoOfTextStrings", rouletteViewInLayout.getTextStrings());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfItemRatio", rouletteViewInLayout.getItemRatios());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfSwitch100", rouletteViewInLayout.getOnOffInfoOfSwitch100());
                    rouletteEditIntent.putIntegerArrayListExtra("editInfoOfSwitch0", rouletteViewInLayout.getOnOffInfoOfSwitch0());

                    startActivityForResult(rouletteEditIntent, RESULT_ROULETTECREATEACTIVITY);
                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                if (rouletteExists) {
                    //RouletteView roulette = (RouletteView) rouletteViewInLayout;
                    Matrix matrix = new Matrix();          //マトリックスを作成
                    matrix.setRotate(0, rouletteViewInLayout.xc, rouletteViewInLayout.yc);  //新しいルーレットが作成れるごとにルーレットの初期位置を調整（最初の項目が0度の位置にくるようにする）→　初めだけだった。→　アクティビティ開始時に動作するように設定を試みよ
                    //onwindowre~とmatrix０度にしたら、普通にはできるようになったけど、文字キーが写ってるとき、タスク消すボタン等、少し画面を離れるとやるとうまくいかない→それらの状態の時はon~のどの状態なのかを調べる。
                    rouletteViewInLayout.setAnimationMatrix(matrix);//APIレベル高すぎて、みんな使えないかも
                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

        rouletteStartButton.setOnClickListener(new View.OnClickListener() {
            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(View view) {
                if (rouletteExists) {
                    //RouletteView roulette = (RouletteView) rouletteViewInLayout;
                    //degree = 0;
                    //float degree = 0;
                    //degreeOld = degree % 360;///////////////////////////////////////////////////////////////////
                    sectorDegree = 360f / (rouletteViewInLayout.sumOfItemRatio * rouletteViewInLayout.splitCount);

                    if (rouletteViewInLayout.getItemProbabilities().isEmpty() || !CheatFlag) {
                        //普通の抽選
                        Log.d("aaaaaaaaaaaaaaaaaa", "通常");
                        degree = 360f - (RANDOM.nextFloat() * 360f);
                        degreeOld = degree % 360;///////////////////////////////////////////////////////////////////
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


                    RotateAnimation rotate = new RotateAnimation(0, (360f - degree) + 7200f, rouletteViewInLayout.xc, rouletteViewInLayout.yc);
                    rotate.setDuration(10000);       // アニメーションにかける時間(ミリ秒)
                    rotate.setFillAfter(true);          // アニメーション表示後の状態を保持
                    rotate.setInterpolator(new DecelerateInterpolator(2.0f)); //勢い良く回り、だんだんゆっくりになって止まるように


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
                            rouletteStartButton.setEnabled(false);
                            createButton.setEnabled(false);
                            toMyRouletteButton.setEnabled(false);
                            editButton.setEnabled(false);
                            plusButton.setEnabled(false);
                            minusButton.setEnabled(false);

                            //背景色、resultTextViewをそれぞれ初期化する
                            constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            resultTextView.setText("");

                            soundPool.play(drumRollStart, 1.0f, 1.0f, 1, 0, 1);
                            drumRollLoopStreamID =
                                    soundPool.play(drumRollLoop, 1.0f, 1.0f, 1, 5, 1);
                            // we empty the result text view when the animation start
                            resultTextView.setText("");

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            soundPool.stop(drumRollLoopStreamID);
                            soundPool.play(finishSound, 1.0f, 1.0f, 1, 0, 1);
                            // we display the correct sector pointed by the triangle at the end of the rotate animation
                            //resultTextView.setText(getSector(360f - (degree % 360)));
                            //ルーレットが止まった位置の項目名と色をそれぞれ設定する
                            constraintLayout.setBackgroundColor(rouletteViewInLayout.getColors().get(getSector(degree, rouletteViewInLayout)));
                            resultTextView.setText(rouletteViewInLayout.getTextStrings().get(getSector(degree, rouletteViewInLayout)));
                            //resultTv.setText(getSector(degree));
                            //Log.d("getsector", String.valueOf(360 - (degree % 720)));
                            rouletteStartButton.setEnabled(true);
                            createButton.setEnabled(true);
                            toMyRouletteButton.setEnabled(true);
                            editButton.setEnabled(true);
                            plusButton.setEnabled(true);
                            minusButton.setEnabled(true);
                            Log.d("getsector", String.valueOf(degree));
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
                if (rouletteExists) {
                    //背景色、resultTextViewをそれぞれ初期化する
                    constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    resultTextView.setText("");
                    //RouletteView roulette = (RouletteView) rouletteViewInLayout;
                    (rouletteViewInLayout.splitCount)++;
                    rouletteViewInLayout.invalidate();
                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rouletteExists) {
                    //背景色、resultTextViewをそれぞれ初期化する
                    constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    resultTextView.setText("");
                    //RouletteView roulette = (RouletteView) rouletteViewInLayout;
                    if (rouletteViewInLayout.splitCount >= 2) {
                        (rouletteViewInLayout.splitCount)--;
                        rouletteViewInLayout.invalidate();
                    }
                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rouletteExists) {
                    //RouletteView roulette = (RouletteView) rouletteViewInLayout;
                    for (int i = 0; i < rouletteViewInLayout.getColors().size(); i++) {
                        //Log.d("Roulette contents of color"+ i, String.valueOf(RouletteView.colors[i]));
                        //Log.d("Rolettte contents of textString" + i, RouletteView.textStrings[i]);
                        Log.d("Roulette contents of color" + i, String.valueOf(rouletteViewInLayout.getColors().get(i)));
                        Log.d("Rolettte contents of textString" + i, rouletteViewInLayout.getTextStrings().get(i));
                    }
                } else {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(getApplicationContext(), notRouletteExistsMessage, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        RouletteView rouletteView = findViewById(R.id.roulette);
        switch (requestCode) {
            case RESULT_ROULETTECREATEACTIVITY:
            case RESULT_MYROULETTE:
                if (RESULT_OK == resultCode && intent != null) {
                    //rouletteView.splitCount = 1;
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

                    rouletteView.setRouletteContents(rouletteNameInfo, colorsInfo, textStringsInfo, itemRatiosInfo, OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
                    setContentView(constraintLayout);

                    //背景色、resultTextViewをそれぞれ初期化する
                    constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    resultTextView.setText("");

                    rouletteExists = true;
                }
                break;
        }

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

        } while (position == null && i < roulette.getColors().size() * roulette.splitCount);

        return position;
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

