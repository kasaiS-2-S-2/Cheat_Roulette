package com.e.myroulette1;

import android.graphics.Color;

import java.util.Random;

public class ColorPalette {
    private String[] colorStrings =
            {"#FFFF0000", "#FFFF0080", "#FFFF00FF", "#FFBF00FF", "#FF8000FF", "#FF0000FF",
                    "#FF0040FF", "#FF0080FF", "#FF00BFFF", "#FF00FFFF", "#FF00FFBF", "#FF00FF40",
            "#FFBFFF00", "#FFFFFF00", "#FFFFBF00", "#FFFF8000", "#FFFF4000", "#FFFF5D5D",
                    "#FFAE5DFF", "#FF5D46FF", "#FF5D85FF", "#FFFFAE5D", "#FF820000",
            "#FF820062", "#FF620082", "#FF410081", "#FF200081", "#FF004181", "#FF006181",
                    "#FF008181", "#FF008120", "#FF618100", "#FF818100", "#FF816100", "#FF814100",
            "#FF803A43", "#FF644681", "#FF38647E", "#FF6D7E5C", "#FF550000", "#FF550055",
                    "#FF47005F", "#FF18005F", "#FF003E53", "#FF005353", "#FF005315", "#FF4E4E00",
            "#FF4E2700"};

    private static final Random RANDOM = new Random();

    private int previousColor = 0;

    public int pickColorRandom() {
        int chosenIndex;
        int chosenColor;

        do {
            chosenIndex = RANDOM.nextInt(colorStrings.length);
            chosenColor = Color.parseColor(colorStrings[chosenIndex]);
        }while (chosenColor == previousColor);

        previousColor = chosenColor;

        return chosenColor;
    }
}
