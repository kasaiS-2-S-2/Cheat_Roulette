package com.kasai.cheatroulette;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

public class PointerView extends View {

    //通知オブジェクトの用意と初期化
    Notification notification = null;

    Paint paint, paint2, paint3, paint4;
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
        paint.setColor(getResources().getColor(R.color.appPink));
        paint.setAntiAlias(true);/////////////////////////////////////////////
        //paint.setStyle(Paint.Style.STROKE);///////////////////////////////////
        //paint.setStrokeWidth(strokeWidth);////////////////////////////////////

        paint2 = new Paint();/////////////////////////////////////////////
        paint2.setColor(getResources().getColor(R.color.pointer_view_border));///////////////////////////////////////
        paint2.setAntiAlias(true);

        paint3 = new Paint();
        paint3.setColor(getResources().getColor(R.color.pointer_view_border));
        paint3.setAntiAlias(true);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(8);

        //paint4 = new Paint();
       // paint4.setColor(getResources().getColor(R.color.appPrimaryColor));
        //paint4.setAntiAlias(true);

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
        paint.setColor(getResources().getColor(R.color.appPink));
        paint.setAntiAlias(true);/////////////////////////////////////////////
        //paint.setStyle(Paint.Style.STROKE);///////////////////////////////////
        //paint.setStrokeWidth(strokeWidth);////////////////////////////////////

        paint2 = new Paint();/////////////////////////////////////////////
        paint2.setColor(getResources().getColor(R.color.pointer_view_border));///////////////////////////////////////
        paint2.setAntiAlias(true);

        paint3 = new Paint();
        paint3.setColor(getResources().getColor(R.color.pointer_view_border));
        paint3.setAntiAlias(true);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(8);

        //paint4 = new Paint();
        //paint4.setColor(getResources().getColor(R.color.appPrimaryColor));
        //paint4.setAntiAlias(true);


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
        float ty1 = yc - xc - (xc/8) + (xc/3);
        float tx2 = xc - (xc/24);
        float ty2 = yc - xc - (xc/8);
        float tx3 = xc + (xc/24);
        float ty3 = yc - xc - (xc/8);

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
            //if (MainActivity.mToast != null) MainActivity.mToast.cancel();
            Context context = MyApplication.getAppContext();
            SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(context);
            if (defaultPref.getBoolean(context.getString(R.string.saved_notification_key), true)) {
                cheatNotification(true);
            } else {
                //通知を削除
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
            }
            //MainActivity.mToast = Toast.makeText(getContext(), "イカサマモードON", Toast.LENGTH_SHORT);
        } else {
            MainActivity.CheatFlag = false;
            //if (MainActivity.mToast != null) MainActivity.mToast.cancel();
            Context context = MyApplication.getAppContext();
            SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(context);
            if (defaultPref.getBoolean(context.getString(R.string.saved_notification_key), true)) {
                cheatNotification(false);
            } else {
                //通知を削除
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
            }
            //MainActivity.mToast = Toast.makeText(getContext(), "イカサマモードOFF", Toast.LENGTH_SHORT);
        }
        //MainActivity.mToast.show();

        return false;
    }

    private void cheatNotification(boolean OnOrOff) {
        Context context = getContext();
        String notificationContent;
        if (OnOrOff) {
            notificationContent = "イカサマモードON";
        } else {
            notificationContent = "イカサマモードOFF";
        }
        /*
        Context context = MyApplication.getAppContext();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "cheatOnNotification")
                .setSmallIcon(R.drawable.ic_baseline_delete_forever_24)
                .setContentTitle("ルーレット!")
                .setContentText("イカサマON")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationId is ic_cheat_roulette_launcher2_foreground unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
         */

        //システムから通知マネージャー取得
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //アプリ名をチャンネルIDとして利用
        String chID = Integer.toString(R.string.app_name);

        //アンドロイドのバージョンで振り分け
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {     //APIが「26」以上の場合
            Log.d("あああああああああああああああああ", "upper26");

            //通知チャンネルIDを生成してインスタンス化
            //音とバイブレーションをデフォルトでOFF
            NotificationChannel notificationChannel = new NotificationChannel(chID, chID, NotificationManager.IMPORTANCE_LOW);
            //通知の説明のセット
            notificationChannel.setDescription(chID);
            //通知チャンネルの作成
            notificationManager.createNotificationChannel(notificationChannel);

            //通知の生成と設定とビルド
            notification = new Notification.Builder(context, chID)
                    .setContentTitle("変更通知")  //通知タイトル
                    .setContentText(notificationContent)        //通知内容
                    .setSmallIcon(R.drawable.ic_baseline_notification_important_24)                  //通知用アイコン
                    .build();                                       //通知のビルド
        } else {
            Log.d("あああああああああああああああああ", "under25");
            //APIが「25」以下の場合
            //通知の生成と設定とビルド
            notification = new Notification.Builder(context)
                    .setContentTitle("変更通知")
                    .setContentText(notificationContent)
                    //音とバイブレーションをデフォルトでOFF
                    .setPriority(Notification.PRIORITY_LOW)
                    .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                    .build();
        }

        //通知の発行
        notificationManager.notify(1, notification);
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
