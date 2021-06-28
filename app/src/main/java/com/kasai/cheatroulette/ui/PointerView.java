package com.kasai.cheatroulette.ui;

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

import com.kasai.cheatroulette.R;
import com.kasai.cheatroulette.activity.mainActivity.MainActivity;
import com.kasai.cheatroulette.common.MyApplication;

public class PointerView extends View {

    //通知オブジェクトの用意と初期化
    Notification notification = null;

    Paint paint, paint2, paint3;
    Path path;

    float xc = 0f;
    float yc = 0f;
    float width = 0;

    public PointerView(Context context) {
        super(context);

        path = new Path();

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.appPink));
        paint.setAntiAlias(true);

        paint2 = new Paint();
        paint2.setColor(getResources().getColor(R.color.pointer_view_border));
        paint2.setAntiAlias(true);

        paint3 = new Paint();
        paint3.setColor(getResources().getColor(R.color.pointer_view_border));
        paint3.setAntiAlias(true);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(8);
    }

    public PointerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        path = new Path();

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.appPink));
        paint.setAntiAlias(true);

        paint2 = new Paint();
        paint2.setColor(getResources().getColor(R.color.pointer_view_border));
        paint2.setAntiAlias(true);

        paint3 = new Paint();
        paint3.setColor(getResources().getColor(R.color.pointer_view_border));
        paint3.setAntiAlias(true);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(xc == 0.0f) xc = getWidth() / 2;
        if(yc == 0.0f) yc = getHeight() / 2;

        // 三角形を書く
        float tx1 = xc;
        float ty1 = yc - xc - (xc/8) + (xc/3);
        float tx2 = xc - (xc/24);
        float ty2 = yc - xc - (xc/8);
        float tx3 = xc + (xc/24);
        float ty3 = yc - xc - (xc/8);

        path.moveTo(tx1, ty1);
        path.lineTo(tx2, ty2);
        path.lineTo(tx3, ty3);
        path.lineTo(tx1, ty1);

        canvas.drawPath(path, paint);
        canvas.drawPath(path, paint3);

        canvas.drawCircle(tx1, ty2,xc/12, paint2);

        canvas.drawCircle(tx1, ty2,xc/20, paint);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {

        ConstraintLayout constraintLayout = (ConstraintLayout) this.getParent();
        View view = constraintLayout.findViewById(R.id.roulette);

        if (!(view instanceof RouletteView))
            return false;
        if (((RouletteView) view).getItemProbabilities().isEmpty())
            return false;
        if (!((xc - xc/6 <= motionEvent.getX() && motionEvent.getX() <= xc + xc/6)
                && (yc - xc - xc/6 - xc/6 <= motionEvent.getY() && motionEvent.getY() <= yc - xc))) //paint2で描いた円の半径の２倍（xc/12 * 2 = x/6） をタッチ範囲とする。　円の中心座標（xc, yc-xc-x/6)
            return false;
        if (!MainActivity.CheatFlag) {
            MainActivity.CheatFlag = true;

            Context context = MyApplication.getAppContext();
            SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(context);
            if (defaultPref.getBoolean(context.getString(R.string.saved_notification_key), true)) {
                cheatNotification(true);
            } else {
                //通知を削除
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
            }
        } else {
            MainActivity.CheatFlag = false;
            Context context = MyApplication.getAppContext();
            SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(context);
            if (defaultPref.getBoolean(context.getString(R.string.saved_notification_key), true)) {
                cheatNotification(false);
            } else {
                //通知を削除
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
            }
        }

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

        //システムから通知マネージャー取得
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //アプリ名をチャンネルIDとして利用
        String chID = Integer.toString(R.string.app_name);

        //アンドロイドのバージョンで振り分け
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //APIが「26」以上の場合
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
                    .setSmallIcon(R.drawable.ic_baseline_notification_important_24) //通知用アイコン
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
}
