package com.kasai.cheatroulette.common;

import android.annotation.SuppressLint;

import com.kasai.cheatroulette.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NowDate {
    public static String getNowDate() {
        @SuppressLint("SimpleDateFormat") final DateFormat df = new SimpleDateFormat(MyApplication.getAppContext().getString(R.string.date_format_of_my_roulette));
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
}
