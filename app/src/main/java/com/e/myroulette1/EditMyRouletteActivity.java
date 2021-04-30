package com.e.myroulette1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.e.myroulette1.MainActivity.adapter;
/*
今はゴリゴリレイアウト書いてるけど、ホントは,リストviewとか、adapterとか使ったほうが良さそう。
 */

public class EditMyRouletteActivity extends AppCompatActivity {

    //public int rouletteCount;
    private LinearLayout layout;
    private ScrollView scrollView;
    private CheckBox checkBox;
    //private EditText editText, editText2;

    private float hue = 0;
    private float[] hsv = {0, 100, 100};
    private int color;
    private int hueLevel = 0;
    private boolean visibleFlag = false;

    private final ColorPalette colorPalette = new ColorPalette();

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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_roulette);

        MainActivity.mWordViewModel.getAllWords().observe(this, words -> {

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

        layout = findViewById(R.id.layout);
        scrollView = findViewById(R.id.scrollView);
        checkBox = findViewById(R.id.checkBox);
        //checkBox.setChecked(false);

        //************** dp単位を取得 *************/

        // dp単位
        final float scale = getResources().getDisplayMetrics().density;
        // マージン 30dp に設定
        final int margins = (int)(10 * scale);


        //Myルーレットにあるルーレットをを編集する場合
        Intent fromMyRouletteIntent = getIntent(); //Mainでedit_Buttonを押した時の遷移

        int rouletteIdFromMyRoulette = fromMyRouletteIntent.getIntExtra("rouletteId", 0);
        String rouletteNameFromMyRoulette = fromMyRouletteIntent.getStringExtra("rouletteName");
        ArrayList<Integer> colorsFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra("colors");
        ArrayList<String> textStringsFromMyRoulette = fromMyRouletteIntent.getStringArrayListExtra("textStrings");
        ArrayList<Integer> itemRatioFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra("itemRatios");
        ArrayList<Integer> Switch100InfoFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra("OnOffInfoOfSwitch100");
        ArrayList<Integer> Switch0InfoFromMyRoulette = fromMyRouletteIntent.getIntegerArrayListExtra("OnOffInfoOfSwitch0");

        EditText editText = findViewById(R.id.rouletteName);
        editText.setText(rouletteNameFromMyRoulette);//ルーレット名の項目に設定

        for (int i = 0; i < colorsFromMyRoulette.size(); i++) {
            addEditView(layout, scale, margins);

            ViewGroup viewGroup1 = (ViewGroup) ((ViewGroup) layout.getChildAt(i)).getChildAt(0); //色ボタン, 項目名, 比率のviewgroup
            ViewGroup viewGroup2 = (ViewGroup) ((ViewGroup) layout.getChildAt(i)).getChildAt(1); //スイッチ２つのviewgroup

            ((GradientDrawable)viewGroup1.getChildAt(0).getBackground()).setColor(colorsFromMyRoulette.get(i)); //色ボタンの色

            ((EditText) viewGroup1.getChildAt(1)).setText(textStringsFromMyRoulette.get(i)); //項目名
            ((EditText) viewGroup1.getChildAt(2)).setText(Integer.toString(itemRatioFromMyRoulette.get(i))); //比率

            if (Switch100InfoFromMyRoulette.get(i) == 1) {
                ((Switch) viewGroup2.getChildAt(0)).setChecked(true); //必中ボタンをON
                //((Switch) viewGroup2.getChildAt(1)).setChecked(false);
            } else if (Switch0InfoFromMyRoulette.get(i) == 1) {
                //((Switch) viewGroup2.getChildAt(0)).setChecked(false);
                ((Switch) viewGroup2.getChildAt(1)).setChecked(true); //絶対ハズレボタンをON
            }
        }

        //************* ボタンの設定 *************/
        Button button = findViewById(R.id.button);

        // リスナーをボタンに登録, lambda
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addEditView(layout, scale, margins);
                scrollView.post(new Runnable() { //scrollviewの一番したまで勝手にスクロールする処理
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                /*
                hueLevel++;
                if (hueLevel >= 12) { hueLevel = 0; }

                 */
            }
        });

        Button returnButton = findViewById(R.id.return_button);
        //textView = findViewById(R.id.text_view);
        //final EditText editText= findViewById(R.id.edit_text);
        //Button button = findViewById(R.id.button);
        // lambda式
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { EditMyRouletteActivity.this.finish(); }
        });


        Button cheatButton = findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visibleFlag) { //trueの場合は、イカサマスイッチを隠す
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        ((ViewGroup) layout.getChildAt(i)).getChildAt(1).setVisibility(View.GONE);
                    }
                    visibleFlag = false;
                    //cheatButton.setVisibility(View.INVISIBLE);
                    cheatButton.setText("");
                    cheatButton.setBackgroundColor(Color.TRANSPARENT);
                } else {// falseの場合は、イカサマスイッチを表示
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        ((ViewGroup) layout.getChildAt(i)).getChildAt(1).setVisibility(View.VISIBLE);
                    }
                    visibleFlag = true;
                    //cheatButton.setVisibility(View.VISIBLE);
                    cheatButton.setText("HIDE");
                    cheatButton.setBackgroundColor(Color.RED);
                }
            }
        });

        EditText rouletteName = findViewById(R.id.rouletteName);

        Button countButton = findViewById(R.id.count_button);
        countButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //ルーレットの色のリスト
                ArrayList<Integer> colors = new ArrayList<Integer>();
                //ルーレットの文字列のリスト
                ArrayList<String> textStrings = new ArrayList<String>();
                //ルーレットの項目比率のリスト
                ArrayList<Integer> itemRatios = new ArrayList<Integer>();
                //ルーレットの項目別の当選確率のリスト
                ArrayList<Float> itemProbabilities = new ArrayList<Float>();
                //必中スイッチのONOFF情報
                ArrayList<Integer> OnOffOfSwitch100 = new ArrayList<Integer>();
                //絶対ハズレスイッチのONOFF情報
                ArrayList<Integer> OnOffOfSwitch0 = new ArrayList<Integer>();

                int switch100PositiveCount = 0;
                int switch0PositiveCount = 0;
                int rouletteItemCount = layout.getChildCount();

                for (int i=0; i < rouletteItemCount; i++) {
                    LinearLayout childLayout = (LinearLayout) layout.getChildAt(i);
                    LinearLayout grandChildLayout1 = (LinearLayout) childLayout.getChildAt(0);
                    LinearLayout grandChildLayout2 = (LinearLayout) childLayout.getChildAt(1);
                    for (int j = 0; j < grandChildLayout1.getChildCount(); j++) {
                        switch (j) {
                            case 0:
                                GradientDrawable buttonColor;
                                buttonColor = (GradientDrawable) grandChildLayout1.getChildAt(j).getBackground();
                                colors.add(buttonColor.getColor().getDefaultColor());
                                break;
                            case 1:
                                textStrings.add(((EditText) grandChildLayout1.getChildAt(j)).getText().toString());
                                break;
                            case 2:
                                if (((EditText) grandChildLayout1.getChildAt(j)).getText().toString().equals("")) {
                                    itemRatios.add(1);
                                } else {
                                    itemRatios.add(Integer.parseInt(String.valueOf(((EditText) grandChildLayout1.getChildAt(j)).getText())));
                                }
                                break;
                        }
                    }
                    if (((Switch) grandChildLayout2.getChildAt(0)).isChecked()) {
                        switch100PositiveCount++;
                        OnOffOfSwitch100.add(1);// 1 = ON
                        OnOffOfSwitch0.add(0);// 0 = OFF
                    } else if (((Switch) grandChildLayout2.getChildAt(1)).isChecked()) {
                        switch0PositiveCount++;
                        OnOffOfSwitch0.add(1);// 1 = ON
                        OnOffOfSwitch100.add(0);// 0 = OFF
                    } else {
                        OnOffOfSwitch100.add(0); // 0 = OFF
                        OnOffOfSwitch0.add(0); // 0 = OFF
                    }
                }
                for (int i=0; i < rouletteItemCount; i++) {
                    LinearLayout childLayout = (LinearLayout) layout.getChildAt(i);
                    LinearLayout grandChildLayout2 = (LinearLayout) childLayout.getChildAt(1);
                    if (((Switch) grandChildLayout2.getChildAt(0)).isChecked() && switch100PositiveCount >= 1) {
                        itemProbabilities.add(1f / switch100PositiveCount);
                    } else if (switch100PositiveCount >= 1) {
                        itemProbabilities.add(0f);
                    } else if (((Switch) grandChildLayout2.getChildAt(1)).isChecked() && switch0PositiveCount >= 1) {
                        itemProbabilities.add(0f);
                    } else if (switch0PositiveCount >= 1) {
                        itemProbabilities.add(1f / (colors.size() - switch0PositiveCount));
                    }
                }

                Word word = new Word(rouletteName.getText().toString(), getNowDate(), colors, textStrings, itemRatios, OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                word.setId(rouletteIdFromMyRoulette);
                MainActivity.mWordViewModel.update(word);


                /*
                Intent toMyRoulette = new Intent(getApplicationContext(), MyRouletteActivity.class);
                startActivity(toMyRoulette);
                finish();

                 */
                finish();
            }
        });



        //************* Layoutの設定 *************/
        // LinearLayout インスタンス生成
        /*

        final LinearLayout layout = new LinearLayout(this);

        // 並ぶ順番は垂直方向
        layout.setOrientation(LinearLayout.VERTICAL);

        // 背景色
        layout.setBackgroundColor(0xffddffee);

        // レイアウトの縦横幅を最大
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        // レイアウト横方向中央寄せ
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        */
    }

    //if (motionEvent.getAction() != MotionEvent.ACTION_UP)
    //  return false;
        /*
        ConstraintLayout constraintLayout = (ConstraintLayout) this.getParent();
        View view = constraintLayout.getChildAt(constraintLayout.getChildCount() - 2);

        //ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        //View view = constraintLayout.getChildAt(constraintLayout.getChildCount() - 1);
        if (!(view instanceof RouletteView))
            return false;
        if (((RouletteView) view).itemProbability.isEmpty())
            return false;
        if (!((xc - xc/6 <= motionEvent.getX() && motionEvent.getX() <= xc + xc/6)
                && (yc - xc - xc/6 - xc/6 <= motionEvent.getY() && motionEvent.getY() <= yc - xc))) //paint2で描いた円の半径の２倍（xc/12 * 2 = x/6） をタッチ範囲とする。　円の中心座標（xc, yc-xc-x/6)
            return false;
        if (!MainActivity.CheatFlag) {
            MainActivity.CheatFlag = true;
            if (MainActivity.mToast != null) MainActivity.mToast.cancel();
            MainActivity.mToast = Toast.makeText(getContext(), "イカサマモードON", Toast.LENGTH_SHORT);
        } else {
            MainActivity.CheatFlag = false;
            if (MainActivity.mToast != null) MainActivity.mToast.cancel();
            MainActivity.mToast = Toast.makeText(getContext(), "イカサマモードOFF", Toast.LENGTH_SHORT);
        }
        MainActivity.mToast.show();
        */



    /*public static Integer getColors(int i) {
        return colors.get(i);
    }

     */
    /*
    public static ArrayList<Integer> getColors() {
        return colors;
    }

     */


    /*public static String getTextString(int i) {
        return textString.get(i);
    }

     */
