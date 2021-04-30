package com.e.myroulette1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PointerView extends View {

    Paint paint, paint2, paint3;
    Path path;

    float xc = 0f;
    float yc = 0f;
    float width = 0;

    public PointerView(Context context) {
        super(context);
/*
        colors.add(Color.argb(255, 255, 255, 128));
        colors.add(Color.argb(255, 255, 128, 255));
        colors.add(Color.argb(255, 128, 255, 255));
        colors.add(Color.argb(255, 128, 128, 255));
        colors.add(Color.argb(255, 128, 255, 128));
        colors.add(Color.argb(255, 255, 128, 128));
        colors.add(Color.argb(255, 255, 128, 10));/////////////////////////////////////
        colors.add(Color.argb(10, 128, 128, 255));/////////////////////////////////////
        colors.add(Color.argb(255, 10, 128, 255));/////////////////////////////////////
        colors.add(Color.argb(255, 10, 10, 10));///////////////////////////////////////

        textStrings.add("カレーライス");
        textStrings.add("肉じゃが");
        textStrings.add("オムレツ");
        textStrings.add("マーボー豆腐");
        textStrings.add("鳥のから揚げ");
        textStrings.add("ビーフシチュー");
        textStrings.add("寿司");
        textStrings.add("ハンバーグ");
        textStrings.add("ドリア");
        textStrings.add("");


 */

        //float strokeWidth = 400.0f;///////////////////////////////////////////
        //this.xc = xc;
        //this.yc = yc;

        path = new Path();

        paint = new Paint();//////////////////////////////////////////////////
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);/////////////////////////////////////////////
        //paint.setStyle(Paint.Style.STROKE);///////////////////////////////////
        //paint.setStrokeWidth(strokeWidth);////////////////////////////////////

        paint2 = new Paint();/////////////////////////////////////////////
        paint2.setColor(Color.BLACK);///////////////////////////////////////
        paint2.setAntiAlias(true);

        paint3 = new Paint();
        paint3.setColor(Color.BLACK);
        paint3.setAntiAlias(true);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(8);

        //this.num = colors.size();
        //angle = (float) 360 / num;
    }

    public PointerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
