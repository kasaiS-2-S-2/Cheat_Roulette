package com.e.myroulette1;

/* 課題　*/
/*
・ edittextのフォーカスのこと　→　フォーカス　→　edittextの場合だったら、入力線が点滅しているときや、文字キーが出ているときのこと。　→　onwindowforcuchangeにおいて、これが作用していそう。
 */


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class RouletteView extends View {
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isAnimation;


    //各ルーレット情報の主キー
    private int id;

    //ルーレットの名前
    private String rouletteName;

    //ルーレットの色のリスト
    private ArrayList<Integer> colors = new ArrayList<Integer>();

    //ルーレットの文字列のリスト
    private ArrayList<String> textStrings = new ArrayList<String>();

    //ルーレットの項目比率のリスト
    private ArrayList<Integer> itemRatios = new ArrayList<Integer>();

    //必中スイッチのONOFF情報
    private ArrayList<Integer> OnOffInfoOfSwitch100 = new ArrayList<Integer>();

    //絶対ハズレスイッチのONOFF情報
    private ArrayList<Integer> OnOffInfoOfSwitch0 = new ArrayList<Integer>();

    //ルーレットの項目別の当選確率のリスト
    private ArrayList<Float> itemProbabilities = new ArrayList<Float>();


    // Canvas 中心点
    float xc = 0.0f;//int →　float
    float yc = 0.0f;//int →　float

    private RectF rectF = null;
////////////////////////////////////////////////////////////////////////////////////////////////////
    Paint paint, paint2, paint3, paint4;

    Paint textPaint;

    float angle;//int → float

    int num;

    int sumOfItemRatio = 0;

    int splitCount = 1;

    int angleCount = 0;

    float sumOfAngle = 0;

    float pos = 0;

    int init = 0;

    boolean isStateNoRoulette = true;


    public RouletteView(Context context, AttributeSet attrs) { //クラスRouletteViewのコンストラクタ
        super(context, attrs);

        //float strokeWidth = 400.0f;///////////////////////////////////////////
        paint = new Paint();//////////////////////////////////////////////////
        paint.setAntiAlias(true);/////////////////////////////////////////////
        //paint.setStyle(Paint.Style.STROKE);///////////////////////////////////
        //paint.setStrokeWidth(strokeWidth);////////////////////////////////////

//            paint = new Paint();
        //          paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.DKGRAY);
        //textPaint.setTextSize(45);
        textPaint.setAntiAlias(true);

        paint2 = new Paint();/////////////////////////////////////////////
        paint2.setColor(Color.BLACK);///////////////////////////////////////
        paint2.setAntiAlias(true);

        paint3 = new Paint();
        paint3.setColor(Color.RED);
        paint3.setAntiAlias(true);

        paint4 = new Paint();
        paint4.setColor(Color.LTGRAY);
        paint4.setAntiAlias(true);

        //this.num = colors.size();
        //angle = (float) 360 / num;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);///////////////////////////////////////////////

        if (isStateNoRoulette) {

            canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2, paint4);

        } else {

            sumOfItemRatio = 0;
            for (int i = 0; i < colors.size(); i++) {
                sumOfItemRatio += itemRatios.get(i);
            }
            this.num = colors.size();
            angle = (float) 360 / (sumOfItemRatio * splitCount);


            // 背景
            //canvas.drawColor(Color.WHITE);
            //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//これ以前に描いたこと全部を透明にするコード

            //if(init == 0) {
            // Canvas 中心点
            //if(xc == 0.0f) xc = (float)canvas.getWidth() / 2;
            //if(yc == 0.0f) yc = (float)canvas.getHeight() / 2;

            if (xc == 0.0f) xc = getWidth() / 2;
            if (yc == 0.0f) yc = getHeight() / 2;

            // 画面の中心から横幅に合わせた正方形を作る
            //if(rectF == null) rectF = new RectF(xc - yc/2, yc - yc/2, xc + yc/2, yc + yc/2);//
            if (rectF == null)
                rectF = new RectF(0.0f, yc - xc, getWidth(), yc + xc);///////////////////////////////////////

            //init = 1;
            //}

            canvas.rotate(270, xc, yc); //項目順になるようにルーレットの初期位置を設定
            sumOfAngle = 0;
            angleCount = 0;
            for (int k = 0; k < splitCount; k++) {
                // パネルの描画
                for (int i = 0; i < num; i++) {
                    //paint.setColor(colors[i]);
                    paint.setColor(colors.get(i));//get(i % colors.size())でやったらめちゃめちゃ処理が遅くなった →　検証したら、多分.size()ってより剰余の計算量がでかいっぽい
                    canvas.drawArc(rectF, sumOfAngle + pos, angle * itemRatios.get(i), true, paint);
                    sumOfAngle += angle * itemRatios.get(i);
                    angleCount++;
                }
            }


            textPaint.setTextSize(getWidth() / 20); //動的に文字の大きさを決める
            for (int k = 0; k < splitCount; k++) {
                // テキストの描画
                for (int j = 0; j < num; j++) {
                    float textAngle = 0;
                    if ((k == 0) && (j == 0)) {//一回目のみこの処理
                        //textAngle = angle + pos;
                        textAngle = (angle * itemRatios.get(j)) / 2 + pos;
                    } else if (j == 0) {
                        textAngle = ((angle * itemRatios.get(colors.size() - 1)) / 2) + ((angle * itemRatios.get(j)) / 2);
                    } else {
                        textAngle = ((angle * itemRatios.get(j - 1)) / 2) + ((angle * itemRatios.get(j)) / 2);
                    }
                    canvas.rotate(textAngle, xc, yc);//rotateすると、canvasごと回る。
                    //canvas.drawText(textStrings[j], xc + 250, yc + 10 , textPaint);
                    canvas.drawText(textStrings.get(j), xc + getWidth() / 4, yc + getWidth() / 68 , textPaint);//get(i % colors.size())でやったらめちゃめちゃ処理が遅くなった　→ 検証したら、多分.size()ってより剰余の計算量がでかいっぽい
                }
            }
            //中心の白い円の描画
            //canvas.drawCircle(xc, yc, 200, paint2);

        }
    }

    public void setId(int id) {
        //各ルーレット情報の主キー
        this.id = id;
    }

    public void setRouletteContents(String rouletteNameInfo,
                                    ArrayList<Integer> colorsInfo,
                                    ArrayList<String> textStringsInfo,
                                    ArrayList<Integer> itemRatiosInfo,
                                    ArrayList<Integer> OnOffOfSwitch100Info,
                                    ArrayList<Integer> OnOffOfSwitch0Info,
                                    ArrayList<Float> itemProbabilitiesInfo) {


        this.isStateNoRoulette = false;
        this.splitCount = 1;

        //ルーレットの名前
        this.rouletteName = rouletteNameInfo;
        //ルーレットの色のリスト
        this.colors = colorsInfo;
        //ルーレットの文字列のリスト
        this.textStrings = textStringsInfo;
        //ルーレットの項目比率のリスト
        this.itemRatios = itemRatiosInfo;
        //必中スイッチのONOFF情報
        this.OnOffInfoOfSwitch100 = OnOffOfSwitch100Info;
        //絶対ハズレスイッチのONOFF情報
        this.OnOffInfoOfSwitch0 = OnOffOfSwitch0Info;
        //ルーレットの項目別の当選確率のリスト
        this.itemProbabilities = itemProbabilitiesInfo;

        //invalidate(); //上記の情報を更新したら勝手に再描画されるから、これは呼ばなくて良い？（setWillNotDrawみたいなやつが関係している？)
    }

    public float getxc() {
        return this.xc;
    }

    public float getyc() {
        return this.yc;
    }

    public void setPositon(float setpos) {
        pos = setpos;
    }

    public int getId() { return id; }
    public String getRouletteName() {
        return rouletteName;
    }
    public ArrayList<Integer> getColors() {
        return colors;
    }
    public ArrayList<String> getTextStrings() {
        return textStrings;
    }
    public ArrayList<Integer> getItemRatios() {
        return itemRatios;
    }
    public ArrayList<Integer> getOnOffInfoOfSwitch100() {
        return OnOffInfoOfSwitch100;
    }
    public ArrayList<Integer> getOnOffInfoOfSwitch0() {
        return OnOffInfoOfSwitch0;
    }
    public ArrayList<Float> getItemProbabilities() {
        return itemProbabilities;
    }

}
