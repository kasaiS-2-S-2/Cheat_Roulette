package com.kasai.cheatroulette;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.util.ArrayList;

public class RouletteView extends View {
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isAnimation;

    //Bitmap rotateIcon, rotateIcon2;

    VectorDrawableCompat rotateIcon;

    //各ルーレット情報の主キー
    private int id;

    //分割数
    private int splitCount = 1;

    //ルーレットの名前
    private String rouletteName;

    //ルーレットの色のリスト
    private ArrayList<Integer> colors = new ArrayList<Integer>();

    //ルーレットの文字列のリスト
    private ArrayList<String> itemNames = new ArrayList<String>();

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
    Paint colorPaint, startButtonPaint, startButtonBelowPaint, firstColorPaint, edgePaint, shadowPaint;

    Paint textPaint;

    float angle;//int → float

    int num;

    int sumOfItemRatio = 0;

    int angleCount = 0;

    float sumOfAngle = 0;

    float pos = 0;

    int init = 0;

    boolean isStateNoRoulette = true;

    Bitmap icLauncherBitmap;
    Matrix matrix;


    public RouletteView(Context context, AttributeSet attrs) { //クラスRouletteViewのコンストラクタ
        super(context, attrs);

        //icLauncherBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        //Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null);
        //icLauncherBitmap = ResourceUtil.drawableToBitmap(drawable);
        //行列を作成する。
        //matrix = new Matrix();
        //rotateIcon = ResourceUtil.getBitmap(getContext(), R.drawable.ic_baseline_refresh_100);
        rotateIcon = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_baseline_refresh_100, null);
        rotateIcon.setTint(getResources().getColor(R.color.roulette_start_icon));
        //float strokeWidth = 400.0f;///////////////////////////////////////////
        colorPaint = new Paint();//////////////////////////////////////////////////
        colorPaint.setAntiAlias(true);/////////////////////////////////////////////
        setLayerType(LAYER_TYPE_HARDWARE, colorPaint);
        //paint.setStyle(Paint.Style.STROKE);///////////////////////////////////
        //paint.setStrokeWidth(strokeWidth);////////////////////////////////////

//            paint = new Paint();
        //          paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        //textPaint.setTextSize(45);
        textPaint.setAntiAlias(true);
        setLayerType(LAYER_TYPE_HARDWARE, textPaint);

        startButtonPaint = new Paint();/////////////////////////////////////////////
        startButtonPaint.setColor(getResources().getColor(R.color.roulette_start_button));///////////////////////////////////////
        startButtonPaint.setAntiAlias(true);
        setLayerType(LAYER_TYPE_SOFTWARE, startButtonPaint);

        startButtonBelowPaint = new Paint();
        startButtonBelowPaint.setColor(getResources().getColor(R.color.roulette_start_button_below));
        startButtonBelowPaint.setAntiAlias(true);
        setLayerType(LAYER_TYPE_HARDWARE, startButtonBelowPaint);

        firstColorPaint = new Paint();
        firstColorPaint.setColor(getResources().getColor(R.color.roulette_first_color));
        firstColorPaint.setAntiAlias(true);
        setLayerType(LAYER_TYPE_HARDWARE, firstColorPaint);

        //枠線の描画
        edgePaint = new Paint();
        edgePaint.setColor(Color.parseColor("#50FFFFFF"));
        //edgePaint.setColor(Color.RED);
        edgePaint.setAntiAlias(true);
        edgePaint.setStyle(Paint.Style.STROKE);
        setLayerType(LAYER_TYPE_HARDWARE, edgePaint);

        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        //ソフトウェアレイアーを使う　→　影をうまく表示できないため
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
        //shadowPaint.setColor(Color.parseColor("#00000000"));

        //edgePaint.setStrokeWidth(48);

        //this.num = colors.size();
        //angle = (float) 360 / num;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);///////////////////////////////////////////////

        canvas.save();

