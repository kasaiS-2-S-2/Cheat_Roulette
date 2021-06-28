package com.kasai.cheatroulette.ui;

import android.content.Context;
import android.util.AttributeSet;

public class ColorButton extends SameWidthHeightButton {

    private int buttonColor;

    public ColorButton(Context context) {
        super(context);
    }

    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setButtonColor(int color) {
        this.buttonColor = color;
    }

    public int getButtonColor() {
        return buttonColor;
    }

}
