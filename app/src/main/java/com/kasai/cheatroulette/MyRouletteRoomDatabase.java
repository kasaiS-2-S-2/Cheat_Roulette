package com.kasai.cheatroulette;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.  In a real
 * app, consider exporting the schema to help you with migrations.
 */

@Database(entities = {MyRoulette.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
abstract class MyRouletteRoomDatabase extends RoomDatabase {

    abstract MyRouletteDao myRouletteDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile MyRouletteRoomDatabase INSTANCE;
    //なんで４？
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MyRouletteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyRouletteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyRouletteRoomDatabase.class, "myRoulette_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                MyRouletteDao dao = INSTANCE.myRouletteDao();
                dao.deleteAll();


                String rouletteName = "今日のディナー";
                String date = "xxxx/yy/zz";
                //ArrayList<String> words = new ArrayList<String>(Arrays.asList("", "", "", "", "", ""));
                ArrayList<Integer> colorsInfo =
                        new ArrayList<Integer>(Arrays.asList(Color.parseColor("#FFEA5555"),
                        Color.parseColor("#FFF39C3C"), Color.parseColor("#FFECD03F"),
                        Color.parseColor("#FF6EB35E"), Color.parseColor("#FF4996C8"),
                        Color.parseColor("#FF774ED8")));

                ArrayList<String> itemNamesInfo = new ArrayList<String>(Arrays.asList("お寿司", "焼き肉", "ラーメン", "コンビニ", "串かつ", "中華"));

                ArrayList<Integer> itemRatiosInfo = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1));

                ArrayList<Integer> OnOffOfSwitch100Info = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));

                ArrayList<Integer> OnOffOfSwitch0Info = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));

                //ArrayList<Float> itemProbabilitiesInfo = new ArrayList<Float>(Arrays.asList(16.66f, 16.66f, 16.66f, 16.66f, 16.66f, 16.66f));
                ArrayList<Float> itemProbabilitiesInfo = new ArrayList<Float>();

                MyRoulette defaultMyRoulette1 =
                        new MyRoulette(rouletteName, date, colorsInfo, itemNamesInfo, itemRatiosInfo, OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
                dao.insert(defaultMyRoulette1);



                String rouletteName2 = "罰ゲーム誰がやる？";
                String date2 = "xxxx/yy/zz";
                //ArrayList<String> words = new ArrayList<String>(Arrays.asList("", "", "", "", "", ""));
                ArrayList<Integer> colorsInfo2 =
                        new ArrayList<Integer>(Arrays.asList(Color.parseColor("#FF631C48"),
                                Color.parseColor("#FF9F265C"), Color.parseColor("#FFCF2967"),
                                Color.parseColor("#FFF15B67"), Color.parseColor("#FFFDB25F"),
                                Color.parseColor("#FFFED883")));

                ArrayList<String> itemNamesInfo2 = new ArrayList<String>(Arrays.asList("田中", "佐藤", "鈴木", "櫻井", "小池", "フィリップ"));

                ArrayList<Integer> itemRatiosInfo2 = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1));

                ArrayList<Integer> OnOffOfSwitch100Info2 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 1));

                ArrayList<Integer> OnOffOfSwitch0Info2 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));

                ArrayList<Float> itemProbabilitiesInfo2 = new ArrayList<Float>(Arrays.asList(0f, 0f, 0f, 0f, 0f, 100f));

                MyRoulette defaultMyRoulette2 =
                        new MyRoulette(rouletteName2, date2, colorsInfo2, itemNamesInfo2, itemRatiosInfo2, OnOffOfSwitch100Info2, OnOffOfSwitch0Info2, itemProbabilitiesInfo2);
                dao.insert(defaultMyRoulette2);


                String rouletteName3 = "今日のディナー";
                String date3 = "xxxx/yy/zz";
                //ArrayList<String> words = new ArrayList<String>(Arrays.asList("", "", "", "", "", ""));
                ArrayList<Integer> colorsInfo3 =
                        new ArrayList<Integer>(Arrays.asList(Color.parseColor("#FFB30047"),
                                Color.parseColor("#FFE34E29"), Color.parseColor("#FFF27A11"),
                                Color.parseColor("#FFF7B211"), Color.parseColor("#FF48A19E"),
                                Color.parseColor("#FF611ACA")));

                ArrayList<String> itemNamesInfo3 = new ArrayList<String>(Arrays.asList("田中", "佐藤", "鈴木", "櫻井", "小池", "フィリップ"));

                ArrayList<Integer> itemRatiosInfo3 = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1));

                ArrayList<Integer> OnOffOfSwitch100Info3 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 1));

                ArrayList<Integer> OnOffOfSwitch0Info3 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));

                ArrayList<Float> itemProbabilitiesInfo3 = new ArrayList<Float>(Arrays.asList(0f, 0f, 0f, 0f, 0f, 100f));


                MyRoulette defaultMyRoulette3 =
                        new MyRoulette(rouletteName3, date3, colorsInfo3, itemNamesInfo3, itemRatiosInfo3, OnOffOfSwitch100Info3, OnOffOfSwitch0Info3, itemProbabilitiesInfo3);
                dao.insert(defaultMyRoulette3);


            });
        }
    };
}
