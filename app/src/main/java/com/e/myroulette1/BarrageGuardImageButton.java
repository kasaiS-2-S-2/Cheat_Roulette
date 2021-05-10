package com.e.myroulette1;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;

/**
 * 連打防止Buttonクラス
 */
public class BarrageGuardImageButton extends AppCompatImageButton {

    public BarrageGuardImageButton(Context context) {
        super(context);
    }

    public BarrageGuardImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarrageGuardImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnClickListener(final View.OnClickListener listener) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {

                view.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        view.setEnabled(true);
                    }
                }, 1000L);

                listener.onClick(view);
            }
        });
    }
}