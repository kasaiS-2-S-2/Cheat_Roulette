package com.kasai.cheatroulette;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class SameWidthHeightButton extends AppCompatButton {
    public SameWidthHeightButton(Context context) {
        super(context);
    }

    public SameWidthHeightButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SameWidthHeightButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        setMeasuredDimension(height, height);
    }

}