/*
    public static ArrayList<String> getTextString() {
        return textString;
    }
 */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("WrongConstant")
    public void addEditView(LinearLayout layout, float scale, int margins) {
        GradientDrawable drawable;
        //************* 1項目全体のLayoutの設定 *************/
        // LinearLayout インスタンス生成
        LinearLayout layoutCloneParent = new LinearLayout(getApplicationContext());

        // 並ぶ順番は垂直方向
        layoutCloneParent.setOrientation(LinearLayout.VERTICAL);

        // 背景色
        layoutCloneParent.setBackgroundColor(-66535);

        LinearLayout.LayoutParams layoutCloneParentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutCloneParent.setLayoutParams(layoutCloneParentParams);

        layoutCloneParentParams.setMargins(margins, margins, margins, margins);

        layoutCloneParent.setGravity(Gravity.CENTER_HORIZONTAL);

        //************* １つめのLayoutの設定 *************/
        // LinearLayout インスタンス生成
        LinearLayout layoutClone = new LinearLayout(getApplicationContext());

        // 並ぶ順番は垂直方向
        layoutClone.setOrientation(LinearLayout.HORIZONTAL);

        // 背景色
        layoutClone.setBackgroundColor(0xffddffee);

        LinearLayout.LayoutParams layoutCloneParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutClone.setLayoutParams(layoutCloneParams);

        layoutCloneParams.setMargins(margins, margins, margins, margins);

        layoutClone.setGravity(Gravity.CENTER_HORIZONTAL);


        //************* ２つめのLayoutの設定 *************/
        // LinearLayout インスタンス生成
        LinearLayout layoutClone2 = new LinearLayout(getApplicationContext());

        // 並ぶ順番は垂直方向
        layoutClone2.setOrientation(LinearLayout.HORIZONTAL);

        // 背景色
        layoutClone2.setBackgroundColor(0xffddffee);

        LinearLayout.LayoutParams layoutClone2Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutClone2.setLayoutParams(layoutClone2Params);

        layoutClone2Params.setMargins(margins, margins, margins, margins);
        // レイアウト横方向中央寄せ
        layoutClone2.setGravity(Gravity.CENTER_HORIZONTAL);

        if (!visibleFlag) { layoutClone2.setVisibility(View.GONE); }
        //setContentView(layoutClone);

        //************* 色決めボタンの設定 *************/
        /*
        drawable = new GradientDrawable();
        drawable.mutate();
        drawable.setShape(1);// 1 oval
        hue = hueLevel*30;
        hsv[0] = hue;
        color = Color.HSVToColor(hsv);
        drawable.setColor(color);
        */

        drawable = new GradientDrawable();
        drawable.mutate();
        drawable.setShape(1);// 1 oval
        drawable.setColor(colorPalette.pickColorRandom());

        Button button = new Button(getApplicationContext());
        button.setBackground(drawable);

        String strTitle = "color";
        button.setText(strTitle);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //ColorPickDialogを開始する
                AlertDialog.Builder colorPickAlert = new AlertDialog.Builder(EditMyRouletteActivity.this, R.style.ColorPickDialogStyle);

                colorPickAlert.setTitle("choose color");

                LayoutInflater inflater = getLayoutInflater();

                View dialoglayout = inflater.inflate(R.layout.color_pick_dialog, null);

                colorPickAlert.setView(dialoglayout);

                ColorPicker picker = (ColorPicker) dialoglayout.findViewById(R.id.picker);

                picker.setShowOldCenterColor(false);

                /*
                //変更前の色
                int buttonColor;
                buttonColor = ((GradientDrawable) button.getBackground()).getColor().getDefaultColor();
                //変更前の色をpickerの初期色に設定
                picker.setColor(buttonColor);

                 */

                SVBar svBar = (SVBar) dialoglayout.findViewById(R.id.svbar);

                OpacityBar opacityBar = (OpacityBar) dialoglayout.findViewById(R.id.opacitybar);

                colorPickAlert.
                        setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        drawable.setColor(picker.getColor());
                        button.setBackground(drawable);
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

                //final AlertDialog colorDialog = colorPickAlert.show();


                picker.addSVBar(svBar);
                picker.addOpacityBar(opacityBar);

                picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener()
                {
                    @Override
                    public void onColorChanged(int color)
                    {
                        //text.setBackgroundColor(color);
                    }
                });
            }
        });

        int buttonWidth = (int)(50 * scale);
        // 横幅 50dp, 縦 50dp に設定
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                buttonWidth, buttonWidth);//大きさは数値で設定してもいいかも

        buttonLayoutParams.setMargins(0, 0, margins, 0);

        button.setLayoutParams(buttonLayoutParams);
        layoutClone.addView(button);

        //************* 項目名のEditTex の設定 *************/
        // EditTex のインスタント生成
        EditText editText = new EditText(getApplicationContext());

        // hintを入れる
        String strHint = "input";
        editText.setHint(strHint);

        // dp単位の横幅
        int editTextWidth = (int)(150 * scale);

        // 横幅 150dp, 縦 WRAP_CONTENT に設定
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                editTextWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        //editTextParams.setMargins(margins, margins, margins, margins);

        // LayoutParamsの設定
        editText.setLayoutParams(editTextParams);

        // 背景色
        editText.setBackgroundColor(0xffffffff);

        // layoutにeditTextを追加
        layoutClone.addView(editText);

        //************* 比率格納EditTex の設定 *************/

        // EditTex のインスタント生成
        EditText editText2 = new EditText(getApplicationContext());

        // hintを入れる
        String strHint2 = "input";
        editText2.setText("1");

        // dp単位の横幅
        int editTextWidth2 = (int)(50 * scale);

        // 横幅 150dp, 縦 WRAP_CONTENT に設定
        LinearLayout.LayoutParams editTextParams2 = new LinearLayout.LayoutParams(
                editTextWidth2, LinearLayout.LayoutParams.WRAP_CONTENT);

        editTextParams2.setMargins(30, 0, 0, 0);

        // LayoutParamsの設定
        editText2.setLayoutParams(editTextParams2);

        // 背景色
        editText2.setBackgroundColor(0xffffffff);

        // layoutにeditTextを追加
        layoutClone.addView(editText2);


        //************* 項目削除ボタンの設定 *************/
        Button deleteButton = new Button(getApplicationContext());

        String deleteButtonIcon = "×";
        deleteButton.setText(deleteButtonIcon);

        int deleteButtonWidth = (int)(75 * scale);
        // 横幅 50dp, 縦 50dp に設定
        LinearLayout.LayoutParams deleteButtonLayoutParams = new LinearLayout.LayoutParams(
                deleteButtonWidth, deleteButtonWidth);//大きさは数値で設定してもいいかも

        deleteButtonLayoutParams.setMargins(10, 0, 0, 0);

        deleteButton.setLayoutParams(deleteButtonLayoutParams);

        setDeleteButtonAction(deleteButton);

        layoutClone.addView(deleteButton);

        //************* 必中switchの設定 *************/
        Switch switch100 = new Switch(getApplicationContext());
        //int switchWidth1 = (int)(50 * scale);
        LinearLayout.LayoutParams switchParams100 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        switchParams100.setMargins(0, 10, 0 , 0);
        // LayoutParamsの設定
        switch100.setLayoutParams(switchParams100);
        // 背景色
        switch100.setBackgroundColor(0xffffffff);

        setSwitch100Action(switch100);

        // layoutにeditTextを追加
        layoutClone2.addView(switch100);


        //************* 絶対ハズレswitchの設定 *************/
        Switch switch0 = new Switch(getApplicationContext());
        //int switchWidth1 = (int)(50 * scale);
        LinearLayout.LayoutParams switchParams0 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        switchParams0.setMargins(30, 10, 0 , 0);
        // LayoutParamsの設定
        switch0.setLayoutParams(switchParams0);
        // 背景色
        switch0.setBackgroundColor(0xffffffff);

        setSwitch0Action(switch0);

        // layoutにeditTextを追加
        layoutClone2.addView(switch0);

        layoutCloneParent.addView(layoutClone);
        layoutCloneParent.addView(layoutClone2);

        //layoutCloneをlayoutへ追加
        layout.addView(layoutCloneParent);
    }

    /*
    public void showColorPickDialog()
    {
        AlertDialog.Builder colorPickAlert = new AlertDialog.Builder(EditMyRouletteActivity.this, R.style.ColorPickDialogStyle);

        colorPickAlert.setTitle("choose color");

        colorPickAlert
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(getApplicationContext(), "ああああああああああああああ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "いいいいいいいいいいいいいいいいいいい", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // ダイアログがキャンセルされた際の処理
                        Toast.makeText(getApplicationContext(), "ううううううううううううううう", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();

        LayoutInflater inflater = getLayoutInflater();

        View dialoglayout = inflater.inflate(R.layout.color_pick_dialog, null);

        colorPickAlert.setView(dialoglayout);

        ColorPicker picker = (ColorPicker) dialoglayout.findViewById(R.id.picker);

        picker.setShowOldCenterColor(false);

        SVBar svBar = (SVBar) dialoglayout.findViewById(R.id.svbar);

        OpacityBar opacityBar = (OpacityBar) dialoglayout.findViewById(R.id.opacitybar);

        final AlertDialog colorDialog = colorPickAlert.show();


        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);

        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener()
        {
            @Override
            public void onColorChanged(int color)
            {
                //text.setBackgroundColor(color);
            }
        });

    }

     */

    public void setSwitch100Action(Switch switch100) {
        switch100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    int count = layout.getChildCount();
                    for (int i=0; i < count; i++) {
                        LinearLayout childLayout = (LinearLayout) layout.getChildAt(i);
                        LinearLayout grandChildLayout2 = (LinearLayout) childLayout.getChildAt(1);
                        ((Switch)grandChildLayout2.getChildAt(1)).setChecked(false);
                    }
                } else {
                    // The toggle is disabled
                }
            }
        });
    }

    public void setSwitch0Action(Switch switch0) {
        switch0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    int flag = 0;
                    int count = layout.getChildCount();
                    for (int i=0; i < count; i++) {
                        LinearLayout childLayout = (LinearLayout) layout.getChildAt(i);
                        LinearLayout grandChildLayout2 = (LinearLayout) childLayout.getChildAt(1);
                        ((Switch)grandChildLayout2.getChildAt(0)).setChecked(false);
                        if (!((Switch)grandChildLayout2.getChildAt(1)).isChecked()) {
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        buttonView.setChecked(false);

                        for (int i=0; i < count; i++) {
                            LinearLayout childLayout = (LinearLayout) layout.getChildAt(i);
                            LinearLayout grandChildLayout2 = (LinearLayout) childLayout.getChildAt(1);
                            ((Switch)grandChildLayout2.getChildAt(1)).setChecked(false);
                        }

                    }
                } else {
                    // The toggle is disabled
                }
            }
        });
    }

    public void setDeleteButtonAction(Button deleteButton) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout.getChildCount() > 1) {
                    ViewGroup rouletteItem = (ViewGroup) deleteButton.getParent().getParent();
                    layout.removeView(rouletteItem);
                }
            }
        });
    }

    public static String getNowDate(){
        @SuppressLint("SimpleDateFormat") final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    //public static int getRouletteCount() { return rouletteCount; }
}