/*
        colors.add(Color.argb(255, 255, 255, 128));
        colors.add(Color.argb(255, 255, 128, 255));
        colors.add(Color.argb(255, 128, 255, 255));
        colors.add(Color.argb(255, 128, 128, 255));
        colors.add(Color.argb(255, 128, 255, 128));
        colors.add(Color.argb(255, 255, 128, 128));
        colors.add(Color.argb(255, 255, 128, 10));/////////////////////////////////////
        colors.add(Color.argb(10, 128, 128, 255));/////////////////////////////////////
        colors.add(Color.argb(255, 10, 128, 255));/////////////////////////////////////
        colors.add(Color.argb(255, 10, 10, 10));///////////////////////////////////////

        textStrings.add("カレーライス");
        textStrings.add("肉じゃが");
        textStrings.add("オムレツ");
        textStrings.add("マーボー豆腐");
        textStrings.add("鳥のから揚げ");
        textStrings.add("ビーフシチュー");
        textStrings.add("寿司");
        textStrings.add("ハンバーグ");
        textStrings.add("ドリア");
        textStrings.add("");


 */

        //float strokeWidth = 400.0f;///////////////////////////////////////////
        //this.xc = xc;
        //this.yc = yc;

        path = new Path();

        paint = new Paint();//////////////////////////////////////////////////
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);/////////////////////////////////////////////
        //paint.setStyle(Paint.Style.STROKE);///////////////////////////////////
        //paint.setStrokeWidth(strokeWidth);////////////////////////////////////

        paint2 = new Paint();/////////////////////////////////////////////
        paint2.setColor(Color.BLACK);///////////////////////////////////////
        paint2.setAntiAlias(true);

        paint3 = new Paint();
        paint3.setColor(Color.BLACK);
        paint3.setAntiAlias(true);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(8);


        //this.num = colors.size();
        //angle = (float) 360 / num;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);///////////////////////////////////////////////

        //if(xc == 0f) xc = (float)canvas.getWidth() / 2;
        //if(yc == 0f) yc = (float)canvas.getHeight() / 2;

        if(xc == 0.0f) xc = getWidth() / 2;
        if(yc == 0.0f) yc = getHeight() / 2;


        // 三角形を書く
        float tx1 = xc;
        float ty1 = xc + xc/4 - xc/6;
        float tx2 = xc - xc/24;
        float ty2 = yc - xc - xc/6;
        float tx3 = xc + xc/24;
        float ty3 = yc - xc - xc/6;

        //paint.setStrokeWidth(10);
        path.moveTo(tx1, ty1);
        path.lineTo(tx2, ty2);
        path.lineTo(tx3, ty3);
        path.lineTo(tx1, ty1);

        canvas.drawPath(path, paint);
        canvas.drawPath(path, paint3);

        canvas.drawCircle(tx1, ty2,xc/12, paint2);

        canvas.drawCircle(tx1, ty2,xc/20, paint);

        //RouletteView rouletteView = new RouletteView(getContext());

        /*
        canvas.dr

        sumOfItemRatio = 0;
        for (int i=0; i<colors.size(); i++) {
            sumOfItemRatio += itemRatio.get(i);
        }
        this.num = colors.size();
        angle = (float) 360 / (sumOfItemRatio * splitCount);


        // 背景
        //canvas.drawColor(Color.WHITE);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//これ以前に描いたこと全部を透明にするコード

        //if(init == 0) {
        // Canvas 中心点

        if(xc == 0.0f) xc = (float)canvas.getWidth() / 2;
        if(yc == 0.0f) yc = (float)canvas.getHeight() / 2;

        // 画面の中心から横幅に合わせた正方形を作る
        //if(rectF == null) rectF = new RectF(xc - yc/2, yc - yc/2, xc + yc/2, yc + yc/2);//
        if(rectF == null) rectF = new RectF(0.0f, yc - xc, canvas.getWidth(), yc + xc);///////////////////////////////////////

        //init = 1;
        //}

        canvas.rotate(270, xc ,yc); //項目順になるようにルーレットの初期位置を設定
        sumOfAngle = 0;
        angleCount = 0;
        for (int k=0; k<splitCount; k++) {
            // パネルの描画
            for (int i = 0; i < num; i++) {
                //paint.setColor(colors[i]);
                paint.setColor(colors.get(i));//get(i % colors.size())でやったらめちゃめちゃ処理が遅くなった →　検証したら、多分.size()ってより剰余の計算量がでかいっぽい
                canvas.drawArc(rectF, sumOfAngle + pos, angle * itemRatio.get(i), true, paint);
                sumOfAngle += angle * itemRatio.get(i);
                angleCount++;
            }
        }


        for (int k=0; k<splitCount; k++) {
            // テキストの描画
            for (int j = 0; j < num; j++) {
                float textAngle = 0;
                if ((k == 0) && (j == 0) ) {//一回目のみこの処理
                    //textAngle = angle + pos;
                    textAngle = (angle * itemRatio.get(j)) / 2 + pos;
                } else if (j == 0) {
                    textAngle = ((angle * itemRatio.get(colors.size() - 1)) / 2) + ((angle * itemRatio.get(j)) / 2);
                } else {
                    textAngle = ((angle * itemRatio.get(j - 1)) / 2) + ((angle * itemRatio.get(j)) / 2);
                }
                canvas.rotate(textAngle, xc, yc);//rotateすると、canvasごと回る。
                //canvas.drawText(textStrings[j], xc + 250, yc + 10 , textPaint);
                canvas.drawText(textStrings.get(j), xc + 250, yc + 10, textPaint);//get(i % colors.size())でやったらめちゃめちゃ処理が遅くなった　→ 検証したら、多分.size()ってより剰余の計算量がでかいっぽい
            }
        }
        //中心の白い円の描画
        //canvas.drawCircle(xc, yc, 200, paint2);

         */
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {

        //if (motionEvent.getAction() != MotionEvent.ACTION_UP)
          //  return false;
        ConstraintLayout constraintLayout = (ConstraintLayout) this.getParent();
        View view = constraintLayout.findViewById(R.id.roulette);

        //ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        //View view = constraintLayout.getChildAt(constraintLayout.getChildCount() - 1);
        if (!(view instanceof RouletteView))
            return false;
        if (((RouletteView) view).getItemProbabilities().isEmpty())
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

        return false;
    }

    /*
    public void reDraw() {
        invalidate();
    }
     */

        /*public float getPosition() {
            return pos;
        }*/

}
