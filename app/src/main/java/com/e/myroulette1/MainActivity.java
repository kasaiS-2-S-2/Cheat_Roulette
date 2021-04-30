package com.e.myroulette1;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Random;

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
    private PushButton pushButton;
    private TextView resultTextView;
    private Button resetButton;
    private Button plusButton;
    private Button minusButton;
    private Button sendButton;
    private Button checkButton;
    private Button editButton;
    private Button toMyRouletteButton;
    private ConstraintLayout constraintLayout;
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

    //private float xc = 0.0f;
    //private float yc = 0.0f;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        constraintLayout = findViewById(R.id.constraintLayout);
        pushButton = findViewById(R.id.pushButton);

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point displayAre = new Point();
        disp.getSize(displayAre);

        constraintLayout.getViewById(R.id.pushButton).getLayoutParams().width = displayAre.x / 3;
        constraintLayout.getViewById(R.id.pushButton).getLayoutParams().height = displayAre.x / 3;

        //rouletteViewInLayout = new RouletteView(getApplicationContext());//初めはインスタンスの中身なし;
        //constraintLayout.addView(rouletteViewInLayout);
        setContentView(constraintLayout);

        pushButton = (PushButton) findViewById(R.id.pushButton);
        resultTextView = findViewById(R.id.resultTextView);
        resetButton = findViewById(R.id.reset_button);
        plusButton = findViewById(R.id.plus_button);
        minusButton = findViewById(R.id.minus_button);
        sendButton = findViewById(R.id.send_button);
        checkButton = findViewById(R.id.check_button);
        editButton = findViewById(R.id.edit_button);
        toMyRouletteButton = findViewById(R.id.myRoulette_button);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRouletteCreateIntent = new Intent(getApplication(), RouletteCreateActivity.class);
                //Intent intent = new Intent(getApplication(), SubActivity.class);
                startActivityForResult(toRouletteCreateIntent, RESULT_ROULETTECREATEACTIVITY);
            }
        });

        toMyRouletteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMyRouletteIntent = new Intent(getApplication(), MyRouletteActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
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
                    Uri uri = null;
                    Intent rouletteEditIntent = new Intent("EDIT_ROULETTE", uri, getApplication(), RouletteCreateActivity.class);
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
            @RequiresApi(api = Build.VERSION_CODES.Q)
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

        pushButton.setOnClickListener(new View.OnClickListener() {
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
                        degree = 360f - RANDOM.nextInt(360);
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

            /*
            animation = new ArcAnimation(rouletteView);
            animation.setDuration(3600);
            animation.setInterpolator(new DecelerateInterpolator()); //勢い良く回り、だんだんゆっくりになって止まるように
            rouletteView.startAnimation(animation);
            */
                    } else { //イカサマ抽選
                        Log.d("aaaaaaaaaaaaaaaaaa", "イカサマ");
                        //degree = 0;
                        int randomNum = RANDOM.nextInt(100) + 1;//1 ~ 100
                        float sumOfProbability = rouletteViewInLayout.getItemProbabilities().get(0);
                        float sumOfDegree = sectorDegree * rouletteViewInLayout.getItemRatios().get(0);

                        if (randomNum <= sumOfProbability) {
                            degree = RANDOM.nextInt((int) (sectorDegree * rouletteViewInLayout.getItemRatios().get(0)));
                        } else {
                            for (int i = 1; i < rouletteViewInLayout.getColors().size(); i++) {
                                if (sumOfProbability < randomNum &&
                                        randomNum <= (sumOfProbability + rouletteViewInLayout.getItemProbabilities().get(i))) {

                                    degree = sumOfDegree +
                                            RANDOM.nextInt((int) (sectorDegree * rouletteViewInLayout.getItemRatios().get(i)));

                                    break;
                                }
                                sumOfProbability += rouletteViewInLayout.getItemProbabilities().get(i);
                                sumOfDegree += sectorDegree * rouletteViewInLayout.getItemRatios().get(i);
                            }
                        }
                    }

                    RotateAnimation rotate = new RotateAnimation(0, (360f - degree) + 7200f, rouletteViewInLayout.xc, rouletteViewInLayout.yc);
                    rotate.setDuration(8500);       // アニメーションにかける時間(ミリ秒)
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

    private String getSector(float degrees, RouletteView roulette) {
        int i = 0;
        String text = null;
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

                text = roulette.getTextStrings().get(i % roulette.getColors().size());//get(i)をget(iのcolorsサイズで割った剰余)に変更　→　スプリットしたときに存在しないtextStringを参照しないように対策 →　%の計算量でかいから注意
            }
            i++;

        } while (text == null && i < roulette.getColors().size() * roulette.splitCount);

        return text;
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

