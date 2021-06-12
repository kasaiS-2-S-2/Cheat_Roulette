package com.kasai.cheatroulette;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ArcAnimation extends Animation {

    //private float currentPos = 0.0f;

    private RouletteView rouletteView;

    public ArcAnimation(RouletteView rouletteView) {
        //currentPos = rouletteView.getPosition();
        this.rouletteView = rouletteView;
    }

    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = 1440 * interpolatedTime;

        rouletteView.setPositon(angle);
        rouletteView.requestLayout();
    }

}