        if (isStateNoRoulette) {
            canvas.drawCircle(getWidth()/2f, getHeight()/2f, (getWidth()/2f) * (15f/16f), firstColorPaint);
            canvas.drawCircle(getWidth()/2f, getHeight()/2f, getWidth()/5.4f ,startButtonBelowPaint);
        } else {

            if (xc == 0.0f) xc = getWidth() / 2f;
            if (yc == 0.0f) yc = getHeight() / 2f;

            float rouletteRadius = (getWidth()/2f) * (15f/16f);

            //shadowPaint.setShadowLayer((getWidth()/2f)+(getWidth()/64f), 0,0, Color.BLACK);
            //shadowPaint.setShadowLayer((rouletteRadius/2f), 0,0, Color.BLACK);
            shadowPaint.setShadowLayer(rouletteRadius/18f, 0, 0, Color.DKGRAY);
            //setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
            //setLayerType(LAYER_TYPE_HARDWARE, shadowPaint);
            canvas.drawCircle(xc, yc, rouletteRadius, shadowPaint);

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

            // 画面の中心から横幅に合わせた正方形を作る
            //if(rectF == null) rectF = new RectF(xc - yc/2, yc - yc/2, xc + yc/2, yc + yc/2);//

            //Log.d("ああああああああああああああああああああ", " " + 0.0f + " " + (yc - xc) + " " + getWidth() +  " " +(yc + xc));
            //Log.d("ああああああああああああああああああああ", " " + (getWidth()/32f) + " " + (yc - rouletteRadius) + " " + (getWidth() - (getWidth()/32f)) + " " + (yc + rouletteRadius));
            //rectF = new RectF(getWidth()/8f, yc - rouletteDiameter, getWidth() - (getWidth()/8f), yc + rouletteDiameter);///////////////////////////////////////
            if (rectF == null) rectF = new RectF(getWidth()/32f, yc - rouletteRadius, getWidth() - getWidth()/32f, yc + rouletteRadius);///////////////////////////////////////
            //init = 1;
            //}

            canvas.rotate(270, xc, yc); //項目順になるようにルーレットの初期位置を設定


            sumOfAngle = 0;
            angleCount = 0;
            for (int k = 0; k < splitCount; k++) {
                // パネルの描画
                for (int i = 0; i < num; i++) {
                    //paint.setColor(colors[i]);
                    colorPaint.setColor(colors.get(i));//get(i % colors.size())でやったらめちゃめちゃ処理が遅くなった →　検証したら、多分.size()ってより剰余の計算量がでかいっぽい
                    canvas.drawArc(rectF, sumOfAngle + pos, angle * itemRatios.get(i), true, colorPaint);
                    //枠線描画
                    //canvas.drawArc(rectF, sumOfAngle + pos, angle * itemRatios.get(i), true, edgePaint);
                    sumOfAngle += angle * itemRatios.get(i);
                    angleCount++;
                }
            }

            canvas.rotate(90, xc, yc);
            //canvas.drawBitmap(rotateIcon, xc - rotateIcon.getWidth()/2f, yc - rotateIcon.getHeight()/2f, null);

            //rotateIcon.setBounds((int)(xc - rotateIcon.getIntrinsicWidth()/2), (int)(yc - rotateIcon.getIntrinsicHeight()/2),(int)xc + rotateIcon.getIntrinsicWidth()/2, (int)yc + rotateIcon.getIntrinsicHeight()/2);
            rotateIcon.setBounds((int)(xc - getWidth()/8), (int)(yc - getWidth()/8),
                    (int)xc + getWidth()/8, (int)yc + getWidth()/8);
            canvas.drawCircle(xc, yc, getWidth()/5.4f ,startButtonBelowPaint);
            startButtonPaint.setShadowLayer(rouletteRadius/30f, 0, 0, Color.BLACK);
            canvas.drawCircle(xc, yc, (getWidth()/7.1f) * 0.9f ,startButtonPaint);
            //canvas.drawCircle(xc, yc, getWidth()/7f, paint4);
            rotateIcon.draw(canvas);

            canvas.rotate(-90, xc, yc);
            textPaint.setTextSize(getWidth() / 25f); //動的に文字の大きさを決める

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

                    //色の明暗の判定
                    if (isColorDark(colors.get(j))) {
                        textPaint.setColor(Color.WHITE);
                        //canvas.drawText(textStrings.get(j), xc + getWidth() / 5.5f, yc + getWidth() / 68f, textPaint);//get(i % colors.size())でやったらめちゃめちゃ処理が遅くなった　→ 検証したら、多分.size()ってより剰余の計算量がでかいっぽい
                    } else {
                        textPaint.setColor(Color.BLACK);
                        //canvas.drawText(textStrings.get(j), xc + getWidth() / 5.5f, yc + getWidth() / 68f, textPaint);//get(i % colors.size())でやったらめちゃめちゃ処理が遅くなった　→ 検証したら、多分.size()ってより剰余の計算量がでかいっぽい
                    }

                    //描く文字列
                    String willDrawText = itemNames.get(j);
                    //文字列の最大右端位置
                    float textEdgeRight = getWidth() - ((3 * getWidth())/32f);
                    //文字列の左端位置
                    float textEdgeLeft = xc + (getWidth()/5f);
                    //文字列の最大幅
                    //float maxTextWidth = (getWidth() - (getWidth()/16f)) - (xc + (getWidth()/6f));
                    float maxTextWidth = textEdgeRight - textEdgeLeft;
                    Log.d("ああああああああああああああああ", String.valueOf(maxTextWidth));
                    //最大幅を文字列幅が超えていたら、省略して表示
                    if (textPaint.measureText(willDrawText) >= maxTextWidth) {
                        int countIndex = 1;
                        while ((textPaint.measureText(willDrawText, 0,  countIndex) < maxTextWidth)) {
                            countIndex++;
                        }
                        String omittedText = willDrawText.substring(0, countIndex) + "...";
                        canvas.drawText(omittedText, textEdgeLeft, yc + getWidth() / 68f, textPaint);//get(i % colors.size())でやったらめちゃめちゃ処理が遅くなった　→ 検証したら、多分.size()ってより剰余の計算量がでかいっぽい
                    } else {
                        //最大幅を超えていなければそのまま描画
                        canvas.drawText(willDrawText, textEdgeLeft, yc + getWidth() / 68f, textPaint);//get(i % colors.size())でやったらめちゃめちゃ処理が遅くなった　→ 検証したら、多分.size()ってより剰余の計算量がでかいっぽい
                    }
                }
            }


            //VectorDrawableCompat rotateIcon = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_baseline_refresh_100, null);
            //rotateIcon.setBounds(0, 0, rotateIcon.getIntrinsicWidth(), rotateIcon.getIntrinsicHeight());
            //rotateIcon.draw(canvas);

            //rotateIcon = ResourceUtil.getBitmap(getContext(), R.drawable.ic_baseline_refresh_100);



            //x方向に2倍拡大する,y方向に0.5倍に縮小する。
            //matrix.postScale(2,0.5f);
            //Bitmapの中央を起点に90度回転する。
            //matrix.postRotate(90,xc , yc);
            //x方向に50,y方向に1500移動する。
            //matrix.postTranslate(xc,yc);

           // canvas.drawBitmap(icLauncherBitmap,0,0,null);
            //canvas.drawBitmap(rotateIcon , matrix , paint3);


            //枠線描画
            float edgeWidth = getWidth()/32f;
            edgePaint.setStrokeWidth(edgeWidth);
            canvas.drawCircle(xc, yc, rouletteRadius - edgeWidth/2f , edgePaint);
            //中心の白い円の描画
            //canvas.drawCircle(xc, yc, 200, paint2)

        }
    }

    public boolean isColorDark(int color) {
        //16進数に変換
        String hexColor = Integer.toHexString(color);

        Boolean isDarkFlag;

        int r = 0;
        int g = 0;
        int b = 0;

        if (hexColor.length() > 6) {
            //ARGB値の場合は6桁のRGB値に直す
            String hexColor6Digit = hexColor.substring(hexColor.length() - 6);

            //16進数から10進数に変換（2桁ずつ分けて変換）
            r = Integer.parseInt(hexColor6Digit.substring(0, 2), 16);
            g = Integer.parseInt(hexColor6Digit.substring(2, 4), 16);
            b = Integer.parseInt(hexColor6Digit.substring(4, 6), 16);

        } else {
            //16進数から10進数に変換（2桁ずつ分けて変換）
            r = Integer.parseInt(hexColor.substring(0, 2), 16);
            g = Integer.parseInt(hexColor.substring(2, 4), 16);
            b = Integer.parseInt(hexColor.substring(4, 6), 16);
        }

        float valueOfLightOrDark = (r*299f + g*587f + b*114f) / 2550f;

        //明暗を判定
        if (valueOfLightOrDark < 51) {
            isDarkFlag = true;
        } else {
            isDarkFlag = false;
        }

        return isDarkFlag;
    }


    public void setId(int id) {
        //各ルーレット情報の主キー
        this.id = id;
    }

    public void setRouletteContents(int splitCount,
                                    String rouletteNameInfo,
                                    ArrayList<Integer> colorsInfo,
                                    ArrayList<String> itemNamesInfo,
                                    ArrayList<Integer> itemRatiosInfo,
                                    ArrayList<Integer> OnOffOfSwitch100Info,
                                    ArrayList<Integer> OnOffOfSwitch0Info,
                                    ArrayList<Float> itemProbabilitiesInfo) {


        this.isStateNoRoulette = false;

        //ルーレットの分割数
        this.splitCount = splitCount;
        //ルーレットの名前
        this.rouletteName = rouletteNameInfo;
        //ルーレットの色のリスト
        this.colors = colorsInfo;
        //ルーレットの文字列のリスト
        this.itemNames = itemNamesInfo;
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

    public void setSplitCount(int splitCount) { this.splitCount = splitCount; }
    public int getSplitCount() { return splitCount; }

    public String getRouletteName() {
        return rouletteName;
    }
    public ArrayList<Integer> getColors() {
        return colors;
    }
    public ArrayList<String> getItemNames() {
        return itemNames;
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

    public void reDraw(float rotateDegree) {
        invalidate();
    }

}